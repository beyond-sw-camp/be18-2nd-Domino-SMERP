package com.domino.smerp.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class EncryptConfig {
    @Value("${encrypt.key}")
    private String key;

    @Value("${encrypt.salt}")
    private String salt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() throws  Exception{
        return new AesBytesEncryptor(key, salt);
    }
}
