package fastPg3.controller.modal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.DeliveryDetailDto;
import fastPg3.util.AlertMessage;
import fastPg3.util.ApiRequest;
import fastPg3.util.ComponentsUtil;
import fastPg3.util.HandleIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryInfoModalController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private VBox formMainTitleIconView;

    @FXML
    private Label formMainTitleId;

    @FXML
    private VBox formGeneralTitleIconView;

    @FXML
    private Label formGeneralIdValue;

    @FXML
    private Label formGeneralStatusValue;

    @FXML
    private Label formGeneralCreateAtValue;

    @FXML
    private Label formGeneralUpdateAtValue;

    @FXML
    private Label formGeneralDescriptionValue;

    @FXML
    private VBox formAddressTitleIconView;

    @FXML
    private Label formAddressStreetValue;

    @FXML
    private Label formAddressNumberValue;

    @FXML
    private Label formAddressDistrictValue;

    @FXML
    private Label formAddressZipCodeValue;

    @FXML
    private Label formAddressStateValue;

    @FXML
    private Label formAddressCityValue;

    @FXML
    private Label formAddressComplementValue;

    @FXML
    private VBox formReceiverTitleIconView;

    @FXML
    private Label formReceiverNameValue;

    @FXML
    private Label formReceiverCpfValue;

    @FXML
    private Label formReceiverContactValue;

    @FXML
    private VBox formDeliveryManTitleIconView;

    @FXML
    private Label formDeliveryManIdValue;

    @FXML
    private Label formDeliveryManActiveValue;

    @FXML
    private Label formDeliveryManNameValue;

    @FXML
    private Label formDeliveryManEmailValue;

    @FXML
    private Label formDeliveryManPhoneValue;

    @FXML
    private Label formDeliveryManCpfValue;

    @FXML
    private VBox formUserTitleIconView;

    @FXML
    private Label formUserIdValue;

    @FXML
    private Label formUserActiveValue;

    @FXML
    private Label formUserNameValue;

    @FXML
    private Label formUserEmailValue;

    @FXML
    private Label formUserPhoneValue;

    @FXML
    private Label formUserCpfValue;

    @FXML
    private VBox formDocumentsTitleIconView;

    @FXML
    private Label formDocumentsReceiverSignedDocument;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private DeliveryDetailDto delivery;

    private Pg3Controller fatherController;

    private AlertMessage alertMessage = new AlertMessage();
    Gson gson = new Gson();

    @Override
    public void initialize(URL location, ResourceBundle resources){ }

    @Override
    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        addIcons();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(formMainTitleIconView, "modalIcon", MaterialDesignIcon.FORMAT_LIST_BULLETED);
        HandleIcon.addMaterialDesignIcon(formGeneralTitleIconView, "modalIcon", MaterialDesignIcon.INFORMATION_OUTLINE);
        HandleIcon.addMaterialDesignIcon(formReceiverTitleIconView, "modalIcon", MaterialDesignIcon.ACCOUNT);
        HandleIcon.addMaterialDesignIcon(formAddressTitleIconView, "modalIcon", MaterialDesignIcon.MAP_MARKER);
        HandleIcon.addMaterialDesignIcon(formUserTitleIconView, "modalIcon", MaterialDesignIcon.ACCOUNT_STAR);
        HandleIcon.addMaterialDesignIcon(formDeliveryManTitleIconView, "modalIcon", MaterialDesignIcon.TRUCK_DELIVERY);
        HandleIcon.addMaterialDesignIcon(formDocumentsTitleIconView, "modalIcon", MaterialDesignIcon.FILE_DOCUMENT);
    }

    public void show(Long deliveryId){
        loadDelivery(deliveryId);
        fillData();
    }

    private void fillData(){
        // Title
        formMainTitleId.setText("#" + getDelivery().getId());

        // Delivery state
        formGeneralStatusValue.setText(getDelivery().getDeliveryState().getStateName());
        formGeneralStatusValue.setTextFill(Color.valueOf(getDelivery().getDeliveryState().getStateColor()));

        // Form general
        formGeneralCreateAtValue.setText(ComponentsUtil.formatDate(getDelivery().getCreatedAt()));
        formGeneralUpdateAtValue.setText( getDelivery().getUpdatedAt() != null
                ? ComponentsUtil.formatDate(getDelivery().getUpdatedAt()) :
                "Essa encomenda não recebeu atualizações");
        formGeneralIdValue.setText(Long.toString(getDelivery().getId()));
        formGeneralDescriptionValue.setText(getDelivery().getDescription());

        // Address
        formAddressStreetValue.setText(getDelivery().getStreet());
        formAddressStateValue.setText(getDelivery().getState());
        formAddressNumberValue.setText(getDelivery().getNumber());
        formAddressDistrictValue.setText(getDelivery().getDistrict());
        formAddressZipCodeValue.setText(getDelivery().getZipCode());
        formAddressCityValue.setText(getDelivery().getCity());
        formAddressComplementValue.setText(
                getDelivery().getComplement().equals("") ? "Não informado" :
                        getDelivery().getComplement()
        );

        // Form receiver
        formReceiverNameValue.setText(getDelivery().getReceiverName());
        formReceiverCpfValue.setText(getDelivery().getReceiverCpf());
        formReceiverContactValue.setText(getDelivery().getReceiverContact());

        // Form delivery man
        formDeliveryManIdValue.setText(Long.toString(getDelivery().getDeliveryManId()));
        formDeliveryManActiveValue.setText(Long.toString(getDelivery().getDeliveryMan().getActive()).equals("1") ? "sim" : "não");
        formDeliveryManNameValue.setText(getDelivery().getDeliveryMan().getName());
        formDeliveryManEmailValue.setText(getDelivery().getDeliveryMan().getEmail());
        formDeliveryManPhoneValue.setText(getDelivery().getDeliveryMan().getPhone());
        formDeliveryManCpfValue.setText(getDelivery().getDeliveryMan().getCpf());

        // Form user
        formUserIdValue.setText(Long.toString(getDelivery().getUserId()));
        formUserActiveValue.setText(Long.toString(getDelivery().getUser().getActive()).equals("1") ? "sim" : "não");
        formUserNameValue.setText(getDelivery().getUser().getName());
        formUserEmailValue.setText(getDelivery().getUser().getEmail());
        formUserPhoneValue.setText(getDelivery().getUser().getPhone());
        formUserCpfValue.setText(getDelivery().getUser().getCpf());

        // Form document
        if(getDelivery().getReceiverSignature() == null || getDelivery().getReceiverSignature().equals("")){
           formDocumentsReceiverSignedDocument.setVisible(false);
        }
    }

    private void loadDelivery(Long id){
        try {
            setDelivery(api.get("/delivery/" + id ).getAsJsonObject());
        } catch(Exception e) {
            alertMessage.showErrorMessage("Não foi possivel carregar os dados da entrega", isDarkMode());
        }
    }

    private JsonObject loadReceiverBlankDocument(Long id) throws Exception{
        return api.get("/delivery/" + id + "/file/receiverdocument").getAsJsonObject();
    }

    @FXML
    void formDocumentsReceiverBlankDocumentOnAction(MouseEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("declaracaoRecebimento.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

            File outputFile = fileChooser.showSaveDialog(getStage());

            if (outputFile != null) {
                FileOutputStream receiverSignature = new FileOutputStream(outputFile);

                String base64 = loadReceiverBlankDocument(getDelivery().getId()).get("base64").getAsString();
                byte[] data = Base64.getDecoder().decode(base64);
                receiverSignature.write(data);
                receiverSignature.close();
            }
        } catch(Exception ex){
            alertMessage.showErrorMessage("Não foi possível salvar o arquivo", isDarkMode());
        }
    }

    @FXML
    void formDocumentsReceiverSignedDocumentOnAction(MouseEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("declaracaoRecebimentoAssinada.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

            File outputFile = fileChooser.showSaveDialog(getStage());

            if (outputFile != null) {
                FileOutputStream receiverSignature = new FileOutputStream(outputFile);

                byte[] data = Base64.getDecoder().decode(getDelivery().getReceiverSignature());
                receiverSignature.write(data);
                receiverSignature.close();
            }
        } catch(Exception ex){
            alertMessage.showErrorMessage("Não foi possível salvar o arquivo assinado pelo usuário", isDarkMode());
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

    public DeliveryDetailDto getDelivery() {
        return delivery;
    }

    public void setDelivery(JsonObject delivery) {
        this.delivery = gson.fromJson(delivery, DeliveryDetailDto.class);
    }

    public Pg3Controller getFatherController() {
        return fatherController;
    }

    public void setFatherController(Pg3Controller fatherController) {
        this.fatherController = fatherController;
    }
}
