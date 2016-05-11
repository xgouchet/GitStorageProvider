package fr.xgouchet.gitsp.credentials;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class CredentialHelper {

    private static final String TAG = CredentialHelper.class.getSimpleName();

    private static final String SSH_DIR = ".ssh";

    public static final String FILE_ID_RSA_PUB = "id_rsa.pubFile";
    public static final String FILE_ID_RSA = "id_rsa";

    public static final String DEFAULT_EMAIL = "android@gitsp";

    public static final String SSH_RSA = "ssh-rsa";
    private static byte[] SSH_RSA_HEADER = new byte[]{0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a'};

    public static class Observable implements rx.Observable.OnSubscribe<Void> {

        @NonNull
        private final Context context;

        private File sshRoot;
        private File pubFile;
        private File privFile;

        public Observable(@NonNull Context context) {
            this.context = context;
        }

        @Override
        public void call(Subscriber<? super Void> subscriber) {
            if (hasValidCredentials()) {
                subscriber.onCompleted();
                return;
            }
            try {
                createNewKey();
                subscriber.onCompleted();
            } catch (NoSuchAlgorithmException | IOException e) {
                subscriber.onError(e);
            }
        }

        private boolean hasValidCredentials() {

            // ensure root dir
            sshRoot = new File(context.getFilesDir(), SSH_DIR);
            if (!sshRoot.exists()) {
                sshRoot.mkdirs();
            }

            // check public and private key files
            pubFile = new File(sshRoot, FILE_ID_RSA_PUB);
            privFile = new File(sshRoot, FILE_ID_RSA);

            if ((!pubFile.exists()) || (!pubFile.isFile()) || (!pubFile.canRead())) {
                return false;
            }
            if ((!privFile.exists()) || (!privFile.isFile()) || (!privFile.canRead())) {
                return false;
            }
            return true;
        }

        private void createNewKey() throws NoSuchAlgorithmException, IOException {
            KeyPair keyPair = generateNewKeyPair();
            writeToFile(getPublicKeyDescription(keyPair), pubFile);
        }

    }

    @NonNull
    private static String getPublicKeyDescription(KeyPair keyPair) throws IOException {
        return SSH_RSA +
                " " +
                Base64.encodeToString(encodePublicKey((RSAPublicKey) keyPair.getPublic()), Base64.NO_WRAP) +
                " " +
                DEFAULT_EMAIL +
                "\n";
    }

    private static void writeToFile(@NonNull String content, @NonNull File file) throws IOException {

        OutputStreamWriter streamWriter = null;
        try {
            streamWriter = new OutputStreamWriter(new FileOutputStream(file));
            streamWriter.write(content);
            streamWriter.flush();
        } finally {
            if (streamWriter != null) {
                streamWriter.close();
            }
        }
    }


    /*
     * The following part has been shamelessly copied from stack overflow
     * http://stackoverflow.com/questions/3706177/how-to-generate-ssh-compatible-id-rsa-pub-from-java
     */

    private static KeyPair generateNewKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator generator;
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();
    }

    private static byte[] encodePublicKey(RSAPublicKey key) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // encode the "ssh-rsa" string

        out.write(SSH_RSA_HEADER);
        // Encode the public exponent
        BigInteger e = key.getPublicExponent();
        byte[] data = e.toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        // Encode the modulus
        BigInteger m = key.getModulus();
        data = m.toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        return out.toByteArray();
    }

    private static void encodeUInt32(int value, OutputStream out) throws IOException {
        byte[] tmp = new byte[4];
        tmp[0] = (byte) ((value >>> 24) & 0xff);
        tmp[1] = (byte) ((value >>> 16) & 0xff);
        tmp[2] = (byte) ((value >>> 8) & 0xff);
        tmp[3] = (byte) (value & 0xff);
        out.write(tmp);
    }
}
