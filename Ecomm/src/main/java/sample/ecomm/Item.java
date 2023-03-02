package sample.ecomm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Products;
import org.w3c.dom.events.MouseEvent;

public class Item {

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;
    private Products products;
    private MyListener myListener;
    public void setData(Products products,MyListener myListener) {
        this.products = products;
        this.myListener = myListener;
        nameLabel.setText(products.getName());
        priceLable.setText("Rs. "+ products.getPrice());
        img.setImage(products.getImage());
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(products);
    }
}
