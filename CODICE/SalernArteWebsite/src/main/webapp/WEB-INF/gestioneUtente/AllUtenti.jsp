<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Lista Utenti"/>
</jsp:include>

<c:choose>
    <c:when test="${empty allUtenti}">
        <h3 class="empty" >Nessun Utente registrato sulla piattaforma</h3>
    </c:when>
    <c:otherwise>
        <div class="slideshow-container">
            <c:forEach items="${allUtenti}" var="utente">
                ${utente.id}    ${utente.tipoUtente} <br>

            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
