<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Lista Utenti"/>
    <jsp:param name="stylesheet" value="CSS/TabUtentiAdmincss.css"/>
</jsp:include>

<c:choose>
    <c:when test="${empty allUtenti}">
        <h3 class="empty" >Nessun ordine effettuato</h3>
    </c:when>
    <c:otherwise>
        <section class="tabellautenti">
            <table>
                <thead>
                <tr>
                    <th>numero</th>
                    <th>tipo utente</th>
                    <th>email</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${allUtenti}" var="utente" varStatus="count">
                    <tr class="line">
                        <td>${count.index}</td>
                        <td>${utente.tipoUtente}</td>
                        <td>${utente.email}</td>
                        <c:if test="${utente.tipoUtente=='utente' || utente.tipoUtente=='scolaresca'}">
                            <td class="flex">
                                <form action="area-utente" method="post">
                                    <input type="hidden" name="id" value="${utente.id}">
                                    <input type="submit" name="goToListaAcquisti" class="buttoninfo" value="Lista acquisti">
                                </form>
                            </td>
                        </c:if>
                        <td class="detailstext2">

                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </section>
    </c:otherwise>
</c:choose>

<jsp:include page="../Footer.jsp" />

</body>
</html>
