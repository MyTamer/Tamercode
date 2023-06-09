package commons.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.makagiga.commons.FS;
import org.makagiga.commons.TK;
import org.makagiga.commons.crypto.CryptoUtils;
import org.makagiga.commons.crypto.PBEInfo;
import org.makagiga.test.AbstractTest;
import org.makagiga.test.Test;
import org.makagiga.test.TestConstructor;
import org.makagiga.test.TestField;
import org.makagiga.test.TestMethod;
import org.makagiga.test.Tester;

@Test(className = PBEInfo.class)
public final class TestPBEInfo extends AbstractTest {

    @Test(fields = @TestField(name = "DEFAULT_TRANSFORMATION"))
    public void test_field() {
        assert PBEInfo.DEFAULT_TRANSFORMATION.equals("PBEWithSHA1AndDESede");
    }

    @Test(constructors = { @TestConstructor(parameters = "char[], String, byte[]"), @TestConstructor(parameters = "char[], boolean, String, byte[]"), @TestConstructor(parameters = "SecretKey, byte[]") }, methods = @TestMethod(name = "finalize"))
    public void test_constructor() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                new PBEInfo(null, null);
            }
        });
        Tester.testIllegalArgumentException(new Tester.Code() {

            public void run() throws Throwable {
                new PBEInfo(new char[0], PBEInfo.DEFAULT_TRANSFORMATION, null);
            }
        });
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                new PBEInfo(null, PBEInfo.DEFAULT_TRANSFORMATION, null);
            }
        });
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                new PBEInfo(validPassword(), null, null);
            }
        });
        Tester.testException(NoSuchAlgorithmException.class, new Tester.Code() {

            public void run() throws Throwable {
                new PBEInfo(validPassword(), "FAKE", null);
            }
        });
    }

    @Test(methods = { @TestMethod(name = "createCipher", parameters = "int"), @TestMethod(name = "createCipher", parameters = "int, String") })
    public void test_createCipher() throws Exception {
        Cipher cipher;
        PBEInfo info;
        Tester.testException(NoSuchAlgorithmException.class, new Tester.Code() {

            public void run() throws Throwable {
                SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
                SecretKey key = factory.generateSecret(new PBEKeySpec(validPassword()));
                PBEInfo info = new PBEInfo(key, null);
                info.createCipher(Cipher.DECRYPT_MODE);
            }
        });
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        SecretKey key = factory.generateSecret(new PBEKeySpec(validPassword()));
        info = new PBEInfo(key, null);
        cipher = info.createCipher(Cipher.DECRYPT_MODE, PBEInfo.DEFAULT_TRANSFORMATION);
        assert cipher.getAlgorithm().equals(PBEInfo.DEFAULT_TRANSFORMATION);
        cipher = info.createCipher(Cipher.ENCRYPT_MODE, PBEInfo.DEFAULT_TRANSFORMATION);
        assert cipher.getAlgorithm().equals(PBEInfo.DEFAULT_TRANSFORMATION);
        info = new PBEInfo(validPassword(), PBEInfo.DEFAULT_TRANSFORMATION, null);
        cipher = info.createCipher(Cipher.DECRYPT_MODE);
        assert cipher.getAlgorithm().equals(PBEInfo.DEFAULT_TRANSFORMATION);
        cipher = info.createCipher(Cipher.ENCRYPT_MODE);
        assert cipher.getAlgorithm().equals(PBEInfo.DEFAULT_TRANSFORMATION);
    }

    @Test(methods = @TestMethod(name = "createMac", parameters = "String"))
    public void test_createMac() throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        SecretKey key = factory.generateSecret(new PBEKeySpec(validPassword()));
        final PBEInfo info = new PBEInfo(key, null);
        Tester.testIllegalArgumentException(new Tester.Code() {

            public void run() throws Throwable {
                info.createMac(null);
            }
        });
        Tester.testIllegalArgumentException(new Tester.Code() {

            public void run() throws Throwable {
                info.createMac("");
            }
        });
        Tester.testException(NoSuchAlgorithmException.class, new Tester.Code() {

            public void run() throws Throwable {
                info.createMac("__FOO-BAR__");
            }
        });
        Mac mac = info.createMac("HmacSHA1");
        assert mac.getAlgorithm().equals("HmacSHA1");
    }

    @Test(methods = { @TestMethod(name = "clear"), @TestMethod(name = "getSalt") })
    public void test_salt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] clearedSalt = new byte[9];
        byte[] infoSalt;
        byte[] salt;
        PBEInfo info;
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        SecretKey key = factory.generateSecret(new PBEKeySpec(new char[0]));
        salt = CryptoUtils.createSalt(9);
        info = new PBEInfo(key, salt);
        infoSalt = info.getSalt();
        assert infoSalt != salt;
        assertEquals(infoSalt, salt);
        info.clear();
        assertEquals(info.getSalt(), clearedSalt);
        salt = CryptoUtils.createSalt(9);
        info = new PBEInfo(validPassword(), PBEInfo.DEFAULT_TRANSFORMATION, salt);
        infoSalt = info.getSalt();
        assert infoSalt != salt;
        assertEquals(infoSalt, salt);
        info.clear();
        assertEquals(info.getSalt(), clearedSalt);
    }

    @Test(methods = @TestMethod(name = "getTransformation"))
    public void test_transformation() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PBEInfo info;
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        SecretKey key = factory.generateSecret(new PBEKeySpec(new char[0]));
        info = new PBEInfo(key, null);
        assert info.getTransformation() == null;
        info = new PBEInfo(validPassword(), PBEInfo.DEFAULT_TRANSFORMATION, null);
        assert info.getTransformation().equals(PBEInfo.DEFAULT_TRANSFORMATION);
    }

    @Test
    public void test_encryption_decryption() throws Exception {
        final char[] goodPass = "pass".toCharArray();
        final char[] badPass = "123".toCharArray();
        Tester.testException(AssertionError.class, new Tester.Code() {

            public void run() throws Throwable {
                test_encryption_decryption(" ", goodPass, badPass, "bad password");
            }
        });
        Tester.testException(AssertionError.class, new Tester.Code() {

            public void run() throws Throwable {
                test_encryption_decryption("foo", goodPass, badPass, "bad password");
            }
        });
        test_encryption_decryption("", goodPass, goodPass, "good password");
        test_encryption_decryption(" ", goodPass, goodPass, "good password");
        test_encryption_decryption("foo", goodPass, goodPass, "good password");
        test_encryption_decryption(TK.filler('x', 1024 * 1024), goodPass, goodPass, "good password");
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            char[] data = new char[r.nextInt(1024 * 50)];
            for (int j = 0; j < data.length; j++) data[j] = (char) r.nextInt(1024);
            test_encryption_decryption(new String(data), goodPass, goodPass, "good password + random data");
        }
    }

    private void test_encryption_decryption(final String data, final char[] encryptPass, final char[] decryptPass, final String note) throws Exception {
        println("Testing encryption/decryption with data length: " + data.length() + ", " + note);
        byte[] salt;
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        SecretKey key = factory.generateSecret(new PBEKeySpec(encryptPass));
        PBEInfo info = new PBEInfo(key, null);
        salt = info.getSalt();
        Cipher cipher = info.createCipher(Cipher.ENCRYPT_MODE, PBEInfo.DEFAULT_TRANSFORMATION);
        ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream encryptedData = new ByteArrayOutputStream();
        CipherOutputStream cipherOut = new CipherOutputStream(encryptedData, cipher);
        DigestOutputStream digestOut1 = new DigestOutputStream(cipherOut, MessageDigest.getInstance("SHA1"));
        CryptoUtils.MacOutputStream macOut = new CryptoUtils.MacOutputStream(digestOut1, info.createMac("HmacSHA1"));
        FS.copyStream(byteArrayIn, macOut);
        FS.close(byteArrayIn);
        FS.close(macOut);
        factory = SecretKeyFactory.getInstance(PBEInfo.DEFAULT_TRANSFORMATION);
        key = factory.generateSecret(new PBEKeySpec(decryptPass));
        info = new PBEInfo(key, salt);
        cipher = info.createCipher(Cipher.DECRYPT_MODE, PBEInfo.DEFAULT_TRANSFORMATION);
        CipherInputStream cipherIn = new CipherInputStream(new ByteArrayInputStream(encryptedData.toByteArray()), cipher);
        ByteArrayOutputStream decryptedData = new ByteArrayOutputStream();
        DigestOutputStream digestOut2 = new DigestOutputStream(decryptedData, MessageDigest.getInstance("SHA1"));
        CryptoUtils.MacInputStream macIn = new CryptoUtils.MacInputStream(cipherIn, info.createMac("HmacSHA1"));
        FS.copyStream(macIn, digestOut2);
        FS.close(macIn);
        FS.close(digestOut2);
        byte[] expectedMac = macOut.getMac().doFinal();
        byte[] actualMac = macIn.getMac().doFinal();
        assertEquals(expectedMac, actualMac);
        byte[] expectedDigest = digestOut1.getMessageDigest().digest();
        byte[] actualDigest = digestOut2.getMessageDigest().digest();
        assertEquals(expectedDigest, actualDigest);
        assert !Arrays.equals(expectedDigest, expectedMac);
        assert !Arrays.equals(actualDigest, actualMac);
        String dataString = new String(decryptedData.toByteArray(), StandardCharsets.UTF_8);
        assert dataString.equals(data);
        assertEquals(dataString.getBytes(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
    }

    private char[] validPassword() {
        return "this is a valid password".toCharArray();
    }
}
