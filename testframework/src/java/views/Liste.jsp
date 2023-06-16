<%@page import="java.util.ArrayList"%>
<%@page import="etu2090.framework.model.Dept"%>
<%
    ArrayList<Dept> listes=(ArrayList<Dept>)request.getAttribute("Liste_personne");
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
        <h1>Bienvenue <%out.print(listes.get(i).getnom()+" "+listes.get(i).getprenom()+". Vous avez "+listes.get(i).getage()+"  ans");%> </h1>
             <p>ne(e)  le <%out.print(listes.get(i).getdate()+" . A  "+listes.get(i).gettime()+"  heures, vous avez une taille de  "+listes.get(i).gettaille()+"  m");%> </h1>
    <% } %>
</body>
</html>