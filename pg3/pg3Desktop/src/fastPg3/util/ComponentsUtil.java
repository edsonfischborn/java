package fastPg3.util;

import com.google.gson.JsonArray;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Édson Fischborn
 */
public class ComponentsUtil {
    public static void addSmallCard(String title, String value, MaterialDesignIcon icon, String cardClass, Pane target){
        // Card container
        VBox card = new VBox();
        card.getStyleClass().addAll(cardClass, "smallCard");

        // Card body
        HBox cardBody = new HBox();
        cardBody.getStyleClass().addAll(cardClass, "smallCardBody");

        // Value and title container
        VBox cardValueContainer = new VBox();
        cardValueContainer.getStyleClass().add("smallCardValueContainer");

        Label cardValue = new Label(String.format("%04d", Integer.parseInt(value)));
        cardValue.getStyleClass().add("smallCardValue");

        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add("smallCardTitle");

        // Icon view
        VBox iconView = new VBox();
        iconView.getStyleClass().add("smallCardIconView");

        HandleIcon.addMaterialDesignIcon(iconView, "smallCardIcon", icon);
        cardValueContainer.getChildren().addAll(cardValue, cardTitle);
        cardBody.getChildren().addAll(iconView, cardValueContainer);
        card.getChildren().add(cardBody);

        target.getChildren().add(card);
    }

    public static void addTableAction(Pane target, String title, MaterialDesignIcon icon, String className, EventHandler<MouseEvent> evt){
        // body
        HBox body = new HBox();
        body.getStyleClass().addAll(className, "tableActionButton");

        Label text = new Label(title);
        text.getStyleClass().add("tableActionButtonText");

        // Icon view
        VBox iconView = new VBox();
        iconView.getStyleClass().add("tableActionButtonIconView");

        HandleIcon.addMaterialDesignIcon(iconView, "tableActionIcon", icon);
        body.getChildren().addAll(iconView, text);

        body.addEventHandler(MouseEvent.MOUSE_CLICKED, evt);
        target.getChildren().add(body);
    }

    public static void addChart(VBox target, JsonArray values, String title, String chartClass){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
        XYChart.Series<String, Number> data = new XYChart.Series<>();

        lineChart.setTitle(title);
        lineChart.getStyleClass().add(chartClass);
        lineChart.setCreateSymbols(false);

        values.forEach( value -> {
            JsonArray array = value.getAsJsonArray();
            data.getData().add(new XYChart.Data<>( array.get(0).getAsString(), array.get(1).getAsInt()));
        });

        lineChart.getData().add(data);
        target.getChildren().add(lineChart);
    }

    public static String formatDate(Date date){
        Locale locale = new Locale("pt","BR");
        return new SimpleDateFormat("dd 'de' MMMM 'de' YYYY 'as' HH:mm:ss z", locale).format(date);
    }
}
