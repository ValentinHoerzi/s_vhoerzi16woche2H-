package net.htlgrieskirchen.pos3.order.fileChooserDecorator;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileChooserDecorator implements IFileChooserDecorator {
    private FileChooser fileChooser;

    public FileChooserDecorator() {
        fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Datei (*.xml)", "*.xml"));
    }

    @Override
    public File showOpenDialog(Window window) {
        return fileChooser.showOpenDialog(window);
    }

    @Override
    public File showSaveDialog(Window window) {
        return fileChooser.showSaveDialog(window);
    }
}
