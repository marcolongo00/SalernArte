<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="${selectedEvento.nome}"/>
</jsp:include>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore') and (not selectedEvento.attivo)}">
<form action="gestione-eventi" method="get">
    <input type="hidden" name="idEvento" value="${selectedEvento.id}">
    <input type="submit" name="accettaIns" value="ACCETTA">
    <input type="submit" name="rifiutaIns" value="RIFIUTA">
</form>
</c:if>
<a href="gestione-eventi?idE=${selectedEvento.id}&bioOrg=true&idOrganizzatore=${selectedEvento.idOrganizzatore}">Bio organizzatore </a>
nome : ${selectedEvento.nome} <br>
descrizione: ${selectedEvento.descrizione}
</body>
</html>
