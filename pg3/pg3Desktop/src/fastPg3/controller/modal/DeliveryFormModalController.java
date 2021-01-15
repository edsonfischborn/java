package fastPg3.controller.modal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.DeliveryScreenController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.CmbOption;
import fastPg3.types.Delivery;
import fastPg3.types.DeliveryDto;
import fastPg3.types.DeliveryState;
import fastPg3.util.AlertMessage;
import fastPg3.util.ApiRequest;
import fastPg3.util.HandleIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class DeliveryFormModalController implements Initializable, Pg3Controller {

    @FXML
    private VBox formMainTitleIconView;

    @FXML
    private Label formMainTitleId;

    @FXML
    private VBox formReceiverTitleIconView;

    @FXML
    private Label formMainTitleText;

    @FXML
    private TextField formReceiverNameField;

    @FXML
    private TextField formReceiverCpfField;

    @FXML
    private TextField formReceiverContactField;

    @FXML
    private VBox formAddressTitleIconView;

    @FXML
    private TextField formAddressStreetField;

    @FXML
    private TextField formAddressDistrictField;

    @FXML
    private TextField formAddressNumberField;

    @FXML
    private TextField formAddressZipCodeField;

    @FXML
    private ComboBox<CmbOption> formAddressStateCombox;

    @FXML
    private ComboBox<CmbOption> formAddressCityCombox;

    @FXML
    private TextField formAddressComplementField;

    @FXML
    private VBox formDeliveryTitleIconView;

    @FXML
    private TextField formDeliveryDescriptionField;

    @FXML
    private VBox formEndTitleIconView;

    @FXML
    private TextField formEndUserIdField;

    @FXML
    private Label formEndDeliveryManLabel;

    @FXML
    private TextField formEndDeliveryManField;

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private Button formSubmitButton;

    private DeliveryDto delivery;

    private JsonArray cities;
    private JsonArray states;

    AlertMessage alertMessage = new AlertMessage();

    private Stage stage;
    private Scene scene;

    private ApiRequest api;
    private ApiRequest ibgeApi = new ApiRequest("https://servicodados.ibge.gov.br/api/v1");
    private Gson gson = new Gson();

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
        addFormAddressStateCombox();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.PENCIL);
        HandleIcon.addMaterialDesignIcon(formReceiverTitleIconView, "modalIcon", MaterialDesignIcon.ACCOUNT);
        HandleIcon.addMaterialDesignIcon(formAddressTitleIconView, "modalIcon", MaterialDesignIcon.MAP_MARKER);
        HandleIcon.addMaterialDesignIcon(formDeliveryTitleIconView, "modalIcon", MaterialDesignIcon.PACKAGE_VARIANT);
        HandleIcon.addMaterialDesignIcon(formEndTitleIconView, "modalIcon", MaterialDesignIcon.CUBE_SEND);
    }

    private void addFormAddressStateCombox(){
        loadStates();

        getStates().forEach( d -> {
            JsonObject obj = d.getAsJsonObject();
            CmbOption option = new CmbOption(obj.get("nome").getAsString(), obj.get("sigla").getAsString());
            formAddressStateCombox.getItems().add(option);
        });
    }

    private void addFormCitiesCombox(String uf){
        loadCities(uf);
        formAddressCityCombox.getItems().clear();

        getCities().forEach( d -> {
            String value = d.getAsJsonObject().get("nome").getAsString();

            CmbOption option = new CmbOption(value, value);
            formAddressCityCombox.getItems().add(option);
        });
    }

    @FXML
    void formAddressStateComboxOnAction(ActionEvent event) {
        CmbOption op = formAddressStateCombox.getSelectionModel().getSelectedItem();
        addFormCitiesCombox(op.getValue());
    }

    public void edit(DeliveryDto delivery){
        setDelivery(delivery);

        formMainTitleText.setText("Edição de encomenda");
        formEndDeliveryManLabel.setText("ID do entregador (obrigatorio)");
        formSubmitButton.setText("Salvar");

        formReceiverNameField.setText(delivery.getReceiverName());
        formReceiverCpfField.setText(delivery.getReceiverCpf());
        formReceiverContactField.setText(delivery.getReceiverContact());

        formAddressStreetField.setText(delivery.getStreet());
        formAddressDistrictField.setText(delivery.getDistrict());
        formAddressNumberField.setText(delivery.getNumber());
        formAddressZipCodeField.setText(delivery.getZipCode());

        String uf = null;
        for(int i = 0; i < formAddressStateCombox.getItems().size(); i++ ){
            if(formAddressStateCombox.getItems().get(i).getValue().equals(delivery.getState())){
                uf = formAddressStateCombox.getItems().get(i).getValue();
                formAddressStateCombox.getSelectionModel().select(i);
            }
        }

        if(uf != null) {
            addFormCitiesCombox(uf);
            for (int i = 0; i < formAddressCityCombox.getItems().size(); i++) {
                String cbCity = formAddressCityCombox.getItems().get(i).getValue().toLowerCase();
                String deliveryCity = delivery.getCity().toLowerCase();
                if (cbCity.equals(deliveryCity)) {
                    formAddressCityCombox.getSelectionModel().select(i);
                }
            }
        }

        formAddressComplementField.setText(delivery.getComplement());
        formDeliveryDescriptionField.setText(delivery.getDescription());
        formEndUserIdField.setText(Long.toString(delivery.getUserId()));
        formEndDeliveryManField.setText(Long.toString(delivery.getDeliveryManId()));
    }

    private Delivery getFormData(){
        Delivery d = new Delivery();

        d.setReceiverName(formReceiverNameField.getText());
        d.setReceiverCpf(formReceiverCpfField.getText());
        d.setReceiverContact(formReceiverContactField.getText());

        d.setStreet(formAddressStreetField.getText());
        d.setDistrict(formAddressDistrictField.getText());
        d.setNumber(formAddressNumberField.getText());
        d.setZipCode(formAddressZipCodeField.getText());
        d.setState(formAddressStateCombox.getSelectionModel().getSelectedItem().getValue());
        d.setCity(formAddressCityCombox.getSelectionModel().getSelectedItem().getValue());
        d.setComplement(formAddressComplementField.getText());

        d.setDescription(formDeliveryDescriptionField.getText());

        d.setUserId(Long.parseLong(formEndUserIdField.getText()));
        d.setDeliveryManId(Long.parseLong(formEndDeliveryManField.getText()));

        return d;
    }

    private void updateDelivery(){
        try {
            Delivery d = getFormData();

            String delivery = gson.toJson(d, Delivery.class);
            JsonObject body = gson.fromJson(delivery, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.put("/delivery/" + getDelivery().getId(), body.toString());

            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível atualizar a encomenda. \n - Verifique se o usuário e entrgador estão ativos", isDarkMode());
        }
    }

    public void createDelivery(){
        try {
            Delivery d = getFormData();

            String delivery = gson.toJson(d, Delivery.class);
            JsonObject body = gson.fromJson(delivery, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.post("/delivery/", body.toString());

            onFinish();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possível criar a encomenda. \n - Verifique se o usuário e entrgador estão ativos", isDarkMode());
        }
    }

    private void onFinish(){
        DeliveryScreenController a = (DeliveryScreenController) getFatherController();
        a.refreshDeliveryData();
        getStage().close();
    }

    @FXML
    void onFormSubmitButtonAction(ActionEvent event) {
        if(getDelivery() != null){
            updateDelivery();
        } else {
            createDelivery();
        }
    }

    private void loadStates(){
        try {
           setStates(ibgeApi.get("/localidades/estados?orderBy=nome").getAsJsonArray());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar a lista de estados " + ex, isDarkMode());
        }
    }

    private void loadCities(String uf){
        try {
            setCities(ibgeApi.get("/localidades/estados/" + uf + "/distritos?orderBy=nome").getAsJsonArray());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar a lista de cidades", isDarkMode());
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

    public DeliveryDto getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryDto delivery) {
        this.delivery = delivery;
    }

    public JsonArray getCities() {
        return cities;
    }

    public void setCities(JsonArray cities) {
        this.cities = cities;
    }

    public JsonArray getStates() {
        return states;
    }

    public void setStates(JsonArray states) {
        this.states = states;
    }
}
