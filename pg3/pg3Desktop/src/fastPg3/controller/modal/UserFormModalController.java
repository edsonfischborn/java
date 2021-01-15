package fastPg3.controller.modal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.UserScreenController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.User;
import fastPg3.types.UserDto;
import fastPg3.util.AlertMessage;
import fastPg3.util.ApiRequest;
import fastPg3.util.HandleIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class UserFormModalController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private VBox formMainTitleIconView;

    @FXML
    private Label formMainTitle;

    @FXML
    private TextField formNameValue;

    @FXML
    private TextField formCpfValue;

    @FXML
    private TextField formPhoneValue;

    @FXML
    private TextField formEmailValue;

    @FXML
    private TextField formPasswordValue;

    @FXML
    private Label formPasswordLabel;

    @FXML
    private Button formButtonSubmit;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private Pg3Controller fatherController;

    private AlertMessage alertMessage = new AlertMessage();
    private Gson gson = new Gson();

    private UserDto user;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    @Override
    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        addIcons();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.PENCIL);
    };

    public void edit(UserDto user){
        setUser(user);

        formMainTitle.setText("Edição de usuário");
        formButtonSubmit.setText("Salvar");
        formPasswordLabel.setText("Senha (vazio ou menor que 6 digitos para manter)");

        formNameValue.setText(getUser().getName());
        formEmailValue.setText(getUser().getEmail());
        formPhoneValue.setText(getUser().getPhone());
        formCpfValue.setText(getUser().getCpf());
    }

    private User getFormData(){
        User user = new User();

        user.setName(formNameValue.getText());
        user.setEmail(formEmailValue.getText());
        user.setPhone(formPhoneValue.getText());
        user.setCpf(formCpfValue.getText());
        user.setPassword(formPasswordValue.getText());

        return user;
    }

    public void createUser(){
        try {
            User u = getFormData();

            String user = gson.toJson(u, User.class);
            JsonObject body = gson.fromJson(user, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.post("/user/", body.toString());

            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível criar o usuario", isDarkMode());
        }
    }

    public void updateUser(){
        try {
            User u = getFormData();

            if(u.getPassword().length() < 6){
                u.setPassword("");
            }

            String user = gson.toJson(u, User.class);
            JsonObject body = gson.fromJson(user, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.put("/user/" + getUser().getId(), body.toString());

            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível atualizar o usuario " + ex, isDarkMode());
        }
    }

    private void onFinish(){
        UserScreenController c = (UserScreenController) getFatherController();
        c.refreshUserData();
        getStage().close();
    }

    @FXML
    void formButtonSubmitOnAction(ActionEvent event) {
        if(getUser() != null){
            updateUser();
        } else {
            createUser();
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

    public Pg3Controller getFatherController() {
        return fatherController;
    }

    public void setFatherController(Pg3Controller fatherController) {
        this.fatherController = fatherController;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
