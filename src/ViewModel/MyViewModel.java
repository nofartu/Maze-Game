package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer{
    private IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    public StringProperty characterPositionRow; //For Binding
    public StringProperty characterPositionColumn; //For Binding
    public StringProperty generating;
    public StringProperty solving;


    public MyViewModel(IModel model){
        this.model = model;
        characterPositionRow = new SimpleStringProperty();
        characterPositionColumn = new SimpleStringProperty();
        generating=new SimpleStringProperty();
        solving=new SimpleStringProperty();
        generating.setValue(model.getPropertyGenerate());
        solving.setValue(model.getPropertySolve());
    }

    @Override
    public void update(Observable o, Object arg) {
        int numNotify=0;
        if(arg!=null)
            numNotify=(int)arg;
       if (o==model&&(numNotify==0)){
            characterPositionRowIndex = model.getCharacterPositionRow();
            //characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            //characterPositionColumn.set(characterPositionColumnIndex + "");
            setChanged();
            notifyObservers();
        }
        if(o==model&&numNotify==1) {
            setChanged();
            notifyObservers(1);
        }
        if(o==model&&numNotify==2) {
            setChanged();
            notifyObservers(2);
        }
    }

    public ArrayList<Position> getThePath(){
        Solution sol=model.getSolution();
        ArrayList<Position> solArr=new ArrayList<Position>();
        ArrayList<AState> arr=sol.getSolutionPath();
        for (AState astate: arr) {
            MazeState mazestate=(MazeState)astate;
            solArr.add(mazestate.getPosition());
        }

        Collections.reverse(solArr);
        solArr.remove(0);
        return solArr;

    }
    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void solverMaze(){
        model.solverMaze();
    }
    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
//        if(hasGameEnded()){ //need to update that game ended. what next?
//            setChanged();
//            notifyObservers();
//        }
    }



    public Maze getMaze() {
        return model.getMaze();
    }
    public Solution getSolution(){
        return model.getSolution();
    }

    public boolean isBorder(Position toGo){
        return model.isBorder(toGo);
    }
    public boolean hasGameEnded(){
        return model.hasGameEnded();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public StringProperty getGenerating() {
        return generating;
    }
    public StringProperty getSolving() {
        return solving;
    }
    public void save(File file){
        model.save(file);
    }
    public void load(File file){
        model.load(file);
    }
    public void stopProgram(){
        model.stopServers();
    }

}
