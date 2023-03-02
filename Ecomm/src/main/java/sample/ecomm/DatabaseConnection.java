package sample.ecomm;
import java.sql.*;
public class DatabaseConnection {
    public Connection databaseLink;

    private Statement getStatement(){
        String dbURL = "jdbc:mysql://localhost:3306/ecommm";
        String userName = "root";
        String password = "DSRdsr12@#";
        try{
            Connection conn = DriverManager.getConnection(dbURL,userName,password);
            return conn.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertUpdate(String query){
        Statement statement = getStatement();
        try {
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ResultSet getQueryTable(String query){

        Statement statement = getStatement();

        try {
            ResultSet rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
