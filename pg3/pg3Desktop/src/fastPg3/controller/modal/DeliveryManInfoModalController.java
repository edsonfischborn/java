package fastPg3.controller.modal;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.DeliveryMan;
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
public class DeliveryManInfoModalController implements Initializable, Pg3Controller {

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

    private DeliveryMan deliveryMan;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private Pg3Controller fatherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    @Override
    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        addIcons();
    }

    public void show(DeliveryMan d){
        setDeliveryMan(d);
        fillData();
    }

    private void fillData(){
        formMainTitleId.setText(" #" + getDeliveryMan().getId());

        // Form user
        formIdValue.setText(Long.toString(getDeliveryMan().getId()));
        formActiveValue.setText(Integer.toString(getDeliveryMan().getActive()).equals("1") ? "sim" : "não");
        formNameValue.setText(getDeliveryMan().getName());
        formEmailValue.setText(getDeliveryMan().getEmail());
        formPhoneValue.setText(getDeliveryMan().getPhone());
        formCpfValue.setText(getDeliveryMan().getCpf());
        formCreatedAtValue.setText(ComponentsUtil.formatDate(getDeliveryMan().getCreatedAt()));
        formUpdatedAtValue.setText(
                getDeliveryMan().getUpdatedAt() != null ? ComponentsUtil.formatDate(getDeliveryMan().getUpdatedAt()) :
                        "Esse entregador não recebeu atualizações"
        );
    }


    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.ACCOUNT);
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

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
