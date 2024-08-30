import java.io.FileNotFoundException;
import java.io.IOException;

public interface Data {

    public boolean checkCreateFile()  throws IOException;
    public String readFile() ;
    public String pull();
    public void push(String data);
}
