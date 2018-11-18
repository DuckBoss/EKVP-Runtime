import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Section implements Serializable {
	private static final long serialVersionUID = -1842018053494433640L;
	
	byte[] name;
	
	List<KeyValuePair> pairs;
	List<String> addedKeys;
	
	Section(String name, SecretKey enc_key) throws Exception {
		Encryptor enc = new Encryptor(enc_key);
		this.name =  enc.encrypt(name.getBytes("UTF8")).getBytes();
		this.pairs = new ArrayList<>();
		this.addedKeys = new ArrayList<>();
	}
	
	Section(List<KeyValuePair> pairs, String name, SecretKey enc_key) throws Exception {
		Encryptor enc = new Encryptor(enc_key);
		this.name =  enc.encrypt(name.getBytes("UTF8")).getBytes();
		this.pairs = pairs;
		
		this.addedKeys = new ArrayList<>();
		for(KeyValuePair pair : this.pairs) {
			this.addedKeys.add(pair.GetKeyDecrypted(enc_key));
		}
	}
	Section(KeyValuePair[] pairs, String name, SecretKey enc_key) throws Exception {
		Encryptor enc = new Encryptor(enc_key);
		this.name =  enc.encrypt(name.getBytes("UTF8")).getBytes();
		this.pairs = Arrays.asList(pairs);
		
		this.addedKeys = new ArrayList<>();
		for(KeyValuePair pair : this.pairs) {
			this.addedKeys.add(pair.GetKeyDecrypted(enc_key));
		}
	}
	Section(KeyValuePair pair, String name, SecretKey enc_key) throws Exception {
		Encryptor enc = new Encryptor(enc_key);
		this.name =  enc.encrypt(name.getBytes("UTF8")).getBytes();
		
		this.pairs = new ArrayList<>();
		this.pairs.add(pair);
		
		this.addedKeys = new ArrayList<>();
		this.addedKeys.add(pair.GetKeyDecrypted(enc_key));
	}
	
	public byte[] GetName() {
		return this.name;
	}
	public String GetNameDecrypted(SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
		Encryptor enc = new Encryptor(key);
		return enc.decrypt(GetName());
	}
	public void SetName(String name, SecretKey enc_key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		Encryptor enc = new Encryptor(enc_key);
		this.name =  enc.encrypt(name.getBytes("UTF8")).getBytes();
	}
	
	public int GetSize() {
		return this.pairs.size();
	}
	
	public KeyValuePair Get(int index) {
		return this.pairs.get(index);
	}
	
	public KeyValuePair Get(KeyValuePair item, SecretKey enc_key) throws Exception {
		for(KeyValuePair obj : this.pairs) {
			if( obj.GetKeyDecrypted(enc_key).equals(item.GetKeyDecrypted(enc_key)) )
				return obj;
		}
		return null;
	}
	
	public KeyValuePair Get(String key, SecretKey enc_key) throws Exception {
		for(KeyValuePair obj : this.pairs) {
			if(obj.GetKeyDecrypted(enc_key).equals(key)) {
				return obj;
			}
		}
		return null;
	}
	
	public List<KeyValuePair> GetSection() {
		return this.pairs;
	}
	public Object[] GetSectionAsArray() {
		return this.pairs.toArray();
	}
	
	public boolean AddKVP(KeyValuePair pair, SecretKey enc_key) throws Exception {
		String decKey = pair.GetKeyDecrypted(enc_key);
		if(this.addedKeys.size() > 0) {
			for(String key : this.addedKeys) {
				if( !(decKey.equals(key)) ) {
					this.pairs.add(pair);
					this.addedKeys.add(decKey);
					return true;
				}
			}
		}
		else {
			this.pairs.add(pair);
			this.addedKeys.add(decKey);
			return true;
		}
		
		System.err.println(GetName() + "-> \"" + pair.GetKeyDecrypted(enc_key) + "\" key already exists! Skipping...");
		return false;
	}
	
	public KeyValuePair RemoveKVP(int index, SecretKey enc_key) throws Exception {
		KeyValuePair removedPair = this.pairs.remove(index);
		int markForRemoval = 0;
		boolean marked = false;
		for(int i = 0; i < this.addedKeys.size(); i++) {
			if(removedPair.GetKeyDecrypted(enc_key).equals(this.addedKeys.get(i))) {
				markForRemoval = i;
				marked = true;
				break;
			}
		}
		if(marked) {
			this.addedKeys.remove(markForRemoval);
		}
		return removedPair;
	}
	
	public boolean RemoveKVP(KeyValuePair pair, SecretKey enc_key) throws Exception {
		boolean removedPair = this.pairs.remove(pair);
		int markForRemoval = 0;
		boolean marked = false;
		for(int i = 0; i < this.addedKeys.size(); i++) {
			if(pair.GetKeyDecrypted(enc_key).equals(this.addedKeys.get(i))) {
				markForRemoval = i;
				marked = true;
				break;
			}
		}
		if(marked) {
			this.addedKeys.remove(markForRemoval);
		}
		return removedPair;
	}
	
	
	public String toStringRaw(SecretKey key) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n-> ");
			
			sb.append(GetNameDecrypted(key));
			
			sb.append(" <-");
			if(GetSize() == 0) {
				sb.append("\n(EMPTY)\n");
				return sb.toString();
			}
			
			int counter = 0;
			for(KeyValuePair pair : GetSection()) {
				sb.append("\n");
				sb.append(counter);
				sb.append(". (");
				sb.append(pair.GetKeyDecrypted(key).trim());
				sb.append(", ");
				sb.append(pair.GetValueDecrypted(key).trim());
				sb.append(")");
				counter++;
			}
			sb.append("\n");
			return sb.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.err.println("There is something wrong with " + GetName() + ". Cannot return KeyValuePairs.");
		return null;
	}
}