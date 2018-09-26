/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.fx;

import net.htlgrieskirchen.pos3.order.fileChooserDecorator.IFileChooserDecorator;
import net.htlgrieskirchen.pos3.order.fileChooserDecorator.factory.FileChooserDecoratorFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.htlgrieskirchen.pos3.order.model.AlertHelper;
import net.htlgrieskirchen.pos3.order.model.Model;
import net.htlgrieskirchen.pos3.order.model.Order;
import net.htlgrieskirchen.pos3.order.model.Position;

/**
 *
 * @author Torsten Welsch
 */
public class Controller implements Initializable {

    @FXML
    private Menu menuFile;
    @FXML
    private Menu menuLanguage;
    @FXML
    private MenuItem menuOpen;
    @FXML
    private MenuItem menuSave;
    @FXML
    private MenuItem menuSaveAs;
    @FXML
    private MenuItem menuExit;
    @FXML
    private Label labelOrder;
    @FXML
    private Label labelPosition;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldStreet;
    @FXML
    private TextField textFieldZipCode;
    @FXML
    private TextField textFieldCity;
    @FXML
    private TextField textFieldAmount;
    @FXML
    private TextField textFieldDescription;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private Label labelOrderPrice;
    @FXML
    private Label labelPositionPrice;
    @FXML
    private TableView<Order> tableOrders;
    @FXML
    private TableColumn<Order, Long> columnId;
    @FXML
    private TableColumn<Order, Date> columnDate;
    @FXML
    private TableColumn<Order, String> columnLastName;
    @FXML
    private TableColumn<Order, String> columnFirstName;
    @FXML
    private TableColumn<Order, String> columnStreet;
    @FXML
    private TableColumn<Order, Integer> columnZipCode;
    @FXML
    private TableColumn<Order, String> columnCity;
    @FXML
    private TableView<Position> tablePositions;
    @FXML
    private TableColumn<Position, Integer> columnAmount;
    @FXML
    private TableColumn<Position, String> columnDescription;
    @FXML
    private TableColumn<Position, Double> columnPrice;
    @FXML
    private Button buttonOrderInsert;
    @FXML
    private Button buttonPositionInsert;
    @FXML
    private Button buttonPositionChange;
    @FXML
    private Button buttonOrderChange;

    private IFileChooserDecorator fileChooser;

    private File file;
    private Model model;
    private Order selectedOrder;
    private Position selectedPosition;
    private ResourceBundle rb;
    private boolean unsaved = false;
    @FXML
    private MenuItem menuGerman;
    @FXML
    private MenuItem menuEnglish;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new Model();
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        columnZipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
    }

    public void initFileChooser(boolean testing) //No Savecheck needed
    {
        this.fileChooser = FileChooserDecoratorFactory.createFileChooserDecorator(testing);
    }

    @FXML
    private void handleOrderSelection(MouseEvent event) //No Savecheck needed
    {
        selectedOrder = tableOrders.getSelectionModel().getSelectedItem();

        if (selectedOrder != null)
        {
            datePicker.setValue(selectedOrder.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            textFieldFirstName.setText(selectedOrder.getFirstName());
            textFieldLastName.setText(selectedOrder.getLastName());
            textFieldCity.setText(selectedOrder.getCity());
            textFieldStreet.setText(selectedOrder.getStreet());
            textFieldZipCode.setText(String.valueOf(selectedOrder.getZipCode()));

            labelOrderPrice.setText(String.valueOf(selectedOrder.calcTotalPrice()));

            ObservableList<Position> list = FXCollections.observableArrayList(selectedOrder.getPositions());
            tablePositions.setItems(list);
        }
    }

    @FXML
    private void handleButtonOrderChange(ActionEvent event) //Savecheck
    {
        if (selectedOrder != null)
        {
            unsaved = true;
            LocalDate localDate;
            Date date = null;
            int zipCode = 0;
            String lastName = null;
            String firstName = null;
            String street = null;
            String city = null;
            try
            {
                localDate = datePicker.getValue();
                date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                zipCode = Integer.parseInt(textFieldZipCode.getText());
            } catch (NumberFormatException e)
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Zip Code or Date", "A problem occured");
                Alert alert = helper.getAlert();
                alert.show();
                return;
            }
            lastName = textFieldLastName.getText();
            firstName = textFieldFirstName.getText();
            street = textFieldStreet.getText();
            city = textFieldCity.getText();

            if ("".equals(lastName) || "".equals(firstName) || "".equals(street) || "".equals(city))
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Last name, first name, street, city", "A problem occured");
                Alert alert = helper.getAlert();
                alert.show();
                return;
            }
            selectedOrder.setCity(city);
            selectedOrder.setDate(date);
            selectedOrder.setFirstName(firstName);
            selectedOrder.setLastName(lastName);
            selectedOrder.setStreet(street);
            selectedOrder.setZipCode(zipCode);
            clearTextFieldsOrder();
            clearTextFieldsPosition();
            labelOrderPrice.setText("");
            labelPositionPrice.setText("");
            tablePositions.getItems().clear();
            tableOrders.getSelectionModel().clearSelection();
            tableOrders.refresh();

        } else
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "No Selected Item", "An error occured");
            Alert alert = helper.getAlert();
            alert.show();
        }
    }

    @FXML
    private void handleButtonOrderInsert(ActionEvent event)//Savecheck
    {
        LocalDate localDate;
        Date date = null;
        int zipCode = 0;
        String lastName = null;
        String firstName = null;
        String street = null;
        String city = null;
        try
        {
            localDate = datePicker.getValue();
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            zipCode = Integer.parseInt(textFieldZipCode.getText());
        } catch (NumberFormatException e)
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Zip Code or Date", "A problem occured");
            Alert alert = helper.getAlert();
            alert.show();
            return;
        }
        lastName = textFieldLastName.getText();
        firstName = textFieldFirstName.getText();
        street = textFieldStreet.getText();
        city = textFieldCity.getText();

        if ("".equals(lastName) || "".equals(firstName) || "".equals(street) || "".equals(city) || "".equals(date.toString()))
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Last name, first name, street, city", "A problem occured");
            Alert alert = helper.getAlert();
            alert.show();
            return;
        }
        Order o = new Order(date, lastName, firstName, street, zipCode, city);
        model.addOrder(o);
        ObservableList<Order> items = FXCollections.observableArrayList(model.getOrders());
        tableOrders.setItems(items);

        clearTextFieldsOrder();
        clearTextFieldsPosition();
        labelOrderPrice.setText("");
        labelPositionPrice.setText("");
        tablePositions.getItems().clear();
        tableOrders.getSelectionModel().clearSelection();
        unsaved = true;
    }

    @FXML
    private void handlePositionSelection(MouseEvent event) //No Savecheck needed
    {
        selectedPosition = tablePositions.getSelectionModel().getSelectedItem();
        if (selectedPosition != null)
        {
            textFieldAmount.setText(String.valueOf(selectedPosition.getAmount()));
            textFieldDescription.setText(selectedPosition.getDescription());
            textFieldPrice.setText(String.valueOf(selectedPosition.getPricePerUnit()));
            
            labelPositionPrice.setText(String.valueOf(selectedPosition.calcTotalPrice()));
        }
    }

    @FXML
    private void handleButtonPositionChange(ActionEvent event)//Savecheck
    {
        if (selectedPosition != null)
        {
            unsaved=true;
            String am = textFieldAmount.getText();
            int amount = 0;
            String pr = textFieldPrice.getText();
            double price = 0;
            String desc = textFieldDescription.getText();
            try
            {
                amount = Integer.parseInt(am);
                price = Double.parseDouble(pr);
            } catch (NumberFormatException e)
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Amount or price", "A problem occured");
                Alert alert = helper.getAlert();
                alert.show();
                clearTextFieldsPosition();
                return;
            }

            if ("".equals(desc) || "".equals(am) || "".equals(pr))
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Description,amount,price", "One of those fields is empty");
                Alert alert = helper.getAlert();
                alert.show();
                clearTextFieldsPosition();
                return;
            }

            List<Position> positions = selectedOrder.getPositions();
            for(Position p : positions){
                if(p.equals(selectedPosition)){
                    p.setAmount(amount);
                    p.setDescription(desc);
                    p.setPricePerUnit(price);
                }
            }
            
            
            selectedOrder.setPositions(positions);
            selectedPosition.setAmount(amount);
            selectedPosition.setDescription(desc);
            selectedPosition.setPricePerUnit(price);
            labelPositionPrice.setText(String.valueOf(selectedPosition.calcTotalPrice()));

            labelOrderPrice.setText("");
            labelPositionPrice.setText("");
            tableOrders.refresh();
            tablePositions.refresh();
        } else
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "No position selected", "You have to select a postion first");
            Alert a = helper.getAlert();
            a.show();
        }
        clearTextFieldsPosition();
        clearTextFieldsOrder();
    }

    @FXML
    private void handleButtonPositionInsert(ActionEvent event)
    {
        if (selectedOrder != null)
        {
            unsaved=true;
            String am = textFieldAmount.getText();
            int amount = 0;
            String pr = textFieldPrice.getText();
            double price = 0;
            String desc = textFieldDescription.getText();
            try
            {
                amount = Integer.parseInt(am);
                price = Double.parseDouble(pr);
            } catch (NumberFormatException e)
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Amount or price", "A problem occured");
                Alert alert = helper.getAlert();
                alert.show();
                clearTextFieldsPosition();
                return;
            }

            if ("".equals(desc) || "".equals(am) || "".equals(pr))
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "Description,amount,price", "One of those fields is empty");
                Alert alert = helper.getAlert();
                alert.show();
                clearTextFieldsPosition();
                return;
            }

            Position newPos = new Position(amount, desc, price);
            selectedOrder.addPosition(newPos);
            ObservableList<Position> list = FXCollections.observableArrayList(selectedOrder.getPositions());
            tablePositions.setItems(list);
            tableOrders.refresh();
            tablePositions.refresh();
        } else
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "No order selected", "You have to select an order first");
            Alert a = helper.getAlert();
            a.show();
        }
        labelOrderPrice.setText("");
        labelPositionPrice.setText("");
        clearTextFieldsOrder();
        clearTextFieldsPosition();
    }

    @FXML
    private void handleMenuOpen(ActionEvent event)
    {
        if (unsaved)
        {
            AlertHelper helper = new AlertHelper(AlertType.CONFIRMATION, "Save", "Unsaved changes", "Do you want to save before opening a file?");
            helper.changeButtons();
            Alert alert = helper.getAlert();
            String text = alert.showAndWait().get().getText();
            if (text.equals("Yes"))
            {
                handleMenuSave(event);
            } else if (text.equals("Cancel"))
            {
                return;
            }
        }
        try
        {
            file=fileChooser.showOpenDialog(Main.getStage());
            model.readXml(file);
        } catch (FileNotFoundException ex)
        {
            AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "File not found", "It appears that there is no such file");
            Alert alert = helper.getAlert();
            alert.show();
        }

        clearTextFieldsOrder();
        clearTextFieldsPosition();

        tableOrders.getItems().clear();
        tablePositions.getItems().clear();

        unsaved = true;
        file = null;

        ObservableList<Order> items = FXCollections.observableArrayList(model.getOrders());
        tableOrders.setItems(items);

    }

    @FXML
    private void handleMenuSave(ActionEvent event)
    {
        if (file != null)
        {
            try
            {
                model.writeXml(file);
            } catch (FileNotFoundException ex)
            {
                AlertHelper helper = new AlertHelper(AlertType.ERROR, "ERROR", "File not found", "It appears that there is no such file");
                Alert alert = helper.getAlert();
                alert.show();
            }
        } else
        {
            handleMenuSaveAs(event);
        }
    }

    @FXML
    private void handleMenuSaveAs(ActionEvent event)
    {
        file = fileChooser.showSaveDialog(Main.getStage());
        handleMenuSave(event);
    }

    @FXML
    private void handleMenuExit(ActionEvent event)
    {
        if (unsaved)
        {
            AlertHelper helper = new AlertHelper(AlertType.CONFIRMATION, "Save", "Unsaved changes", "Do you want to save your progress before quitting?");
            helper.changeButtons();
            Alert alert = helper.getAlert();
            String text = alert.showAndWait().get().getText();
            if (text.equals("Yes"))
            {
                handleMenuSave(event);
            } else if (text.equals("Cancel"))
            {
                return;
            }
        }
        Platform.exit();
        System.exit(0);
    }

    //<editor-fold desc="More methodes">
    @FXML
    private void handleMenuGerman(ActionEvent event)
    {
        changeLanguage(Locale.GERMAN);
    }

    @FXML
    private void handleMenuEnglish(ActionEvent event)
    {
        changeLanguage(Locale.ENGLISH);
    }

    private void changeLanguage(Locale locale)
    {
        rb = ResourceBundle.getBundle("net.htlgrieskirchen.pos3.order.fx.MessageBundle", locale);
        menuFile.setText(rb.getString("file"));
        menuOpen.setText(rb.getString("open"));
        menuSave.setText(rb.getString("save"));
        menuSaveAs.setText(rb.getString("saveAs"));
        menuExit.setText(rb.getString("exit"));
        menuLanguage.setText(rb.getString("language"));
        menuEnglish.setText(rb.getString("english"));
        menuGerman.setText(rb.getString("german"));

        labelOrder.setText(rb.getString("orders"));

        columnDate.setText(rb.getString("date"));
        columnLastName.setText(rb.getString("lastName"));
        columnFirstName.setText(rb.getString("firstName"));
        columnStreet.setText(rb.getString("street"));
        columnZipCode.setText(rb.getString("zipCode"));
        columnCity.setText(rb.getString("city"));

        buttonOrderChange.setText(rb.getString("change"));
        buttonOrderInsert.setText(rb.getString("insert"));

        labelPosition.setText(rb.getString("positions"));

        columnAmount.setText(rb.getString("amount"));
        columnDescription.setText(rb.getString("description"));
        columnPrice.setText(rb.getString("price"));

        buttonPositionChange.setText(rb.getString("change"));
        buttonPositionInsert.setText(rb.getString("insert"));
    }

    private void clearTextFieldsPosition()
    {
        textFieldAmount.setText("");
        textFieldPrice.setText("");
        textFieldDescription.setText("");
    }

    private void clearTextFieldsOrder()
    {
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldStreet.setText("");
        textFieldCity.setText("");
        datePicker.setValue(null);
        textFieldZipCode.setText("");
    }
    //</editor-fold>
}
