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
public class GPAModel {
    //fields 
    private double additionalMark1;
    private double newGPA;
    private String Grade;
    private int id;
    private String name; 
    
    //getters 
    public double getAdditionalMark1() {
        return this.additionalMark1;
    }
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    //function to calculate new gpa 
    public void calculateNewGPA(double currentGPA,double additionalMark1)
    {//determine the gpa of the additional course
        //get the average mark of the addtional subjects 
        double additionalGPA;
        if(additionalMark1>=90){
            additionalGPA=4.0;
        }else if(additionalMark1>=85&&additionalMark1<90){
           additionalGPA=3.5;
        }else if(additionalMark1>=80&&additionalMark1<85){
           additionalGPA=3.0;
        }else if(additionalMark1>=75&&additionalMark1<80){
           additionalGPA=2.5;
        } else if(additionalMark1>=70&&additionalMark1<75){
            additionalGPA=2.0;
        }else if(additionalMark1>=65&&additionalMark1<70){
            additionalGPA=1.5;
        }else if(additionalMark1>=60&&additionalMark1<65){
            additionalGPA=1.0;
        }else{
            additionalGPA=0;
        }//determine new gpa
        this.newGPA=(additionalGPA+currentGPA)/2;
        //determine the grade of the newly found gpa 
        String temp = null;
        if(this.newGPA==4){
           temp="A";
        }else if(this.newGPA>=3.5&&this.newGPA<4){
            temp="B+";
        }else if(this.newGPA>=3.0&&this.newGPA<3.5){
           temp="B";
        }else if(this.newGPA>=2.5&&this.newGPA<3.0){
           temp="C+";
        }else if(this.newGPA>=2.0&&this.newGPA<2.5){
            temp="C";
        }else if(this.newGPA>=1.5&&this.newGPA<2.0){
           temp="D+";
        }else if(this.newGPA>=1.0&&this.newGPA<1.5){
            temp="D";
        }else if (this.newGPA<1.0){
            temp="F"; 
        }
        this.Grade=temp;
    }//get new gpa
    public double getNewGPA() {
        return newGPA;
    }//get grade
    public String getGrade() {
        return Grade;
    }
    
}
