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
    <h1>Connexion</h1>
    <form action="getDeptAuthentif" method="get">
            <label for = "nom">votre nom</label>
            <input type="text" name="nom" id = "nom" />
            </br></br>
            <label for = "age">votre age </label>
            <input type="text" name="age" id = "age" />
            </br></br>
            <input type="submit" value="Log In" />
        </form>

 <p><a href="login">login<a></p> 
</body>
</html>