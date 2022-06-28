<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 20/06/2022
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>${selectedEvento.nome} </title>
</head>
<body>
nome : ${selectedEvento.nome} <br>
descrizione: ${selectedEvento.descrizione}
</body>
</html>
