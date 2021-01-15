package fastPg3.controller;

import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class MainScreenController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox menuIconView;

    @FXML
    private VBox menuItems;

    @FXML
    private Label menuCopyrightText;

    @FXML
    private VBox menuGithubIconView;

    @FXML
    private Pane headerInfo;

    @FXML
    private Label headerDateText;

    @FXML
    private VBox headerNightBtnIconView;

    @FXML
    private ToggleButton headerNightBtn;

    @FXML
    private VBox headerAccountIconView;

    @FXML
    private Label headerAccountText;

    @FXML
    private Pane screenContainer;

    private Stage stage;
    private Scene scene;

    private boolean darkMode;
    private ApiRequest api;

    private AlertMessage alertMessage = new AlertMessage();

    private String selectedScreen;

    private JsonObject user;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void start(boolean darkMode, ApiRequest api) {
        onInit();

        setDarkMode(darkMode);
        setApi(api);

        getStage().setTitle("FAST PG3 | Sistema");
        getStage().setScene(getScene());
        getStage().show();
        getStage().setMinWidth(950);
        getStage().setMinHeight(600);

        fillData();

        selectDefaultScreen();
    }

    private void fillData(){
        try {
            loadUser();

            if(!isAdmin()){
                alertMessage.showErrorMessage("Erro, você não é um administrador", isDarkMode());
                getStage().close();
                return;
            }

            headerDateText.setText(ComponentsUtil.formatDate(new Date()).split("as")[0]);
            menuCopyrightText.setText("@FAST PGIII " + Calendar.getInstance().get(Calendar.YEAR));
            headerAccountText.setText(user.get("email").getAsString());

            addIcons();
            createMenuItems();

        } catch(Exception ex) {
            alertMessage.showErrorMessage("Erro ao carregar as informações", isDarkMode());
            getStage().close();
        }
    }

    private void selectDefaultScreen(){
        changeMainScreen("DeliveryScreen");
        HBox item = (HBox) menuItems.getChildren().get(0);
        item.getChildren().get(1).getStyleClass().add("menuItemTextSelected");
    }

    private void addIcons(){
        // Aside menu
        HandleIcon.addMaterialDesignIcon(menuIconView, "menuIcon", MaterialDesignIcon.PACKAGE_VARIANT_CLOSED);
        HandleIcon.addMaterialDesignIcon(menuGithubIconView, "defaultIcon", MaterialDesignIcon.GITHUB_CIRCLE);

        // Header
        HandleIcon.addMaterialDesignIcon(headerAccountIconView, "defaultIcon", MaterialDesignIcon.ACCOUNT_OUTLINE);
        HandleIcon.addMaterialDesignIcon(headerNightBtnIconView, "defaultIcon", MaterialDesignIcon.WEATHER_SUNNY);
    }

    private void createMenuItems(){
        addMenuItem(MaterialDesignIcon.CUBE_SEND, "Entregas", "Delivery");
        addMenuItem(MaterialDesignIcon.ACCOUNT_OUTLINE, "Usuários", "User");
        addMenuItem(MaterialDesignIcon.TRUCK_DELIVERY, "Entregadores", "DeliveryMan");
        addMenuItem(MaterialDesignIcon.MESSAGE_TEXT_OUTLINE, "Mensagens", "Message");
    }

    private void addMenuItem(MaterialDesignIcon icon, String text, String id){
        HBox ct = new HBox();
        ct.getStyleClass().add("menuOptionContainer");
        ct.setId(id);

        HandleIcon.addMaterialDesignIcon(ct,"menuItemIcon", icon);

        Label label = new Label(text);
        label.getStyleClass().add("menuItemText");
        ct.getChildren().add(label);

        ct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            HBox target = (HBox) e.getSource();

            menuItems.getChildren().forEach(item -> {
                HBox menuItem = (HBox) item;
                menuItem.getChildren().get(1).getStyleClass().remove("menuItemTextSelected");
            });

            target.getChildren().get(1).getStyleClass().add("menuItemTextSelected");
            this.changeMainScreen(target.getId() + "Screen");
        });

        menuItems.getChildren().add(ct);
    }

    @FXML
    private void headerNightBtnOnAction(ActionEvent event) {
        headerNightBtnIconView.getChildren().remove(0);

        if(headerNightBtn.isSelected()) {
            setDarkMode(true);
            mainAcPane.getStylesheets().add("fastPg3/assets/darkTheme.css");
            HandleIcon.addMaterialDesignIcon(headerNightBtnIconView, "defaultIcon", MaterialDesignIcon.WEATHER_NIGHT);
        } else{
            setDarkMode(false);
            mainAcPane.getStylesheets().remove("fastPg3/assets/darkTheme.css");
            HandleIcon.addMaterialDesignIcon(headerNightBtnIconView, "defaultIcon", MaterialDesignIcon.WEATHER_SUNNY);
        }
    }

    private void changeMainScreen(String screenName){
        try {
            setSelectedScreen(screenName);

            ScreenLoader loader = new ScreenLoader(screenName);
            AnchorPane acPane = (AnchorPane) loader.getParent();

            acPane.prefWidthProperty().bind(screenContainer.widthProperty());
            acPane.prefHeightProperty().bind(screenContainer.heightProperty());

            screenContainer.getChildren().add(loader.getParent());

            Pg3Controller c = loader.getController();
            c.start(this);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void loadUser() throws Exception{
        JsonObject decodedToken = TokenUtil.decode(api.getHeaders().get(2)[1]);
        String id = decodedToken.get("jti").getAsString();

        setUser(api.get("/user/" + id).getAsJsonObject());
    }

    private boolean isAdmin(){
        return user.get("admin").getAsString().equals("1");
    }

    @Override
    public void setStage(){
        this.stage = new Stage();
    }

    @Override
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void setScene(){
        this.scene = new Scene(mainAcPane);
    }

    @Override
    public Scene getScene(){
        return this.scene;
    }

    @Override
    public boolean isDarkMode() {
        return darkMode;
    }

    @Override
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getSelectedScreen() {
        return selectedScreen;
    }

    public void setSelectedScreen(String selectedScreen) {
        this.selectedScreen = selectedScreen;
    }

    @Override
    public ApiRequest getApi() {
        return api;
    }

    @Override
    public void setApi(ApiRequest api) {
        this.api = api;
    }

    public JsonObject getUser() {
        return user;
    }

    public void setUser(JsonObject user) {
        this.user = user;
    }
}
