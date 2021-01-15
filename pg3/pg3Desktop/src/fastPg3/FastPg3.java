/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fastPg3;

import fastPg3.controller.LoginScreenController;
import fastPg3.util.ApiRequest;
import fastPg3.util.ScreenLoader;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Edson
 */
public class FastPg3 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            System.setProperty("prism.lcdtext", "false");

            ScreenLoader loader = new ScreenLoader("LoginScreen");
            ApiRequest api = new ApiRequest();

            primaryStage.setScene(loader.getScene());
            LoginScreenController c = loader.getController();
            c.start(api);

            primaryStage.setTitle("FAST PGIII");
            primaryStage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
