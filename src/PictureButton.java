import javafx.scene.control.Button;

import java.util.ArrayList;

public class PictureButton {
    private String name;
    private Button button;
    private int row;
    private int col;

    public PictureButton(String name,int row,int col,String[][] safe,Button[][] visualSafe){
        this.row = row;
        this.col = col;
        this.button = new Button(name);
        this.button.setMinSize(80,80);
        this.button.setMaxSize(80,80);
        this.button.setStyle("-fx-font-size: 36; ");
        this.button.setOnAction(e->{
            changeAtPoint(safe);
            SafeGUI.updateStage(visualSafe,safe);
        });
    }

    public Button getButton(){
        return this.button;
    }
    /*
    If not in editing mode, adds a laser if possible at a place.  If not
    possible, does nothing.  If in editing mode, cycles the current tile through all options.
     */
    public String[][] changeAtPoint(String[][] safe){
        String options = "01234XL.";
        if (!SafeGUI.editing) {
            Main main = new Main();
            if (safe[row][col].equals(".")) {
                boolean place = main.canPlace(safe, row, col);
                if (place) {
                    ArrayList<String> toReplace = new ArrayList<>();
                    toReplace.add(".");
                    toReplace.add("L");
                    toReplace.add("*");
                    safe[row][col] = "L";
                    safe = main.replaceFromPoint(safe, row, col, toReplace, "*");
                }
            } else if (safe[row][col].equals("L")) {
                safe = main.removeLaser(safe, row, col);
                safe = main.addBeams(safe);
            }
        }
        else {
            int currentIdx = options.indexOf(safe[row][col]);
            if (currentIdx==options.length()-1){
                currentIdx = -1;
            }
            currentIdx++;
            safe[row][col] = options.substring(currentIdx,currentIdx+1);
        }

        return safe;
    }
}