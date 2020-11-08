package util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 *
 * @author Edson
 */
public class Message {
    
    private String className = "dialog";
    private String cssFilePath = "/css/styles.css";
    
    public Message(){}
    
    public Message(String className, String ccFilePath){
        setClassName(className);
        setCssFilePath(cssFilePath);
    }
    
    // Show message
    public void showMessage(String msg){
        Alert alert = createBasicAlert("Atenção", "O sistema retornou uma mensagem", msg,
        AlertType.INFORMATION);
        
        alert.show();
    }
    
    // Show error message
    public void showErrorMessage(String msg){
        Alert alert = createBasicAlert("Ocorreu um problema",
                "O sistema retornou o(s) seguinte(s) erro(s)", msg, AlertType.ERROR);
        alert.show();
    }
    
    // Show confirm msg
    public boolean showConfirmMessage(String msg){
        Alert a = createBasicAlert("Confirmação", "Confirme que deseja prosegguir com",
                msg, AlertType.CONFIRMATION);
        a.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        Optional<ButtonType> res = a.showAndWait();
        
        if(res.get() == ButtonType.YES){
            return true;
        }
        
        return false;
        
    }
    
    private Alert createBasicAlert(String title, String headerText, String message, AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource(cssFilePath).toExternalForm());
        dialogPane.getStyleClass().add(className);
        
        return alert;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCssFilePath() {
        return cssFilePath;
    }

    public void setCssFilePath(String cssFilePath) {
        this.cssFilePath = cssFilePath;
    }
}
