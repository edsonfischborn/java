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
public class Country {
    private int countryId;
    private String name;
    private String cod;
    private Connection connection;

    public Country() {
    }

    public Country(Connection connection) {
        this.connection = connection;
    }

    public Country(int countryId, String name, String cod) {
        setCountryId(countryId);
        setName(name);
        setCod(cod);
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getName() {
        return name;
    }

    public String getCod() {
        return cod;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getCountryList() throws SQLException {
        String sql =
                "select"
                        + " idPais, nome, cod "
                        + "from pais order by nome asc";

        PreparedStatement request = this.connection.prepareStatement(sql);
        return request.executeQuery();
    }

    public ObservableList<Country> getCountryListArray() throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        ResultSet result = getCountryList();

        while (result.next()) {
            countryList.add(new Country(
                    result.getInt("idPais"),
                    result.getString("nome"),
                    result.getString("cod")
            ));
        }

        return countryList;
    }
    
    public ObservableList<Country> getCountryListForDelArray() throws SQLException {
        String sql =
                "select"
                        + " idPais, nome, cod "
                        + "from pais where idPais not in(select idPais from livro)"
                        + " order by nome asc";

        PreparedStatement request = this.connection.prepareStatement(sql);
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        ResultSet result = request.executeQuery();

        while (result.next()) {
            countryList.add(new Country(
                    result.getInt("idPais"),
                    result.getString("nome"),
                    result.getString("cod")
            ));
        }

        return countryList;
    }

    public boolean getCountryById(int countryId) throws SQLException {
        String sql =
                "select"
                        + " idPais, nome, cod "
                        + "from pais where idPais = ?";

        PreparedStatement request = this.connection.prepareStatement(sql);
        request.setInt(1, countryId);

        ResultSet result = request.executeQuery();
        if (result.next()) {
            this.setCountryId(result.getInt("idPais"));
            this.setName(result.getString("nome"));
            this.setCod(result.getString("cod"));

            return true;
        }

        return false;
    }

    public void insertCountry() throws SQLException {
        String sql =
                "insert into pais"
                        + "(nome, cod) "
                        + "values(?,?)";

        PreparedStatement request = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        
        request.setString(1, this.getName());
        request.setString(2, this.getCod());

        int result = request.executeUpdate();
        this.setCountryId(result);
    }

    public void updateCountry() throws SQLException {
        String sql =
                "update pais set"
                        + " nome = ?, cod = ? "
                        + "where idPais = ?";

        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setString(1, this.getName());
        request.setString(2, this.getCod());

        request.executeUpdate();
    }

    public void deleteCountry() throws SQLException {
        String sql = "delete from pais where idPais= ?";
        PreparedStatement request = this.connection.prepareStatement(sql);

        request.setInt(1, this.getCountryId());

        request.execute();
    }
    
    @Override
    public String toString(){
        return name;
    }
}
