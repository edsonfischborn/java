package handlelibrary.controller;

import handlelibrary.model.Db;
import handlelibrary.model.Language;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import util.Message;

/**
 * FXML Controller class
 *
 * @author Edson
 */
public class RemoveLanguageFXMLController implements Initializable {
    
    private Db db;
    private Message message = new Message();
      
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }  
    
    public void init(Db db){
       this.db = db;
       this.db.connect();
       loadLanguage();
    }
    
    @FXML
    private Button btnDelete;
    
    @FXML
    private Label warnLabel;
 
    @FXML
    private ListView<Language> languageList;

    @FXML
    void btnCancelOnClick(MouseEvent event) {
       
    }

    @FXML
    void btnDeleteOnClick(MouseEvent event) {
           if(languageList.getSelectionModel().getSelectedItem() != null){
               Language selectedLanguage = languageList.getSelectionModel().getSelectedItem();
               
               try {
                    selectedLanguage.setConnection(db.getConnection());
                    
                    if(message.showConfirmMessage("Deseja realmente deletar?")){
                        selectedLanguage.deleteLanguage();
                        loadLanguage();
                    }
               } catch(SQLException ex){
                   message.showErrorMessage("Não foi possivel deletar esse pais");
               }
               
           } else {
               message.showErrorMessage("Nenhum pais foi selecionado");
           }
    }
    
    private void loadLanguage(){
        Language l = new Language(db.getConnection());
        try {
            ObservableList<Language> list = l.getLanguageListForDelArray();
            languageList.setItems(list);
            
            if(list.size() <= 0){
                warnLabel.setText("Não há registros para mostrar");
            }
            
        } catch(SQLException e){
            message.showErrorMessage("Não foi possivel carregar a lista de paises");
        }
    }  
    
    public Db getDb() {
        return db;
    }
}
