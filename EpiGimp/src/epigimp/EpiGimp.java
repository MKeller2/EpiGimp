/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epigimp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EpiGimp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = 
                new FXMLLoader(getClass().getResource("gimpfxml.fxml"));
        Parent root = (Parent)loader.load();
        gimpfxmlController controller = 
                (gimpfxmlController)loader.getController();
        controller.setStage(stage);
        stage.setTitle("EpiGimp");
        stage.setScene(new Scene(root));
        controller.setup();
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
