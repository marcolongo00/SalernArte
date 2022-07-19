<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 20/06/2022
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<div id="logon" class="modal">
    <!-- il form class="logon-content" con la X per chiudere il login -->
    <div class="modalContainer">
        <div class="closeLogin">
            <span class="close"  title="Close Logon">&times;</span>
        </div>
        <form  action="autenticazione-controller" method="post" name="LoginForm">
            <!-- il div con i campi da compilare-->
            <div class="campi tipo">
                <p class="titolo"> Login: </p>
                <a id="Linkreg">Non sei ancora iscritto?</a> <br>

                <input type="radio" id="Utente" name="tipoUtente" value="utente">
                <label for="Utente"><b>Utente</b></label>

                <input type="radio" id="Scolaresca" name="tipoUtente" value="scolaresca">
                <label for="Scolaresca"><b>Scolaresca</b></label>

                <input type="radio" id="Organizzatore" name="tipoUtente" value="organizzatore">
                <label for="Organizzatore"><b>Organizzatore</b></label>


                <input type="radio" id="Amministratore" name="tipoUtente" value="amministratore">
                <label for="Amministratore"><b>Amministratore</b></label><br>

                <label for="email"><b>email: </b></label> <br>
                <input type="text" id="email" name="email" required> <br>
                <label for="password"><b>Password: </b></label> <br>
                <input type="password" id="password" name="password"  required> <br>

                <input type="submit" id="acc" name="Accedi" value="Accedi"> <br>
            </div>
        </form>
    </div>
</div>
</body>
</html>
