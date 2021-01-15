package fastPg3.interfaces;

import fastPg3.util.ApiRequest;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Édson Fischborn
 */
public interface Pg3Controller {

   void setStage();

   void setScene();

   void setDarkMode(boolean darkMode);

   void setApi(ApiRequest api);

   Stage getStage();

   Scene getScene();

   boolean isDarkMode();

   ApiRequest getApi();

   default void onInit(){
       setScene();
       setStage();
   }

   default void start(Pg3Controller c) {}
}
