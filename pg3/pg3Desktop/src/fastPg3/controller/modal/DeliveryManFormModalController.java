package fastPg3.controller.modal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.DeliveryManScreenController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.DeliveryMan;
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
public class DeliveryManFormModalController implements Initializable, Pg3Controller {

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
    private Button formButtonSubmit;

    private ApiRequest api;

    private AlertMessage alertMessage = new AlertMessage();
    private Gson gson = new Gson();
    private DeliveryMan deliveryMan;

    private Stage stage;
    private Scene scene;

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

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.PENCIL);
    }

    public void edit(DeliveryMan deliveryMan){
        setDeliveryMan(deliveryMan);

        formMainTitle.setText("Edição de entregador");
        formButtonSubmit.setText("Salvar");

        formNameValue.setText(getDeliveryMan().getName());
        formEmailValue.setText(getDeliveryMan().getEmail());
        formPhoneValue.setText(getDeliveryMan().getPhone());
        formCpfValue.setText(getDeliveryMan().getCpf());
    }

    private DeliveryMan getFormData(){
        DeliveryMan deliveryMan = new DeliveryMan();

        deliveryMan.setName(formNameValue.getText());
        deliveryMan.setEmail(formEmailValue.getText());
        deliveryMan.setPhone(formPhoneValue.getText());
        deliveryMan.setCpf(formCpfValue.getText());

        return deliveryMan;
    }

    private String prepareData(){
        DeliveryMan d = getFormData();

        String deliveryMan = gson.toJson(d, DeliveryMan.class);
        JsonObject body = gson.fromJson(deliveryMan, JsonObject.class);
        body.remove("createdAt");
        body.remove("updatedAt");

        return body.toString();
    }

   private void createDeliveryMan(){
        try {
            api.post("/deliveryman/", prepareData());
            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível criar o entregador", isDarkMode());
        }
    }

    private void updateDeliveryMan(){
        try {
            api.put("/deliveryman/" + getDeliveryMan().getId(), prepareData());
            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível atualizar o entregador " + ex, isDarkMode());
        }
    }

    private void onFinish(){
        DeliveryManScreenController c = (DeliveryManScreenController) getFatherController();
        c.refreshDeliveryManData();
        getStage().close();
    }

    @FXML
    void formButtonSubmitOnAction(ActionEvent event) {
        if(getDeliveryMan() != null){
            updateDeliveryMan();
        } else {
            createDeliveryMan();
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

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
