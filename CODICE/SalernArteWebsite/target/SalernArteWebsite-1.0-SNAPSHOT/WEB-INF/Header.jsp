<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 20/06/2022
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://kit.fontawesome.com/a076d05399.js"></script> <%--per le icone--%>
<html>
<head>
    <title>${param.pageTitle}</title>
    <c:forEach items="${paramValues.get('stylesheet')}" var="par">
        <link rel="stylesheet" type="text/css" href="${par}">
    </c:forEach>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="jQuery/myjQuery.js"></script>

    <link rel="stylesheet" type="text/css" href="CSS/Mycss.css">
    <link rel="stylesheet" type="text/css" href="CSS/Footer.css">

</head>
<body>
<div id="provaFlex" style="display: inline-flex;">
<div style="display: inline-flex;">
    <a class="header" href="index.html" style="margin: 5px;">Home</a>
    <form action="autenticazione-controller" method="get" >
        <c:choose>
            <c:when test="${sessionScope.selezionato !=null}">
               Ciao  ${sessionScope.selezionato.nome} tu sei ${sessionScope.tipoUtente}
                <input type="submit" style="margin: 5px;" name="logout" value="logout"> <br>
                <a href="area-utente?goToProfilo=true" style="margin: 5px;"> PROFILO UTENTE </a> <br>
            </c:when>
            <c:otherwise>
                <input type="submit" style="margin: 5px;"name="goToLogin" value="login">
            </c:otherwise>
        </c:choose>
    </form>
    <form action="registrazione-controller" method="get" >

            <c:if test="${sessionScope.selezionato ==null}">
                <input type="submit" style="margin: 5px;" name="goToRegistrazione" value="Registrati">
            </c:if>
    </form>
</div>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='organizzatore')}">
<form action="gestione-eventi" method="get">
    <input type="submit" style="margin: 5px;" name="goToRichiestaEvento" value="Richiedi Evento">
</form>
</c:if>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore')}">
    <a href="gestione-eventi?goToAllRichiesteEventi=true" style="margin: 5px;"> RICHIESTE EVENTI</a> <br>
<a href="area-utente?listaUtenti=true" style="margin: 5px;"> LISTA UTENTI </a>
</c:if>
<br>
    <a href="gestione-eventi?goToTipoEventi=teatro" style="margin: 5px;">  EVENTI TEATRALI </a> <br>
    <a href="gestione-eventi?goToTipoEventi=mostra" style="margin: 5px;">  EVENTI MOSTRE</a><br>

<c:if test="${(sessionScope.tipoUtente=='utente') or (sessionScope.tipoUtente=='scolaresca') or (sessionScope.selezionato==null)}">
     <a href="gestione-acquisti?goToCarrello=true" style="margin: 5px;"> CARRELLO</a> <br>
</c:if>

</div>

