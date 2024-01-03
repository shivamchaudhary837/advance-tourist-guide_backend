package com.capgemini.ocean.institute.training.jwt;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    @SuppressWarnings("unused")
	private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";

    // validity in milliseconds
    private long validityInMs = 86400000; // 1 day

    public String getSecretKey() {
        byte[] secretKeyBytes = new byte[64]; // Adjust the size based on your requirements
        new SecureRandom().nextBytes(secretKeyBytes);
        return Base64.getEncoder().encodeToString(secretKeyBytes);
    }

    public long getValidityInMs() {
        return validityInMs;
    }
}
