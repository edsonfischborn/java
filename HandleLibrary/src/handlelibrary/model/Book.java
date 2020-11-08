package handlelibrary.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Edson
 */
public class Book {
    private int bookId;
    private String title;
    private int launchYear;
    private int numberPages;
    private String resum;
    private int countryId;
    private int languageId;
    private Connection connection;

    public Book() {

    }

    public Book(Connection connection) {
        this.connection = connection;
    }

    public Book(int bookId, String title, int launchYear, int numberPages, String resum, int countryId, int languageId) {
        setBookId(bookId);
        setTitle(title);
        setLaunchYear(launchYear);
        setNumberPages(numberPages);
        setResum(resum);
        setCountryId(countryId);
        setLanguageId(languageId);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(int launchYear) {
        this.launchYear = launchYear;
    }

    public int getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(int numberPages) {
        this.numberPages = numberPages;
    }

    public String getResum() {
        return resum;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ResultSet getBookList() throws SQLException {
        String sql =
                "select"
                        + " idLivro, tituloLivro, anoLancamento, numeroPaginas, resumo, idPais, idIdioma "
                        + "from livro";

        PreparedStatement request = this.connection.prepareStatement(sql);
        return request.executeQuery();
    }

    public ObservableList<Book> getBookListArray() throws SQLException {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        ResultSet result = getBookList();
        
        while (result.next()) {
            bookList.add(new Book(
                    result.getInt("idLivro"),
                    result.getString("tituloLivro"),
                    result.getInt("anoLancamento"),
                    result.getInt("numeroPaginas"),
                    result.getString("resumo"),
                    result.getInt("idPais"),
                    result.getInt("idIdioma")
                )
            );
        }
  
        return bookList;
    }

    public boolean getBookById(int bookId) throws SQLException {
        String sql =
                "select"
                        + " tituloLivro, anoLancamento, numeroPaginas, resumo, idPais, idIdioma "
                        + "where idLivro = ?";

        PreparedStatement request = this.connection.prepareStatement(sql);
        request.setInt(1, bookId);

        ResultSet result = request.executeQuery();
        if (result.next()) {
            this.setBookId(result.getInt("idLivro"));
            this.setTitle(result.getString("tituloLivro"));
            this.setLaunchYear(result.getInt("anoLancamento"));
            this.setNumberPages(result.getInt("numeroPaginas"));
            this.setResum(result.getString("resumo"));
            this.setCountryId(result.getInt("idPais"));
            this.setLanguageId(result.getInt("idIdioma"));

            return true;
        }

        return false;
    }

    public void insertBook() throws SQLException {
        String sql =
                "insert into livro"
                        + "(tituloLivro, anoLancamento, numeroPaginas, resumo, idPais, idIdioma) "
                        + "values(?,?,?,?,?,?)";

        PreparedStatement request = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        request.setString(1, this.getTitle());
        request.setInt(2, this.getLaunchYear());
        request.setInt(3, this.getNumberPages());
        request.setString(4, this.getResum());
        request.setInt(5, this.getCountryId());
        request.setInt(6, this.getLanguageId());

        int result = request.executeUpdate();    
        this.setBookId(result);
        
    }

    public void updateBook() throws SQLException {
        String sql =
                "update livro set"
                        + " tituloLivro=?, anoLancamento=?, numeroPaginas=?, resumo=?, idPais=?, idIdioma=? "
                        + "where idLivro = ?";

        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setString(1, this.getTitle());
        request.setInt(2, this.getLaunchYear());
        request.setInt(3, this.getNumberPages());
        request.setString(4, this.getResum());
        request.setInt(5, this.getCountryId());
        request.setInt(6, this.getLanguageId());
        request.setInt(7, this.getBookId());

        request.executeUpdate();
    }

    public void deleteBook() throws SQLException {
        String sql = "delete from livro where idLivro = ?";
        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setInt(1, this.getBookId());

        request.execute();
    }
}
