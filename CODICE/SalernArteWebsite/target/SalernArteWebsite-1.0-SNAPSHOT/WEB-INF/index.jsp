<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 20/06/2022
  Time: 13:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="Header.jsp">
    <jsp:param name="pageTitle" value="Home"/>
</jsp:include>

<c:choose>
    <c:when test="${empty eventi}">
        <h3 class="empty" >Nessun evento disponibile al momento.</h3>
    </c:when>
    <c:otherwise>
        <div class="slideshow-container">
            <c:forEach items="${eventi}" var="evento">
                <div class="mySlides fade">
                    <div class="mTitle"> <a href="GestioneEventiController?idE=${evento.id}&detailsE=true">${evento.nome} </a></div>
                    <div class="mDesc">${evento.descrizione}</div>
                    <div class="exDurata"> <strong>inizio:</strong> <fmt:formatDate value="${evento.dataInizio}" pattern="dd-MM-yyyy"/> <strong>fine:</strong>  <fmt:formatDate value="${evento.dataFine}" pattern="dd-MM-yyyy"/></div>

                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

</body>
</html>
