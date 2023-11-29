import View.Login;
import View.NewDocument;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args){
        Login login = new Login();
        Connection conn = DBConncection();
        login.createLoginPage(conn);
//        NewDocument doc = new NewDocument();
//        doc.display();


    }
    public static Connection DBConncection(){
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String DBurl = "jdbc:mysql://localhost:3306/texteditor";
            String userName = "root";
            String password = "!mySQL@786#";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(DBurl, userName, password);
            return conn;

        }
        catch(Exception e){

        }
        return null;
    }
}
