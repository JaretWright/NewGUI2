
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import model.Person;

/**
 * FXML Controller class
 *
 * @author jaret_000
 */
public class NewUserViewController implements Initializable {
    
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private PasswordField pwField1;
    @FXML private PasswordField pwField2;
    @FXML private DatePicker datePicker;
    @FXML private ImageView image;
    @FXML private Label errMsg;
    
    private File imageFile;
    private boolean imageChanged;
    private SceneChanger sceneChanger;
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        sceneChanger.changeScene(event, "AllVolunteerView.fxml", "All Volunteers");
    }
            
          
    /**
     * This method will launch a FileChooser and load the image if accessible
     */
    public void chooseImageButtonPushed(ActionEvent event)
    {
        //get the stage to open a new window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for only .jpg and .png files
        FileChooser.ExtensionFilter jpgFilter 
                = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter 
                = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        
        //Set to the user's picture directory or C drive if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannnot navigate to the pictures directory, go to the user home
        if (!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        imageFile = fileChooser.showOpenDialog(stage);
        
        //ensure the user selected a file
        if (imageFile.isFile())
        {
            try
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image img = SwingFXUtils.toFXImage(bufferedImage,null);
                image.setImage(img);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
            imageChanged = true;
        }
    }
 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sceneChanger = new SceneChanger();
        errMsg.setText("");
        datePicker.setValue(LocalDate.now().minusYears(20));
        try
        {
            imageFile = new File("./src/images/defaultPerson.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            image.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }    
    
    
    /**
     * This method will create a Person object and store it in the database
     */
    public void saveButtonPushed(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException
    {
        
        Person person;
        try{
            if (passwordValid()){
                //check if it is a new Person with a custom image
                if (imageChanged)
                {
                    
                    person = new Person(firstNameTextField.getText(),
                                        lastNameTextField.getText(),
                                        datePicker.getValue(),
                                        pwField1.getText(),
                                        imageFile
                                        );
                    person.insertIntoDB();
                }
                else    //use the default image (this way it will not duplicate the default image in the file system)
                {
                    person = new Person(firstNameTextField.getText(),
                                        lastNameTextField.getText(),
                                        datePicker.getValue(),
                                        pwField1.getText());
                    person.insertIntoDB();
                } 
            }
        }catch (IllegalArgumentException e)
        {
            System.err.println(e.getMessage());
        }  
       
        
        sceneChanger.changeScene(event, "AllVolunteerView.fxml", "All Volunteers");
    }
    
    
     /**
     * This method will update the users password and return to the edit user view
     * @param event 
     */
    public boolean passwordValid()
    {
        //check if password meets the requirements of Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
        if (!(pwField1.getText().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")))
        {
            this.errMsg.setText(String.format("Password must have a minimum of 8 characters, at least one uppercase letter, %none lowercase letter, one number and one special character"));
            return false;
        }
                        
        //check that the passwords match
        else if (!(pwField1.getText().equals(pwField2.getText())))
        {
            errMsg.setText("The passwords do not match.");
            return false;
        }
        
        return true;
    }
    
}
