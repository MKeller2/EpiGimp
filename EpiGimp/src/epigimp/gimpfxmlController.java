/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epigimp;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public class gimpfxmlController implements Initializable {
    
    @FXML
    private ColorPicker colorpicker;
    
    @FXML
    private TextField bsize;
    
    @FXML
    private Canvas canvas;

    @FXML
    private Button brush;

    @FXML
    private Button recbrush;
    
    @FXML
    private Pane pane;
    
    @FXML private TextField wWidth;
    @FXML private TextField wHeight;
    @FXML private ToolBar topToolbar;
    @FXML private ToolBar leftToolbar;
    
    private Stage primaryStage;
    
    boolean brushSelected = false;
    
    boolean brushRecSelected = false;
    
    GraphicsContext brushTool;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged( e -> {
            double size = Double.parseDouble(bsize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;
            
            if(brushSelected == true && !bsize.getText().isEmpty()) {
                brushTool.setFill(colorpicker.getValue());
                brushTool.fillRoundRect(x, y, size, size, size ,size);
            }
            else if(brushRecSelected == true && !bsize.getText().isEmpty()) {
                brushTool.setFill(colorpicker.getValue());
                brushTool.fillRect(x, y, size, size);
           }
        });
    }
   
    public void setup() {
       ChangeListener<Number> stageListener = (obs, oldValue, newValue) -> {
            double width = this.primaryStage.getWidth();
            double height = this.primaryStage.getHeight();
            this.topToolbar.setPrefWidth(width);
            this.leftToolbar.setPrefHeight(height - 36);
            this.canvas.setHeight(height - 46);
            this.canvas.setWidth(width - 52);
            this.primaryStage.setWidth(width);
            this.primaryStage.setHeight(height);
            this.wHeight.setText(String.valueOf(height));
            this.wWidth.setText(String.valueOf(width));
        };
       this.primaryStage.widthProperty().addListener(stageListener);
       this.primaryStage.heightProperty().addListener(stageListener);
       this.primaryStage.setMinWidth(280);
       this.primaryStage.setMinHeight(330);
    }
    
    @FXML
    public void saveCanvas(ActionEvent e) {
        FileChooser filechooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("png files (*.png", "*.png");
        filechooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage)pane.getScene().getWindow();
        File file = filechooser.showSaveDialog(stage);
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        filechooser.setTitle("Save your drawings");
        try
        {
            if (file != null)
            {
              WritableImage image = new WritableImage((int) canvas.getWidth(),
                      (int)canvas.getHeight());
              canvas.snapshot(null, image);
              RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
              ImageIO.write(renderedImage, "png", file);
            }
        }
        catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
    
    @FXML
    public void clearCanvas(ActionEvent e) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    @FXML
    public void toolselected(ActionEvent e) {
        brushSelected = true;
        brushRecSelected = false;
        recbrush.getStyleClass().removeAll("selected");
        brush.getStyleClass().add("selected");
    }
    
    @FXML
    public void brushrecselected(ActionEvent e) {
        brushSelected = false;
        brushRecSelected = true;
        brush.getStyleClass().removeAll("selected");
        recbrush.getStyleClass().add("selected");
    }
    
    @FXML
    public void closeWindow(ActionEvent e) {
       Stage stage = (Stage)pane.getScene().getWindow();
       stage.close();
    }
    
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    @FXML
    public void openSettings(ActionEvent e) throws Exception {
        try {
            FXMLLoader loader = 
                    new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = (Parent)loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Settings");
            stage.show();
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public static boolean checkInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkDimensions(String width, String height) {
        if (!checkInteger(width) || !checkInteger(height))
            return false;
        double newWidth = Double.parseDouble(width);
        double newHeight = Double.parseDouble(height);
        if (newWidth <= 1920.0 && newWidth >= 280.0 && newHeight <= 1080.0 && 
                newHeight >= 330.0) {
            return true;
        }
        return false;
    }
    
    @FXML
    public void changeWindowSize(ActionEvent e) {
        String width = this.wWidth.getText();
        String height = this.wHeight.getText();
        if (checkDimensions(width, height)) {
            double doubleWidth = Double.parseDouble(width);
            double doubleHeight = Double.parseDouble(height);
            this.topToolbar.setPrefWidth(doubleWidth);
            this.leftToolbar.setPrefHeight(doubleHeight - 36);
            this.canvas.setHeight(doubleHeight - 46);
            this.canvas.setWidth(doubleWidth - 52);
            this.primaryStage.setWidth(doubleWidth);
            this.primaryStage.setHeight(doubleHeight);
        }
        else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Wrong dimensions");
        alert.setHeaderText(null);
        alert.setContentText("You have entered wrong dimensions"
                + " for Width and/or Height fields");
        this.wHeight.setText("" + this.primaryStage.getHeight());
        this.wWidth.setText("" + this.primaryStage.getWidth());
        alert.showAndWait();
        }
    }
}
