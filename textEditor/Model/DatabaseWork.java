package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWork {
    private Connection connection;
    public DatabaseWork(Connection conn){
        this.connection = conn;
    }
    // This function will give total number of documents made by a user which is recieved as parameter.
    public int getTotalDocuments(final String username){
        String query = "select count(documentName) from " +
                "(select distinct documentName from document where username = ?) as f;";
        int count = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            count = resultSet.getInt("count(documentName)");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return count;
    }
    public List getDocumentNames(final String username){
        String query = "select distinct documentName from document where username = ?;";
        List<String> documentNames = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String names = resultSet.getString(1);
                documentNames.add(names);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        //System.out.println(documentNames);
        return documentNames;
    }
}
