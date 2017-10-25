
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;
import model.SHAPasswordGenerator;

/**
 * FXML Controller class
 *
 * @author JWright
 */
public class LoginViewController implements Initializable {
    @FXML private TextField userNameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMsg;
    private int volunteerNum;
    private SceneChanger sceneChanger;
    
    public void submitButtonPushed(ActionEvent event) throws SQLException
    {
        //query the database with the volunteer #, retrieve the
        //encrypted password and the salt
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String dbPassword = null;
        byte[] dbSalt = null;
        boolean administrator = false;
        
        String userName = "student";
        String password = "student";

        volunteerNum = Integer.parseInt(userNameTextField.getText());
                
        try{
            // 1. connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcDemo", userName, password);

           //2.  create a string that holds query with ? for user inputs
            String sql = "SELECT pw, salt, administrator FROM people WHERE personID = ?";
            
            //3. prepare the query
            statement = conn.prepareStatement(sql);

            
            //4.  bind values to the parameters
           statement.setInt(1, Integer.parseInt(userNameTextField.getText()));

            
            // 5. execute the query
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                dbPassword = resultSet.getString(1);
                
                Blob blob = resultSet.getBlob(2);
                int blobLength = (int) blob.length();
                
                dbSalt = resultSet.getBlob(2).getBytes(1, blobLength);
                administrator = resultSet.getBoolean(3);
            }
            
            //create encrypted password
            String userPw = SHAPasswordGenerator.get_SHA_512_SecurePassword(this.passwordField.getText(), dbSalt);

            
            if (userPw.equals(dbPassword) && administrator)
                changeToAllVolunteerScene(event);
            else if (userPw.equals(dbPassword))
                changeToLogHoursView(event);
            else
                errorMsg.setText("Volunteer ID or password were incorrect.");
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
    
    public void changeToAllVolunteerScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AllVolunteerView.fxml"));
        Parent parent = loader.load();
        
        Scene tableViewScene = new Scene(parent);
          
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Volunteers");
        window.setScene(tableViewScene);
        window.show();
    }
    
    
    public void changeToLogHoursView(ActionEvent event) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LogHoursView.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        LogHoursViewController controller = loader.getController();
        
        //remove selected Person from the list prior to sending over to the detailed view scene.
         
        controller.preloadData(volunteerNum);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Log Hours");
        window.setScene(tableViewScene);
        window.show();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsg.setText("");
        sceneChanger = new SceneChanger();
    }    
    
}
