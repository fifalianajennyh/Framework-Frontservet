/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package etu2090.framework.model;
import etu2090.framework.annotation.Url;
import etu2090.framework.annotation.scope;
import etu2090.framework.annotation.Argument;
import etu2090.framework.annotation.Auth;
import etu2090.framework.annotation.Session;
import etu2090.framework.ModelViews.ModelView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

@scope(singleton=true)
public class Dept{
    String nom;
    String prenom;
    int age;
    Time time;
    Date date;
    double taille;
    String lieu;
    double poids;


    public String getlieu() {
        return lieu;
    }
    public void setlieu(String lieu) {
        this.lieu = lieu;
    }
    public double getpoids() {
        return poids;
    }
    public void setpoids(double poids) {
        this.poids = poids;
    }

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

    public Dept(String nom,String prenom,int age,Date daty,String lieu,double poids,Time time)
    {
         this.setnom(nom);
         this.setprenom(prenom);
         this.setage(age);
         this.setdate(daty);
         this.setlieu(lieu);
         this.setpoids(poids);
         this.settime(time);
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

    public Dept(String nom,int age)
    {
        this.nom=nom;
        this.age=age;
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

    @Auth("")
    @Url("getDeptTestAuthenf")
    @Session()
    public ModelView DeptPeuAvoirAcces() {
        String s1=".jsp";
        String s="Liste";
        String vString=s+s1;
       ModelView m=new ModelView(vString);
       m.add("test1", "I love you jesus");
       //m.add("test2", new String[] { "Tableau Mety"});
        return m;
    }
    
    @Auth("admin")
    @Url("getDeptAuthentif")
    @Session()
    public ModelView DeptAdmin(@Argument("nom") String nom,@Argument("age") int age) {
        String s1=".jsp";
        String s="Liste";
        String vString=s+s1;
       ModelView m=new ModelView(vString);
       ArrayList<Dept> olona=new ArrayList<Dept>();
       Dept user=new Dept(nom,age);
       olona.add(user);
       m.add("val",olona);
        return m;
    }
    @Url("DeptGetId")
    public ModelView myMethodId(@Argument("nom") String nom, @Argument("prenom") String prenom,@Argument("age") int age,@Argument("date") Date date,@Argument("lieu") String lieu,@Argument("poids") double poids,@Argument("time") Time time) {
        String s1=".jsp";
        String s="Liste";
        String vString=s+s1;
       ModelView m=new ModelView(vString);
       ArrayList<Dept> olona=new ArrayList<Dept>();
       Dept user=new Dept(nom,prenom,age,date,lieu,poids,time);
       olona.add(user);
       m.add("val",olona);
        return m;
    }
    
}
//economie memoire positif singleton
//sprint8 bis
//css / fotsin na /* .do na .action   ex @getall.do am fonction findAll
//filter
//sprint9

