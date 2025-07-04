package Admin.Controller;

import Objects.Actor;
import Objects.Role;
import Objects.User;
import Objects.Cast;
import Objects.Director;
import Objects.Content;
import Database.DatabaseHandler;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class AdminCastController implements Initializable {

    ObservableList<Actor> actorList = FXCollections.observableArrayList();
    ObservableList<Role> roleList = FXCollections.observableArrayList();
    ObservableList<Cast> castList = FXCollections.observableArrayList();
    ObservableList<Content> contentList = FXCollections.observableArrayList();
    ObservableList<Director> directorList = FXCollections.observableArrayList();

// ============================== ACTOR & ROLE TAB ================================

    @FXML
    private Tab actorAndRoleTab;

// ============================== ACTOR ================================

    @FXML
    private TextField actorTextField;

    @FXML
    private TableView<Actor> actorTable;

    @FXML 
    private TableColumn<Actor, Integer> actorIDColumn;

    @FXML
    private TableColumn<Actor, String> actorNameColumn;

    @FXML
    private Button addActorButton;

    @FXML
    private Button updateActorButton;

    @FXML
    private Button deleteActorButton;

// ============================== ROLE ================================

    @FXML
    private TextField roleTextField;

    @FXML
    private TableView<Role> roleTable;

    @FXML
    private TableColumn<Role, Integer> roleIDColumn;

    @FXML
    private TableColumn<Role, String> roleNameColumn;

    @FXML
    private Button createRoleButton;

    @FXML
    private Button updateRoleButton;

    @FXML
    private Button deleteRoleButton;

// ============================== CAST TAB ================================

    @FXML
    private Tab castTab;

    @FXML
    private ComboBox <Actor> actorComboBox;

    @FXML
    private ComboBox <Role> roleComboBox;

    @FXML
    private ComboBox <Content> contentComboBox;

    @FXML
    private TableView<Cast> castTable;

    @FXML
    private TableColumn<Cast, Integer> castIDColumn;

    @FXML
    private TableColumn<Cast, String> actorColumn;

    @FXML
    private TableColumn<Cast, String> roleColumn;

    @FXML
    private TableColumn<Cast, String> contentColumn;

    @FXML
    private Button addCastButton;

    @FXML
    private Button updateCastButton;

    @FXML
    private Button deleteCastButton;

// ============================== DIRECTOR TAB ================================

    @FXML
    private Tab directorTab;

    @FXML
    private TextField directorTextField;

    @FXML
    private TableView<Director> directorTable;

    @FXML
    private TableColumn<Director, Integer> directorIDColumn;

    @FXML
    private TableColumn<Director, String> directorNameColumn;

    @FXML
    private Button addDirectorButton;

    @FXML
    private Button updateDirectorButton;

    @FXML
    private Button deleteDirectorButton;

// ============================== NAVIGATION ================================

    @FXML
    private Button userButton;

    @FXML
    private Button contentButton;

    @FXML
    private Button castButton;

    @FXML
    private Button watchlistButton;

    @FXML
    private Button ratingButton;

    @FXML
    private Button reviewButton;

    @FXML
    private Button likeButton;

    @FXML 
    private Button dislikeButton;

    @FXML
    private Button backButton;

    private Actor selectedActor = null;
    private Role selectedRole = null;
    private Cast selectedCast = null;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeActorColumn();
        initializeRoleColumn();
        initializeCastColumn();
        initializeDirectorColumn();

        displayActor();
        displayRole();
        displayDirector();

        loadActors();
        loadRoles();
        loadContents();
        loadCasts();
        bindComboBoxes();

        actorTable.setOnMouseClicked(event -> {
            selectedActor = actorTable.getSelectionModel().getSelectedItem();
            if (selectedActor != null) {
                actorTextField.setText(selectedActor.getActorName());
            }
        });

        roleTable.setOnMouseClicked(event -> {
            selectedRole = roleTable.getSelectionModel().getSelectedItem();
            if (selectedRole != null) {
                roleTextField.setText(selectedRole.getRoleName());
            }
        });


        castTable.setOnMouseClicked(event -> {
            selectedCast = castTable.getSelectionModel().getSelectedItem();
            if (selectedCast != null) {
                for (Actor actor : actorComboBox.getItems()) {
                    if (actor.getActorID() == selectedCast.getActorID()) {
                        actorComboBox.setValue(actor);
                        break;
                    }
                }
                for (Role role : roleComboBox.getItems()) {
                    if (role.getRoleID() == selectedCast.getRoleID()) {
                        roleComboBox.setValue(role);
                        break;
                    }
                }
                for (Content content : contentComboBox.getItems()) {
                    if (content.getContentID() == selectedCast.getContentID()) {
                        contentComboBox.setValue(content);
                        break;
                    }
                }
            }
        });

        directorTable.setOnMouseClicked(event -> {
            Director selectedDirector = directorTable.getSelectionModel().getSelectedItem();
            if (selectedDirector != null) {
                directorTextField.setText(selectedDirector.getDirectorName());
            }
        });

        actorTable.setEditable(true);




 }

    private void initializeActorColumn() {
        actorIDColumn.setCellValueFactory(new PropertyValueFactory<>("actorID"));
        actorNameColumn.setCellValueFactory(new PropertyValueFactory<>("actorName"));
    }

    private void initializeRoleColumn() {
        roleIDColumn.setCellValueFactory(new PropertyValueFactory<>("roleID"));
        roleNameColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
    }

    private void initializeCastColumn() {
        castIDColumn.setCellValueFactory(new PropertyValueFactory<>("castID"));
        actorColumn.setCellValueFactory(new PropertyValueFactory<>("actorName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contentTitle"));

        castTable.setItems(castList);
        castTable.setEditable(false);
    }

    private void initializeDirectorColumn() {
        directorIDColumn.setCellValueFactory(new PropertyValueFactory<>("directorID"));
        directorNameColumn.setCellValueFactory(new PropertyValueFactory<>("directorName"));
    }

    private void bindComboBoxes() {
        actorComboBox.setItems(actorList);
        roleComboBox.setItems(roleList);
        contentComboBox.setItems(contentList);
    }

    private void clearForm() {
        actorTextField.clear();
        roleTextField.clear();
        directorTextField.clear();
        selectedActor = null;
        selectedRole = null;
        selectedCast = null;
        actorComboBox.setValue(null);
        roleComboBox.setValue(null);       
        contentComboBox.setValue(null);
    }


// ============================ ACTOR ===============================

    private void displayActor() {
        actorList.clear();

        ResultSet result = DatabaseHandler.getActor();

        try {
            while (result.next()) {
                int actorID = result.getInt("actorID");
                String actorName = result.getString("actorName");
                Actor actor = new Actor(actorID, actorName);
                actorList.add(actor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        actorTable.setItems(actorList);
    }

    @FXML
    private void createActor(ActionEvent event) throws IOException {

        String actorName = actorTextField.getText();
        
        actorName = actorName.trim();

        Actor actor = new Actor(0, actorName);

        if (DatabaseHandler.createActor(actor)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Actor created successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to create actor");
            alert.showAndWait();
        }
        displayActor();
        clearForm();
    }

    @FXML
    private void deleteActor (ActionEvent event) {

        Actor actor = actorTable.getSelectionModel().getSelectedItem();

        int actorID = actor.getActorID();

        if (DatabaseHandler.deleteActor(actor)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Actor deleted successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete actor");
            alert.showAndWait();
        }
        displayActor();
        clearForm();
    }

    @FXML
    private void updateActor (ActionEvent event) {

        Actor actor = actorTable.getSelectionModel().getSelectedItem();

        if (actor != null) {
            String newActorName = actorTextField.getText().trim();
            if (!newActorName.isEmpty()) {
                actor.setActorName(newActorName);
                if (DatabaseHandler.updateActor(actor)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Actor updated successfully!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update actor");
                    alert.showAndWait();
                }
                displayActor();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Actor name cannot be empty");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No actor selected for update");
            alert.showAndWait();
        }

        clearForm();
    }

// ============================ ROLE ===============================

    private void displayRole() {
        roleList.clear();

        ResultSet result = DatabaseHandler.getRole();

        try {
            while (result.next()) {
                int roleID = result.getInt("roleID");
                String roleName = result.getString("roleName");
                Role role = new Role(roleID, roleName);
                roleList.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        roleTable.setItems(roleList);
    }

    @FXML
    private void createRole(ActionEvent event) throws IOException {

        String roleName = roleTextField.getText();
        
        roleName = roleName.trim();

        Role role = new Role(0, roleName);

        if (DatabaseHandler.createRole(role)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Role created successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to create role");
            alert.showAndWait();
        }
        displayRole();
        clearForm();
    }

    @FXML
    private void deleteRole (ActionEvent event) {

        Role role = roleTable.getSelectionModel().getSelectedItem();

        int roleID = role.getRoleID();

        if (DatabaseHandler.deleteRole(role)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Role deleted successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete role");
            alert.showAndWait();
        }
        displayRole();
        clearForm();
    }

    @FXML
    private void updateRole (ActionEvent event) {
        Role role = roleTable.getSelectionModel().getSelectedItem();
        if (role != null) {
            String newRoleName = roleTextField.getText().trim();
            if (!newRoleName.isEmpty()) {
                role.setRoleName(newRoleName);
                if (DatabaseHandler.updateRole(role)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Role updated successfully!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update role");
                    alert.showAndWait();
                }
                displayRole();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Role name cannot be empty");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No role selected for update");
            alert.showAndWait();
        }

        clearForm();
    }

// ============================ CAST ===============================

    private void loadActors() {
        actorList.clear();
        ResultSet rs = DatabaseHandler.getActor();
        try {
            while (rs.next()) {
                actorList.add(new Actor(rs.getInt("actorID"), rs.getString("actorName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRoles() {
        roleList.clear();
        ResultSet rs = DatabaseHandler.getRole();
        try {
            while (rs.next()) {
                roleList.add(new Role(rs.getInt("roleID"), rs.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContents() {
        contentList.clear();
        ResultSet rs = DatabaseHandler.getContent();
        try {
            while (rs.next()) {
                contentList.add(new Content(rs.getInt("contentID"), rs.getString("contentTitle"), rs.getString("contentRuntime"), rs.getObject("contentSeason", Integer.class), rs.getObject("contentEpisode", Integer.class), rs.getObject("contentReleaseDate", java.time.LocalDate.class), rs.getString("contentSynopsis"), rs.getInt("directorID"), rs.getInt("contentPhase"), rs.getString("contentAgeRating"), rs.getInt("contentChronologicalOrder"), rs.getString("contentPoster"), rs.getString("contentTrailer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCasts() {
        castList.clear();
        ResultSet rs = DatabaseHandler.getCast();
        try {
            while (rs.next()) {
                castList.add(new Cast(
                    rs.getInt("castID"),
                    rs.getInt("actorID"),
                    rs.getInt("roleID"),
                    rs.getInt("contentID"),
                    rs.getString("actorName"),
                    rs.getString("roleName"),
                    rs.getString("contentTitle")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createCast(ActionEvent event) {
        Actor actor = actorComboBox.getValue();
        Role role = roleComboBox.getValue();
        Content content = contentComboBox.getValue();
        if (actor == null || role == null || content == null) {
            showAlert(Alert.AlertType.ERROR, "Please select an actor, role, and content");
            return;
        }
        if (DatabaseHandler.createCast(actor.getActorID(), role.getRoleID(), content.getContentID())) {
            showAlert(Alert.AlertType.INFORMATION, "Cast added successfully!");
            loadCasts();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to add cast");
        }

        clearForm();
    }

    @FXML
    private void updateCast(ActionEvent event) {
        Cast selected = castTable.getSelectionModel().getSelectedItem();
        Actor actor = actorComboBox.getValue();
        Role role = roleComboBox.getValue();
        Content content = contentComboBox.getValue();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "No cast selected");
            return;
        }
        if (DatabaseHandler.updateCast(selected.getCastID(), actor.getActorID(), role.getRoleID(), content.getContentID())) {
            showAlert(Alert.AlertType.INFORMATION, "Cast updated successfully!");
            loadCasts();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to update cast");
        }

        clearForm();
    }

    @FXML
    private void deleteCast(ActionEvent event) {
        Cast selected = castTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "No cast selected");
            return;
        }
        if (DatabaseHandler.deleteCast(selected.getCastID())) {
            showAlert(Alert.AlertType.INFORMATION, "Cast deleted successfully!");
            loadCasts();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to delete cast");
        }

        clearForm();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // @FXML
    // private void updateCast(ActionEvent event) {
    //     Cast selected = castTable.getSelectionModel().getSelectedItem();
    //     if (selected != null) {
    //         Actor actor = actorComboBox.getValue();
    //         Role role = roleComboBox.getValue();
    //         Content content = contentComboBox.getValue();
    //         if (actor != null && role != null && content != null) {
    //             selected.setActorName(actor.getActorName());
    //             selected.setRoleName(role.getRoleName());
    //             selected.setContentTitle(content.getContentTitle());
    //             if (DatabaseHandler.updateCast(selected)) {
    //                 Alert alert = new Alert(AlertType.INFORMATION);
    //                 alert.setTitle("Success");
    //                 alert.setHeaderText(null);
    //                 alert.setContentText("Cast updated successfully!");
    //                 alert.showAndWait();
    //             } else {
    //                 Alert alert = new Alert(AlertType.ERROR);
    //                 alert.setTitle("Error");
    //                 alert.setHeaderText(null);
    //                 alert.setContentText("Failed to update cast");
    //                 alert.showAndWait();
    //             }
    //             loadCasts();
    //         } else {
    //             Alert alert = new Alert(AlertType.WARNING);
    //             alert.setTitle("Warning");
    //             alert.setHeaderText(null);
    //             alert.setContentText("Please select an actor, role, and content");
    //             alert.showAndWait();
    //         }
    //     } else {
    //         Alert alert = new Alert(AlertType.ERROR);
    //         alert.setTitle("Error");
    //         alert.setHeaderText(null);
    //         alert.setContentText("No cast selected for update");
    //         alert.showAndWait();
    //     }
    // }

// ============================ DIRECTOR ===============================

    private void displayDirector() {
        directorList.clear();

        ResultSet result = DatabaseHandler.getDirector();

        try {
            while (result.next()) {
                int directorID = result.getInt("directorID");
                String directorName = result.getString("directorName");
                Director director = new Director(directorID, directorName);
                directorList.add(director);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        directorTable.setItems(directorList);
    }

    @FXML
    private void createDirector(ActionEvent event) throws IOException {

        String directorName = directorTextField.getText();
        
        directorName = directorName.trim();

        Director director = new Director(0, directorName);

        if (DatabaseHandler.createDirector(director)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Director created successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to create director");
            alert.showAndWait();
        }
        displayDirector();
        clearForm();
    }

    @FXML
    private void deleteDirector (ActionEvent event) {

        Director director = directorTable.getSelectionModel().getSelectedItem();

        int directorID = director.getDirectorID();

        if (DatabaseHandler.deleteDirector(director)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Director deleted successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete director");
            alert.showAndWait();
        }
        displayDirector();
        clearForm();
    }

    @FXML 
    private void updateDirector (ActionEvent event) {

        Director director = directorTable.getSelectionModel().getSelectedItem();

        if (director != null) {
            String newDirectorName = directorTextField.getText().trim();
            if (!newDirectorName.isEmpty()) {
                director.setDirectorName(newDirectorName);
                if (DatabaseHandler.updateDirector(director)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Director updated successfully!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update director");
                    alert.showAndWait();
                }
                displayDirector();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Director name cannot be empty");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No director selected for update");
            alert.showAndWait();
        }
        clearForm();
    }

// === NAVIGATION =====================================================================================
    @FXML
    private void userButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminUser.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void contentButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminContent.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void castButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminCast.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void watchlistButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminWatchlist.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Admin/FXML/AdminHome.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void ratingButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminRating.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void reviewButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminReview.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void likeButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminLike.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
    
    @FXML
    private void dislikeButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/FXML/AdminDislike.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}
