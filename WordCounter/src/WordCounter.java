import java.io.*;
import java.sql.*;
import java.util.*;
import lib.*;


class FileText{
    public String fileName;
    public String fullText;
    public int id;
    public FileText(String fileName, String fullText, int id) {
        this.fileName = fileName;
        this.fullText = fullText;
        this.id = id;
    }
}


public class WordCounter{
    public static void main (String args[]) throws IOException, InterruptedException, SQLException {
        Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/javastudy", "root", "");
        String fullText = "";
        String path = "";
        String deleteChar = "1234567890-—.,()!?;:«»\n\t\b\r\'\"";
        ArrayList<Word> wordsList = new ArrayList<>();
        ArrayList<FileText> FileTextArray = new ArrayList<>();

//        Scanner in = new Scanner(System.in);
//        System.out.print("Введите путь до директории: ");
//        path = in.nextLine();
//
//        if(path.length() == 0) {
//            System.out.println("Ошибка. Укажите директорию.");
//            return;
//        }

        path  = "C:\\Users\\name\\Desktop\\test";
        File folder = new File(path);

        String[] files = folder.list(new FilenameFilter() {
            public boolean accept(File folder, String name) {
                return name.endsWith(".txt");
            }
        });
        if(files.length == 0){
            System.out.println("В директории отсуствтуют текстовые файлы");
            return;
        }
        System.out.println("Количество файлов в директории: "+files.length);


       // executeSql("delete from filename");
        //executeSql("ALTER TABLE filename AUTO_INCREMENT = 1");
        int index = 1;
        for (String fileName : files) {

          //  executeSql("insert into filename(name) values('"+fileName+"')");
            FileThread fileThread = new FileThread(path+"/"+fileName);
            fileThread.run();
            fullText += fileThread.getFullText();
            FileTextArray.add(new FileText(fileName,fullText,index));
            index++;
        }

        for(int i = 0; i < FileTextArray.size(); ++i){
            for (char c : deleteChar.toCharArray()) {
                FileTextArray.get(i).fullText = FileTextArray.get(i).fullText.replace(c + "", "");
            }
        }

        for (char c : deleteChar.toCharArray()) {
            fullText = fullText.replace(c+"", "");
        }

        int wordCount = 0;
        for (String word : fullText.split(" ")) {
            boolean check = false;
            if(word.length() > 0){
                for(int i = 0; i < wordsList.size(); ++i){
                    if(wordsList.get(i).getWord().equals(word.toLowerCase())) {
                        wordsList.get(i).inc();
                        check = true;
                        break;
                    }
                }


                if(!check) {
                    Word tmp = new Word(word.toLowerCase(),1);
                    wordsList.add(tmp);
                    wordCount++;
                }
            }


        }
        System.out.println("Количество уникальных слов: "+wordCount);

        Collections.sort(wordsList);

        File file = new File("result.txt");
        file.delete();


       // executeSql("delete from wordcounter");
        //executeSql("ALTER TABLE wordcounter AUTO_INCREMENT = 1");
        for(int i = 0; i < wordsList.size(); ++i){
            System.out.println(wordsList.get(i).getWord() + " - " + wordsList.get(i).getCount());
          //  executeSql("insert into wordcounter(word,frequency) values('"+wordsList.get(i).getWord()+"', '"+wordsList.get(i).getCount()+"')");
        }


        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javastudy", "root", "");

        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("select * from wordcounter");
        while(rs.next()){
            for(int i = 0; i < FileTextArray.size(); ++i){
                for(String word : FileTextArray.get(i).fullText.split(" ")){
                    if(rs.getString("word").equals(word.toLowerCase())){
                        executeSql("insert into filetoword(fileid,wordid) values('"+FileTextArray.get(i).id+"','"+rs.getInt("id")+"')");

                        break;
                    }
                }
            }
        }



        connection.close();

    }

    public static void executeSql(String request){
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/javastudy", "root", "");

            PreparedStatement stm = connection.prepareStatement(request);
            stm.execute();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}