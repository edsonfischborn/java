package fastPg3.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author Édson Fischborn
 */

public class HandleIcon {
    private static void addIcon(Pane target, String className, Node icon){
        icon.getStyleClass().add(className);
        target.getChildren().add(icon);
    }

    public static void addMaterialDesignIcon(Pane target, String className, MaterialDesignIcon icon){
        addIcon(target, className, new MaterialDesignIconView(icon));
    }

    public static void addFontAwsmIcon(Pane target, String className, FontAwesomeIcon icon){
        addIcon(target, className, new FontAwesomeIconView(icon));
    }
}
