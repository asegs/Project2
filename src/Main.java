import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /*
    Creates a two dimensional array from a file formatted specifically.
    Returns filled array.
     */
    public String[][] createSafe(String filename) {
        String input = FileReader.reader(filename, "\n");
        String[] tokens = input.split("\n",0);
        String line1 = tokens[0];
        String[] tokens1 = line1.split(" ",0);
        int height = Integer.parseInt(tokens1[0]);
        int width = Integer.parseInt(tokens1[1]);
        String[][] safe = new String[height][width];
        String[] lines = input.split("\n");
        for (int i = 1; i < height + 1; i++) {
            for (int b = 0; b < width * 2; b += 2) {
                safe[i - 1][b / 2] = lines[i].substring(b, b + 1);
            }
        }
        return safe;


    }

    /*
    Takes a two dimensional array and draws it with numerical indexes.
    Prints to System.out.
     */
    public void drawSafe(String[][] safe) {
        int width = safe[0].length;
        int length = safe.length;
        String safeVisual = "  ";
        for (int i = 0; i < width; i++) {
            safeVisual += i + " ";
        }
        safeVisual += "\n  ";
        for (int i = 0; i < width; i++) {
            safeVisual += "--";
        }
        safeVisual += "\n";
        for (int i = 0; i < length; i++) {
            safeVisual += i + "|";
            for (int b = 0; b < width; b++) {
                safeVisual += safe[i][b];
                safeVisual+=" ";
            }
            safeVisual += "\n";
        }
        System.out.println(safeVisual);
    }

    /*
    Checks if a string is numeric.  Used later.
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    Checks if a pillar which can require 0-4 lasers is
    satisfied, returns different int values signifying
    results.
     */
    public int pillarSatisfied(String[][] safe, int x, int y) {
        String pillar = safe[x][y];
        int required = 65556;
        if (pillar.equals("X") || isNumeric(pillar)) {
            if (isNumeric(pillar) && Integer.parseInt(pillar) <= 4) {
                required = Integer.parseInt(pillar);
                if (x > 0) {
                    if (safe[x - 1][y].equals("L")) {
                        required -= 1;
                    }
                }
                if (x < safe.length-1) {
                    if (safe[x + 1][y].equals("L")) {
                        required -= 1;
                    }
                }
                if (y > 0) {
                    if (safe[x][y - 1].equals("L")) {
                        required -= 1;
                    }
                }
                if (y < safe[0].length-1) {
                    if (safe[x][y + 1].equals("L")) {
                        required -= 1;
                    }
                }
            }
        }
        if (required == 65556){
            return 0;
        }
        if (required<65556&&required>0){
            return 1;
        }
        if (required == 0){
            return 2;
        }
        if (required < 0){
            System.out.println("The pillar at "+x+","+y+" is overloaded!");
            return 3;
        }
        return 3;
    }

    /*
    Checks each index of array to see if a space is a laser.
    Draws beams radially if it is.  Used to clean up after deletion.
    Returns new two dimensional array with * added.
     */
    public String[][] addBeams(String[][] safe){
        int tempInt = 0;
        ArrayList<String> replace = new ArrayList<>();
        replace.add(".");
        replace.add("L");
        replace.add("*");
        for (int i = 0;i<safe.length;i++){
            for (int b = 0;b<safe[0].length;b++){
                if (safe[i][b].equals("L")){
                    replaceFromPoint(safe,i,b,replace,"*");

                }
            }
        }
        return safe;
    }

    /*
    Removes a laser from a certain position on the grid.
    Removes beams as well and returns updated grid.
     */
    public String[][] removeLaser(String[][] safe,int x,int y){
        ArrayList<String> replaceable = new ArrayList<>();
        replaceable.add("*");
        if (safe[x][y].equals("L")){
            replaceFromPoint(safe,x,y,replaceable,".");
            safe[x][y] = ".";
        }
        return safe;
    }
    /*
    From a point, looks vertically and horizontally for
    characters in seek, and replaces them with the replace
    char.  If it hits a pillar or edge, stops in that direction.
    Returns updated grid.
     */
    public String[][] replaceFromPoint(String[][] safe, int row, int col, ArrayList<String> seek, String replace){
        int tempInt = col-1;
        while (tempInt>=0){
            if (seek.contains(safe[row][tempInt])){
                safe[row][tempInt] = replace;
                tempInt--;
            }
            else {
                break;
            }
        }
        tempInt = col+1;
        while (tempInt<safe[0].length){
            if (seek.contains(safe[row][tempInt])){
                safe[row][tempInt] = replace;
                tempInt++;
            }
            else {
                break;
            }
        }
        tempInt = row-1;
        while (tempInt>=0){
            if (seek.contains(safe[tempInt][col])){
                safe[tempInt][col] = replace;
                tempInt--;

            }
            else {
                break;
            }
        }
        tempInt = row+1;
        while (tempInt<safe.length){
            if (seek.contains(safe[tempInt][col])){
                safe[tempInt][col] = replace;
                tempInt++;
            }
            else {
                break;
            }

        }
        return safe;
    }

    /*
    Checks a location to make sure a laser can be placed there.
    Looks for pillars, laser beams, other lasers, or full pillar
    edges.  Returns a boolean, true if place is good.
     */
    public boolean placeChecker(String[][] safe,int x, int y) {
        int tempPlace = -1;
        if (!(safe[x][y].equals(".") || safe[x][y].equals("*"))) {
            tempPlace = (pillarSatisfied(safe, x, y));
            if (tempPlace == 2 || tempPlace == 3) {
                System.out.println("Pillar at " + x + "," + y + " is full!");
                return false;
            }

        }
        return true;
    }
    /*
    Checks if a place is valid, works with placeChecker.
     */
    public boolean canPlace(String[][] safe, int x, int y) {
        if (x >= 0 && y >= 0 && x < safe.length && y < safe[0].length) {
            String letter = safe[x][y];
            if (letter.equals("*")) {
                System.out.println("This laser will be hit by a laser beam!");
                return false;
            }
            if (letter.equals(".")) {
                boolean goodToPlace = true;
                int tempPlace = -1;
                if (x>0){
                    goodToPlace = placeChecker(safe,x-1,y);
                }
                if (x<safe.length-1){
                    if (goodToPlace) {
                        goodToPlace = placeChecker(safe, x + 1, y);
                    }
                }
                if (y>0){
                    if (goodToPlace) {
                        goodToPlace = placeChecker(safe, x, y - 1);
                    }
                }
                if (y<safe[0].length-1){
                    if (goodToPlace) {
                        goodToPlace = placeChecker(safe, x, y + 1);
                    }
                }
                return goodToPlace;
            }
            if (letter.equals("L")){
                System.out.println("There is already a laser block here!");
            }
            else {
                System.out.println("There is already a pillar here!");
            }
            return false;
        }
        System.out.println("This space is not on the board!");
        return false;
    }

    /*
    Checks to make sure no blank spaces are still present.
    Returns true if all spaces guarded.
     */
    public boolean verify(String[][] safe){
        for (String[] line:safe){
            for(String letter:line){
                if (letter.equals(".")){
                    return false;
                }
            }
        }
        return true;
    }

    /*
    Switch handles tokenized input, gives user options,
    runs on while loop.  Calls various functions, check if
    switch did something, tries to tell user if they have syntax wrong.
    Refreshes grid when it is changed.
     */

    public String[][] addLaserAtPoint(String[][] safe,int x,int y){
        boolean place = canPlace(safe,x, y);
        if (place) {
            ArrayList<String> toReplace = new ArrayList<>();
            toReplace.add(".");
            toReplace.add("L");
            toReplace.add("*");
            safe[x][y] = "L";
            safe = replaceFromPoint(safe,x,y,toReplace,"*");
            drawSafe(safe);
        }
        return safe;
    }

    public String[][] actions(String[][] safe,String filename) {
        Scanner scanner = new Scanner(System.in);
        //while (true) {
            System.out.println("Enter your command or type 'help':");
            String choice = scanner.nextLine();
            String[] terms = choice.split(" ", 0);
            boolean switchRan = false;
            switch (terms[0]) {
                case "help":
                    System.out.println("to add laser: 'a row column' (a 2 3)\n" +
                            "to display the safe: 'd'\n" +
                            "to get help: 'h'\n" +
                            "to quit: 'q'\n" +
                            "to remove a laser: 'r row column' (r 3 1)\n" +
                            "to reset the safe: 'reset'\n" +
                            "to verify the safe: 'v'");
                    switchRan = true;
                    break;
                case "d":
                    drawSafe(safe);
                    switchRan = true;
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    switchRan = true;
                    System.exit(0);
                case "a":
                    if (terms.length == 3 && isNumeric(terms[1]) && isNumeric(terms[2])) {
                        int x = Integer.parseInt(terms[1]);
                        int y = Integer.parseInt(terms[2]);
                        boolean place = canPlace(safe,x, y);
                        if (place) {
                            ArrayList<String> toReplace = new ArrayList<>();
                            toReplace.add(".");
                            toReplace.add("L");
                            toReplace.add("*");
                            safe[x][y] = "L";
                            safe = replaceFromPoint(safe,x,y,toReplace,"*");
                            drawSafe(safe);
                        }
                    }
                    switchRan = true;
                    break;
                case "v":
                    boolean done = verify(safe);
                    if (done){
                        System.out.println("Finished!");
                    }
                    else {
                        System.out.println("Spaces are unguarded!");
                    }
                    switchRan = true;
                    break;
                case  "r":
                    if (terms.length == 3 && isNumeric(terms[1]) && isNumeric(terms[2])) {
                        int x = Integer.parseInt(terms[1]);
                        int y = Integer.parseInt(terms[2]);
                        safe = removeLaser(safe, x, y);
                        safe = addBeams(safe);
                        drawSafe(safe);
                    }
                    else {
                        System.out.println("Use valid syntax please");
                    }
                    switchRan = true;
                    break;
                case "reset":
                    safe = createSafe(filename);
                    drawSafe(safe);
                    switchRan = true;
                    break;
            }
            if (!switchRan){
                System.out.println("Invalid command.");
            }

        //}
        return safe;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.actions(main.createSafe(args[0]),args[0]);

    }
}