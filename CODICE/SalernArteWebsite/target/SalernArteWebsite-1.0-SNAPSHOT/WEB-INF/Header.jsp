<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 20/06/2022
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${param.pageTitle}</title>
</head>
<body>
<div>
    <a class="header" href="index.html">Home</a>
    <form action="autenticazione-controller" method="get" >
        <c:choose>
            <c:when test="${sessionScope.selezionato !=null}">
               Ciao  ${sessionScope.selezionato.nome} tu sei ${sessionScope.tipoUtente}
                <input type="submit" name="logout" value="logout"> <br>
                <a href="area-utente?goToProfilo=true"> PROFILO UTENTE </a> <br>
            </c:when>
            <c:otherwise>
                <input type="submit" name="goToLogin" value="login">
            </c:otherwise>
        </c:choose>
    </form>
    <form action="registrazione-controller" method="get" >

            <c:if test="${sessionScope.selezionato ==null}">
                <input type="submit" name="goToRegistrazione" value="Registrati">
            </c:if>
    </form>
</div>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='organizzatore')}">
<form action="gestione-eventi" method="get">
    <input type="submit" name="goToRichiestaEvento" value="Richiedi Evento">
</form>
</c:if>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore')}">
<a href="gestione-eventi?goToAllRichiesteEventi=true"> RICHIESTE EVENTI </a> <br>
<a href="area-utente?listaUtenti=true"> LISTA UTENTI </a>
</c:if>
<br>
<a href="gestione-eventi?goToTipoEventi=teatro"> EVENTI TEATRALI </a> <br>
<a href="gestione-eventi?goToTipoEventi=mostra"> EVENTI MOSTRE </a>


