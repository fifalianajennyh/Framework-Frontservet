<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%
  // Récupérer la HashMap de la session
            HashMap<String, Object> sessions = (HashMap<String, Object>) request.getAttribute("session");
          
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
       // Vérifier si la HashMap est vide
          <%  if (session != null && !sessions.isEmpty()) {
                // Parcourir les entrées de la HashMap
                for (String key : sessions.keySet()) {
                    Object value = sessions.get(key);
        %>
                    <p>Clé : <%= key %>, Valeur : <%= value %></p>
        <%
                }
            } else {
        %>
                <p>La session est vide.</p>
        <%
            }
        %>
</body>
</html>