package handlelibrary.controller;

import handlelibrary.model.Country;
import handlelibrary.model.Db;
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
public class InsertCountryFXMLController implements Initializable {
    
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
    private TextField txCod;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    void btnCancelOnClick(MouseEvent event) {
        txCod.setText("");
        txName.setText("");
    }

    @FXML
    void btnSaveOnClick(MouseEvent event) {
        boolean valid = true;
        String errorMessages = "";
        String name = txName.getText();
        String cod = txCod.getText();
        
        Country c = new Country(db.getConnection());
        
        if(name.equals("") || name.length() <= 3){
            valid = false;
            errorMessages += "O campo nome precisa ser preenchido e conter mais de 3 caracteres\n";
        }else {
            // valid if country exists
            try {
                for( Country country : c.getCountryListArray()) {
                    if(country.getName().equals(name)){
                        throw new Exception("Country already exists");
                    }
                }
            }catch(SQLException e){
                message.showErrorMessage("Erro ao validar o nome do pais");
                return;
            }catch(Exception e){
                message.showErrorMessage("Esse pais já existe!");
                return;
            } 
        }
        
        if(cod.equals("") || cod.length() > 3 || cod.length() <= 1 ){
            valid = false;
            errorMessages += "O campo codigo, deve ser preenchido com 2-3 caracteres\n";
        }    
        
        if(valid){
            c.setCod(cod);
            c.setName(name);
            
            try {
                c.insertCountry();
                message.showMessage(name + " , cadastrado com sucesso!");
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
