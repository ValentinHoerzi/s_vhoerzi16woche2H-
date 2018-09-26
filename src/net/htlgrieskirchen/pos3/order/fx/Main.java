/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.fx;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Torsten Welsch
 */
public class Main extends Application {

    private static Controller controller;

    private static Stage stage;

    public static Stage getStage()
    {
        return stage;
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        ResourceBundle rb = ResourceBundle.getBundle("net.htlgrieskirchen.pos3.order.fx.MessageBundle", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"), rb);
        Parent root = loader.load();
        controller = loader.getController();
        controller.initFileChooser(false);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    public static Controller getController()
    {
        return controller;
    }

}
