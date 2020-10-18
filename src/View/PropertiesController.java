package View;


import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class PropertiesController {

    MyViewModel viewModel;
    @FXML
    TextField generate;
    @FXML
    TextField solve;

    @FXML
    javafx.scene.control.Label gen;
    @FXML
    javafx.scene.control.Label sol;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        generate=new TextField();
        solve=new TextField();

        generate.textProperty().bind(viewModel.generating);
        solve.textProperty().bind(viewModel.solving);
        if(generate.getText().equals("My"))
            gen.setText("My MazeGenerator");
        else if(generate.getText().equals("Simple"))
            gen.setText("Simple MazeGenerator");
        else
            gen.setText(generate.getText());

        if(solve.getText().equals("BFS"))
            sol.setText("Breadth first search");
        else if(solve.getText().equals("BestFS"))
            sol.setText("best first search");
        else if(solve.getText().equals("DFS"))
            sol.setText("Depth first search");
        else
            sol.setText(solve.getText());

    }

}
