package fastPg3.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import fastPg3.controller.modal.MessageInfoModalController;
import fastPg3.interfaces.Pg3Controller;
import fastPg3.types.CmbOption;
import fastPg3.types.Message;
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
public class MessageScreenController implements Initializable, Pg3Controller {

    @FXML
    private AnchorPane mainAcPane;

    @FXML
    private HBox smallCardContainer;

    @FXML
    private VBox messageListSectionIconView;

    @FXML
    private TextField formUserNameValue;

    @FXML
    private ComboBox<CmbOption> formReadMessagesValue;

    @FXML
    private HBox tableActionsContainer;

    @FXML
    private TableView<Message> mainTable;

    @FXML
    private Pagination mainTablePagination;

    private Stage stage;
    private Scene scene;

    private Gson gson = new Gson();

    private JsonObject messages;
    private JsonObject messageStatus;

    private AlertMessage alertMessage = new AlertMessage();

    private ApiRequest api;

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
            loadMessages();
            addTableData();
            mainTablePagination.setVisible(true);
        });

        addTableActions();
        addFormReadMessagesOptions();
        refreshMessageData();
    }

    public void refreshMessageData(){
        smallCardContainer.getChildren().clear();

        loadMessages();
        loadMessageStatus();
        addSmallCards();
        addTableData();
    }

    private void addIcons(){
        HandleIcon.addMaterialDesignIcon(messageListSectionIconView, "sectionIcon", MaterialDesignIcon.FORMAT_LIST_BULLETED);
    }

    private void addSmallCards(){
        ComponentsUtil.addSmallCard("Mensagens", getMessageStatus().get("allMessages").getAsString(),
                MaterialDesignIcon.INFORMATION_OUTLINE, "infoBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Lidas", getMessageStatus().get("readMessages").getAsString(),
                MaterialDesignIcon.CHECK, "successBgColor", smallCardContainer);

        ComponentsUtil.addSmallCard("Não lidas", getMessageStatus().get("unreadMessages").getAsString(),
                MaterialDesignIcon.CLOSE, "dangerBgColor", smallCardContainer);
    }

    private void addFormReadMessagesOptions(){
        formReadMessagesValue.getItems().addAll(
                new CmbOption("Não lidas", "0"),
                new CmbOption("Lidas", "1")
        );

        formReadMessagesValue.getSelectionModel().select(0);
    }

    private void addTableActions(){
        ComponentsUtil.addTableAction(tableActionsContainer,"Visualizar", MaterialDesignIcon.EYE, "infoBgColor",
                evt -> showMessage());

        ComponentsUtil.addTableAction(tableActionsContainer,"Marcar como lida", MaterialDesignIcon.CHECK, "successBgColor",
                evt -> this.readMessage(1));

        ComponentsUtil.addTableAction(tableActionsContainer,"Desmarcar como lida", MaterialDesignIcon.CLOSE, "dangerBgColor",
                evt -> this.readMessage(0));
    }

    private void addTableData(){
        mainTable.getItems().clear();
        mainTable.getColumns().clear();

        mainTablePagination.setPageCount(getMessages().get("totalPages").getAsInt());

        JsonArray messages = getMessages().getAsJsonArray("content");
        messages.forEach( m -> {
            Message message = gson.fromJson(m, Message.class);
            mainTable.getItems().add(message);
        });

        TableColumn<Message, String> id =
                createTableColumn("id","columnSmall",
                        m -> new SimpleStringProperty(Long.toString(m.getValue().getId())));

        TableColumn<Message, String> read =
                createTableColumn("lida","columnSmall",
                        m -> new SimpleStringProperty(m.getValue().getRead() == 1 ? "sim" : "não"));

        TableColumn<Message, String> name =
                createTableColumn("name", "columnLarge",
                        m -> new SimpleStringProperty(m.getValue().getName()));

        TableColumn<Message, String> eMail =
                createTableColumn("e-mail", "columnLarge",
                        m -> new SimpleStringProperty(m.getValue().getEmail()));

        TableColumn<Message, String> message =
                createTableColumn("message", "columnLarge",
                        m -> new SimpleStringProperty(m.getValue().getMessage()));

        mainTable.getColumns().addAll(id, read, name, eMail, message);
    }

    private TableColumn createTableColumn(String title, String className, Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>> c){
        TableColumn<Message, String> column = new TableColumn<>(title);
        column.getStyleClass().add(className);
        column.setCellValueFactory(c);

        return column;
    }

    @FXML
    void tableFilterButtonOnAction(ActionEvent event) {
        loadMessages();
        addTableData();
    }

    private void showMessage(){
        Message message = mainTable.getSelectionModel().getSelectedItem();
        if(message == null) return;
        MessageInfoModalController controller =
                (MessageInfoModalController) ShowModal.show("MessageInfoModal", "Detalhes da mensagem", isDarkMode(), scene);

        controller.start(this);
        controller.show(message);
    }

    private void readMessage(int read){
        try {
            Message message = mainTable.getSelectionModel().getSelectedItem();

            if(message == null || read == message.getRead()) return;
            message.setRead(read);

            String messageData = gson.toJson(message, Message.class);
            JsonObject body = gson.fromJson(messageData, JsonObject.class);
            body.remove("createdAt");
            body.remove("updatedAt");

            api.put("/message/" + message.getId(), body.toString());

            refreshMessageData();
        } catch(Exception ex) {
            ex.printStackTrace();
            alertMessage.showErrorMessage("Não foi possivel atualizar o status da mensagem", isDarkMode());
        }
    }

    private void loadMessages(){
        try {
            String params = "?sort=id,desc";

            if(!formUserNameValue.getText().equals("")){
                params += "&name=" + URLEncoder.encode(formUserNameValue.getText(), "utf-8").trim();
            }

            if(formReadMessagesValue.getSelectionModel().getSelectedIndex() >= 0){
                params += "&read=" + formReadMessagesValue.getSelectionModel().getSelectedItem().getValue();
            }

            params += "&page=" + mainTablePagination.getCurrentPageIndex();

            setMessages(api.get("/message/" + params).getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar as mensagens", isDarkMode());
        }
    }

    private void loadMessageStatus(){
        try {
            setMessageStatus(api.get("/status/message").getAsJsonObject());
        } catch (Exception ex){
            alertMessage.showErrorMessage("Não foi possivel carregar os status das mensagens", isDarkMode());
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

    public JsonObject getMessages() {
        return messages;
    }

    public void setMessages(JsonObject messages) {
        this.messages = messages;
    }

    public JsonObject getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(JsonObject messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Pg3Controller getFatherController() {
        return fatherController;
    }

    public void setFatherController(Pg3Controller fatherController) {
        this.fatherController = fatherController;
    }
}
