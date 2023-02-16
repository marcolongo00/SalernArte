<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="${organizzatore.nome}"/>
</jsp:include>
<a href="gestione-eventi?idE=${idE}&detailsE=true">torna all'evento </a><br>

nome : ${organizzatore.nome}
cognome : ${organizzatore.cognome} <br>
foto...<br>
biografia: ${organizzatore.biografia}
</body>
</html>
