<%@page import="java.util.ArrayList"%>
<%@page import="etu2090.framework.model.Dept"%>
<%
    ArrayList<Dept> listes=(ArrayList<Dept>)request.getAttribute("ListeFichier");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienvenue</title>
</head>
<body>
    <%for(int i=0;i<listes.size();i++) { %>
        <h1>Bienvenue <%out.print(listes.get(i).getnom());%></h1>
        <p>Nom du fichier : <%out.print (listes.get(i).getfile().getname()); %></p>
        <p>Nombre de bytes : <%out.print(listes.get(i).getfile().getbyte().length); %></p>
    <% } %>
</body>
</html>