package bwmorg.bouncycastle.crypto.generators;

import bigjava.math.BigInteger;
import bigjava.security.SecureRandom;
import bwmorg.bouncycastle.crypto.*;
import bwmorg.bouncycastle.crypto.params.*;

/**
 * a GOST3410 key pair generator.
 * This generates GOST3410 keys in line with the method described
 * in GOST R 34.10-94.
 */
public class GOST3410KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {

    private static final BigInteger ZERO = BigInteger.valueOf(0);

    private GOST3410KeyGenerationParameters param;

    public void init(KeyGenerationParameters param) {
        this.param = (GOST3410KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger p, q, a, x, y;
        GOST3410Parameters GOST3410Params = param.getParameters();
        SecureRandom random = param.getRandom();
        q = GOST3410Params.getQ();
        p = GOST3410Params.getP();
        a = GOST3410Params.getA();
        do {
            x = new BigInteger(256, random);
        } while (x.equals(ZERO) || x.compareTo(q) >= 0);
        y = a.modPow(x, p);
        return new AsymmetricCipherKeyPair(new GOST3410PublicKeyParameters(y, GOST3410Params), new GOST3410PrivateKeyParameters(x, GOST3410Params));
    }
}
