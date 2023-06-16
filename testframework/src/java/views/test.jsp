<%-- 
    Document   : index
    Created on : 28 mars 2023, 02:08:00
    Author     : itu
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="etu2090.framework.model.Dept" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/insertion.css">
    <title>Bienvenue</title>
</head>
<body>
    <h1>AJOUT FORMULAIRE</h1>
    <form action="Deptsave" method="get">
            <label for = "nom">votre nom</label>
            <input type="text" name="nom" id = "nom" />
            </br></br>
            <label for = "prenom">votre prenom</label>
            <input type="prenom" name="prenom" id = "prenom" />
            </br></br>
            <label for = "age">votre age</label>
            <input type="number" name="age" id = "age" />
            </br></br>  
            
            <label for = "date">votre date de naissance</label>
            <input type="date" name="date" id = "date" />
            </br></br>  
            
            <label for = "time">votre heure de naissance</label>
            <input type="time" name="time" id = "time" />
            </br></br>  
            
            <label for = "taille">votre taille</label>
            <input type="number" name="taille" id = "taille" />
            </br></br>  
            <input type="submit" value="valider" />
        </form> 
</body>
</html>