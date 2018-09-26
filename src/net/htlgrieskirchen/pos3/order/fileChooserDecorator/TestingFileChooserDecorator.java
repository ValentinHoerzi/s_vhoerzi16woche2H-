package net.htlgrieskirchen.pos3.order.fileChooserDecorator;

import javafx.stage.Window;

import java.io.File;

public class TestingFileChooserDecorator implements IFileChooserDecorator {
    @Override
    public File showOpenDialog(Window window) {
        return new File("orders_test.xml");
    }

    @Override
    public File showSaveDialog(Window window) {
        return new File("orders_test_save.xml");
    }
}
