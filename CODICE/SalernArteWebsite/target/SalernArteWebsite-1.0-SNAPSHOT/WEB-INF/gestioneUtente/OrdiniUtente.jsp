<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../Header.jsp">
  <jsp:param name="pageTitle" value="OrdiniUtente"/>
  <jsp:param name="stylesheet" value="CSS/ListaOrdini.css"/>
</jsp:include>
<div class="ordiniContainer">
  <c:if test="${selezionato.tipoUtente=='amministratore'}">
    <button class="bottonedecoratoblu" id="bottoneAdmin"><a class="linkListaUtente" href="area-utente?listaUtenti=true">Torna alla lista utenti</a></button>
  </c:if>
  <c:if test="${empty ordini}">
    <div class="ordine"><h3> Nessun acquisto effettuato. </h3></div>
  </c:if>
  <c:forEach items="${ordini}" var="ordine" varStatus="loop">
    <div class="ordine">
      <div class="top">
        <div class="numOrdine">#${fatture.size() + loop.index}</div>
        <div class="ordDetails">DATA: ${ordine.data} TOTALE: <fmt:formatNumber type="number" maxFractionDigits="2" value="${ordine.totale}" /> €</div>

      </div>

      <div class="prodotti">
        <p>PRODOTTI:</p>
        <div class="textList">
            ${ordine.prodotti}
        </div>
      </div>
    </div>
  </c:forEach>
</div>
</body>
</html>