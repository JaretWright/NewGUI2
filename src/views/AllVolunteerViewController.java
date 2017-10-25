package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Person;

/**
 * FXML Controller class
 *
 * @author JWright
 */
public class AllVolunteerViewController implements Initializable, ControllerClass {
   
    @FXML private TableView<Person> volunteerTable;
    @FXML private TableColumn<Person, Integer> volunteerNumColumn;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableColumn<Person, LocalDate> birthdayColumn;
    
    @FXML private Button editButton;
    @FXML private Button logHoursButton;
    
    private SceneChanger sceneChanger;
    
    /**
     * This method will change the scene to create a new Person object
     */
    public void createEmployeeButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "NewUserView.fxml", "Create new volunteer");
    }
    
   
    /**
     * This method will change scenes to the PersonView and pass in the details of 
     * the person selected in the table
     */
    public void editButtonPushed(ActionEvent event) throws IOException, InstantiationException, IllegalAccessException
    {
        Person personSelected = volunteerTable.getSelectionModel().getSelectedItem(); 
      
        PersonViewController pvc = new PersonViewController();
        sceneChanger.changeScene(event, "PersonView.fxml", "Edit person", personSelected, pvc);
    }
    
    
    /**
     * This method will change scenes to the LogHoursView scene 
     */
    public void logHoursButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LogHoursView.fxml", "Log Hours");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        sceneChanger = new SceneChanger();
        
        editButton.setDisable(true);
        logHoursButton.setDisable(true);
        
         //Configure the table columns
        volunteerNumColumn.setCellValueFactory(new PropertyValueFactory<Person, Integer>("personID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthdate"));
        
        
        try {
            //load all the volunteers from the database
            loadVolunteers();
        } catch (SQLException ex) {
            Logger.getLogger(AllVolunteerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadVolunteers() throws SQLException
    {
        ObservableList<Person> volunteers  = FXCollections.observableArrayList();
        
        Connection myConn = null;
        Statement myStatement = null;
        ResultSet resultSet = null;

        String userName = "student";
        String password = "student";

        try{
            // 1. connect to the DB
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcDemo", userName, password);

            // 2. create a statement object 
            myStatement = myConn.createStatement();

            // 3. execute the query using the statement object
            resultSet = myStatement.executeQuery("SELECT * FROM people");

                       
            // 4. Create people objects and add to the TableView
            while (resultSet.next())
            {
                Person newPerson = new Person(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),resultSet.getDate(4).toLocalDate());
                                                
                newPerson.setImageFile(new File("./src/images/"+resultSet.getString(5)));
                volunteers.add(newPerson);
            }
            
            volunteerTable.getItems().addAll(volunteers);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
                resultSet.close();
            if (myStatement != null)
                myStatement.close();
            if (myConn != null)
                myConn.close();
        }
        
    }
    
    /**
     * This will enable the edit button once a volunteer is selected in the table
     */
    public void volunteerSelected()
    {
        editButton.setDisable(false);
        logHoursButton.setDisable(false);
    }

    @Override
    public void preloadData(Person person) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preloadData(int personID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
