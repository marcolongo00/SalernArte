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
    <form action="autenticazione-controller" method="get" >
        <c:choose>
            <c:when test="${sessionScope.selezionato !=null}">
                <input type="submit" name="logout" value="logout">
            </c:when>
            <c:otherwise>
                <input type="submit" name="goToLogin" value="login">
                <input type="submit" name="goToRegistrazione" value="Registrati">
            </c:otherwise>
        </c:choose>
    </form>
</div>
<c:if test="${sessionScope.selezionato !=null}">
<c:if test="${sessionScope.tipoUtente == 'utenteRegistrato'}">
test per controlli jstl su utenti
</c:if>
<br>SEI LOGGATO ${sessionScope.tipoUtente}<br>
</c:if>


