package etu2090.framework.model;
public class Emp {
  String nom;
  int age;
  public Emp(){}
  public int getAge() {
      return age;
  }
  public void setAge(int age) {
      this.age = age;
  }
  public String getNom() {
      return nom;
  }
  public void setNom(String nom) {
      this.nom = nom;
  }
  public Emp(String nom,int age)
  {
    this.setAge(age);
    this.setNom(nom);
  }    
}