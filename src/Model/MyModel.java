package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private Solution solved;
    private Server generetor;
    private Server solver;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public MyModel() {
        //Raise the servers
        generetor=new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solver=new Server(5401,1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        generetor.start();
        solver.start();
    }

    public void stopServers() {
        generetor.stop();
        solver.stop();
        threadPool.shutdown();
    }

    //private AMazeGenerator generetor=new MyMazeGenerator();
//    @Override
//    public void generateMaze(int width, int height) {
//        //Generate maze
//        threadPool.execute(() -> {
//            //generateRandomMaze(width,height);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            setChanged();
//            notifyObservers();
//        });
//    }
//
//    private int[][] generateRandomMaze(int width, int height) {
//        Random rand = new Random();
//        maze = new int[width][height];
//        for (int i = 0; i < maze.length; i++) {
//            for (int j = 0; j < maze[i].length; j++) {
//                maze[i][j] = Math.abs(rand.nextInt() % 2);
//            }
//        }
//        return maze;
//    }

    public void generateMaze(int row, int col) {
        //Generate maze
        threadPool.execute(() -> {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            //solved=null;
                            System.gc();
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};
                            toServer.writeObject(mazeDimensions); //send maze dimensions to server
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            //**********########### understand size!!!!!###########******//
                            byte[] decompressedMaze = new byte[10000000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                            is.read(decompressedMaze); //Fill decompressedMaze with bytes
//                            Maze maze = new Maze(decompressedMaze);
                            Maze mazeTmp = new Maze(decompressedMaze);
                            maze = mazeTmp;
                            characterPositionColumn=maze.getStartPosition().getColumnIndex();
                            characterPositionRow=maze.getStartPosition().getRowIndex();

                            //maze.print();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("modelGene1");
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("modelGene2");
            }
            setChanged();
            notifyObservers();
        });
    }


    public void solverMaze(){
        threadPool.execute(() -> {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
//                            MyMazeGenerator mg = new MyMazeGenerator();
//                            Maze maze = mg.generate(50, 50);
                            //maze.print();
                            maze.setStartPosition(new Position(characterPositionRow,characterPositionColumn));
                            toServer.writeObject(maze); //send maze to server
                            toServer.flush();
                            //Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            solved=(Solution) fromServer.readObject();
                            //Print Maze Solution retrieved from the server
//                            System.out.println(java.lang.String.format("Solution steps: %s", mazeSolution));
                            //ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
//                            for (int i = 0; i < mazeSolutionSteps.size(); i++) {
//                                System.out.println(java.lang.String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("modelsolve1");
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("modelsolve2");
            }
            setChanged();
            notifyObservers(2);
        });
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    public Solution getSolution() {
        return solved;
    }

    public boolean hasGameEnded(){
        if(characterPositionColumn==maze.getGoalPosition().getColumnIndex() && characterPositionRow==maze.getGoalPosition().getRowIndex()) {
            return true;
        }
        return false;
    }

    public boolean isBorder(Position toGo){
        int x=toGo.getRowIndex();
        int y=toGo.getColumnIndex();
        int rows=maze.getNumRow();
        int cols=maze.getNumCol();
        if(x<0||y<0||x>=rows||y>=cols)
            return false;
        if(maze.getState(toGo)==1)
            return false;
        return true;

    }


    @Override
    public void moveCharacter(KeyCode movement) {
        //what to do if the movement is not allowed?
        if(movement==KeyCode.UP || movement==KeyCode.NUMPAD8|| movement==KeyCode.DIGIT8){
            if(isBorder(new Position(characterPositionRow-1,characterPositionColumn)))
                characterPositionRow--;
        }
        else if(movement==KeyCode.DOWN || movement==KeyCode.NUMPAD2 || movement==KeyCode.DIGIT2){
            if(isBorder(new Position(characterPositionRow+1,characterPositionColumn)))
                characterPositionRow++;
        }
        else if(movement==KeyCode.RIGHT || movement==KeyCode.NUMPAD6|| movement==KeyCode.DIGIT6){
            if(isBorder(new Position(characterPositionRow,characterPositionColumn+1)))
                characterPositionColumn++;
        }
        else if(movement==KeyCode.LEFT || movement==KeyCode.NUMPAD4|| movement==KeyCode.DIGIT4){
            if(isBorder(new Position(characterPositionRow,characterPositionColumn-1)))
                characterPositionColumn--;
        }
        else if(movement==KeyCode.NUMPAD9|| movement==KeyCode.DIGIT9){
            if(isBorder(new Position(characterPositionRow-1,characterPositionColumn+1))) {
                characterPositionColumn++;
                characterPositionRow--;
            }
        }
        else if(movement==KeyCode.NUMPAD3|| movement==KeyCode.DIGIT3){
            if(isBorder(new Position(characterPositionRow+1,characterPositionColumn+1))) {
                characterPositionColumn++;
                characterPositionRow++;
            }
        }
        else if(movement==KeyCode.NUMPAD1|| movement==KeyCode.DIGIT1){
            if(isBorder(new Position(characterPositionRow+1,characterPositionColumn-1))) {
                characterPositionColumn--;
                characterPositionRow++;
            }
        }
        else if(movement==KeyCode.NUMPAD7|| movement==KeyCode.DIGIT7){
            if(isBorder(new Position(characterPositionRow-1,characterPositionColumn-1))) {
                characterPositionColumn--;
                characterPositionRow--;
            }
        }
        if(!hasGameEnded()){
            setChanged();
            notifyObservers();
        }
        else{
            setChanged();
            notifyObservers(1);
        }
    }

    public void save(File file){
        if(file!=null)
            System.out.println("ok");
        try{
            ObjectOutputStream outStream=new ObjectOutputStream(new FileOutputStream(file.getAbsoluteFile()));
            outStream.writeObject(maze);
            outStream.writeObject(new Position(characterPositionRow,characterPositionColumn));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void load(File file){
        try{
            ObjectInputStream inStream=new ObjectInputStream (new FileInputStream(file.getAbsoluteFile()));
            maze=(Maze)inStream.readObject();
            Position tmp=(Position)inStream.readObject();
            characterPositionRow=tmp.getRowIndex();
            characterPositionColumn=tmp.getColumnIndex();
            setChanged();
            notifyObservers();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getPropertyGenerate(){
        return Server.Configurations.getProperty("GeneWith");
    }
    public String getPropertySolve(){
        return Server.Configurations.getProperty("solveWith");
    }
    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
}
