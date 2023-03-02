package sample.ecomm;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Products;
import org.w3c.dom.events.MouseEvent;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Customer customer;
    public Products chosenProduct;

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    @FXML
    private ImageView cartImg;
    @FXML
    private Button noncustomerlogin;
    @FXML
    private VBox chosenPCard;

    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<Integer> combobox;

    @FXML
    private ImageView pimg;

    @FXML
    private Label pnameLabel;
    @FXML
    private Label customerLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label ppriceLabel;
    @FXML
    private TextField searchTextField;

    @FXML
    private ScrollPane scrollPane;

    private List<Products> products = new ArrayList<>();
    private  MyListener myListener;

    private List<Products> getData() {
        List<Products> products = new ArrayList<>();

        DatabaseConnection dbCon = new DatabaseConnection();
        String query = "Select * from products";
        ResultSet rs = dbCon.getQueryTable(query);
        try {
            while (rs != null && rs.next()) {
                Products product = new Products();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDesc(rs.getString("pdesc"));
                product.setColor(rs.getString("color"));
                product.setPrice(rs.getDouble("price"));
                product.setQty(rs.getInt("qty"));
                //retrieve img from db
                Blob blob = rs.getBlob("img");
                InputStream inputStream= blob.getBinaryStream();
                Image image = new Image(inputStream);
                product.setImage(image);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private List<Products> getSearchData(String search) {
        List<Products> products = new ArrayList<>();
        DatabaseConnection dbCon = new DatabaseConnection();
        String query = "Select * from products where pname Like '%"+search+"%'";
//        System.out.println(query);
        ResultSet rs = dbCon.getQueryTable(query);
        try {
            while (rs != null && rs.next()) {
                Products product = new Products();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDesc(rs.getString("pdesc"));
                product.setColor(rs.getString("color"));
                product.setPrice(rs.getDouble("price"));
                product.setQty(rs.getInt("qty"));
                //retrieve img from db
                Blob blob = rs.getBlob("img");
                InputStream inputStream= blob.getBinaryStream();
                Image image = new Image(inputStream);
                product.setImage(image);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public void setLabelMsg(Customer customer){
        if(customer!=null){
            customerLabel.setText("Welcome " +customer.getName());
            cartImg.setStyle("-fx-opacity:1");
        }else{
            noncustomerlogin.setStyle("-fx-opacity:1");
            customerLabel.setText("");
        }
    }

    public void nonCustomerLoginAction( ActionEvent e) throws IOException {
        if(customer==null){
            System.out.println("called");
           Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
           Scene scene = new Scene(root);
//        stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.show();
        }
    }
    public void cartImgAction() throws IOException {
        if(customer!=null){
            products.clear();
            gridPane.getChildren().clear();
            products.addAll(getCartData(customer.getId()));
            if(products.size() > 0){
                setChosenProduct(products.get(0));
                myListener = new MyListener() {
                    @Override
                    public void onClickListener(Products products) {
                        setChosenProduct(products);
                    }
                };
            }
            int col = 0;
            int row =1;
            try {
                for (int i = 0; i < products.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    Item item = fxmlLoader.getController();
                    item.setData(products.get(i),myListener);
                    if(col==3){
                        col=0;
                        row++;
                    }
                    gridPane.add(anchorPane,col++,row);
                    gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                    gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);

                    GridPane.setMargin(anchorPane,new Insets(10));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Products> getCartData(int cid) {
        List<Products> products = new ArrayList<>();
//        if (customer == null) return products;
        DatabaseConnection dbCon = new DatabaseConnection();
        String query = "select * from products where pid in (select pid from cart where cid = "+cid+");";
        System.out.println(query);
        ResultSet rs = dbCon.getQueryTable(query);
        try {
            while (rs != null && rs.next()) {
                Products product = new Products();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDesc(rs.getString("pdesc"));
                product.setColor(rs.getString("color"));
                product.setPrice(rs.getDouble("price"));
                product.setQty(rs.getInt("qty"));
                //retrieve img from db
                Blob blob = rs.getBlob("img");
                InputStream inputStream= blob.getBinaryStream();
                Image image = new Image(inputStream);
                product.setImage(image);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private void showDialogue(String message){
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("Order Status");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText(message);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);


        dialog.showAndWait();

    }
    public void searchButtonAction(ActionEvent event){
        products.clear();
        gridPane.getChildren().clear();
        products.addAll(getSearchData(searchTextField.getText()));

        if(products.size() > 0){
            setChosenProduct(products.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Products products) {
                    setChosenProduct(products);
                }
            };
        }
        int col = 0;
        int row =1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                Item item = fxmlLoader.getController();
                item.setData(products.get(i),myListener);
                if(col==3){
                    col=0;
                    row++;
                }
                gridPane.add(anchorPane,col++,row);
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(anchorPane,new Insets(10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeButtonAction(ActionEvent actionEvent){
        Platform.exit();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            products.addAll(getData());
        if(products.size() > 0){
            setChosenProduct(products.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Products products) {
                    setChosenProduct(products);
                }
            };
        }
        int col = 0;
        int row =1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                Item item = fxmlLoader.getController();
                item.setData(products.get(i),myListener);
                if(col==3){
                    col=0;
                    row++;
                }
                gridPane.add(anchorPane,col++,row);
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(anchorPane,new Insets(10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void  setChosenProduct(Products product){
        chosenProduct = product;
        pnameLabel.setText(product.getName());
        ppriceLabel.setText("Rs. " + product.getPrice());
        pimg.setImage(product.getImage());
        descLabel.setText(product.getDesc());
        combobox.getItems().clear();
        int q = product.getQty();
        if(q==0){
            combobox.getItems().add(0);
            combobox.setValue(0);
        }else
            combobox.setValue(1);
        for(int i = 1;i<=q;i++){
            combobox.getItems().add(i);
        }
        chosenPCard.setStyle("-fx-background-color: "+product.getColor()+";\n -fx-background-radius : 30;");
    }

    public void buyButtonAction(ActionEvent e){
        if(chosenProduct.getQty()==0){
            showDialogue("Product Out Of Stock, Will be back in Stock Soon!!!!");
        }else{
        if(customer==null){
            showDialogue("Please Login First!!!");
        }else{
            boolean placed = Order.placeOrder(customer,chosenProduct);
            if(placed){
                Order.updateQty(chosenProduct,combobox.getValue());
                searchButtonAction(e);
                showDialogue("Order Placed SuccessFully !!!");
            }else{
                showDialogue("Something Went Wrong, Please Try Again !!!");
            }
        }
    }}

    public void addCartButtonAction(ActionEvent e){
        if(chosenProduct.getQty()==0){
            showDialogue("Product Out Of Stock, Will be back in Stock Soon!!!!");
        }else{
        if(customer==null){
            showDialogue("Please Login First!!!");
        }else{
            boolean placed = Order.addToCart(customer,chosenProduct);
            if(placed){
                showDialogue("Added to Cart SuccessFully !!!");
            }else{
                showDialogue("Something Went Wrong, Please Try Again !!!");
            }
        }
    }}
}
