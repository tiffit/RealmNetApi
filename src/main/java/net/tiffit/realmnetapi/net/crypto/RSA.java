package net.tiffit.realmnetapi.net.crypto;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    private static String SERVER_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCKFctVrhfF3m2Kes0FBL/JFeOcmNg9eJz8k/hQy1kadD+XFUpluRqa//Uxp2s9W2qE0EoUCu59ugcf/p7lGuL99UoSGmQEynkBvZct+/M40L0E0rZ4BVgzLOJmIbXMp0J4PnPcb6VLZvxazGcmSfjauC7F3yWYqUbZd/HCBtawwIDAQAB\n";
    private static PublicKey KEY;

    public static String encrypt(String string) {
        if(KEY == null){
            try {
                X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(SERVER_KEY));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                KEY = kf.generatePublic(spec);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                return null;
            }
        }
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
            int readPos = 0;
            int outputPos = 0;
            byte[] output = new byte[1024*16];
            while(readPos != bytes.length){
                int readAmount = Math.min(bytes.length - readPos, 117);
                outputPos += cipher.doFinal(bytes, readPos, readAmount, output, outputPos);
                readPos += readAmount;
            }
            byte[] finalOut = new byte[outputPos];
            System.arraycopy(output, 0, finalOut, 0, outputPos);
            return DatatypeConverter.printBase64Binary(finalOut);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}