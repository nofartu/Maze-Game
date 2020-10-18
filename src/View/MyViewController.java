package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;



public class MyViewController implements Observer,IView {
    @FXML
    //public MenuItem Exit = new MenuItem();
    //Stage primaryStage;
    private boolean isSolDisp=false;
    private ArrayList<Position> pos;
    private MyViewModel viewModel;
    private int count=0;
    private MediaPlayer mediaPlayer;
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solutionDisplayer;
    public PlayerDisplayer playerDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);
        btn_solveMaze.setDisable(true);
        Media sound=new Media(new File("./resources/music/allgame.mp3").toURI().toString());
        mediaPlayer=new MediaPlayer(sound);
        txtfld_rowsNum.setOnAction((ActionEvent event)->{
            txtfld_rowsNum.requestFocus();
        });
         //btn_studentit.setSelected(true);
//        btn_student.setSelected(false);
       // btn_studentit.setSelected(true);
    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
    }

    @Override
    public void update(Observable o, Object arg) {
        int numNotify=0;
        if(arg!=null)
            numNotify=(int)arg;
        if(o == viewModel&&numNotify==1) {
            gameEnded();
        }
        if (o == viewModel&&(numNotify==0)) {
            displayMaze(viewModel.getMaze());
            btn_generateMaze.setDisable(false);
            btn_solveMaze.setDisable(false);
            txtfld_rowsNum.setDisable(false);
            txtfld_columnsNum.setDisable(false);
        }
        if (o == viewModel&&(numNotify==2)) {
            dispalySolution(viewModel.getMaze());
            btn_solveMaze.setDisable(true);

        }
//        txtfld_rowsNum.s.setOnAction((ActionEvent event)->{
           // txtfld_rowsNum.requestFocus();
//        });
    }

    private void gameEnded() {
        //showAlert("You are rich now!");
        mediaPlayer.stop();
        Media sound=new Media(new File("./resources/music/end.mp3").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(sound);
        mediaPlayer.play();
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game ended");
        alert.setHeaderText("Good job!!");
        alert.setContentText("You are rich now!");
        alert.show();

//        Optional<ButtonType> answer=alert.showAndWait();
//        if(answer.get()==ButtonType.OK){
//            //generateMaze();
//            viewModel.generateMaze(10,10);
//        }else if(answer.get()==ButtonType.CANCEL){
//
//        }

    }

    //@Override
    public void displayMaze(Maze maze) {
        if(isSolDisp){
            isSolDisp=false;
            solutionDisplayer.setSolution(maze,null);
        }
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        playerDisplayer.setCharacterPosition(maze,characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }

    private void dispalySolution(Maze maze){
        pos=viewModel.getThePath();
        solutionDisplayer.setSolution(maze,pos);
        isSolDisp=true;

    }

    public void generateMaze() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Generating...");
        alert.setHeaderText("Generating just for you!");
        alert.setContentText("Press ok to continue");
        alert.showAndWait();
        if(count==0){
//            txtfld_rowsNum.setDisable(true);
//            txtfld_columnsNum.setDisable(true);
            count++;
        }
        try {
            int height = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());
            btn_generateMaze.setDisable(true);
//        txtfld_rowsNum.setDisable(true);
//        txtfld_columnsNum.setDisable(true);
            viewModel.generateMaze(width, height);
            startTune();
        }catch (Exception e){
            showAlert("Attention!","Wrong input of sizes","Only complete numbers allowed. \n Don't use letter or ; , . / ect.. \n Can't be empty." );
            btn_generateMaze.setDisable(false);
//            txtfld_rowsNum.setDisable(false);
//            txtfld_columnsNum.setDisable(false);

        }
    }
    public void solve(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SOLVING");
        alert.setHeaderText("The way to the monay is by studing");
        alert.setContentText("Press ok to continue");
        alert.showAndWait();

       // showAlert("SOLVING!","solving maze","press ok to continue" );
        btn_solveMaze.setDisable(true);
        viewModel.solverMaze();
    }

    private void showAlert(String title, String header, String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        try {
            viewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        }
        catch (Exception e){

        }
    }
    public void Save(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Saving");
        fileChooser.setInitialDirectory(new File("./resources"));
        File file=fileChooser.showSaveDialog(null);
        if(file!=null){
            file=new File(file.getAbsoluteFile()+".maze");
        }
        viewModel.save(file);
    }
    public void Load(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Loading");
        fileChooser.setInitialDirectory(new File("./resources"));
        FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("maze files(*.maze)","*.maze");
        fileChooser.getExtensionFilters().add(filter);
        File file=fileChooser.showOpenDialog(null);
        if(file!=null)
            viewModel.load(file);
    }

    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

//    public void setResizeEvent(Scene scene) {
//        long width = 0;
//        long height = 0;
//        //this.primaryStage=scene;
//        scene.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
//            }
//            //mazeDisplayer.redraw();
//        });
//        scene.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
//            }
//        });
//        //mazeDisplayer.getHeight();
//        mazeDisplayer.redraw();
//        playerDisplayer.redraw();
//        //solutionDisplayer.redraw();
//    }

    public void setResizeEvent(Stage stage) {
        long width = 0;
        long height = 0;
        //this.primaryStage=scene;
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                //System.out.println("Width: " + newSceneWidth);
                mazeDisplayer.redraw();
                playerDisplayer.redraw();
                if(isSolDisp)
                    solutionDisplayer.redraw(pos);
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                //System.out.println("Height: " + newSceneHeight);
                mazeDisplayer.redraw();
                playerDisplayer.redraw();
                if(isSolDisp)
                    solutionDisplayer.redraw(pos);
            }
        });
    }

    private void startTune(){
        mediaPlayer.setCycleCount(3);
        mediaPlayer.play();
    }

//need to improve this!
//    public void setSceneEvents(Scene scene) {
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
//        {
//            @Override
//            public void handle(KeyEvent ke)
//            {
//                if (!ke.getCode().equals(KeyCode.CONTROL))
//                {
//                    return;
//                }
//            }
//        });
//        //handles mouse scrolling
//        scene.setOnScroll(new EventHandler<ScrollEvent>() {
//                    @Override
//                    public void handle(ScrollEvent event) {
//                        double zoomFactor = 1.05;
//                        double deltaY = event.getDeltaY();
//                        if (deltaY < 0){
//                            zoomFactor = 2.0 - zoomFactor;
//                        }
//                        System.out.println(zoomFactor);
//                        mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
//                        mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
//                        playerDisplayer.setScaleX(playerDisplayer.getScaleX() * zoomFactor);
//                        playerDisplayer.setScaleY(playerDisplayer.getScaleY() * zoomFactor);
//                        solutionDisplayer.setScaleX(solutionDisplayer.getScaleX() * zoomFactor);
//                        solutionDisplayer.setScaleY(solutionDisplayer.getScaleY() * zoomFactor);
//                        event.consume();
//                    }
//                });
//
//    }

    @FXML
    public void stopProgram(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
            viewModel.stopProgram();
            //primaryStage.close();
        } else {
        }
//        Exit.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent t) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Exit");
//                alert.setHeaderText("Are you sure you want to exit?");
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.get() == ButtonType.OK){
//                    Platform.exit();
//                    viewModel.stopProgram();
//                    //primaryStage.close();
//                } else {
//                }
//            }
//        });
    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 800, 450);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 800, 700);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    //need to end this
    public void Properties(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 600, 350);
            stage.setScene(scene);
            PropertiesController properties=fxmlLoader.getController();
            properties.setViewModel(viewModel);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            System.out.println("catching");
        }
    }
    //endregion
}
