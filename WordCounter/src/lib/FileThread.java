package lib;

import java.io.FileReader;
import java.io.IOException;

public class FileThread extends Thread {
    private String pathToFile;
    private String fullText;

    public String getFullText() {return this.fullText;}


    public FileThread(String path) throws IOException {
        this.pathToFile = path;
    }

    @Override
    public void run() {
        try(FileReader reader = new FileReader(pathToFile))
        {
            int c;
            while((c=reader.read())!=-1){
                this.fullText += (char)c;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
