import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class DataSourceFile implements Data {
     final File file;

    public DataSourceFile(String fileName) {
        this.file = new File(fileName + ".txt");
    }

    public boolean checkCreateFile() throws IOException {
        return this.file.createNewFile();
    }

    public String readFile() {
        if (this.file.exists()) {
            StringBuilder content = new StringBuilder();
            try (Scanner readerObj = new Scanner(this.file)) {
                while (readerObj.hasNextLine()) {
                    content.append(readerObj.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found.");
                e.printStackTrace();
            }

            return content.toString();
        }
        return "No such file exists!";
    }

    public void push(String data) {
        try (FileWriter fw = new FileWriter(this.file, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(data + "\n");
            System.out.println("Data is pushed into the file!");

        } catch (IOException e) {
            System.out.println("Error while writing to the file!");
            e.printStackTrace();
        }
    }

    public String pull() {
        try {

            List<String> lines = Files.readAllLines(Paths.get(this.file.getAbsolutePath()));

            if (!lines.isEmpty()) {
                String res = lines.remove(0);

                Files.write(Paths.get(this.file.getAbsolutePath()), lines);
                return res;
            } else {
                return "The file is empty, nothing to remove.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
