package sample.ecomm;

import model.Products;

public class Order {
    public static boolean placeOrder(Customer customer, Products product){
        try{
            String query = "INSERT INTO orders(cid,pid,status) values("+customer.getId()+","+product.getId()+",'Ordered');";

            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void updateQty(Products product, int qty) {
        try{
            String query =  "Update products set qty = "+ (product.getQty() - qty) +" where pid = "+product.getId()+";";
//            System.out.println(query);
            DatabaseConnection dbConn = new DatabaseConnection();
            dbConn.insertUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean addToCart(Customer customer, Products product) {
        try{
            String query = "INSERT INTO cart(cid,pid) values("+customer.getId()+","+product.getId()+");";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
