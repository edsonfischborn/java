package handlelibrary.controller;

import handlelibrary.model.Country;
import handlelibrary.model.Db;
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
public class RemoveCountryFXMLController implements Initializable {
    
    private Db db;
    private Message message = new Message();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }  
    
    public void init(Db db){
       this.db = db;
       this.db.connect();
       loadCountry();
    }
    
    @FXML
    private Button btnDelete;
 
    @FXML
    private ListView<Country> countryList;
    
    @FXML
    private Label warnLabel;

    @FXML
    void btnCancelOnClick(MouseEvent event) {
       
    }
    

    @FXML
    void btnDeleteOnClick(MouseEvent event) {
           if(countryList.getSelectionModel().getSelectedItem() != null){
               Country selectedCountry = countryList.getSelectionModel().getSelectedItem();
               
               try {
                    selectedCountry.setConnection(db.getConnection());
                    
                    if(message.showConfirmMessage("Deseja realmente deletar?")){
                        selectedCountry.deleteCountry();
                        loadCountry();
                    }
               } catch(SQLException ex){
                   message.showErrorMessage("Não foi possivel deletar esse pais");
               }
               
           } else {
               message.showErrorMessage("Nenhum pais foi selecionado");
           }
    }
    
    private void loadCountry(){
    Country c = new Country(db.getConnection());
        try {
            ObservableList<Country> list = c.getCountryListForDelArray();
            countryList.setItems(list);
            
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
