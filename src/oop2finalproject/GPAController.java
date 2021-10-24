/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oop2finalproject;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Shinozaki-Kirishima
 */
public class GPAController {
    //get connection to the database
    public Connection getConnection()
    {
        Connection connection = null;
        try {
            connection=DriverManager.getConnection("jdbc:mariadb://localhost:3306/finalproject","root","");
            return connection;
        } catch (SQLException ex) {//if not connected, alret the user 
            JOptionPane.showMessageDialog(null, "Not Connected.", "Not Connected",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return null;
        }
    }
    //function to store user information in an arraylist
    public ArrayList<Object> addToArrayList (String userName, String ID, Connection connect, ArrayList<Object> studentInfo)
    {
           //create object arraylist to store values
            studentInfo=new ArrayList<Object>();
            //create sql query
            String query="SELECT * FROM students WHERE name= '"+userName+"' AND uniqueID='"+ID+"'"; 
            //create statement variable and result set variables
            Statement statement;
            ResultSet resultSet;
            /*try catch statement to execute sql query and store information from database in arraylist*/
             try
             {//initialize statement and resultSet objects 
                 statement=connect.createStatement();
                 resultSet=statement.executeQuery(query);
                 //while resultset.next store user information to arraylist 
                 while(resultSet.next())
                 {//store student info 
                     int id=resultSet.getInt("id");
                     String  studentName=resultSet.getString("name");
                     int uniqueID=resultSet.getInt("uniqueID");
                     String grade=resultSet.getString("Grade");
                     double gpa=resultSet.getDouble("GPA");
                     //add sudent information to arraylist 
                     studentInfo.add(id);
                     studentInfo.add(studentName);
                     studentInfo.add(uniqueID);
                     studentInfo.add(grade);
                     studentInfo.add(gpa);
                 }
             }catch(SQLException evt)
             {//show error message if the student data doesn't exist
                 JOptionPane.showMessageDialog(null, "Incorrect User Name or Password"
                         + "If not in position of an account please register below.", 
                         "Error",JOptionPane.INFORMATION_MESSAGE);
             }
             return studentInfo;
    }
    //make instances of views and model
    private GPAModel model;
    private GPAView view;
    //contructor 
    public GPAController(GPAModel model,GPAView view)
    {
        getConnection();
        this.model=model;
        this.view=view;
        /*Tell the View that when ever the calculate, register and save buttons
        *are clicked to execute the actionPerformed functions
        */
        this.view.addSearchListener(new searchListener());
        this.view.addRegisterListener(new registerListener());
        this.view.addUpdateListener(new updateListener());
    }//action listener for registration button in the registration view 
    class searchListener implements ActionListener
    {/* action listener for search button in the main view
       *create function to search for specific user info in the database 
       *based on user input.
       */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //create object arraylist to store values
            ArrayList<Object> studentInfo=new ArrayList<Object>();
            Connection connection=getConnection();//get connection
            //add information to an arraylist and set to display
            try
            {
            view.setDisplayArea(addToArrayList(view.getEnterName(),view.getEnterID(),connection,studentInfo));
            }catch(Exception ev)
            {
                JOptionPane.showMessageDialog(null, "Incorrect User Name or Password"
                         + "If not in position of an account please register below.", 
                         "Error",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//end of searchListener class
    
    class registerListener implements ActionListener
    {//action listener for register button in the main view
        @Override
        public void actionPerformed(ActionEvent e)
        {
        /*register button closes main view and takes user to the the registration
        page to enter thier information 
        */ 
            view.dispose();
            EventQueue.invokeLater(() -> {//make event queue
                    try{
                         Register register=new Register();
                    }catch(Exception ev){
                        JOptionPane.showMessageDialog(null, "ERROR!");
                    }
                });//create joptionpane that allows users to continue or exit program
            int ok=JOptionPane.showConfirmDialog(null, "Would you like to proceed to register?",
                    "Confirmation",JOptionPane.YES_NO_OPTION);
            if(ok==JOptionPane.YES_OPTION)
            {//if option chosen was yes, 
                Thread newThread=new Thread(){//create thread 
                    @Override//create our runnable function for thread
                    public void run(){
                        //make instances of our register class 
                        Register register=new Register();
                        register.setSize(600, 650);
                        register.setVisible(true);//set our main JFrame visibilty to 'true'
                        register.setLocationRelativeTo(null);
                    }
                };
                newThread.start();//start new thread
            }else if (ok==JOptionPane.NO_OPTION)
            {//if option chosen was no
                Thread secondThread=new Thread(){//create thread 
                    @Override//create our runnable function for thread
                    public void run(){
                        //make instances of our model,view and controller classes
                        GPAView view=new GPAView();
                        GPAModel model=new GPAModel();
                        GPAController controller=new GPAController(model, view);
                        view.setVisible(true);//set our main JFrame visibilty to 'true'
                        view.setLocationRelativeTo(null);
                    }
                };
                secondThread.start();//start new thread
            }
        }
    }//end of registerListener class
    
    class updateListener implements ActionListener
    {//action listener for update button in the main view
        @Override
        public void actionPerformed(ActionEvent e)
        {
             String updateQuery=null;//will use to store our query
             Connection connection=getConnection();//get connection
             ArrayList<Object> studentInfo=new ArrayList<Object>();
             PreparedStatement preparedStatement=null;//create prepared statement object
            //incase update query code in try-catch block 
            try
            {
                //create update query
                updateQuery="UPDATE students SET Grade = ?, GPA = ? WHERE uniqueID = ? AND name = ?;"; 
                //add updateQuery to prepared statement
                preparedStatement=connection.prepareStatement(updateQuery);
                //get function from model to calculate the new gps
                model.calculateNewGPA(view.getCurrentGPA(), view.getAddMark1());
                //get new gpa and grade and add to prepared statement.
                preparedStatement.setString(1, model.getGrade());
                preparedStatement.setDouble(2, model.getNewGPA());
                preparedStatement.setString(3, view.getDatabaseID());
                preparedStatement.setString(4, view.getDatabaseUserName());
                //execute update
                preparedStatement.executeUpdate();
                
                view.setDisplayArea(addToArrayList(view.getDatabaseUserName(),view.getDatabaseID(),connection,studentInfo));
            }catch(SQLException evt)
            {//if error occurs display:
                 JOptionPane.showMessageDialog(null,"Error has occured", "Error has occured",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//end of updateListener class 
}//end of controller class
