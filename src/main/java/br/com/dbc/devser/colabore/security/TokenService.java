package br.com.dbc.devser.colabore.security;

import br.com.dbc.devser.colabore.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String getToken(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(expiration));

        String token = Jwts.builder()
                .setIssuer("PrimeiroProjetoSpring")
                .setSubject(userEntity.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return PREFIX + token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String tokenBearer = request.getHeader(HEADER_AUTHORIZATION);

        if (tokenBearer != null) {
            String token = tokenBearer.replace(PREFIX, "");
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            String user = body.getSubject();
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        }
        return null;
    }
}