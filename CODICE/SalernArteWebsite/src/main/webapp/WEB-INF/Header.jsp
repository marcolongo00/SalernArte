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

        <input type="submit" name="goToLogin" value="login">
    </form>
</div>
<c:if test="${sessionScope.selezionato !=null}">
<br>SEI LOGGATO<br>
</c:if>


