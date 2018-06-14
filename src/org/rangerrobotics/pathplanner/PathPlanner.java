package org.rangerrobotics.pathplanner;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.rangerrobotics.pathplanner.gui.MainScene;

import java.io.File;

public class PathPlanner extends Application {
    private static Stage mStage;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
        mStage = stage;
        mStage.setTitle("PathPlanner");
        mStage.getIcons().add(new Image(getClass().getResourceAsStream("32.png")));
        mStage.getIcons().add(new Image(getClass().getResourceAsStream("64.png")));
        mStage.getIcons().add(new Image(getClass().getResourceAsStream("128.png")));

        mStage.setOnCloseRequest(event -> {
            event.consume();
            mStage.close();
            System.exit(0);
        });

        mStage.setScene(MainScene.getScene());
        mStage.show();
    }

    public static File chooseOutputFolder(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Destination Folder...");
        if(!Preferences.lastGenerateDir.equals("none")) {
            chooser.setInitialDirectory(new File(Preferences.lastGenerateDir));
        }
        return chooser.showDialog(mStage);
    }

    public static File chooseSaveFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save as...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PATH files (*.path)", "*.path"));
        chooser.setInitialFileName(Preferences.currentPathName);
        if(!Preferences.lastPathDir.equals("none")) {
            chooser.setInitialDirectory(new File(Preferences.lastPathDir));
        }
        return chooser.showSaveDialog(mStage);
    }

    public static File chooseLoadFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open path...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PATH files (*.path)", "*.path"));
        if(!Preferences.lastPathDir.equals("none")) {
            chooser.setInitialDirectory(new File(Preferences.lastPathDir));
        }
        return chooser.showOpenDialog(mStage);
    }
}
