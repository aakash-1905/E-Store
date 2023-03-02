package sample.ecomm;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    private static byte[] getSha(String input){

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEncryptedPassword(String pass){
        try{
            BigInteger num = new BigInteger(1,getSha(pass));
            StringBuilder hexString = new StringBuilder(num.toString(16));
            return hexString.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Customer validateLogin(String email, String password){
        DatabaseConnection dbCon = new DatabaseConnection();
        String hashpassword = getEncryptedPassword(password);
//        System.out.println(hashpassword)
        String verifyLogin =" SELECT * from Users WHERE email = '"+email+"' AND pass ='"+hashpassword+"'";
//        System.out.println(verifyLogin);
        ResultSet rs = dbCon.getQueryTable(verifyLogin);
        try {
            if(rs!=null && rs.next()){
//                System.out.println(rs.getInt("id")+" "+ rs.getString("name"));
                return new Customer(rs.getInt("id"),rs.getString("name"),rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
