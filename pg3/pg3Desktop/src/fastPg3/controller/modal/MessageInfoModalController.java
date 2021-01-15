package fastPg3.controller.modal;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.Message;
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
public class MessageInfoModalController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private VBox formMainTitleIconView;

    @FXML
    private Label formMainTitleId;

    @FXML
    private Label formIdValue;

    @FXML
    private Label formReadValue;

    @FXML
    private Label formNameValue;

    @FXML
    private Label formEmailValue;

    @FXML
    private Label formCreatedAtValue;

    @FXML
    private Label formUpdatedAtValue;

    @FXML
    private Label formMessageValue;

    private Stage stage;
    private Scene scene;

    private Message message;

    private ApiRequest api;

    private Pg3Controller fatherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        addIcons();
    }

    public void show(Message message){
        setMessage(message);
        fillData();
    }

    private void fillData(){
        formMainTitleId.setText(" #" + getMessage().getId());

        // Form user
        formIdValue.setText(Long.toString(getMessage().getId()));
        formReadValue.setText(Integer.toString(getMessage().getRead()).equals("1") ? "sim" : "não");
        formNameValue.setText(getMessage().getName());
        formEmailValue.setText(getMessage().getEmail());
        formMessageValue.setText(getMessage().getMessage());
        formCreatedAtValue.setText(ComponentsUtil.formatDate(getMessage().getCreatedAt()));
        formUpdatedAtValue.setText(
                getMessage().getUpdatedAt() != null ? ComponentsUtil.formatDate(getMessage().getUpdatedAt()) :
                        "Essa mensagem não recebeu atualizações"
        );
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.MESSAGE_TEXT);
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

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
