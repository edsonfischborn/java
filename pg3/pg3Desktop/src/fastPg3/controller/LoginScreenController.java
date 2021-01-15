package fastPg3.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.UserLogin;
import fastPg3.util.ApiRequest;
import fastPg3.util.HandleIcon;
import fastPg3.util.AlertMessage;
import fastPg3.util.ScreenLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class LoginScreenController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox logoView;

    @FXML
    private Button loginBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private VBox emailLogoView;

    @FXML
    private VBox passwordLogoView;

    @FXML
    private VBox formLogoView;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField userNameInput;

    private Stage stage;
    private Scene scene;

    AlertMessage alertMessage = new AlertMessage();

    private ApiRequest api = new ApiRequest();
    private Gson gson = new Gson();

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void start(ApiRequest api) {
        onInit();

        getStage().initStyle(StageStyle.TRANSPARENT);
        getScene().setFill(Color.TRANSPARENT);

        setApi(api);

        // Unfocus inputs
        Platform.runLater( () -> logoView.requestFocus() );

        // Add screen icons
        addIcons();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(logoView,"boxIcon", MaterialDesignIcon.PACKAGE_VARIANT_CLOSED);
        HandleIcon.addFontAwsmIcon(formLogoView,"formIcon", FontAwesomeIcon.GEAR);
        HandleIcon.addMaterialDesignIcon(emailLogoView,"inputIcon", MaterialDesignIcon.EMAIL_OUTLINE );
        HandleIcon.addMaterialDesignIcon(passwordLogoView,"inputIcon", MaterialDesignIcon.LOCK_OUTLINE );
    }

    @FXML
    void closeBtnOnAction(ActionEvent event) {
        getStage().close();
    }

    @FXML
    void loginBtnOnAction(ActionEvent event) {
        String password = passwordInput.getText();
        String userName = userNameInput.getText();

        if(!userName.equals("") && !password.equals("")) {
            UserLogin userLogin = new UserLogin(userName, password);
            String requestBody = gson.toJson(userLogin, UserLogin.class);

            try {
                JsonObject response = api.post("/login", requestBody).getAsJsonObject();
                String token = "Bearer " + response.get("token").getAsString();
                String[] auth = {"Authorization", token};

                api.getHeaders().add(auth);

                ScreenLoader loader = new ScreenLoader("MainScreen");
                MainScreenController c = loader.getController();

                getStage().close();
                c.start(isDarkMode(), getApi());
            } catch(Exception ex){
                alertMessage.showErrorMessage("Usuário ou senha invalidos", isDarkMode());
            }
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
        return false;
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

}
