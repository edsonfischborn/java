package handlelibrary.controller;

import handlelibrary.model.Db;
import handlelibrary.model.Language;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.Message;

/**
 * FXML Controller class
 *
 * @author Edson
 */
public class InsertLanguageFXMLController implements Initializable {
    
    private Db db;
    private Message message = new Message();
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
    public void init(Db db){
       this.db = db;
       this.db.connect();
    }
    
    @FXML
    private TextField txName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    void btnCancelOnClick(MouseEvent event) {
        txName.setText("");
    }

    @FXML
    void btnSaveOnClick(MouseEvent event) {
        boolean valid = true;
        String errorMessages = "";
        String name = txName.getText();
        
        Language l = new Language(db.getConnection());
        
        if(name.equals("") || name.length() <= 3){
            valid = false;
            errorMessages += "O campo nome precisa ser preenchido e conter mais de 3 caracteres\n";
        } else {
             try {
                for( Language language : l.getLanguageListArray()) {
                    if(language.getName().equals(name)){
                        throw new Exception("Language already exists");
                    }
                }
            }catch(SQLException e){
                message.showErrorMessage("Erro ao validar o nome do idioma");
                return;
            }catch(Exception e){
                message.showErrorMessage("Esse idioma já existe!");
                return;
            } 
        }
        
        if(valid){
            l.setName(name);
          
            try {
                l.insertLanguage();
                message.showMessage(name + " , cadastrada com sucesso!");
            } catch(SQLException ex){
                message.showMessage("Erro no cadastro de " + name + "!");
            } 
        } else {
            message.showErrorMessage(errorMessages);
        }
    }
    
    public Db getDb() {
        return db;
    }
}
