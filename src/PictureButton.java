import javafx.scene.control.Button;

import java.util.ArrayList;

public class PictureButton {
    private String name;
    private Button button;
    private int row;
    private int col;

    public PictureButton(String name,int row,int col,Button[][] visualSafe){
        this.row = row;
        this.col = col;
        this.button = new Button(name);
        this.button.setMinSize(80,80);
        this.button.setMaxSize(80,80);
        this.button.setStyle("-fx-font-size: 36; ");
        this.button.setOnAction(e->{
            changeAtPoint();
            SafeGUI.updateStage(visualSafe, SafeGUI.safe);
        });
    }

    public Button getButton(){
        return this.button;
    }
    /*
    If not in editing mode, adds a laser if possible at a place.  If not
    possible, does nothing.  If in editing mode, cycles the current tile through all options.
     */
    public String[][] changeAtPoint(){
        String options = "01234XL.";
        if (!SafeGUI.editing) {
            if (SafeGUI.safe[row][col].equals("*")){
                SafeGUI.status.setText("This would be hit by a laser!");
            }
            Main main = new Main();
            if (SafeGUI.safe[row][col].equals(".")) {
                boolean place = main.canPlace(SafeGUI.safe, row, col);
                if (place) {
                    ArrayList<String> toReplace = new ArrayList<>();
                    toReplace.add(".");
                    toReplace.add("L");
                    toReplace.add("*");
                    SafeGUI.safe[row][col] = "L";
                    SafeGUI.safe = main.replaceFromPoint(SafeGUI.safe, row, col, toReplace, "*");
                }
            } else if (SafeGUI.safe[row][col].equals("L")) {
                SafeGUI.safe = main.removeLaser(SafeGUI.safe, row, col);
                SafeGUI.safe = main.addBeams(SafeGUI.safe);
            }
        }
        else {
            int currentIdx = options.indexOf(SafeGUI.safe[row][col]);
            if (currentIdx==options.length()-1){
                currentIdx = -1;
            }
            currentIdx++;
            SafeGUI.safe[row][col] = options.substring(currentIdx,currentIdx+1);
        }

        return SafeGUI.safe;
    }
}