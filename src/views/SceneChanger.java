
package views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Person;
import views.LogHoursViewController;

/**
 *
 * @author jaret_000
 */
public class SceneChanger {

    public void changeScene(ActionEvent event, String viewName, String title) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle(title);
        window.setScene(scene);
        window.show();
    }
    
    
  
    //<CC extends ControllerClass.  This way, you can pass in any sub class of the ControllerClass
    public void changeScene(ActionEvent event, String viewName, String title, Person person, ControllerClass controllerClass) throws IOException, InstantiationException, IllegalAccessException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene tableViewScene = new Scene(parent);
          
        //access the controller and call a method
        controllerClass = loader.getController();
        
        //remove selected Person from the list prior to sending over to the detailed view scene.
        controllerClass.preloadData(person);
      
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Edit user");
        window.setScene(tableViewScene);
        window.show();
    }
    
    
    public void changeScene(ActionEvent event, String viewName, String title, int personID, ControllerClass controllerClass) throws IOException, InstantiationException, IllegalAccessException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene tableViewScene = new Scene(parent);
        
        //access the controller and call a method
        controllerClass = loader.getController();
        
        System.out.printf("The controller class is : %s%n", controllerClass.getClass());
        //remove selected Person from the list prior to sending over to the detailed view scene.
        //controllerClass..preloadData(personID);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Edit user");
        window.setScene(tableViewScene);
        window.show();
    }

}
