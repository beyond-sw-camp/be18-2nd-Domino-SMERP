package com.domino.smerp.common.encrypt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsnEncryptor {
    private final AesBytesEncryptor aesBytesEncryptor;

    public String SsnEncryptor(String ssn) {

        byte[] encrypt = aesBytesEncryptor.encrypt(ssn.getBytes());
        return new String(Base64.encode(encrypt));
    }

    public String SsnDecryptor(String ssn) {

        byte[] decode = Base64.decode(ssn.getBytes());
        return new String(aesBytesEncryptor.decrypt(decode));
    }
}
