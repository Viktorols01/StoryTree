package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {

    public static File getFile(String folderName, String description, String extension, boolean save) {
        JFileChooser chooser = new JFileChooser();
        File folder = new File(folderName);
        chooser.setCurrentDirectory(folder);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter(description, extension));
        switch (save ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                File file = new File(
                        folderName + "/" + chooser.getSelectedFile().getName() + (save ? ("." + extension) : ""));
                return file;
            default:
                return null;
        }
    }

    public static <T extends Serializable> void saveObject(T t, String pathname, String description, String extension) {
        File file = getFile(pathname, description, extension, true);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String pathname, String description, String extension) {
        File file = getFile(pathname, description, extension, false);
        if (file == null) {
            return null;
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(fis);
            try {
                return (T) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                ois.close();
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
