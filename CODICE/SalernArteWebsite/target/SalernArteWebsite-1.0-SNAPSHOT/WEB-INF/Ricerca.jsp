
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Header.jsp">
    <jsp:param name="pageTitle" value="Ricerca"/>
    <jsp:param name="stylesheet" value="CSS/Ricerca.css"/>
</jsp:include>

<c:choose>
    <c:when test="${empty eventi}">
        <h3 class="empty">Nessun risultato.</h3>
    </c:when>
    <c:otherwise>

        <div id="titoloRicerca">Risultati della ricerca:</div>
        <div class="searchContainer">
            <c:forEach items="${eventi}" var="evento">
                <a href="gestione-eventi?idE=${evento.id}&detailsE=true">
                    <div class="itemSearch">
                        <div class="itemTitle">${evento.nome}</div>
                        <div class="center">
                                ${evento.descrizione}
                        </div>
                        <img src="${evento.path}" alt="${evento.nome}">
                    </div> </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
<script>
    $(document).ready(function () {
        var showChar = 100;  // caratteri mostrati
        var ellipsestext = "...";
        $('.center ').each(function() {
            var content = $(this).html();

            if(content.length > showChar) {
                var c = content.substr(0, showChar) +ellipsestext;
                $(this).html(c);
            }

        });
    });
</script>


</body>
</html>
