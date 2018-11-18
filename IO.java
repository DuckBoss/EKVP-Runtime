import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IO {
	
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	
	public IO(){
		
	}
	
	public boolean WriteToFile(Section section) {
		try {
			this.oos.writeObject(section);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Section ReadFromFile() {
		
		try {
			Section section = (Section) this.ois.readObject();
			return section;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public void OpenReadFile(String filePath) throws FileNotFoundException, IOException {
		ois = new ObjectInputStream(new FileInputStream(filePath));
	}
	public void OpenWriteFile(String filePath) throws FileNotFoundException, IOException {
		oos = new ObjectOutputStream(new FileOutputStream(filePath));
	}
	public void CloseReadFile() throws IOException {
		this.ois.close();
	}
	public void CloseWriteFile() throws IOException {
		this.oos.close();
	}
	
}
