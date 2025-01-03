package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.dto.ApiKeyParameter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EncryptionUtilityTest {

    private static final String PAYLOAD = "Test_Payload Test_Payload Test_Payload Test_Payload Test_Payload";
    private static final String TEST_PASSWORD = "test_password";

    private static String rsaPublicKey;

    @BeforeAll
    static void init() throws Exception{
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        rsaPublicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
    }

    @Test
    public void encryptionRound() throws Exception {
        EncryptionUtility encryptionUtility = new EncryptionUtility(rsaPublicKey, TEST_PASSWORD);

        ApiKeyParameter apiKeyParameter = encryptionUtility.createApiKeyParameter();
        assertNotNull(apiKeyParameter);
        assertNotNull(apiKeyParameter.getNonce());
        assertNotNull(apiKeyParameter.getTimestamp());

        String encrypt = encryptionUtility.encrypt(PAYLOAD);
        String decrypt = encryptionUtility.decrypt(encrypt);
        assertEquals(PAYLOAD, decrypt);
    }
}