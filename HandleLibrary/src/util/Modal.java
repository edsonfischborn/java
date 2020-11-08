package util;

import handlelibrary.model.Db;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Edson
 */
public class Modal {
    
    FXMLLoader fxmlLoader;
    Scene scene;
    Stage stage;
    String path;
    int width = 318;
    int height = 337;
    AnchorPane acPane;
    String title;
    
    public Modal(String path, AnchorPane ac, String title, int width, int height) throws IOException{
        setPath(path);
        setAcPane(ac);
        setTitle(title);
        setWidth(width);
        setHeight(height);
        init();
    }
    
    public Modal(String path, AnchorPane ac, String title) throws IOException{
        setPath(path);
        setAcPane(ac);
        setTitle(title);
        init();
    }
    
    public void init() throws IOException{
        loadFxml();
        createScene();
        createStage();
    }
    
    public void show(){
        centerByMainAchorPane();
        stage.show();
    }
    
    private void loadFxml(){
        fxmlLoader = new FXMLLoader(getClass().getResource(path));
    }
    
    private void createScene() throws IOException{
        scene = new Scene(fxmlLoader.load(), width,  height);
    }
    
    private void createStage(){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
    }
    
    private void centerByMainAchorPane(){
        Bounds AchorPaneData = acPane.localToScreen(acPane.getBoundsInLocal());
        stage.setX(AchorPaneData.getMinX() + AchorPaneData.getWidth() / 2 - scene.getWidth() / 2 );
        stage.setY(AchorPaneData.getMinY() + AchorPaneData.getHeight() / 2 - scene.getHeight() / 2 - 30);
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public AnchorPane getAcPane() {
        return acPane;
    }

    public void setAcPane(AnchorPane acPane) {
        this.acPane = acPane;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }   

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
