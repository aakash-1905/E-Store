package sample.ecomm;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.fxml.FXMLLoader.load;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    Customer loggedIn = null;
    Login login = new Login();
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Hyperlink exitLink;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private Label loginMsgLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addTextField;
    @FXML
    private Label signupMsgLabel;
    @FXML
    private PasswordField passPasswordField;

    public Controller() {
    }

    @FXML
    protected void loginButtonAction(ActionEvent e) throws IOException {

        if(emailTextField.getText().isBlank()==false && passPasswordField.getText().isBlank() ==false){
//            loginMsgLabel.setText("You Tried To Login");
            loggedIn = login.validateLogin(emailTextField.getText(),passPasswordField.getText());
            if(loggedIn != null){
                loginMsgLabel.setText("Welcome");
                switchToHome(e);

            }else{
                loginMsgLabel.setText("Invalid Login!!! Please Try Again");
            }
        }else{
            loginMsgLabel.setText("Please Enter Credentials !!!");
        }
    }
    @FXML
    public void exitLinkAction(ActionEvent e){
        Platform.exit();
    }



    public void switchToHome(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
        root = fxmlLoader.load();

        HomeController homeController = fxmlLoader.getController();
        homeController.setCustomer(loggedIn);
        homeController.setLabelMsg(loggedIn);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
//        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.setX(150);
        stage.setY(40);
        stage.show();
    }
    public void switchToLogin(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
//        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToSignUp(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void signUpButtonAction(ActionEvent actionEvent) throws IOException {
        if(emailTextField.getText().isBlank()==false && passPasswordField.getText().isBlank()==false
                && nameTextField.getText().isBlank()==false && addTextField.getText().isBlank()==false){
            boolean notExist = SignUp.validateEmail(emailTextField.getText());
            if(notExist){
                boolean signup = SignUp.signUp(emailTextField.getText(),nameTextField.getText(),
                        addTextField.getText(),passPasswordField.getText());
                if(signup){
                    switchToLogin(actionEvent);
                }else{
                    signupMsgLabel.setText("Something Went Wrong Please Try Again");
                }
            }else{
                signupMsgLabel.setText("Email Exists, Please Login!!!");
            }
    }else{
            signupMsgLabel.setText("Please Enter Credentials !!!");
    }
    }
}