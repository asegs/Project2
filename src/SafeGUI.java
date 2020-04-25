import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class SafeGUI extends Application {
    Main main = new Main();
    String filename = "src/test1.txt";
    String[][] safe = main.createSafe(filename);
    @Override
    public void start(Stage stage) throws Exception {
            VBox vBox = new VBox();
            Button[][] visualSafe = new Button[safe.length][safe[0].length];
            for (int i = 0; i < safe.length; i++) {
                HBox hBox = new HBox();
                for (int b = 0; b < safe[0].length; b++) {
                    PictureButton pb = new PictureButton("",i,b,safe,visualSafe);
                    pb.getButton().setText(safe[i][b]);
                    visualSafe[i][b] = pb.getButton();
                    hBox.getChildren().add(pb.getButton());
                }
                vBox.getChildren().add(hBox);
            }
            Label status = new Label("Ready!");
            HBox actionButtons = new HBox();
            Button check = new Button("Check");
            check.setOnAction(e->{
                boolean done = main.verify(safe);
                if (done){
                    status.setText("Completed!");
                }
                else {
                    status.setText("Spaces are unguarded!");
                }
            });
            Button restart =  new Button("Restart");
            restart.setOnAction(e->{
                safe = main.createSafe(filename);
                updateStage(visualSafe,safe);
            });
        File f = new File("/home/aaron/IdeaProjects/Project2/src");
        String[] fileNamesRaw = f.list();
        ArrayList<String> filenames = new ArrayList<>();
        for (String file:fileNamesRaw){
            if (file.substring(file.indexOf("."),file.length()).equals(".txt")){
                filenames.add(file);
            }
        }
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll(filenames);

        ComboBox<String>comboBox = new ComboBox<>(options);
        comboBox.setPromptText("Load");
        comboBox.setOnAction(e->{
            filename = "src/"+comboBox.getValue();
            safe = main.createSafe(filename);
            updateStage(visualSafe,safe);
        });
            actionButtons.getChildren().addAll(check,restart,comboBox);
            vBox.getChildren().addAll(actionButtons,status);
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
    }


    public static void updateStage(Button[][] visual,String[][] safe){

        for (int i = 0;i<visual.length;i++){
            for (int b = 0;b<visual[0].length;b++){
                visual[i][b].setText(safe[i][b]);
            }
        }
    }

}
