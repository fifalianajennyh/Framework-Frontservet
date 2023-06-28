<%@page import="java.util.ArrayList"%>
<%@page import="etu2090.framework.model.Dept"%>
<%@page import="etu2090.framework.FileUpload"%>
<%
    ArrayList<Dept> listes=(ArrayList<Dept>)request.getAttribute("Fichier");
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
<% for(int i=0; i<listes.size(); i++) { %>
    Bienvenue <%= listes.get(i).getnom() %>
    <% if (listes.get(i).getfile() != null) { %>
        Nom du fichier : <%= listes.get(i).getfile().getname() %>
        Nombre de bytes : <%=listes.get(i).getfile().getbyte().length %>
    <% } else { %>
        Nom du fichier : Null
        Nombre de bytes : Null
    <% } %>
<% } %>

</body>
</html>