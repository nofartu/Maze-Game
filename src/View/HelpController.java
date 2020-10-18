package View;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.image.ImageView ;
import javafx.scene.image.Image ;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    @FXML
    private ImageView imageView,imageView2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("resources/Images/programmer.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        File file1 = new File("resources/Images/keyboard.jpg");
        Image image1 = new Image(file1.toURI().toString());
        imageView2.setImage(image1);
    }
}
