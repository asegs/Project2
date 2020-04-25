
import java.io.FileWriter;
import java.io.IOException;

public class fileEditor {
    public static void replaceFile(String filename,String content, boolean printing){
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(content);
            myWriter.close();
            if (printing) {
                System.out.println("Successfully replaced the file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
