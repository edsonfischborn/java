package fastPg3.controller.modal;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.UserDto;
import fastPg3.util.ApiRequest;
import fastPg3.util.ComponentsUtil;
import fastPg3.util.HandleIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class UserInfoModalController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private VBox formMainTitleIconView;

    @FXML
    private Label formMainTitleId;

    @FXML
    private Label formIdValue;

    @FXML
    private Label formActiveValue;

    @FXML
    private Label formNameValue;

    @FXML
    private Label formEmailValue;

    @FXML
    private Label formPhoneValue;

    @FXML
    private Label formCpfValue;

    @FXML
    private Label formCreatedAtValue;

    @FXML
    private Label formUpdatedAtValue;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private Pg3Controller fatherController;

    UserDto user;

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
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.ACCOUNT);
    }

    public void show(UserDto user){
        setUser(user);
        fillData();
    }

    private void fillData(){
        formMainTitleId.setText(" #" + getUser().getId());

        // Form user
        formIdValue.setText(Long.toString(getUser().getId()));
        formActiveValue.setText(Integer.toString(getUser().getActive()).equals("1") ? "sim" : "não");
        formNameValue.setText(getUser().getName());
        formEmailValue.setText(getUser().getEmail());
        formPhoneValue.setText(getUser().getPhone());
        formCpfValue.setText(getUser().getCpf());
        formCreatedAtValue.setText(ComponentsUtil.formatDate(getUser().getCreatedAt()));
        formUpdatedAtValue.setText(
                getUser().getUpdatedAt() != null ? ComponentsUtil.formatDate(getUser().getUpdatedAt()) :
                        "Este usuário não recebeu atualizações"
                );
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
