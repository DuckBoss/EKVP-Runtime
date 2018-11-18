import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EKVP_Tester {
    public static void main(String [] args) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {

        // The name of the file to create.
        String filePath = "test.ekvp";
        String desKey = "1234567812345678";
        
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(desKey);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
		SecretKey enc_key = factory.generateSecret(new DESKeySpec(keyBytes));

        try {
        	//WRITE
        	IO IOInterface = new IO();
        	IOInterface.OpenWriteFile(filePath);
        	for(int sects = 0; sects < 1500; sects++) {
	        	List<KeyValuePair> allPairs = new ArrayList<>();
	        	Section newSect = new Section("Section_"+sects, enc_key);
	        	for(int keypair = 0; keypair < 100; keypair++) {
	        		KeyValuePair newInfo = new KeyValuePair("key_"+keypair, "value_"+keypair, enc_key);
	        		allPairs.add(newInfo);
	        		newSect.AddKVP(newInfo, enc_key);
	        		System.out.println("Writing KeyValuePair to Section...[" + keypair + "]");
	        	}
	        	IOInterface.WriteToFile(newSect);
	        	System.out.println("Writing Section to file...[" + sects + "]");
        	}
        	IOInterface.CloseWriteFile();
        	System.out.println("Finished writing file!");
        	
        	//READ
        	IOInterface = new IO();
        	IOInterface.OpenReadFile(filePath);
        	System.out.println("Opening encrypted file...");
        	Section readSect = IOInterface.ReadFromFile();
        	while(readSect != null) {
        		System.out.println(readSect.toStringRaw(enc_key));
        		readSect = IOInterface.ReadFromFile();
        	}
    		
	        IOInterface.CloseReadFile();
			
        }
        catch(Exception ex) {
            System.err.println(
                "Error writing file '"
                + filePath + "'");
            ex.printStackTrace();
        }
    }
}
