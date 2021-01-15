package fastPg3.util;

import fastPg3.interfaces.Pg3Controller;
import javafx.scene.Scene;

/**
 *
 * @author Édson Fischborn
 */
public class ShowModal {
    public static Pg3Controller show(String screenName, String modalTitle, boolean darkMode, Scene scene){
        try {
            ScreenLoader loader = new ScreenLoader("modal/" + screenName);
            Scene _scene = loader.getScene();

            if(darkMode){
                _scene.getRoot().getStylesheets().add("/fastPg3/assets/darkTheme.css");
            }

            Modal m = new Modal(_scene, modalTitle);
            m.showCenter(scene);

            return loader.getController();
        } catch(Exception ex){
            return null;
        }
    }
}
