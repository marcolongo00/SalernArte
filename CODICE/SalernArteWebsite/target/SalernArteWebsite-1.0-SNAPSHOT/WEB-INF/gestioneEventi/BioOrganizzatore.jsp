<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="${organizzatore.nome}"/>
</jsp:include>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore') and (not selectedEvento.attivo)}">
    <form action="gestione-eventi" method="get">
        <input type="hidden" name="idEvento" value="${selectedEvento.id}">

        <a href="gestione-eventi?idE=${evento.id}&bioOrg=true&idOrganizzatore=${evento.idOrganizzatore}">Bio organizzatore </a>
        <input type="submit" name="accettaIns" value="ACCETTA">
        <input type="submit" name="rifiutaIns" value="RIFIUTA">
    </form>
</c:if>
<a href="gestione-eventi?idE=${idE}&detailsE=true">torna all'evento </a><br>

nome : ${organizzatore.nome}
nome : ${organizzatore.cognome}
foto...<br>
biografia: ${organizzatore.biografia}
</body>
</html>
