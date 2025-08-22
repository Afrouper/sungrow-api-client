package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.dto.ApiKeyParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EncryptionUtilityTest {

    private static final String PAYLOAD = "Test_Payload Test_Payload Test_Payload Test_Payload Test_Payload";

    @Test
    void encryptionRound() {
        EncryptionUtility encryptionUtility = new EncryptionUtility(TestHelper.generateRSAKey(), TestHelper.TEST_PASSWORD);

        String randomPublicKey = encryptionUtility.createRandomPublicKey();
        assertNotNull(randomPublicKey);

        ApiKeyParameter apiKeyParameter = encryptionUtility.createApiKeyParameter();
        assertNotNull(apiKeyParameter);
        assertNotNull(apiKeyParameter.nonce());
        assertNotNull(apiKeyParameter.timestamp());

        String encrypt = encryptionUtility.encrypt(PAYLOAD);
        String decrypt = encryptionUtility.decrypt(encrypt);
        assertEquals(PAYLOAD, decrypt);
    }
}