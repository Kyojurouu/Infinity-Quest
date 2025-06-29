package User.Controller;

import Objects.User;

import Database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("unused")
public class UserSignupController {

    private User selectedUser = null;

    @FXML
    private TextField bioTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField profilePictureTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void clearForm() {
        usernameTextField.clear();
        passwordPasswordField.clear();
        emailTextField.clear();
        bioTextField.clear();
        profilePictureTextField.clear();
        selectedUser = null;
    }    

    @FXML
    private void signUpButtonHandler(ActionEvent event) {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordPasswordField.getText();
        String bio = bioTextField.getText();
        String profilePicture = profilePictureTextField.getText();

        User user = new User(0, username, password, email, bio, profilePicture);

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || bio.isEmpty())  {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        if (DatabaseHandler.createUser(user)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully!");
            alert.showAndWait();
            clearForm();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/FXML/UserWelcome.fxml"));
                root = loader.load();
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to create an account.");
            alert.showAndWait();
        }
    }
}
