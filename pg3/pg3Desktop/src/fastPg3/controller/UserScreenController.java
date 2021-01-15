package fastPg3.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.modal.UserFormModalController;
import fastPg3.controller.modal.UserInfoModalController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.CmbOption;
import fastPg3.types.UserDto;
import fastPg3.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class UserScreenController implements Initializable, Pg3Controller {
    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox smallCardContainer;

    @FXML
    private VBox userListSectionIconView;

    @FXML
    private TextField formUserNameValue;

    @FXML
    private ComboBox<CmbOption> formUserActiveValue;

    @FXML
    private HBox tableActionsContainer;

    @FXML
    private TableView<UserDto> mainTable;

    @FXML
    private Pagination mainTablePagination;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private AlertMessage alertMessage = new AlertMessage();

    private Gson gson = new Gson();

    private JsonObject users;
    private JsonObject userStatus;

    private Pg3Controller fatherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        fillData();
    }

    private void fillData(){
        addIcons();

        mainTablePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            mainTablePagination.setVisible(false);
            loadUsers();
            addTableData();
            mainTablePagination.setVisible(true);
        });

        addTableActions();
        addUserActiveOptions();
        refreshUserData();
    }

    public void refreshUserData(){
        smallCardContainer.getChildren().clear();

        loadUsers();
        loadUserStatus();
        addSmallCards();
        addTableData();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(userListSectionIconView, "sectionIcon", MaterialDesignIcon.FORMAT_LIST_BULLETED);
    }

    private void addSmallCards(){
        ComponentsUtil.addSmallCard("Usuários", Integer.toString(getUserStatus().get("allUsers").getAsInt() - 1),
                MaterialDesignIcon.INFORMATION_OUTLINE, "infoBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Ativos", Integer.toString(getUserStatus().get("activeUsers").getAsInt() - 1),
                MaterialDesignIcon.CHECK, "successBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Inativos", getUserStatus().get("inactiveUsers").getAsString(),
                MaterialDesignIcon.CLOSE, "dangerBgColor", smallCardContainer);
    }

    private void addUserActiveOptions(){
        formUserActiveValue.getItems().addAll(
                new CmbOption("Ativos", "1"),
                new CmbOption("Inativos", "0")
        );

        formUserActiveValue.getSelectionModel().select(0);
    }

    private void addTableActions(){
        ComponentsUtil.addTableAction(tableActionsContainer, "Adicionar", MaterialDesignIcon.PLUS, "indigoBgColor",
                evt -> createUser());

        ComponentsUtil.addTableAction(tableActionsContainer, "Visualizar", MaterialDesignIcon.EYE, "infoBgColor",
                evt -> showUser());

        ComponentsUtil.addTableAction(tableActionsContainer, "Editar", MaterialDesignIcon.PENCIL, "pg3BgColor",
                evt -> editUser());

        ComponentsUtil.addTableAction(tableActionsContainer, "Ativar", MaterialDesignIcon.CHECK, "successBgColor",
                evt -> this.changeUserActive(1));

        ComponentsUtil.addTableAction(tableActionsContainer, "Desativar", MaterialDesignIcon.CLOSE, "dangerBgColor",
                evt -> this.changeUserActive(0));
    }

    private void addTableData(){
        mainTable.getItems().clear();
        mainTable.getColumns().clear();

        mainTablePagination.setPageCount(getUsers().get("totalPages").getAsInt());

        JsonArray users = getUsers().getAsJsonArray("content");
        users.forEach( u -> {
            UserDto user = gson.fromJson(u, UserDto.class);

            if(user.getAdmin() == 0) {
                mainTable.getItems().add(user);
            }
        });

        TableColumn<UserDto, String> id =
                createTableColumn("id", "columnSmall",
                        d -> new SimpleStringProperty(Long.toString(d.getValue().getId())));

        TableColumn<UserDto, String> active =
                createTableColumn("ativo", "columnSmall",
                        d -> new SimpleStringProperty(d.getValue().getActive() == 1 ? "sim" : "não"));

        TableColumn<UserDto, String> name =
                createTableColumn("nome",  "columnLarge",
                        d -> new SimpleStringProperty(d.getValue().getName()));

        TableColumn<UserDto, String> cpf =
                createTableColumn("cpf", "",
                        d -> new SimpleStringProperty(d.getValue().getCpf()));

        TableColumn<UserDto, String> phone =
                createTableColumn("telefone", "",
                        d -> new SimpleStringProperty(d.getValue().getPhone()));

        TableColumn<UserDto, String> eMail =
                createTableColumn("e-mail", "columnBig",
                        d -> new SimpleStringProperty(d.getValue().getEmail()));

        mainTable.getColumns().addAll(id, active, name,  cpf, phone, eMail);
    }

    private TableColumn createTableColumn(String title, String className, Callback<TableColumn.CellDataFeatures<UserDto, String>, ObservableValue<String>> c){
        TableColumn<UserDto, String> column = new TableColumn<>(title);
        column.getStyleClass().add(className);
        column.setCellValueFactory(c);

        return column;
    }

    @FXML
    void tableFilterButtonOnAction(ActionEvent event) {
        loadUsers();
        addTableData();
    }

    private void showUser(){
        UserDto user = mainTable.getSelectionModel().getSelectedItem();
        if(user == null) return;
        UserInfoModalController controller =
                (UserInfoModalController) ShowModal.show("UserInfoModal", "Detalhes do usuário", isDarkMode(), scene);

        controller.start(this);
        controller.show(user);
    }

    private void editUser(){
        UserDto user = mainTable.getSelectionModel().getSelectedItem();
        if(user == null) return;
        UserFormModalController controller =
                (UserFormModalController) ShowModal.show("UserFormModal", "Edição do usuário", isDarkMode(), scene);

        controller.start(this);
        controller.edit(user);
    }

    private void createUser(){
        UserFormModalController controller =
                (UserFormModalController) ShowModal.show("UserFormModal", "Adicionar usuário", isDarkMode(), scene);

        controller.start(this);
    }

    private void changeUserActive(int active){
        try {
            UserDto user = mainTable.getSelectionModel().getSelectedItem();

            if(user == null || active == user.getActive()) return;

            user.setActive(active);

            String userData = gson.toJson(user, UserDto.class);
            JsonObject body = gson.fromJson(userData, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");
            body.addProperty("password", "");

            api.put("/user/" + user.getId(), body.toString());

            refreshUserData();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possivel atualizar o status do usuario \n - Verifique se o usuário possui encomendas pendentes", isDarkMode());
        }
    }

    private void loadUsers(){
        try {
            String params = "?sort=id,desc";

            if(!formUserNameValue.getText().equals("")){
                params += "&name=" + URLEncoder.encode(formUserNameValue.getText(), "utf-8").trim();
            }

            if(formUserActiveValue.getSelectionModel().getSelectedIndex() >= 0){
                params += "&active=" + formUserActiveValue.getSelectionModel().getSelectedItem().getValue();
            }

            params += "&page=" + mainTablePagination.getCurrentPageIndex();

            setUsers(api.get("/user" + params).getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os usuários", isDarkMode());
        }
    }

    private void loadUserStatus(){
        try {
            setUserStatus(api.get("/status/user").getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os status dos usuários", isDarkMode());
        }
    }

    @Override
    public void setStage(){
        this.stage = (Stage) mainAcPane.getScene().getWindow();
    }

    @Override
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void setScene(){
        this.scene = mainAcPane.getScene();
    }

    @Override
    public Scene getScene(){
        return this.scene;
    }

    @Override
    public boolean isDarkMode() {
        return fatherController.isDarkMode();
    }

    @Override
    public void setDarkMode(boolean darkMode) {}

    @Override
    public ApiRequest getApi() {
        return api;
    }

    @Override
    public void setApi(ApiRequest api) {
        this.api = api;
    }

    public JsonObject getUsers() {
        return users;
    }

    public void setUsers(JsonObject users) {
        this.users = users;
    }

    public JsonObject getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(JsonObject userStatus) {
        this.userStatus = userStatus;
    }

    public Pg3Controller getFatherController() {
        return fatherController;
    }

    public void setFatherController(Pg3Controller fatherController) {
        this.fatherController = fatherController;
    }
}
