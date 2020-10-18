package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;



public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow ;
    private int characterPositionColumn;


    public void setMaze(Maze maze) {
        this.maze = maze;
        characterPositionColumn=maze.getStartPosition().getColumnIndex();
        characterPositionRow=maze.getStartPosition().getRowIndex();
        redraw();
    }
//    public void setSolution(Maze maze, Solution sol) {
//        this.maze=maze;
//        //disagreement
//        redrawSol();
//    }



//    public void setCharacterPosition(int row, int column) {
//        characterPositionRow = row;
//        characterPositionColumn = column;
//        redraw();
//    }
//
//    public int getCharacterPositionRow() {
//        return characterPositionRow;
//    }
//
//    public int getCharacterPositionColumn() {
//        return characterPositionColumn;
//    }

    private boolean hasGoodWalls(int loc){
        if(loc==1){
            for(int i=0;i<maze.getNumCol();i++)
                if(maze.getState(new Position(0,i))!=1)
                    return false;
        }
        else if(loc==2){
            for(int i=0;i<maze.getNumRow();i++)
                if(maze.getState(new Position(maze.getNumCol()-1,i))!=1)
                    return false;
        }
        else if(loc==3){
            for(int i=0;i<maze.getNumCol();i++)
                if(maze.getState(new Position(maze.getNumRow()-1,i))!=1)
                    return false;
        }
        else if(loc==4){
            for(int i=0;i<maze.getNumRow();i++)
                if(maze.getState(new Position(0,i))!=1)
                    return false;
        }

        return  true;
    }


    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getNumRow();
            double cellWidth = canvasWidth / maze.getNumCol();

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                // Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image endImage=new Image(new FileInputStream(ImageFileNameEnd.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
//
//                if(!hasGoodWalls(1)){
//                    for(int i=0;i<maze.getNumCol();i++) {
//                        gc.drawImage(wallImage, 0 * cellWidth, i * cellHeight, cellHeight, cellWidth);
//
//                    }
//                }
//                if(!hasGoodWalls(2)){
//                    for(int i=0;i<maze.getNumRow();i++)
//                        gc.drawImage(wallImage, i * cellWidth, (maze.getNumCol()-1) * cellHeight, cellHeight, cellWidth);
//
//                }
//                if(!hasGoodWalls(3)){
//                    for(int i=0;i<maze.getNumCol();i++)
//                        gc.drawImage(wallImage, (maze.getNumRow()-1) * cellWidth, i * cellHeight, cellHeight, cellWidth);
//
//                }
//                if(!hasGoodWalls(4)){
//                    for(int i=0;i<maze.getNumRow();i++)
//                        gc.drawImage(wallImage, i * cellWidth, 0 * cellHeight, cellHeight, cellWidth);
//
//                }


//                for(int k=0; k<maze.getNumCol();k++)
//                    gc.drawImage(wallImage, k * cellWidth, 0 * cellHeight, cellWidth, cellHeight);

                for (int i = 0; i < maze.getNumRow(); i++) {

                    for (int j = 0; j < maze.getNumCol(); j++) {
                        Position go=new Position(i,j);
                        if (maze.getState(go) == 1 && !go.equals(maze.getGoalPosition())) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        if(go.equals(maze.getGoalPosition())){
                            gc.drawImage(endImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }

                    }
                }
//                for(int k=0; k<maze.getNumCol();k++)
//                    gc.drawImage(wallImage, k * cellWidth, maze.getNumRow() * cellHeight, cellWidth, cellHeight);

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                // gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }
    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    //private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameEnd = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileNameEnd() {
        return ImageFileNameEnd.get();
    }
//    public String getImageFileNameCharacter() {
//        return ImageFileNameCharacter.get();
//    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }
    public void setImageFileNameEnd(String imageFileNameEnd) {
        this.ImageFileNameEnd.set(imageFileNameEnd);
    }
//    public void setImageFileNameCharacter(String imageFileNameCharacter) {
//        this.ImageFileNameCharacter.set(imageFileNameCharacter);
//    }


    //endregion

}
