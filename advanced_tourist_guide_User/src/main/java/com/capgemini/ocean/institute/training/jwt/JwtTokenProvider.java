package com.capgemini.ocean.institute.training.jwt;

import static java.util.stream.Collectors.joining;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties = new JwtProperties();

    private SecretKey secretKey;
    
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);


    @PostConstruct
    public void init() {
    	 log.info("Initializing JwtTokenProvider");
        String secretKeyString = jwtProperties.getSecretKey();

        if (secretKeyString == null || secretKeyString.isEmpty()) {
            throw new IllegalStateException("Secret key is null or empty");
        }

        var secret = Base64.getEncoder().encodeToString(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Creating token for single sign-on");

        Claims claims = Jwts.claims().setSubject(username);
        if (authorities != null && !authorities.isEmpty()) {
            claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }


    public String createTokenForSingleSignOn(String name) {
    	Collection<? extends GrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        ((ArrayList<SimpleGrantedAuthority>) authorities).add(new SimpleGrantedAuthority("USER"));
       
        Claims claims = Jwts.claims().setSubject(name);
        if (!authorities.isEmpty()) {
            claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();


        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token);
            //  parseClaimsJws will check expiration date. No need do here.
            //log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            //log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

}
