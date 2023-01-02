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
<jsp:include page="./Header.jsp">
    <jsp:param name="pageTitle" value="Home"/>
    <jsp:param name="stylesheet" value="CSS/Gallerycss.css"/>
</jsp:include>

<c:choose>
    <c:when test="${empty eventi}">
        <h3 class="empty" >Nessun evento disponibile al momento.</h3>
    </c:when>
    <c:otherwise>
        <div id="quadriTOT" class="containerGall row" >

            <c:forEach items="${eventi}" var="evento">
                <a href="gestione-eventi?idE=${evento.id}&detailsE=true" class="noOpacityhover">
                    <figure class="titoloOver">
                        <img class="textoverimage" src="${evento.path}">
                        <figcaption><p>${evento.nome}</p></figcaption>
                    </figure>
                </a>
            </c:forEach>

        </div>
    </c:otherwise>
</c:choose>

</body>
</html>
