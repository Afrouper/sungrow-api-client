package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.dto.ApiKeyParameter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

class EncryptionUtility {

    private final Base64.Encoder encoder = Base64.getUrlEncoder();
    private final Base64.Decoder decoder = Base64.getUrlDecoder();

    private final String rsaPublicKey;

    private final SecretKey secretKey;

    EncryptionUtility(String rsaPublicKey, String password) {
        Objects.requireNonNull(rsaPublicKey, "RSA public key cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        this.rsaPublicKey = rsaPublicKey;
        byte[] passwordBytes = getSecretKey(password) ;
        secretKey = new SecretKeySpec(passwordBytes, "AES");
    }

    String createRandomPublicKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decoder.decode(rsaPublicKey));
            RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, secretKey.getEncoded(), key.getModulus().bitLength());
            return encoder.encodeToString(bytes);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    ApiKeyParameter createApiKeyParameter() {
        return new ApiKeyParameter(
                UUID.randomUUID().toString().replaceAll("-", ""),
                Long.toString(System.currentTimeMillis())
        );
    }

    /**
     *AES encryption rule：
     *Encryption mode：ECB
     *Padding method：pkcs5padding
     *data block：128 bit
     *Offset：no offset
     *Output：hex
     *Character set：utf8 encoding
     **/
    String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance("AES"); //documented is unsecure "AES/ECB/PKCS5Padding"
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return parseByte2HexStr(result);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Decryption mode：ECB
     *Padding method：pkcs5padding
     *Data block：128 bit
     *Offset：no offset
     *Output：hex
     *Character set：utf8 encoding;
     **/
    String decrypt(String content) {
        try {
            byte[] decryptFrom = parseHexStr2Byte(content);
            Objects.requireNonNull(decryptFrom, "Decrypt from string is null");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] original = cipher.doFinal(decryptFrom);
            return new String(original);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) throws IOException, GeneralSecurityException {
        int maxBlock;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] buff;
            int i = 0;
            while(datas.length > offSet){
                if(datas.length - offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        }
    }

    private byte[] getSecretKey(String key) {
        final byte paddingChar = '0';
        byte[] realKey = new byte[16];
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        for (int i =0;i<realKey.length;i++){
            if (i<byteKey.length){
                realKey[i] = byteKey[i];
            }else{
                realKey[i] = paddingChar;
            }
        }
        return realKey;
    }

    private String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.isEmpty()) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
