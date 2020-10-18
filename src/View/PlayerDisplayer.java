package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerDisplayer extends Canvas {
    private Maze maze;
    private int characterPositionRow ;
    private int characterPositionColumn;


    //    public void setSolution(Maze maze,ArrayList<Position> sol) {
//        this.maze = maze;
//        characterPositionColumn=maze.getStartPosition().getColumnIndex();
//        characterPositionRow=maze.getStartPosition().getRowIndex();
//        redraw(sol);
//    }
//    public void setSolution(Maze maze, Solution sol) {
//        this.maze=maze;
//        //disagreement
//        redrawSol();
//    }
//
    public void setCharacterPosition(Maze maze, int row, int column) {
        this.maze=maze;
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    //public  void changeChar()

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getNumRow();
            double cellWidth = canvasWidth / maze.getNumCol();

            try {
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                //Draw Character
                //gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(characterImage, characterPositionColumn *cellWidth, characterPositionRow *cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }


    //region Properties
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }


    //endregion

}
