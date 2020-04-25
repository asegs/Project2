import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class SafeGUI extends Application {
    public static Main main = new Main();
    public static String filename = "src/test1.txt";
    public static String[][] safe = main.createSafe(filename);
    public static boolean editing = false;
    public static Label status = new Label("Ready!");
    @Override

    /*
    Does pretty much everything. Creates a 2 dimensional array of buttons with text,
    as well as interactive buttons to control the safe. Will explain more in method.
     */
    public void start(Stage stage) throws Exception {
            VBox vBox = new VBox();
            //Creates the visualGrid of buttons.
            Button[][] visualSafe = new Button[safe.length][safe[0].length];
            for (int i = 0; i < safe.length; i++) {
                HBox hBox = new HBox();
                for (int b = 0; b < safe[0].length; b++) {
                    PictureButton pb = new PictureButton("",i,b,visualSafe);
                    pb.getButton().setText(safe[i][b]);
                    visualSafe[i][b] = pb.getButton();
                    hBox.getChildren().add(pb.getButton());
                }
                vBox.getChildren().add(hBox);
            }
            //Shows if the safe is solved

            HBox actionButtons = new HBox();
            //Runs script to see if safe is solved, updates status label
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
            //Resets the safe from the file
            Button restart =  new Button("Restart");
            restart.setOnAction(e->{
                safe = main.createSafe(filename);
                updateStage(visualSafe,safe);
            });
            //Below allows the user to see a drop down of the .txt files in the directory.
        File f = new File("/home/aaron/IdeaProjects/Project2/src"); //CHANGE THIS LINE TO RUN ON OTHER MACHINES, SORRY :(
        String[] fileNamesRaw = f.list();
        ArrayList<String> filenames = new ArrayList<>();
        for (String file:fileNamesRaw){
            if (file.substring(file.indexOf("."),file.length()).equals(".txt")){
                filenames.add(file);
            }
        }
        //Adds .txt files to observable list and creates a select combo box to choose one
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll(filenames);

        ComboBox<String>comboBox = new ComboBox<>(options);
        comboBox.setPromptText("Load");
        comboBox.setOnAction(e->{
            filename = "src/"+comboBox.getValue();
            safe = main.createSafe(filename);
            stage.setTitle(filename);
            updateStage(visualSafe,safe);
        });
        //Allows the user to use an editor to change the file
        Button edit = new Button("Editor");
        //Lets user add new file in textfield and name it
        TextField newName = new TextField();
        //Creates new file with textfield name
        Button create = new Button("Create");
        create.setOnAction(e->{
            System.out.println(safe[0][0]);
            fileGenerator.createFile("src/"+newName.getText()+".txt");
            String formattedSafe = safe.length +" "+ safe[0].length+"\n";
            for (int i = 0;i<safe.length;i++){
                for (int b = 0;b<safe[0].length;b++){
                    formattedSafe+=visualSafe[i][b].getText()+" ";
                }
                formattedSafe+="\n";
            }
            System.out.println(formattedSafe);
            fileEditor.replaceFile("src/"+newName.getText()+".txt",formattedSafe,false);
        });
        //If editor is present, remove it.  If not add editor and turn on editing mode
        edit.setOnAction(e->{
            if (actionButtons.getChildren().contains(newName)){
                actionButtons.getChildren().removeAll(newName,create);
                editing = false;
            }
            else {
                safe = main.createSafe(filename);
                updateStage(visualSafe,safe);
                actionButtons.getChildren().addAll(newName,create);
                editing = true;

            }
        });
        //Exits program
        Button quit = new Button("Quit");
        quit.setOnAction(e->System.exit(0));

        //Adds everything to VBoxes and HBoxes
            actionButtons.getChildren().addAll(check,restart,comboBox,edit,quit);
            vBox.getChildren().addAll(actionButtons,status);
            Scene scene = new Scene(vBox);
            stage.setTitle(filename);
            stage.setScene(scene);
            stage.show();
    }

/*
Updates the grid of buttons when called, uses the text in the two dimensional String array 'safe'
 */
    public static void updateStage(Button[][] visual,String[][] safe){

        for (int i = 0;i<visual.length;i++){
            for (int b = 0;b<visual[0].length;b++){
                visual[i][b].setText(safe[i][b]);
            }
        }
    }

}
