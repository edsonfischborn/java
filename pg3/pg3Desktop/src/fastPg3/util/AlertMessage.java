package fastPg3.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 *
 * @author Édson Fischborn
 */
public class AlertMessage {
    
    private String className = "dialog";

    // Show message
    public void showMessage(String msg, boolean darkMode){
        Alert alert = createBasicAlert("Atenção", "O sistema retornou uma mensagem", msg,
        AlertType.INFORMATION, darkMode);
        
        alert.show();
    }
    
    // Show error message
    public void showErrorMessage(String msg, boolean darkMode){
        Alert alert = createBasicAlert("Ocorreu um problema",
                "O sistema retornou o(s) seguinte(s) erro(s)", msg, AlertType.ERROR, darkMode);
        alert.show();
    }
    
    // Show confirm msg
    public boolean showConfirmMessage(String msg, boolean darkMode){
        Alert a = createBasicAlert("Confirmação", "Confirme que deseja proseguir com",
                msg, AlertType.CONFIRMATION, darkMode);
        a.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        Optional<ButtonType> res = a.showAndWait();
        
        if(res.get() == ButtonType.YES){
            return true;
        }
        
        return false;
        
    }
    
    private Alert createBasicAlert(String title, String headerText, String message, AlertType type, boolean darkMode){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/fastPg3/assets/mainScreen.css");
        dialogPane.getStyleClass().add(className);

        if(darkMode){
            dialogPane.getStylesheets().add("/fastPg3/assets/darkTheme.css");
        }

        return alert;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
