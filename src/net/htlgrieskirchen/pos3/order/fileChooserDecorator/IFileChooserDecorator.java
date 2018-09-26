package net.htlgrieskirchen.pos3.order.fileChooserDecorator;

import javafx.stage.Window;

import java.io.File;

public interface IFileChooserDecorator {
    File showOpenDialog(Window window);

    File showSaveDialog(Window window);
}
