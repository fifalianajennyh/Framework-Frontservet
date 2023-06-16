/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2090.framework.model;

import etu2090.framework.annotation.Url;
import etu2090.framework.ModelViews.ModelView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Dept{
    String nom;
    String prenom;
    int age;
    Time time;
    Date date;
    double taille;


    public Date getdate() {
        return date;
    }
    public double gettaille() {
        return taille;
    }
    public Time gettime() {
        return time;
    }
    public void setdate(Date date) {
        this.date = date;
    }
    public void settaille(double taille) {
        this.taille = taille;
    }
    public void settime(Time time) {
        this.time = time;
    }
    public String getnom() {
        return nom;
    }
    public void setnom(String nom) {
        this.nom = nom;
    }
    public String getprenom() {
        return prenom;
    }
    public void setprenom(String prenom) {
        this.prenom = prenom;
    }
    public int getage() {
        return age;
    }
    public void setage(int age) {
        this.age = age;
    }

    public Dept()
    {

    }

    public Dept(String nom,String prenom,int age,Date date,Time time,double taille)
    {
        this.setnom(nom);
        this.setprenom(prenom);
        this.setage(age);
        this.setdate(date);
        this.settime(time);
        this.settaille(taille);
    }

    @Url("getAllDept")
    public ModelView myMethodDept() {
        String s1=".jsp";
        String s="test";
        String vString=s+s1;
       ModelView m=new ModelView(vString);
       m.add("test1", "I love you jesus");
       //m.add("test2", new String[] { "Tableau Mety"});
        return m;
    }

    @Url("Deptsave")
    public ModelView myMethodDeptsave() {
        String s1=".jsp";
        String s="Liste";
        String vString=s+s1;
       ModelView m=new ModelView(vString);
       ArrayList<Dept> olona=new ArrayList<Dept>();
       Dept user=new Dept(this.getnom(),this.getprenom(),this.getage(),this.getdate(),this.gettime(),this.gettaille());
       olona.add(user);
       m.add("Liste_personne",olona);
        return m;
    }
}