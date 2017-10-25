/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
 * @author JWright
 */
public class PersonViewController implements Initializable, ControllerClass {

    //hold the Person object that is displayed in the view
    private Person person;
    private File imageFile;
    private boolean imageChanged=false;
    private SceneChanger sceneChanger;
    
    //FXML elements that interact with the Person class
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker datePicker;
    @FXML private ImageView image;
    @FXML private Label errMsg;
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        sceneChanger.changeScene(event, "AllVolunteerView.fxml", "Volunteers");
    }
            
          
    /**
     * This method will allow a user to change their password
     */
    public void changePWButtonPushed(ActionEvent event) throws IOException
    {
        sceneChanger.changeScene(event, "ChangePWView.fxml", "Change Password");
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
     * This method will load a Person object and set all of the fields
     * to reflect the Person
     */
    public void preloadData(Person personSelected) 
    {
        
        try{
            person = personSelected;
            firstNameTextField.setText(person.getFirstName());
            lastNameTextField.setText(person.getLastName());
            datePicker.setValue(person.getBirthdate());    
            BufferedImage bufferedImage = ImageIO.read(person.getImageFile());
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            image.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sceneChanger = new SceneChanger();
        
        errMsg.setText("");
        
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
    public void saveButtonPushed(ActionEvent event) throws SQLException, IOException
    {
        try{           
            person.setFirstName(firstNameTextField.getText());
            person.setLastName(lastNameTextField.getText());
            person.setBirthdate(datePicker.getValue());
            person.updateDB();
                           
            if (imageChanged)
            {
                person.setImageFile(imageFile);
                person.copyImageFile();
                person.updateDB();
            }
        }catch (IllegalArgumentException e)
        {
            System.err.println(e.getMessage());
        }
       
    }

    @Override
    public void preloadData(int personID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
