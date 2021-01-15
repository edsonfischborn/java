package fastPg3.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.modal.DeliveryManFormModalController;
import fastPg3.controller.modal.DeliveryManInfoModalController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.CmbOption;
import fastPg3.types.DeliveryMan;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryManScreenController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox smallCardContainer;

    @FXML
    private VBox deliveryManListSectionIconView;

    @FXML
    private TextField formDeliveryManNameValue;

    @FXML
    private ComboBox<CmbOption> formDeliveryManActiveValue;

    @FXML
    private HBox tableActionsContainer;

    @FXML
    private TableView<DeliveryMan> mainTable;

    @FXML
    private Pagination mainTablePagination;

    private Stage stage;
    private Scene scene;

    private ApiRequest api;

    private AlertMessage alertMessage = new AlertMessage();

    private Gson gson = new Gson();

    private JsonObject deliveryMan;
    private JsonObject deliveryManStatus;

    private Pg3Controller fatherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void start(Pg3Controller fatherController){
        onInit();

        setFatherController(fatherController);
        setApi(getFatherController().getApi());

        fillData();
    }

    private void fillData(){
        addIcons();

        mainTablePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            mainTablePagination.setVisible(false);
            loadDeliveryMan();
            addTableData();
            mainTablePagination.setVisible(true);
        });

        addTableActions();
        addDeliveryManActiveOptions();
        refreshDeliveryManData();
    }

    public void refreshDeliveryManData(){
        smallCardContainer.getChildren().clear();

        loadDeliveryMan();
        loadUserStatus();
        addSmallCards();
        addTableData();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(deliveryManListSectionIconView, "sectionIcon", MaterialDesignIcon.FORMAT_LIST_BULLETED);
    }

    private void addSmallCards(){
        ComponentsUtil.addSmallCard("Entregadores", getDeliveryManStatus().get("allDeliveryMan").getAsString(),
                MaterialDesignIcon.INFORMATION_OUTLINE, "infoBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Ativos", getDeliveryManStatus().get("activeDeliveryMan").getAsString(),
                MaterialDesignIcon.CHECK, "successBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Inativos", getDeliveryManStatus().get("inactiveDeliveryMan").getAsString(),
                MaterialDesignIcon.CLOSE, "dangerBgColor", smallCardContainer);
    }

    private void addDeliveryManActiveOptions(){
        formDeliveryManActiveValue.getItems().addAll(
                new CmbOption("Ativos", "1"),
                new CmbOption("Inativos", "0")
        );

        formDeliveryManActiveValue.getSelectionModel().select(0);
    }

    private void addTableActions(){
        ComponentsUtil.addTableAction(tableActionsContainer,"Adicionar", MaterialDesignIcon.PLUS, "indigoBgColor",
                evt -> createDeliveryMan());

        ComponentsUtil.addTableAction(tableActionsContainer,"Visualizar", MaterialDesignIcon.EYE, "infoBgColor",
                evt -> showDeliveryMan());

        ComponentsUtil.addTableAction(tableActionsContainer,"Editar", MaterialDesignIcon.PENCIL, "pg3BgColor",
                evt -> editDeliveryMan());

        ComponentsUtil.addTableAction(tableActionsContainer,"Ativar", MaterialDesignIcon.CHECK, "successBgColor",
                evt -> this.changeDeliveryManActive(1));

        ComponentsUtil.addTableAction(tableActionsContainer,"Desativar", MaterialDesignIcon.CLOSE, "dangerBgColor",
                evt -> this.changeDeliveryManActive(0));
    }

    private void addTableData(){
        mainTable.getItems().clear();
        mainTable.getColumns().clear();

        mainTablePagination.setPageCount(getDeliveryMan().get("totalPages").getAsInt());

        JsonArray users = getDeliveryMan().getAsJsonArray("content");
        users.forEach( d -> {
            DeliveryMan deliveryMan = gson.fromJson(d, DeliveryMan.class);
            mainTable.getItems().add(deliveryMan);
        });

        TableColumn<DeliveryMan, String> id =
                createTableColumn("id", "columnSmall",
                        d -> new SimpleStringProperty(Long.toString(d.getValue().getId())));

        TableColumn<DeliveryMan, String> active =
                createTableColumn("ativo", "columnSmall",
                        d -> new SimpleStringProperty(d.getValue().getActive() == 1 ? "sim" : "não"));

        TableColumn<DeliveryMan, String> name =
                createTableColumn("nome",  "columnLarge",
                        d -> new SimpleStringProperty(d.getValue().getName()));

        TableColumn<DeliveryMan, String> cpf =
                createTableColumn("cpf", "",
                        d -> new SimpleStringProperty(d.getValue().getCpf()));

        TableColumn<DeliveryMan, String> phone =
                createTableColumn("telefone", "",
                        d -> new SimpleStringProperty(d.getValue().getPhone()));

        TableColumn<DeliveryMan, String> eMail =
                createTableColumn("e-mail", "columnBig",
                        d -> new SimpleStringProperty(d.getValue().getEmail()));

        mainTable.getColumns().addAll(id, active, name, cpf, phone, eMail);
    }

    private TableColumn createTableColumn(String title, String className, Callback<TableColumn.CellDataFeatures<DeliveryMan, String>, ObservableValue<String>> c){
        TableColumn<DeliveryMan, String> column = new TableColumn<>(title);
        column.getStyleClass().add(className);
        column.setCellValueFactory(c);

        return column;
    }

    @FXML
    void tableFilterButtonOnAction(ActionEvent event) {
        loadDeliveryMan();
        addTableData();
    }

    private void showDeliveryMan(){
        DeliveryMan deliveryMan = mainTable.getSelectionModel().getSelectedItem();
        if(deliveryMan == null) return;
        DeliveryManInfoModalController controller =
                (DeliveryManInfoModalController) ShowModal.show("DeliveryManInfoModal", "Detalhes do entregador", isDarkMode(), scene);

        controller.start(this);
        controller.show(deliveryMan);
    }

    private void editDeliveryMan(){
        DeliveryMan deliveryMan = mainTable.getSelectionModel().getSelectedItem();
        if(deliveryMan == null) return;
        DeliveryManFormModalController controller =
                (DeliveryManFormModalController) ShowModal.show("DeliveryManFormModal", "Edição do entregador", isDarkMode(), scene);

        controller.start(this);
        controller.edit(deliveryMan);
    }

    private void createDeliveryMan(){
        DeliveryManFormModalController controller =
                (DeliveryManFormModalController) ShowModal.show("DeliveryManFormModal", "Edição do entregador", isDarkMode(), scene);

        controller.start(this);
    }

    private void changeDeliveryManActive(int active){
        try {
            DeliveryMan deliveryMan = mainTable.getSelectionModel().getSelectedItem();

            if(deliveryMan == null || active == deliveryMan.getActive()) return;
            deliveryMan.setActive(active);

            String userData = gson.toJson(deliveryMan, DeliveryMan.class);
            JsonObject body = gson.fromJson(userData, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.put("/deliveryman/" + deliveryMan.getId(), body.toString());

            refreshDeliveryManData();
        } catch(Exception ex) {
            alertMessage.showErrorMessage("Não foi possivel atualizar o status do entregador", isDarkMode());
        }
    }

    private void loadDeliveryMan(){
        try {
            String params = "?sort=id,desc";

            if(!formDeliveryManNameValue.getText().equals("")){
                params += "&name=" + URLEncoder.encode(formDeliveryManNameValue.getText(), "utf-8").trim();
            }

            if(formDeliveryManActiveValue.getSelectionModel().getSelectedIndex() >= 0){
                params += "&active=" + formDeliveryManActiveValue.getSelectionModel().getSelectedItem().getValue();
            }

            params += "&page=" + mainTablePagination.getCurrentPageIndex();

            setDeliveryMan(api.get("/deliveryman" + params).getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os entregadores", isDarkMode());
        }
    }

    private void loadUserStatus(){
        try {
            setDeliveryManStatus(api.get("/status/deliveryMan").getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os status dos Entregadores", isDarkMode());
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

    public JsonObject getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(JsonObject deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public JsonObject getDeliveryManStatus() {
        return deliveryManStatus;
    }

    public void setDeliveryManStatus(JsonObject deliveryManStatus) {
        this.deliveryManStatus = deliveryManStatus;
    }

    public Pg3Controller getFatherController() {
        return fatherController;
    }

    public void setFatherController(Pg3Controller fatherController) {
        this.fatherController = fatherController;
    }
}
