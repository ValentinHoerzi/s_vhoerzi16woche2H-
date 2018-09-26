/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Valentin
 */
public class AlertHelper {
    
    private final Alert alert;

    public AlertHelper(AlertType type, String title, String headerText, String content)
    {
        alert = new Alert(type, content);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
    }

    public Alert getAlert()
    {
        return alert;
    }

    public void changeButtons()
    {
        List<ButtonType> list = new ArrayList<>();
        list.add(new ButtonType("Yes"));
        list.add(new ButtonType("No"));
        list.add(new ButtonType("Cancel"));
        alert.getButtonTypes().setAll(list);
    }
}
