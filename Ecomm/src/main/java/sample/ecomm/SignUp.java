package sample.ecomm;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

public class SignUp {
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
    public static boolean validateEmail(String email){
        DatabaseConnection dbCon = new DatabaseConnection();
        String verifyLogin =" SELECT count(1) from Users WHERE email = '"+email+"'";
        ResultSet rs = dbCon.getQueryTable(verifyLogin);
       try{
           while(rs.next()){
                if(rs.getInt(1)==0){
                    return true;
                }
           }
       }catch(Exception e){
           e.printStackTrace();
       }
       return false;
    }

    public static boolean signUp(String email, String name, String add, String pass) {
        String hashPassword = getEncryptedPassword(pass);
        String s = "Insert into Users(name,email,address,pass) values('"+name+"','"+email+"','"+add+"','"+hashPassword+"');";
        try{
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdate(s);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
