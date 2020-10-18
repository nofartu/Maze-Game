package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolutionDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow ;
    private int characterPositionColumn;


    public void setSolution(Maze maze,ArrayList<Position> sol) {
        this.maze = maze;
        characterPositionColumn=maze.getStartPosition().getColumnIndex();
        characterPositionRow=maze.getStartPosition().getRowIndex();
        redraw(sol);
    }
//    public void setSolution(Maze maze, Solution sol) {
//        this.maze=maze;
//        //disagreement
//        redrawSol();
//    }
//
//    public void setCharacterPosition(int row, int column) {
//        characterPositionRow = row;
//        characterPositionColumn = column;
//        redraw();
//    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw(ArrayList<Position> solPath) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getNumRow();
            double cellWidth = canvasWidth / maze.getNumCol();

            try {
                Image pathImage = new Image(new FileInputStream(ImageFileNamePath.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Solution
                if(solPath==null){
                    gc.clearRect(0, 0, getWidth(), getHeight());
                }
                else {
                    for (Position p:solPath) {
                        gc.drawImage(pathImage, p.getColumnIndex() * cellWidth, p.getRowIndex() * cellHeight, cellWidth, cellHeight);
                    }
                }


                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }


    //region Properties
    private StringProperty ImageFileNamePath = new SimpleStringProperty();


    public String getImageFileNamePath() {
        return ImageFileNamePath.get();
    }

    public void setImageFileNamePath(String imageFileNamePath) {
        this.ImageFileNamePath.set(imageFileNamePath);
    }



    //endregion

}
