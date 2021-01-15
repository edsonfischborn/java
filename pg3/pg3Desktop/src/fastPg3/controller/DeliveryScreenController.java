package fastPg3.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.modal.DeliveryFormModalController;
import fastPg3.controller.modal.DeliveryInfoModalController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.DeliveryDto;
import fastPg3.types.DeliveryState;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryScreenController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox smallCardContainer;

    @FXML
    private VBox deliveryListSectionIconView;

    @FXML
    private TextField formReceiverNameValue;

    @FXML
    private TextField formUserIdValue;

    @FXML
    private TextField formDeliveryManIdValue;

    @FXML
    private ComboBox<DeliveryState> formDeliveryStateValue;

    @FXML
    private HBox tableActionsContainer;

    @FXML
    private Pagination mainTablePagination;

    @FXML
    private VBox chartSectionIconView;

    @FXML
    private VBox deliveryCountChart;

    @FXML
    private VBox badDeliveryChart;

    @FXML
    private TableView<DeliveryDto> mainTable;

    private Stage stage;
    private Scene scene;

    private AlertMessage alertMessage = new AlertMessage();

    private Gson gson = new Gson();

    private JsonObject deliveries;
    private JsonObject deliveriesStatus;
    private JsonObject deliveriesState;

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

        fillData();
    }

    private void fillData(){
        addIcons();
        addTableActions();
        loadDeliveriesState();
        addFormDeliveryStateOptions();

        mainTablePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            mainTablePagination.setVisible(false);
            loadDeliveries();
            addTableData();
            mainTablePagination.setVisible(true);
        });

        refreshDeliveryData();
    }

    public void refreshDeliveryData(){
        deliveryCountChart.getChildren().clear();
        badDeliveryChart.getChildren().clear();
        smallCardContainer.getChildren().clear();

        loadDeliveriesStatus();
        loadDeliveries();
        addTableData();
        addSmallCards();
        addCharts();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(chartSectionIconView, "sectionIcon",
                MaterialDesignIcon.CHART_LINE);

        HandleIcon.addMaterialDesignIcon(deliveryListSectionIconView, "sectionIcon",
                MaterialDesignIcon.FORMAT_LIST_BULLETED);
    }

    private void addSmallCards(){
        ComponentsUtil.addSmallCard("Encomendas", deliveriesStatus.get("allDeliveries").getAsString(),
                MaterialDesignIcon.INFORMATION_OUTLINE, "infoBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Entregues", deliveriesStatus.get("deliveredDeliveries").getAsString(),
                MaterialDesignIcon.CHECK, "successBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Pendentes", deliveriesStatus.get("pendingDeliveries").getAsString(),
                MaterialDesignIcon.CLOCK, "alertBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Com problemas", deliveriesStatus.get("problemsDeliveries").getAsString(),
                MaterialDesignIcon.ALERT_OUTLINE, "dangerBgColor", smallCardContainer);
    }

    private void addCharts(){
        ComponentsUtil.addChart(deliveryCountChart, deliveriesStatus.getAsJsonArray("lastDaysDeliveredDeliveries"),
                "Total de encomendas", "chartInfo");

        ComponentsUtil.addChart(badDeliveryChart, deliveriesStatus.getAsJsonArray("lastDaysProblemsDeliveries"),
                "Encomendas com problemas", "chartDanger");
    }

    private void addFormDeliveryStateOptions(){
        JsonArray states = getDeliveriesState().getAsJsonArray("content");
        states.forEach( d -> {
            DeliveryState state = gson.fromJson(d, DeliveryState.class);
            formDeliveryStateValue.getItems().add(state);
        });

        formDeliveryStateValue.getSelectionModel().select(0);
    }

    private void addTableActions(){
        ComponentsUtil.addTableAction(tableActionsContainer, "Adicionar", MaterialDesignIcon.PLUS, "indigoBgColor",
                evt -> tableAddDeliveryButtonOnAction());

        ComponentsUtil.addTableAction(tableActionsContainer, "Visualizar", MaterialDesignIcon.EYE, "infoBgColor",
                evt -> tableShowDeliveryOnAction());

        ComponentsUtil.addTableAction(tableActionsContainer, "Editar", MaterialDesignIcon.PENCIL, "pg3BgColor",
                evt -> tableEditDeliveryButtonOnAction());

        ComponentsUtil.addTableAction(tableActionsContainer, "Definir entregue",
                MaterialDesignIcon.CHECK, "successBgColor", evt -> this.deliver());

        ComponentsUtil.addTableAction(tableActionsContainer, "Definir pendente",
                MaterialDesignIcon.CLOCK, "alertBgColor", evt -> this.changeDeliveryStatus(1));

        ComponentsUtil.addTableAction(tableActionsContainer, "Definir problema",
                MaterialDesignIcon.ALERT_OUTLINE, "dangerBgColor", evt -> this.changeDeliveryStatus(3));
    }

    private void addTableData(){
        mainTable.getItems().clear();
        mainTable.getColumns().clear();

        mainTablePagination.setPageCount(getDeliveries().get("totalPages").getAsInt());

        JsonArray deliveries = getDeliveries().getAsJsonArray("content");
        deliveries.forEach( d -> {
            DeliveryDto delivery = gson.fromJson(d, DeliveryDto.class);
            mainTable.getItems().add(delivery);
        });

        TableColumn<DeliveryDto, String> id =
                createTableColumn("id", "columnSmall",
                        d -> new SimpleStringProperty(Long.toString(d.getValue().getId())));

        TableColumn<DeliveryDto, String> userId =
                createTableColumn("Uid", "columnSmall",
                        d -> new SimpleStringProperty(Long.toString(d.getValue().getUserId())));

        TableColumn<DeliveryDto, String> deliveryManId =
                createTableColumn("Eid", "columnSmall",
                        d -> new SimpleStringProperty(Long.toString(d.getValue().getDeliveryManId())));

        TableColumn<DeliveryDto, String> receiverName =
                createTableColumn("destinatário", "columnLarge",
                        d -> new SimpleStringProperty(d.getValue().getReceiverName()));

        TableColumn<DeliveryDto, String> receiverCpf =
                createTableColumn("cpf", "",
                        d -> new SimpleStringProperty(d.getValue().getReceiverCpf()));

        TableColumn<DeliveryDto, String> city =
                createTableColumn("cidade", "columnLarge",
                        d -> new SimpleStringProperty(d.getValue().getCity()));

        TableColumn<DeliveryDto, String> state =
                createTableColumn("uf", "columnSmall",
                        d -> new SimpleStringProperty(d.getValue().getState()));

        TableColumn<DeliveryDto, String> status =
                createTableColumn("status", "",
                        d -> new SimpleStringProperty(d.getValue().getDeliveryState().getStateName()));

        mainTable.getColumns().addAll(id, userId, deliveryManId, state, city, receiverName, receiverCpf, status);
    }

    private TableColumn createTableColumn(String title, String className, Callback<TableColumn.CellDataFeatures<DeliveryDto, String>, ObservableValue<String>> c){
        TableColumn<DeliveryDto, String> column = new TableColumn<>(title);
        column.getStyleClass().add(className);
        column.setCellValueFactory(c);

        return column;
    }

    @FXML
    void tableFilterButtonOnAction(ActionEvent event) {
        loadDeliveries();
        addTableData();
    }

    private void tableShowDeliveryOnAction(){
        DeliveryDto delivery = mainTable.getSelectionModel().getSelectedItem();

        if(delivery == null) return;

        DeliveryInfoModalController controller =
                (DeliveryInfoModalController) ShowModal.show("DeliveryInfoModal", "Detalhes da entrega", isDarkMode(), scene);

        controller.start(this);
        controller.show(delivery.getId());
    }

    private void tableAddDeliveryButtonOnAction(){
        DeliveryFormModalController controller =
                (DeliveryFormModalController) ShowModal.show("DeliveryFormModal", "Adicionar entrega", isDarkMode(), scene);

        controller.start(this);
    }

    private void tableEditDeliveryButtonOnAction(){
        DeliveryDto delivery = mainTable.getSelectionModel().getSelectedItem();

        if(delivery == null) return;

        DeliveryFormModalController controller =
                (DeliveryFormModalController) ShowModal.show("DeliveryFormModal", "Adicionar entrega", isDarkMode(), scene);

        controller.start(this);
        controller.edit(delivery);
    }

    private void changeDeliveryStatus(Integer deliveryStateId){
        DeliveryDto d = mainTable.getSelectionModel().getSelectedItem();

        if(d == null || d.getDeliveryState().getId() == deliveryStateId) return;

        String delivery = gson.toJson(d, DeliveryDto.class);
        JsonObject body = gson.fromJson(delivery, JsonObject.class);
        body.addProperty("deliveryState", deliveryStateId);

        updateDelivery(body, d.getId());
    }

    private void deliver(){
        try {
            DeliveryDto d = mainTable.getSelectionModel().getSelectedItem();

            if(d == null || d.getDeliveryState().getId() == 2) return;

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

            File outputFile = fileChooser.showOpenDialog(getStage());

            String base64File = "";
            if (outputFile != null) {
                byte[] fileContent = Files.readAllBytes(outputFile.toPath());
                base64File = Base64.getEncoder().encodeToString(fileContent);
            }

            String delivery = gson.toJson(d, DeliveryDto.class);
            JsonObject body = gson.fromJson(delivery, JsonObject.class);
            body.addProperty("deliveryState", 2);
            body.addProperty("receiverSignature", base64File);

            if(!base64File.equals("")) {
                updateDelivery(body, d.getId());
            }

        } catch(IOException ex){
            alertMessage.showErrorMessage("Não foi possível carregar o arquivo", isDarkMode());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possível entregar a encomenda", isDarkMode());
        }
    }

    private void updateDelivery(JsonObject body, Long id){
        try {
            body.remove("createdAt");
            body.remove("updatedAt");

            api.put("/delivery/" + id, body.toString());

            refreshDeliveryData();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possivel atualizar o status da encomenda" + ex, isDarkMode());
        }
    }

    private void loadDeliveries(){
        try {
            String params = "?sort=id,desc";

            if(!formReceiverNameValue.getText().equals("")){
                params += "&receiverName=" + URLEncoder.encode(formReceiverNameValue.getText(), "utf-8").trim();
            }

            if(!formUserIdValue.getText().equals("")){
                params += "&userId=" + URLEncoder.encode(formUserIdValue.getText(), "utf-8").trim();
            }

            if(!formDeliveryManIdValue.getText().equals("")){
                params += "&deliveryManId=" + URLEncoder.encode(formDeliveryManIdValue.getText(), "utf-8").trim();
            }

            if(formDeliveryStateValue.getSelectionModel().getSelectedIndex() >= 0){
                params += "&deliveryState=" + formDeliveryStateValue.getSelectionModel().getSelectedItem().getId();
            }

            params += "&page=" + mainTablePagination.getCurrentPageIndex();

            setDeliveries(api.get("/delivery" + params).getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar as entregas", isDarkMode());
        }
    }

    // All deliveries info
    private void loadDeliveriesStatus(){
        try {
            setDeliveriesStatus(api.get("/status/delivery").getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os status das entregas", isDarkMode());
        }
    }

    private void loadDeliveriesState(){
        try {
            setDeliveriesState(api.get("/delivery/deliveryState").getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar a lista de estado das entregas", isDarkMode());
        }
    }

    public JsonObject getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(JsonObject deliveries) {
        this.deliveries = deliveries;
    }

    public JsonObject getDeliveriesStatus() {
        return deliveriesStatus;
    }

    public void setDeliveriesStatus(JsonObject deliveriesStatus) {
        this.deliveriesStatus = deliveriesStatus;
    }

    public JsonObject getDeliveriesState() {
        return deliveriesState;
    }

    public void setDeliveriesState(JsonObject deliveriesState) {
        this.deliveriesState = deliveriesState;
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
}
