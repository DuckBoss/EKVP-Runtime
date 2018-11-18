import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

class Encryptor {
	
	Cipher enCipher;
	Cipher deCipher;
	SecretKey key;
	
	public Encryptor(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		//this.key = KeyGenerator.getInstance("DES").generateKey();
		this.key = key;
		IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		
		this.enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		this.enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
		
		this.deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		this.deCipher.init(Cipher.DECRYPT_MODE, key, iv);
	}

	public String encrypt(byte[] encrypt) {
	    byte[] result;
		try {
	        result = this.enCipher.doFinal(encrypt);
	        result = Base64.getEncoder().encode(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = null;
	    }

	    return new String(result);
	}
	
	public String decrypt(byte[] decrypt) throws UnsupportedEncodingException {
	    byte[] result;
		try {
			decrypt = Base64.getDecoder().decode(decrypt);
	        result = this.deCipher.doFinal(decrypt);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = null;
	    }

	    return new String(result, "UTF8");
	}
	
}