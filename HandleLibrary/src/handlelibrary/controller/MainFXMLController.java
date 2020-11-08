package handlelibrary.controller;

// Model
import handlelibrary.model.Book;
import handlelibrary.model.Country;
import handlelibrary.model.Db;
import handlelibrary.model.Language;
import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

// Fx
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import util.Message;
import util.Modal;

/**
 * @author Edson
 */
public class MainFXMLController implements Initializable {
    
    private Db db;
    private Message message = new Message();
    
    public MainFXMLController(){
        db =  new Db("host", "port", "user", "pass", "db");
        db.connect();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadAllIcons();
        initTableCelbyBookProperty();
        loadBooks();
        loadCountry();
        loadLanguage();
    }

    @FXML
    private AnchorPane AcPane;
    
    @FXML
    private TableView<Book> bookList;

    @FXML
    private TableColumn<Book, Integer> bookListId;

    @FXML
    private TableColumn<Book, String> bookListTitle;

    @FXML
    private TableColumn<Book, Integer> bookListYear;

    @FXML
    private TableColumn<Book, Integer> bookListPages;

    @FXML
    private TextField txBookTitle;

    @FXML
    private TextField txBookLaunchYear;

    @FXML
    private TextField txBookNumberPages;

    @FXML
    private ComboBox<Country> cbBookCountry;

    @FXML
    private ComboBox<Language> cbBookLanguage;

    @FXML
    private Button btnAddBookCountry;

    @FXML
    private TextArea txBookResum;

    @FXML
    private Button btnRemoveBookCountry;

    @FXML
    private Button btnAddBookLang;

    @FXML
    private Button btnRemoveBookLang;

    @FXML
    private Label lblBookId;

    @FXML
    private Button btnNewBook;

    @FXML
    private Button btnDeleteBook;

    @FXML
    private Button btnSaveBook;

    @FXML
    private Button btnCancelAction;

    @FXML
    void btnAddBookCountryOnAction(ActionEvent event) {
        try {
            Modal modal = new Modal("/handlelibrary/view/InsertCountryFXML.fxml",
                AcPane,
                "Adição de país"
            );  
            
            InsertCountryFXMLController controller = modal.getFxmlLoader().getController();
            controller.init(db);
            modal.show();
        } catch( IOException ex ){
            message.showErrorMessage("Erro ao carregar a tela de adição de país ");
        }
    }

    @FXML
    void btnAddBookLangOnAction(ActionEvent event) {
        try {
            Modal modal = new Modal("/handlelibrary/view/InsertLanguageFXML.fxml",
                AcPane,
                "Adição de Idioma",
                318,
                280
            );  
            
            InsertLanguageFXMLController controller = modal.getFxmlLoader().getController();
            controller.init(db);
            modal.show();
        } catch( IOException ex ){
            message.showErrorMessage("Erro ao carregar a tela de adição de idioma " + ex);
        }
    }
    
    @FXML
    void btnRemoveBookCountryOnAction(ActionEvent event) {
        try {
            Modal modal = new Modal("/handlelibrary/view/RemoveCountryFXML.fxml",
                AcPane,
                "Remoção de país"
            );  
            
            RemoveCountryFXMLController controller = modal.getFxmlLoader().getController();
            controller.init(db);
            modal.show();
        } catch( IOException ex ){
            ex.printStackTrace();
            message.showErrorMessage("Não foi possivel carregar a tela de remoção de pais");
        }
    }

    @FXML
    void btnRemoveBookLangOnAction(ActionEvent event) {
        try {
            Modal modal = new Modal("/handlelibrary/view/RemoveLanguageFXML.fxml",
                AcPane,
                "Remoção de idioma"
            );  
            
            RemoveLanguageFXMLController controller = modal.getFxmlLoader().getController();
            controller.init(db);
            modal.show();
        } catch( IOException ex ){
            message.showErrorMessage("Não foi possivel carregar a tela de remoção de idioma");
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        clearForm();
    }
    
    @FXML
    void cbCountryOnClick(MouseEvent event) {
        loadCountry();
    }
    
    @FXML
    void cbLanguageOnClick(MouseEvent event) {
        loadLanguage();
    }
   
    @FXML
    void btnDeleteBookOnAction(ActionEvent event) {
        if(bookList.getSelectionModel().getSelectedItem() != null){
            Book b = bookList.getSelectionModel().getSelectedItem();
            b.setConnection(db.getConnection());
            
            if(message.showConfirmMessage("Deseja realmente deletar o livro, " + b.getTitle() + "?")){
                try{
                    b.deleteBook();
                    loadBooks();
                    clearForm();
                
                }catch(SQLException ex){
                    message.showErrorMessage("Não foi possivel deletar o livro");
                }
            }
        } else {
            message.showErrorMessage("Selecione um livro para deletar");
        }
    }

    @FXML
    void btnNewBookOnAction(ActionEvent event) {
         clearForm();
    }

    @FXML
    void btnSaveBookOnAction(ActionEvent event) {
        boolean error = false;
        String errorMessage = "";
        
        Book b = new Book(db.getConnection());
      
        
        // Tx resum
        b.setResum(txBookResum.getText());
        
        // Tx Title
        if(!"".equals(txBookTitle.getText())){
            
            // valid if book exists
            try {
                for( Book book : b.getBookListArray()) {
                    if(book.getTitle().equals(txBookTitle.getText())){
                        throw new Exception("Book already exists");
                    }
                }
            }catch(SQLException e){
                message.showErrorMessage("Erro ao validar o nome do livro");
                return;
            }catch(Exception e){
                message.showErrorMessage("Esse livro já existe! que tal edita-lo?");
                return;
            } 
            
            b.setTitle(txBookTitle.getText());
        } else{
            errorMessage += "Informe o titulo do livro\n";
            error = true;
        }
        
        
        // Tx launch year
        try {
            b.setLaunchYear(Integer.parseInt(txBookLaunchYear.getText()));
        }catch(Exception ex){
            errorMessage += "O campo ano de lançamento deve ser preenchido com números\n";
            error = true;
        }
        
        // Tx number pages
        try {
            b.setNumberPages(Integer.parseInt(txBookNumberPages.getText()));
        }catch(Exception ex){
            errorMessage += "O campo número de paginas deve ser preenchido com números\n";
            error = true;
        }
        
        // Cb country
        if(cbBookCountry.getSelectionModel().getSelectedItem() != null){
            b.setCountryId(cbBookCountry.getSelectionModel().getSelectedItem().getCountryId());
        } else {
            errorMessage += "Selecione um pais \n";
            error = true;
        }
        
        // Cb language
        if(cbBookLanguage.getSelectionModel().getSelectedItem() != null){
            b.setLanguageId(cbBookLanguage.getSelectionModel().getSelectedItem().getLanguageId());
        } else {
            errorMessage+="Selecione um idioma\n";
            error = true;
        }
        
        if(error){
            message.showErrorMessage(errorMessage);
        } else{
            // Inclui um novo livro
            if(lblBookId.getText().equals("-")){
                try{
                    b.insertBook();
                    loadBooks();
                    clearForm();
                    message.showMessage("Livro salvo com sucesso!");
                }catch(SQLException ex){
                    System.out.println(ex);
                    message.showErrorMessage("Não foi possivel gravar o livro");
                }
            // Atualiza um novo livro
            } else {
                try{      
                    b.setBookId(Integer.parseInt(lblBookId.getText()));
                    b.updateBook();
                    loadBooks(); 
                    message.showMessage("Livro atualizado com sucesso!");
                }catch(SQLException ex){
                    message.showErrorMessage("Não foi possivel atualizar o livro");
                }
            }
        }
        
    }
    
    @FXML
    void handleBookListClick(MouseEvent event) {
        if(bookList.getSelectionModel().getSelectedItem() != null){
            Book b = bookList.getSelectionModel().getSelectedItem();
            refreshForm(b);   
        }
    }
    

    // Load application icons
    private void LoadAllIcons(){
        btnAddBookCountry.setGraphic(getImageView("/icons/incluir16p.png"));
        btnRemoveBookCountry.setGraphic(getImageView("/icons/deletar16p.png"));
        btnAddBookLang.setGraphic(getImageView("/icons/incluir16p.png"));
        btnRemoveBookLang.setGraphic(getImageView("/icons/deletar16p.png"));
    }
    
    // Get ImageView by image path
    private ImageView getImageView(String src){
       Image img = new Image(getClass().getResourceAsStream(src));
       return new ImageView(img);
    }
    
    // Handle TableView
    private void initTableCelbyBookProperty(){
        bookListId.setCellValueFactory(new PropertyValueFactory("bookId"));
        bookListTitle.setCellValueFactory(new PropertyValueFactory("title"));
        bookListPages.setCellValueFactory(new PropertyValueFactory("numberPages"));
        bookListYear.setCellValueFactory(new PropertyValueFactory("launchYear"));
    }
    
    // Reset form data
    private void clearForm(){
        lblBookId.setText("-");
        txBookLaunchYear.setText("");
        txBookTitle.setText(""); 
        txBookNumberPages.setText("");
        txBookResum.setText("");
        cbBookCountry.setValue(null);
        cbBookLanguage.setValue(null);
    }
    
    // Update form data
    private void refreshForm(Book b){
        lblBookId.setText(Integer.toString(b.getBookId()));
        txBookLaunchYear.setText(Integer.toString(b.getLaunchYear()));
        txBookTitle.setText(b.getTitle()); 
        txBookNumberPages.setText(Integer.toString(b.getNumberPages()));
        txBookResum.setText(b.getResum());
      
        // cb
        try {
            Language lang  = new Language(db.getConnection());
            lang.getLanguageById(b.getLanguageId());
            Country cty  = new Country(db.getConnection());
            cty.getCountryById(b.getCountryId());
            cbBookCountry.getSelectionModel().select(cty);
            cbBookLanguage.getSelectionModel().select(lang);
        } catch(SQLException ex){
            message.showErrorMessage("Erro ao carregar o idioma/pais do livro. =(");
        }
    }
    
    // Load books
    private void loadBooks(){
        Book b = new Book(db.getConnection());

        try {
            bookList.setItems(b.getBookListArray());
        }catch(SQLException ex){
            message.showErrorMessage("Não foi possivel carregar a lista de livros");
        }
        
    }
    
    // Load language
    private void loadLanguage(){
        Language lang = new Language(db.getConnection());

        try {
            cbBookLanguage.setItems(lang.getLanguageListArray());
        }catch(SQLException ex){
            message.showErrorMessage("Não foi possivel carregar a lista de idiomas");
        }
        
    }
    
    // Load country
    private void loadCountry(){
        Country lang = new Country(db.getConnection());

        try {
            cbBookCountry.setItems(lang.getCountryListArray());
        }catch(SQLException ex){
            message.showErrorMessage("Não foi possivel carregar a lista de paises");
        }
        
    }
    
    

}
