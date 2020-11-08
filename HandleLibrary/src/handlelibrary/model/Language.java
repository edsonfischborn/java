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
public class Language {
    private int languageId;
    private String name;
    private Connection connection;

    public Language() {
    }

    public Language(Connection connection) {
        this.connection = connection;
    }

    public Language(int languageId, String name) {
        setLanguageId(languageId);
        setName(name);
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ResultSet getLanguageList() throws SQLException {
        String sql =
                "select"
                        + " idIdioma, nome "
                        + "from idioma order by nome asc";

        PreparedStatement request = this.connection.prepareStatement(sql);
        return request.executeQuery();
    }

    public ObservableList<Language> getLanguageListArray() throws SQLException {
        ObservableList<Language> languageList = FXCollections.observableArrayList();
        ResultSet result = getLanguageList();

        while (result.next()) {
            languageList.add(new Language(
                    result.getInt("idIdioma"),
                    result.getString("nome")
            ));
        }

        return languageList;
    }
    
    public ObservableList<Language> getLanguageListForDelArray() throws SQLException {
        String sql =
                "select"
                        + " idIdioma, nome "
                        + "from idioma where idIdioma not in(select idIdioma from livro) order by nome asc";

        PreparedStatement request = this.connection.prepareStatement(sql);
        ObservableList<Language> languageList = FXCollections.observableArrayList();
        ResultSet result = request.executeQuery();

        while (result.next()) {
            languageList.add(new Language(
                    result.getInt("idIdioma"),
                    result.getString("nome")
            ));
        }

        return languageList;
    }

    public boolean getLanguageById(int languageId) throws SQLException {
        String sql =
                "select"
                        + " idIdioma, nome "
                        + "from idioma where idIdioma = ?";

        PreparedStatement request = this.connection.prepareStatement(sql);
        request.setInt(1, languageId);

        ResultSet result = request.executeQuery();
        if (result.next()) {
            this.setLanguageId(result.getInt("idIdioma"));
            this.setName(result.getString("nome"));

            return true;
        }

        return false;
    }

    public void insertLanguage() throws SQLException {
        String sql =
                "insert into idioma"
                        + "(nome) "
                        + "values(?)";

        PreparedStatement request = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        request.setString(1, this.getName());


        int result = request.executeUpdate();
        this.setLanguageId(result);
        
    }

    public void updateLanguage() throws SQLException {
        String sql =
                "update idioma set"
                        + " nome = ? "
                        + "where idIdioma= ?";

        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setString(1, this.getName());
        request.setInt(2, this.getLanguageId());

        request.executeUpdate();
    }

    public void deleteLanguage() throws SQLException {
        String sql = "delete from idioma where idIdioma= ?";
        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setInt(1, this.getLanguageId());

        request.execute();
    }
    
    @Override
    public String toString(){
        return name;
    }
}
