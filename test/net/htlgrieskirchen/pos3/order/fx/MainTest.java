package net.htlgrieskirchen.pos3.order.fx;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import net.htlgrieskirchen.pos3.order.model.Order;
import net.htlgrieskirchen.pos3.order.model.Position;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;

public class MainTest extends ApplicationTest {
    private static final int WAIT_TIME = 100;

    private FxRobot robot = new FxRobot();
    CountDownLatch menuShownLatch = new CountDownLatch(1);



    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("net.htlgrieskirchen.pos3.order.fx.MessageBundle", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"), rb);
        Parent mainNode = loader.load();
        Controller controller = loader.getController();
        controller.initFileChooser(true);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Order.globalId = 0;
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        GradingUtil.increaseMaxPoints();
    }


    @Test
    public void testGermanChange() throws InterruptedException {
        robot.clickOn("#menuLanguage");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        robot.clickOn("#menuGerman");

        verifyThat("#labelOrder", LabeledMatchers.hasText("Bestellungen"));
        assertEquals(((TableColumnHeader) lookup("#columnDate").query()).getTableColumn().getText(), "Datum");
        assertEquals(((TableColumnHeader) lookup("#columnLastName").query()).getTableColumn().getText(), "Nachname");
        assertTrue(((TableColumnHeader) lookup("#columnStreet").query()).getTableColumn().getText().contains("Stra"));
        assertEquals(((TableColumnHeader) lookup("#columnFirstName").query()).getTableColumn().getText(), "Vorname");
        assertEquals(((TableColumnHeader) lookup("#columnZipCode").query()).getTableColumn().getText(), "PLZ");
        assertEquals(((TableColumnHeader) lookup("#columnCity").query()).getTableColumn().getText(), "Stadt");

        assertEquals(((TableColumnHeader) lookup("#columnAmount").query()).getTableColumn().getText(), "Menge");
        assertEquals(((TableColumnHeader) lookup("#columnDescription").query()).getTableColumn().getText(), "Beschreibung");
        assertEquals(((TableColumnHeader) lookup("#columnPrice").query()).getTableColumn().getText(), "Preis");

        assertTrue(((Button) lookup("#buttonOrderChange").query()).getText().contains("ndern"));
        assertTrue(((Button) lookup("#buttonOrderInsert").query()).getText().contains("Einf"));
        assertTrue(((Button) lookup("#buttonOrderInsert").query()).getText().contains("gen"));


        GradingUtil.increaseAchievedPoints();
    }

    @AfterClass
    public static void doYourOneTimeTeardown() {
        GradingUtil.computeGrading();
    }

    @Test
    public void testEnglishChange() throws InterruptedException {
        robot.clickOn("#menuLanguage");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        robot.clickOn("#menuEnglish");

        verifyThat("#labelOrder", LabeledMatchers.hasText("Orders"));

        assertEquals(((TableColumnHeader) lookup("#columnDate").query()).getTableColumn().getText(), "Date");
        assertEquals(((TableColumnHeader) lookup("#columnLastName").query()).getTableColumn().getText(), "Last Name");
        assertTrue(((TableColumnHeader) lookup("#columnStreet").query()).getTableColumn().getText().contains("Street"));
        assertEquals(((TableColumnHeader) lookup("#columnFirstName").query()).getTableColumn().getText(), "First Name");
        assertEquals(((TableColumnHeader) lookup("#columnZipCode").query()).getTableColumn().getText(), "Zip Code");
        assertEquals(((TableColumnHeader) lookup("#columnCity").query()).getTableColumn().getText(), "City");

        assertEquals(((TableColumnHeader) lookup("#columnAmount").query()).getTableColumn().getText(), "Amount");
        assertEquals(((TableColumnHeader) lookup("#columnDescription").query()).getTableColumn().getText(), "Description");
        assertEquals(((TableColumnHeader) lookup("#columnPrice").query()).getTableColumn().getText(), "Price");

        assertTrue(((Button) lookup("#buttonOrderChange").query()).getText().contains("Change"));
        assertTrue(((Button) lookup("#buttonOrderInsert").query()).getText().contains("Insert"));

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testOpenFile() throws InterruptedException, ParseException {
        loadFile();

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertEquals(4, orderTableView.getItems().size());

        Order o1 = new Order();
        o1.setId(0);
        o1.setLastName("a");
        o1.setFirstName("b");
        o1.setStreet("c");
        o1.setZipCode(1);
        o1.setCity("d");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-16"));

        Position p1 = new Position();
        p1.setAmount(47);
        p1.setDescription("Something");
        p1.setPricePerUnit(11d);
        o1.addPosition(p1);

        Position p2 = new Position();
        p2.setAmount(11);
        p2.setDescription("Someotherthing34");
        p2.setPricePerUnit(-4d);
        o1.addPosition(p2);

        Position p3 = new Position();
        p2.setAmount(2);
        p2.setDescription("asdf");
        p2.setPricePerUnit(4d);
        o1.addPosition(p3);

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        Order o2 = new Order();
        o2.setId(1);
        o2.setLastName("");
        o2.setFirstName("");
        o2.setStreet("");
        o2.setZipCode(0);
        o2.setCity("");
        o2.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-10"));

        assertTrue(findOrderItem(orderTableView.getItems(), o2));

        Order o3 = new Order();
        o3.setId(2);
        o3.setLastName("tNN");
        o3.setFirstName("tVN");
        o3.setStreet("tStr");
        o3.setZipCode(1234);
        o3.setCity("tStadt");
        o3.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-10"));

        Order o4 = new Order();
        o4.setId(3);
        o4.setLastName("t1");
        o4.setFirstName("t2");
        o4.setStreet("t3");
        o4.setZipCode(4321);
        o4.setCity("t4");
        o4.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-10"));

        Position p4 = new Position();
        p4.setAmount(4);
        p4.setDescription("abc");
        p4.setPricePerUnit(43d);
        o4.addPosition(p4);

        assertTrue(findOrderItem(orderTableView.getItems(), o4));

        GradingUtil.increaseAchievedPoints();

    }

    private boolean findOrderItem(ObservableList<Order> orderList, Order orderToFind) {
        for (Order o : orderList) {
            if (orderEqualsOrder(o, orderToFind))
                return true;
        }

        return false;
    }

    public boolean orderEqualsOrder(Order a, Order b) {
        if (a.getId() == b.getId() &&
                a.getZipCode() == b.getZipCode() &&
                Objects.equals(a.getDate(), b.getDate()) &&
                Objects.equals(a.getLastName(), b.getLastName()) &&
                Objects.equals(a.getFirstName(), b.getFirstName()) &&
                Objects.equals(a.getStreet(), b.getStreet()) &&
                Objects.equals(a.getCity(), b.getCity())) {

            if (a.getPositions().size() == b.getPositions().size() &&
                    a.getPositions().size() == 0 &&
                    b.getPositions().size() == 0)
                return true;

            for (Position p : a.getPositions()) {
                if (b.getPositions().contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    public void testChangeClickTextFields() throws InterruptedException {
        loadFile();

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertEquals(4, orderTableView.getItems().size());

        goToTableViewRow(0);

        TableView<Position> tablePositionsView = lookup("#tablePositions").query();

        assertEquals(3, tablePositionsView.getItems().size());

        DatePicker orderDatePicker = lookup("#datePicker").query();
        assertEquals(orderDatePicker.getValue().toString(), "2018-09-16");

        verifyThat("#textFieldLastName", hasText("a"));
        verifyThat("#textFieldFirstName", hasText("b"));
        verifyThat("#textFieldStreet", hasText("c"));
        verifyThat("#textFieldZipCode", hasText("1"));
        verifyThat("#textFieldCity", hasText("d"));

        goToTableViewRow(3);

        verifyThat("#textFieldLastName", hasText("t1"));
        verifyThat("#textFieldFirstName", hasText("t2"));
        verifyThat("#textFieldStreet", hasText("t3"));
        verifyThat("#textFieldZipCode", hasText("4321"));
        verifyThat("#textFieldCity", hasText("t4"));

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testChangeClickSubView() throws InterruptedException {
        loadFile();

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertEquals(4, orderTableView.getItems().size());

        goToTableViewRow(0);

        TableView<Position> tablePositionsView = lookup("#tablePositions").query();

        assertEquals(3, tablePositionsView.getItems().size());

        int firstCellInSubTableView = goToChildTableView(0);

        goToTableViewRow(firstCellInSubTableView + 1);

        verifyThat("#textFieldAmount", hasText("11"));
        verifyThat("#textFieldDescription", hasText("Someotherthing34"));
        verifyThat("#textFieldPrice", hasText("-4.0"));

        goToTableViewRow(firstCellInSubTableView + 2);

        verifyThat("#textFieldAmount", hasText("2"));
        verifyThat("#textFieldDescription", hasText("asdf"));
        verifyThat("#textFieldPrice", hasText("4.0"));

        GradingUtil.increaseAchievedPoints();
    }

    private int goToChildTableView(int startIndex) throws InterruptedException {
        int firstCellInSubTableView = -1;

        for(int i = 4; i < lookup(".table-row-cell").queryAll().size(); i++) {
            goToTableViewRow(startIndex);
            goToTableViewRow(i);

            TextField tfAmount = lookup("#textFieldAmount").query();
            TextField tfDescr = lookup("#textFieldDescription").query();
            TextField tfPrice = lookup("#textFieldPrice").query();

            if(!tfAmount.getText().equals("") &&
                    !tfDescr.getText().equals("") &&
                    !tfPrice.getText().equals("")) {
                firstCellInSubTableView = i;
                break;
            }
        }

        assertNotEquals(-1, firstCellInSubTableView);

        return firstCellInSubTableView;
    }

    private void loadFile() throws InterruptedException {
        robot.clickOn("#menuFile");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        robot.clickOn("#menuOpen");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    private void saveFile() throws InterruptedException {
        robot.clickOn("#menuFile");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        robot.clickOn("#menuSave");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    private void saveFileAs() throws InterruptedException {
        robot.clickOn("#menuFile");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        robot.clickOn("#menuSaveAs");
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testCalculationOrders() throws InterruptedException {
        loadFile();

        goToTableViewRow(0);
        assertEquals(((Label)lookup("#labelOrderPrice").query()).getText(), "481.0");
        goToTableViewRow(1);
        assertEquals(((Label)lookup("#labelOrderPrice").query()).getText(), "0.0");
        goToTableViewRow(2);
        assertEquals(((Label)lookup("#labelOrderPrice").query()).getText(), "0.0");
        goToTableViewRow(3);
        assertEquals(((Label)lookup("#labelOrderPrice").query()).getText(), "172.0");

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testCalculationPositions() throws InterruptedException {
        loadFile();

        goToTableViewRow(0);

        int firstCellInSubTableView = goToChildTableView(0);
        goToTableViewRow(0);

        goToTableViewRow(firstCellInSubTableView);
        assertEquals(((Label)lookup("#labelPositionPrice").query()).getText(), "517.0");

        goToTableViewRow(firstCellInSubTableView + 1);
        assertEquals(((Label)lookup("#labelPositionPrice").query()).getText(), "-44.0");

        goToTableViewRow(firstCellInSubTableView + 2);
        assertEquals(((Label)lookup("#labelPositionPrice").query()).getText(), "8.0");

        GradingUtil.increaseAchievedPoints();
    }

    private void goToTableViewRow(int n) throws InterruptedException {
        robot.clickOn((Node) lookup(".table-row-cell").nth(n).query());
        menuShownLatch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testChangeOrder() throws InterruptedException, ParseException {
        loadFile();
        changeOrder();

        Order o1 = new Order();
        o1.setId(0);
        o1.setLastName("lastname");
        o1.setFirstName("firstname");
        o1.setStreet("street");
        o1.setZipCode(1337);
        o1.setCity("city");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-16"));

        Position p1 = new Position();
        p1.setAmount(47);
        p1.setDescription("Something");
        p1.setPricePerUnit(11d);
        o1.addPosition(p1);

        Position p2 = new Position();
        p2.setAmount(11);
        p2.setDescription("Someotherthing34");
        p2.setPricePerUnit(-4d);
        o1.addPosition(p2);

        Position p3 = new Position();
        p2.setAmount(2);
        p2.setDescription("asdf");
        p2.setPricePerUnit(4d);
        o1.addPosition(p3);

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        GradingUtil.increaseAchievedPoints();
    }

    private void changeOrder() throws InterruptedException {
        goToTableViewRow(0);

        robot.clickOn("#textFieldLastName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("lastname");

        robot.clickOn("#textFieldFirstName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("firstname");

        robot.clickOn("#textFieldStreet");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("street");

        robot.clickOn("#textFieldZipCode");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1337");

        robot.clickOn("#textFieldCity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("city");

        robot.clickOn("#buttonOrderChange");
    }

    @Test
    public void testAddOrder() throws ParseException {

        Order.globalId = 0;

        robot.clickOn("#datePicker");
        robot.write("12.09.2018");

        robot.clickOn("#textFieldLastName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("lastname");

        robot.clickOn("#textFieldFirstName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("firstname");

        robot.clickOn("#textFieldStreet");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("street");

        robot.clickOn("#textFieldZipCode");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1337");

        robot.clickOn("#textFieldCity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("city");

        robot.clickOn("#buttonOrderInsert");


        Order o1 = new Order();
        o1.setId(0);
        o1.setLastName("lastname");
        o1.setFirstName("firstname");
        o1.setStreet("street");
        o1.setZipCode(1337);
        o1.setCity("city");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-12"));

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testAddOrderWithFileLoaded() throws InterruptedException, ParseException {
        loadFile();

        robot.clickOn("#datePicker");
        robot.write("12.09.2018");

        robot.clickOn("#textFieldLastName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("lastname");

        robot.clickOn("#textFieldFirstName");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("firstname");

        robot.clickOn("#textFieldStreet");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("street");

        robot.clickOn("#textFieldZipCode");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1337");

        robot.clickOn("#textFieldCity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("city");

        robot.clickOn("#buttonOrderInsert");

        Order o1 = new Order();
        o1.setId(4);
        o1.setLastName("lastname");
        o1.setFirstName("firstname");
        o1.setStreet("street");
        o1.setZipCode(1337);
        o1.setCity("city");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-12"));

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testChangePosition() throws InterruptedException, ParseException {
        loadFile();

        changePosition();

        Order o1 = new Order();
        o1.setId(0);
        o1.setLastName("a");
        o1.setFirstName("b");
        o1.setStreet("c");
        o1.setZipCode(1);
        o1.setCity("d");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-16"));

        Position p1 = new Position();
        p1.setAmount(12);
        p1.setDescription("foo");
        p1.setPricePerUnit(12.1d);
        o1.addPosition(p1);

        Position p2 = new Position();
        p2.setAmount(11);
        p2.setDescription("Someotherthing34");
        p2.setPricePerUnit(-4d);
        o1.addPosition(p2);

        Position p3 = new Position();
        p2.setAmount(2);
        p2.setDescription("asdf");
        p2.setPricePerUnit(4d);
        o1.addPosition(p3);

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        GradingUtil.increaseAchievedPoints();
    }

    private void changePosition() throws InterruptedException {
        goToTableViewRow(0);

        int firstCellInSubTableView = goToChildTableView(0);
        goToTableViewRow(firstCellInSubTableView);


        robot.clickOn("#textFieldAmount");
        typeBackSpace(2);
        robot.write("12");

        robot.clickOn("#textFieldDescription");
        typeBackSpace(9);
        robot.write("foo");

        robot.clickOn("#textFieldPrice");
        typeBackSpace(4);
        robot.write("12.1");

        robot.clickOn("#buttonPositionChange");

    }

    @Test
    public void testAddAndChangePositionWithoutOrder() {
        robot.clickOn("#textFieldAmount");
        typeBackSpace(2);
        robot.write("12");

        robot.clickOn("#textFieldDescription");
        typeBackSpace(9);
        robot.write("foo");

        robot.clickOn("#textFieldPrice");
        typeBackSpace(4);
        robot.write("12.1");

        robot.clickOn("#buttonPositionInsert");
        robot.clickOn("OK");

        robot.clickOn("#buttonPositionChange");
        robot.clickOn("OK");

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testAddPosition() throws InterruptedException, ParseException {
        loadFile();

        goToTableViewRow(0);

        robot.clickOn("#textFieldAmount");
        typeBackSpace(2);
        robot.write("12");

        robot.clickOn("#textFieldDescription");
        typeBackSpace(9);
        robot.write("foo");

        robot.clickOn("#textFieldPrice");
        typeBackSpace(4);
        robot.write("12.1");

        robot.clickOn("#buttonPositionInsert");

        Order o1 = new Order();
        o1.setId(0);
        o1.setLastName("a");
        o1.setFirstName("b");
        o1.setStreet("c");
        o1.setZipCode(1);
        o1.setCity("d");
        o1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-16"));

        Position p1 = new Position();
        p1.setAmount(47);
        p1.setDescription("Something");
        p1.setPricePerUnit(11d);
        o1.addPosition(p1);

        Position p2 = new Position();
        p2.setAmount(11);
        p2.setDescription("Someotherthing34");
        p2.setPricePerUnit(-4d);
        o1.addPosition(p2);

        Position p3 = new Position();
        p2.setAmount(2);
        p2.setDescription("asdf");
        p2.setPricePerUnit(4d);
        o1.addPosition(p3);

        Position p4 = new Position();
        p4.setAmount(12);
        p4.setDescription("foo");
        p4.setPricePerUnit(12.1d);
        o1.addPosition(p4);

        TableView<Order> orderTableView = lookup("#tableOrders").query();

        assertTrue(findOrderItem(orderTableView.getItems(), o1));

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testLoadAndSave() throws InterruptedException, IOException {
        loadFile();
        saveFile();

        String[] lines = FileConstants.TEST_FILE.split("\\r?\\n");
        File xmlFile = new File("orders_test.xml");
        TestUtil.assertContentContains(lines, xmlFile);

        GradingUtil.increaseAchievedPoints();
    }

    @Test
    public void testChangedSave() throws InterruptedException, IOException {
        loadFile();

        changeOrder();
        changePosition();

        saveFileAs();

        String[] lines = FileConstants.TEST_FILE_SAVED.split("\\r?\\n");
        File xmlFile = new File("orders_test_save.xml");
        TestUtil.assertContentContains(lines, xmlFile);

        File savedFile = new File("orders_test_save.xml");
        savedFile.delete();

        GradingUtil.increaseAchievedPoints();
    }


    private void typeBackSpace(int n) {
        for(int i = 0; i < n; i++) {
            robot.type(KeyCode.BACK_SPACE);
        }
    }
}