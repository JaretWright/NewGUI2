/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    private XYChart.Series series;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         series = new XYChart.Series<>();
         
         //barChart.setTitle("Hours Worked");
         //monthAxis.setLabel("Months");
         hoursAxis.setLabel("Hours");
         series.setName("Hours");
         
        try {
            populateChartFromDB();
        } catch (SQLException ex) {
            Logger.getLogger(MonthViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        barChart.getData().addAll(series);
         
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
            String sql = "SELECT MONTHNAME(dayWorked), SUM(hoursworked) "+
                         "FROM hoursWorked "+
                         "GROUP BY MONTH(dayWorked) "+
                         "ORDER BY MONTH(dayWorked); ";
            
            //4. execute the query
            resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                series.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
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
