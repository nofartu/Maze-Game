package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    void generateMaze(int width, int height);
    void solverMaze();
    void moveCharacter(KeyCode movement);
    Maze getMaze();
    Solution getSolution();
    boolean hasGameEnded();
    boolean isBorder(Position toGo);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void load(File file);
    void save(File file);
    void stopServers();
    String getPropertyGenerate();
    String getPropertySolve();
}
