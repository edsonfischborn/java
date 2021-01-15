package fastPg3.util;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Édson Fischborn
 */
public class Modal {
    Scene scene;
    Stage stage;
    int width;
    int height;
    String title;

    public Modal(Scene scene, String title){
        init(title, scene);
    }

    public void init(String title, Scene scene){
        setHeight((int) scene.getHeight());
        setWidth((int) scene.getWidth());
        setTitle(title);
        setScene(scene);
        createStage();
    }

    public void show(){
        stage.show();
    }

    public void showCenter(Scene s){
        centerByMainAnchorPane(s);
        stage.show();
    }

    private void createStage(){
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        scene.getProperties();
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setScene(scene);
        stage.setResizable(false);
    }
    
    private void centerByMainAnchorPane(Scene s){
        Bounds data = scene.getRoot().getLayoutBounds();
        stage.setX(data.getMinX() + data.getWidth() / 2 - scene.getWidth() / 2 );
        stage.setY(data.getMinY() + data.getHeight() / 2 - scene.getHeight() / 2);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
