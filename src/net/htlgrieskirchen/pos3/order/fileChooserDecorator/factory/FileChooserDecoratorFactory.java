package net.htlgrieskirchen.pos3.order.fileChooserDecorator.factory;

import net.htlgrieskirchen.pos3.order.fileChooserDecorator.FileChooserDecorator;
import net.htlgrieskirchen.pos3.order.fileChooserDecorator.IFileChooserDecorator;
import net.htlgrieskirchen.pos3.order.fileChooserDecorator.TestingFileChooserDecorator;

public class FileChooserDecoratorFactory {
    public static IFileChooserDecorator createFileChooserDecorator(boolean testing) {
        if(testing) {
            return new TestingFileChooserDecorator();
        }

        return new FileChooserDecorator();
    }
}
