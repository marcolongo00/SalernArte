<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Profilo Utente"/>
</jsp:include>
email: ${sessionScope.selezionato.email}<br>
password:${sessionScope.selezionato.password}<br>
<c:choose>
    <c:when test="${sessionScope.tipoUtente=='utente'}">
        dati dell'utenteg
    </c:when>
    <c:otherwise>
        nessun dato
    </c:otherwise>
</c:choose>
</body>
</html>
