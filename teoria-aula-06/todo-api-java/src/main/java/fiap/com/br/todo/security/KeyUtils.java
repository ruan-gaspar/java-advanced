package fiap.com.br.todo.security;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;

public class KeyUtils {

    public static RSAPublicKey loadPublicKey(String path) {
        try {
            String key = readKey(path)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar public key", e);
        }
    }

    public static RSAPrivateKey loadPrivateKey(String path) {
        try {
            String key = readKey(path)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar private key", e);
        }
    }

    private static String readKey(String path) throws Exception {
        InputStream is = KeyUtils.class.getClassLoader()
                .getResourceAsStream(path.replace("classpath:", ""));
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}