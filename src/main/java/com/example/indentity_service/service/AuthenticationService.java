package com.example.indentity_service.service;

import com.example.indentity_service.dto.request.AuthenicationRequest;
import com.example.indentity_service.dto.request.IntrospectRequest;
import com.example.indentity_service.dto.request.LogoutRequest;
import com.example.indentity_service.dto.request.RefreshRequest;
import com.example.indentity_service.dto.response.AuthenicationResponse;
import com.example.indentity_service.dto.response.IntrospectResponse;
import com.example.indentity_service.entity.InvalidatedToken;
import com.example.indentity_service.entity.User;
import com.example.indentity_service.exception.AppException;
import com.example.indentity_service.exception.ErrorCode;
import com.example.indentity_service.repository.InvalidatedRepository;
import com.example.indentity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedRepository invalidatedRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;

    public AuthenicationResponse authenticate(AuthenicationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenication = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenication) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        var token = generateToken(user);

        return AuthenicationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    String generateToken(User user) {
        //Header Thuật toán kí token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //phần payload chứa thông tin token
        JWTClaimsSet jwtClaimNames = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ducnguyen.com")
                .issueTime(new Date())

                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buidlScope(user))
                .build();
        Payload payload = new Payload(jwtClaimNames.toJSONObject());
        //Kí token
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error when generate token", e);
            throw new RuntimeException(e);
        }

    }

    public IntrospectResponse introspectResponse(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean inValid= true;
        try {
            verifyToken(token);
        }
        catch (AppException e){
             inValid =false;
        }

        return IntrospectResponse.builder()
                .valid(inValid)
                .build();
    }

    private String buidlScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }

            });
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryDate(expiryTime)
                .build();
        invalidatedRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var verified = signedJWT.verify(verifier);
        //Lấy thời gian hêt hạn của token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
       if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
           throw new AppException(ErrorCode.UNAUTHORIZED);
       }
        return signedJWT;

    }
    public AuthenicationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken());
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expirationTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryDate(expirationTime)
                .build();
        invalidatedRepository.save(invalidatedToken);
        User user = userRepository.findByUsername(signJWT.getJWTClaimsSet().getSubject()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var token = generateToken(user);

        return AuthenicationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
}
