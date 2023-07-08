<%@page import="java.util.ArrayList"%>
<%@page import="etu2090.framework.model.Dept"%>
<%
    ArrayList<Dept> listes=(ArrayList<Dept>)request.getAttribute("data");
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
        <h1>Bienvenue</h1>
        <h3><%out.print("vous etes "+listes.get(i).getnom()+" "+listes.get(i).getage()+" ans");%></h3>
    <% } %>    
</body>
</html>