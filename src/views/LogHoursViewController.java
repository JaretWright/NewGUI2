package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import model.Person;
import model.SHAPasswordGenerator;

/**
 * FXML Controller class
 *
 * @author JWright
 */
public class LogHoursViewController implements Initializable, ControllerClass {

    private Person person;
  
    @FXML private Label personIdLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private DatePicker datePicker;
    @FXML private Spinner hoursSpinner;
    
    public void logoutButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LoginView.fxml", "Login");
    } 
    
    
    public void preloadData(Person personSelected)
    {
        person = personSelected;
        personIdLabel.setText("Volunteer ID: " + person.getPersonID());
        firstNameLabel.setText("First name: " + person.getFirstName());
        lastNameLabel.setText("Last name: "+ person.getLastName());
        datePicker.setValue(LocalDate.now());
    }
    
    
    @Override
    public void preloadData(int personID) 
    {
        //query the database with the volunteer #, retrieve the
        //encrypted password and the salt
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
       
        String userName = "student";
        String password = "student";

        try{
            // 1. connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcDemo", userName, password);

           //2.  create a string that holds query with ? for user inputs
            String sql = "SELECT * FROM people WHERE personID = ?";
            
            //3. prepare the query
            statement = conn.prepareStatement(sql);
            
            //4.  bind values to the parameters
           statement.setInt(1, personID);

            // 5. execute the query
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Person newPerson = new Person(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),resultSet.getDate(4).toLocalDate());                             
                newPerson.setImageFile(new File("./src/images/"+resultSet.getString(5))); 
                preloadData(newPerson);
            }      
           
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
            }
            catch (Exception e)
            {
                System.err.print(e.getMessage());
            }
        }
    }
    
    public void viewHoursButtonPushed(ActionEvent event) throws IOException, InstantiationException, IllegalAccessException
    {
        SceneChanger sc = new SceneChanger();
        ViewHoursWorkedController hoursController = new ViewHoursWorkedController();
        sc.changeScene(event, "ViewHoursWorked.fxml", "Hours worked", person, hoursController);
    }
    
    /**
     * If the save button is pushed, the hours should be logged in the database
     * @param event 
     */
    public void saveButtonPushed(ActionEvent event) throws SQLException, IOException
    {
        person.logHours( datePicker.getValue(), (Integer) hoursSpinner.getValue());
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configure the hoursSpinner object to only accept values 0-24, with a default value of 8
        SpinnerValueFactory<Integer> hoursFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24,8);
        hoursSpinner.setValueFactory(hoursFactory);
        
    }    
}
