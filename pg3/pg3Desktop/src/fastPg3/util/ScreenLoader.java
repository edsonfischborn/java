package fastPg3.util;

import fastPg3.FastPg3;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.net.URL;

/**
 *
 * @author Édson Fischborn
 */
public class ScreenLoader {
    private String rootUrl = "/fastPg3/view/";
    private URL fullUrl;
    private FXMLLoader loader;
    private Parent parent;

    public ScreenLoader(String viewPath) throws Exception{
        fullUrl = FastPg3.class.getResource( rootUrl + viewPath + ".fxml" );
        loader = new FXMLLoader(fullUrl);
        parent = loader.load();
    }

    public Parent getParent(){
        return this.parent;
    }

    public Scene getScene() {
        return new Scene(getParent());
    }

    public <T> T getController() {
        return (T)loader.getController();
    }
}
