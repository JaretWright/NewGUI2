package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.HoursWorked;
import model.Person;

/**
 * FXML Controller class
 *
 * @author jaret_000
 */
public class ViewHoursWorkedController  implements Initializable, ControllerClass {

    private Person person;
    private int hoursWorked;
    
    @FXML private Label personIDLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label hoursWorkedLabel;
    @FXML private TableView hoursTable;
    @FXML private TableColumn<HoursWorked, LocalDate> dateWorkedColumn;
    @FXML private TableColumn<HoursWorked, Integer> hoursWorkedColumn;
   
    
    
    @Override
    public void preloadData(Person personSelected)
    {
        person = personSelected;
        personIDLabel.setText(Integer.toString(person.getPersonID()));
        firstNameLabel.setText(person.getFirstName());
        lastNameLabel.setText(person.getLastName());
        hoursWorked=0;
        try{
            loadHoursInTable();    
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        
    }
    
    public void changeToLogHoursScene(ActionEvent event) throws IOException, InstantiationException, IllegalAccessException
    {
        SceneChanger sc = new SceneChanger();
        LogHoursViewController logHoursController = new LogHoursViewController();
        sc.changeScene(event, "LogHoursView.fxml", "Log hours", person, logHoursController);
    }
    
    /**
     * This method will populate the TableView object with the hours worked
     * by the volunteer
     * @param event
     * @throws IOException 
     */
    public void loadHoursInTable() throws SQLException
    {
        ObservableList<HoursWorked> hours  = FXCollections.observableArrayList();
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
       
        String userName = "student";
        String password = "student";

        try{
            // 1. connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcDemo", userName, password);

           //2.  create a string that holds query with ? for user inputs
            String sql = "SELECT * FROM hoursWorked WHERE personID = ?";
            
            //3. prepare the query
            statement = conn.prepareStatement(sql);
            
            //4.  bind values to the parameters
           statement.setInt(1, person.getPersonID());

            // 5. execute the query
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                HoursWorked hw = new HoursWorked(resultSet.getDate("dayWorked").toLocalDate(), resultSet.getInt("hoursWorked"));
                hoursWorked += resultSet.getInt("hoursWorked");
                hours.add(hw);
            }      
            hoursTable.getItems().addAll(hours);
            hoursWorkedLabel.setText(Integer.toString(hoursWorked));
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
        }

    }
    
    
    public void logoutButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LoginView.fxml", "Login");
    } 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        hoursWorked = 0;
        
        
        //Configure the table columns
        dateWorkedColumn.setCellValueFactory(new PropertyValueFactory<HoursWorked, LocalDate>("dateWorked"));
        hoursWorkedColumn.setCellValueFactory(new PropertyValueFactory<HoursWorked, Integer>("hoursWorked"));
    }    

    @Override
    public void preloadData(int personID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
