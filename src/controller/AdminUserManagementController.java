package controller;

import DAO.UserDAO;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.Navigation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminUserManagementController {

    @FXML
    private TableView<User> userTableView;
    
    @FXML
    private TableColumn<User, Integer> colID;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colMember;
    @FXML
    private TableColumn<User, Void> colActions;

    @FXML private TextField txtSearch;
    @FXML private Label lblSearchAlert;

    private final UserDAO userDAO = new UserDAO();
    private ObservableList<User> allUsers;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
        loadUsers();
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
        });
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("memberType"));
    }

    private void setupActionColumn() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final HBox hBox = new HBox(10);
            private final ImageView viewIcon = new ImageView(new Image("/assets/viewIconBlack.png"));
            private final ImageView editIcon = new ImageView(new Image("/assets/editIconBlack.png"));
            private final ImageView deleteIcon = new ImageView(new Image("/assets/deleteIconBlack.png"));

            {
                Stream.of(viewIcon, editIcon, deleteIcon).forEach(icon -> {
                    icon.setFitWidth(20);
                    icon.setFitHeight(20);
                    icon.setCursor(Cursor.HAND);
                });

                viewIcon.setOnMouseClicked(e -> openViewPopup(getTableView().getItems().get(getIndex())));
                editIcon.setOnMouseClicked(e -> openUpdatePopup(getTableView().getItems().get(getIndex())));
                deleteIcon.setOnMouseClicked(e -> openDeletePopup(getTableView().getItems().get(getIndex())));

                hBox.getChildren().addAll(viewIcon, editIcon, deleteIcon);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        allUsers = FXCollections.observableArrayList(users);
        userTableView.setItems(allUsers);
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            lblSearchAlert.setText("Search field cannot be empty.");
            userTableView.setItems(allUsers);
            return;
        }

        lblSearchAlert.setText("");
        List<User> filtered = allUsers.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(keyword)
                             || user.getEmail().toLowerCase().contains(keyword)
                             || user.getFullName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        userTableView.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void btnAddUserOnAction(ActionEvent event) {
        try {
            Navigation.openPopup("AdminAddUser.fxml", (AdminAddUserController controller) -> {
                controller.setUser(currentUser);
                controller.setOnUserAdded(this::loadUsers);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openViewPopup(User user) {
        try {
            Navigation.openPopup("AdminViewUser.fxml", (AdminViewUserController controller) -> {
                controller.setUser(user);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUpdatePopup(User user) {
        try {
            Navigation.openPopup("AdminUpdateUser.fxml", (AdminUpdateUserController controller) -> {
                controller.setUser(user);
                controller.setOnUserUpdated(this::loadUsers);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePopup(User user) {
        try {
            Navigation.openPopup("AdminDeleteUser.fxml", (AdminDeleteUserController controller) -> {
                controller.setUser(user);
                controller.setOnUserDeleted(this::loadUsers);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnHomeOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("AdminDashboard.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnLogOutAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
