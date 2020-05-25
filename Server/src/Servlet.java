import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Servlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String word = request.getReader().readLine();

        ArrayList<Integer> fileIDs = new ArrayList<>();


        int cid = -1;
        String res = "";
        String resultSet = "";
        File file = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javastudy", "root", "");


            ResultSet result = getResult("SELECT id FROM `wordcounter` WHERE word = '"+word+"'", connection);

            while(result.next()) {
                cid = result.getInt("id");
            }

            //=================================

            result = getResult("SELECT FileID FROM `filetoword` WHERE wordid = '"+cid+"'", connection);

            while(result.next()) {
                fileIDs.add(result.getInt("FileID"));
            }

            resultSet += "[";
            for(int i = 0; i < fileIDs.size(); ++i){

                result = getResult("SELECT name FROM `filename` WHERE id = '"+fileIDs.get(i)+"'", connection);


                while(result.next()) {
                    res = result.getString("name");


                    String fileText = "";
                    Scanner in = new Scanner(new File("C:\\Users\\name\\IdeaProjects\\untitled2\\data\\"+res),"UTF-8");
                    while(in.hasNext()){
                        fileText += in.nextLine();
                    }

                    resultSet += "{\"file\":\""+res+"\",\"text\":\""+fileText+"\"}";

                }
                if(i < fileIDs.size()-1) resultSet += ",";
            }
            resultSet += "]";



        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(resultSet);
        writer.close();
    }



    ResultSet getResult(String query, Connection connection) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(query);
        return stm.executeQuery();

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String indexFile = "";
        Scanner in = new Scanner(new File("C:\\Users\\name\\IdeaProjects\\untitled2\\web\\WEB-INF\\index.html"),"UTF-8");
        while(in.hasNext()){
            indexFile += in.nextLine() + "\r\n";
        }

        response.setContentType("text/html;charset=utf-8");

        PrintWriter writer = response.getWriter();
        writer.print(indexFile);
        writer.close();


    }
}
