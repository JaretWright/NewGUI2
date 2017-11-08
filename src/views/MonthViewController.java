package views;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author jaret_000
 */
public class MonthViewController implements Initializable {
    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private CategoryAxis monthAxis;

    @FXML
    private NumberAxis hoursAxis;
    
    private XYChart.Series currentYearSeries;
    private XYChart.Series lastYearSeries;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentYearSeries = new XYChart.Series<>();
        lastYearSeries = new XYChart.Series<>();
        
        initializeSeries(lastYearSeries);
        initializeSeries(currentYearSeries);

        //barChart.setTitle("Hours Worked");
        //monthAxis.setLabel("Months");
        hoursAxis.setLabel("Hours");
        currentYearSeries.setName("2017");
        lastYearSeries.setName("2016");
        
        try {
            populateChartFromDB();
        } catch (SQLException ex) {
            Logger.getLogger(MonthViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        displaySeriesContents(lastYearSeries);
        
        
        barChart.getData().addAll(lastYearSeries);
        barChart.getData().addAll(currentYearSeries);
    }    
    
    public void displaySeriesContents(XYChart.Series series)
    {
        for (Object data : series.getData())
        {
            System.out.println(data);
        }
    }
    
    public void initializeSeries(XYChart.Series series)
    {
       series.getData().add(new XYChart.Data("January", 0));
       series.getData().add(new XYChart.Data("February", 0));
       series.getData().add(new XYChart.Data("March", 0));
       series.getData().add(new XYChart.Data("April", 0));
       series.getData().add(new XYChart.Data("May", 0));
       series.getData().add(new XYChart.Data("June", 0));
       series.getData().add(new XYChart.Data("July", 0));
       series.getData().add(new XYChart.Data("August", 0));
       series.getData().add(new XYChart.Data("September", 0));
       series.getData().add(new XYChart.Data("October", 0));
       series.getData().add(new XYChart.Data("November", 0));
       series.getData().add(new XYChart.Data("December", 0));
    }
    
    
    public void populateChartFromDB() throws SQLException
    {
        //get the results from the database
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
       
        try{
            // 1. connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcDemo", "student", "student");

            //2.  create the statement
            statement = conn.createStatement();
            
            //3.  create a string with sql statement
            String sql = "SELECT YEAR(dayWorked), MONTHNAME(dayWorked), SUM(hoursworked) "+
                         "FROM hoursWorked "+
                         "GROUP BY YEAR(dayWorked), MONTH(dayWorked) "+
                         "ORDER BY MONTH(dayWorked); ";
            
            //4. execute the query
            resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                if (resultSet.getInt(1) == LocalDate.now().getYear())
                    currentYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
                else
                    lastYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
            }      
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
	
    
}
