/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oop2finalproject;

/**
 *
 * @author Shinozaki-Kirishima 
 */
public class OOP2FinalProject implements Runnable{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //create thread 
        Thread firstThread = new Thread(new OOP2FinalProject());
        //start first thread
        firstThread.start();
    }
    @Override
    public void run() {
        //make instances of our model, view and controller
        GPAView mainView=new GPAView();
        GPAModel model=new GPAModel();
        GPAController controller=new GPAController(model, mainView);
        mainView.setVisible(true);//set frame visibilty
        mainView.setLocationRelativeTo(null);//set frame to center
    }//end of run function 
}//end of main class
