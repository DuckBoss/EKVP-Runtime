import java.io.Serializable;

import javax.crypto.SecretKey;

public class KeyValuePair implements Serializable {
	private static final long serialVersionUID = -7273760890339188405L;
	
	byte[] key;
	byte[] value;
		
	KeyValuePair(String key, String value, SecretKey enc_key) throws Exception {
		//DES
		Encryptor enc = new Encryptor(enc_key);
		
		this.key = enc.encrypt(key.getBytes("UTF8")).getBytes();
		this.value = enc.encrypt(value.getBytes("UTF8")).getBytes();
		
		//Unencrypted
		//this.key = key.getBytes(StandardCharsets.UTF_8);
		//this.value = value.getBytes(StandardCharsets.UTF_8);
	}
	
	public byte[] GetKey() {
		return this.key;
	}
	public byte[] GetValue() {
		return this.value;
	}
	
	public String GetKeyDecrypted(SecretKey dec_key) throws Exception {
		
		//Broken encryption
		//return Encryptor.decrypt(GetKey());
		Encryptor enc = new Encryptor(dec_key);
		
		return enc.decrypt(GetKey());
		
		//Unencrypted
		//return new String(GetKey(), StandardCharsets.UTF_8);
	}
	public String GetValueDecrypted(SecretKey dec_key) throws Exception {
		//Broken encryption
		//return Encryptor.decrypt(GetValue(), String.valueOf(serialVersionUID));

		Encryptor enc = new Encryptor(dec_key);
		
		return enc.decrypt(GetValue());
		
		//Unencrypted
		//return new String(GetValue(), StandardCharsets.UTF_8);
	}
	
	public String toStringRaw(SecretKey key) {
		String decKey = null;
		String decVal = null;
		try {
			decKey = GetKeyDecrypted(key);
			decVal = GetValueDecrypted(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Key:"+decKey+"\nValue:"+decVal;
	}
}