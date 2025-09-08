package com.domino.smerp.common.encrypt;

import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsnEncryptor {
    private final AesBytesEncryptor aesBytesEncryptor;

    public String SsnEncrypt(String ssn) {

        byte[] encrypt = aesBytesEncryptor.encrypt(ssn.getBytes());
        return Base64.getEncoder().encodeToString(encrypt);
    }

    public String SsnDecrypt(String ssn) {

        byte[] decode = Base64.getDecoder().decode(ssn);
        return new String(aesBytesEncryptor.decrypt(decode));
    }
}
