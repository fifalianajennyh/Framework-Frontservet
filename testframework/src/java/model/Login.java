/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package etu2090.framework.model;
import etu2090.framework.annotation.Url;
import etu2090.framework.annotation.scope;
import etu2090.framework.annotation.Argument;
import etu2090.framework.annotation.Auth;
import etu2090.framework.ModelViews.ModelView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Login{
    String nom;
    String mdp;

   public String getNom() {
       return nom;
   }
   public void setMdp(String mdp) {
       this.mdp = mdp;
   }
   public String getMdp() {
       return mdp;
   }
   public void setNom(String nom) {
       this.nom = nom;
   }
    
   @Url("connexion")
   public ModelView Connected() {
       String jsp="redirect";
       String point=".jsp";
       String view=jsp+point;
       ModelView v=new ModelView(view);
       boolean  value=true;
       String name=this.nom;
       v.addAuthenf("isconnected",value);
       v.addAuthenf("profil",name);
      return v;
   }
   
}
//economie memoire positif singleton
//sprint8 bis
//css / fotsin na /* .do na .action   ex @getall.do am fonction findAll
//filter
//sprint9

