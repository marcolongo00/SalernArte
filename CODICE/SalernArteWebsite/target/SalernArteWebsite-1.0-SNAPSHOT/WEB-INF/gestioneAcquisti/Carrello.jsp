
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Carrello"/>
    <jsp:param name="stylesheet" value="CSS/Carrello.css"/>
</jsp:include>
<div class="carrContainer">
    <div id="sommario">
        <form action="gestione-acquisti" method="post">
            <c:choose>
                <c:when test="${empty carrello.prodotti}">
                    <h3 class="empty">NESSUN PRODOTTO NEL CARRELLO</h3>
                </c:when>
                <c:when test="${alertCarrello}">
                    <strong>Rilevati prodotti non più disponibili, rimuoverli per procedere.</strong>
                </c:when>
                <c:when test="${(sessionScope.tipoUtente=='utente') or (sessionScope.tipoUtente=='scolaresca')}">
                    <strong>TOTALE:</strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${carrello.prezzoTot}" /> €<br>
                    <input type="submit" name="datiCartaAcquisto" class="bottonedecoratoblu" value="COMPLETA ACQUISTO">
                    <input type="submit" name="svuotaCarrello" class="bottonedecoratoblu" value="SVUOTA CARRELLO">
                </c:when>
                <c:otherwise>
                    <strong>TOTALE:</strong> ${carrello.prezzoTot}<br>
                    <input type="submit" name="datiCartaAcquisto" class="bottonedecoratoblu" value="COMPLETA ACQUISTO" disabled>
                    <input type="submit" name="svuotaCarrello" class="bottonedecoratoblu" value="SVUOTA CARRELLO"><br>
                    <strong> Effettua il login o Registrati per completare l'acquisto </strong>
                </c:otherwise>

            </c:choose>
        </form>
    </div>
    <c:forEach items="${carrello.prodotti}" var="pq">
        <div class="itemCarr">
            <div class="itemTitle"><a href="gestione-eventi?idE=${pq.prodotto.id}&detailsE=true">${pq.prodotto.nome} </a></div>
            <div class="center">
                <c:choose>
                    <c:when test="${pq.prodotto.numBiglietti eq 0 || pq.prodotto.numBiglietti < pq.quantita}">
                        <p class="errorProd">PRODOTTO NON PIU' DISPONIBILE</p>
                    </c:when>

                    <c:otherwise>
                        Quantita:
                        <select class="quantita" id="${pq.prodotto.id}">
                            <c:forEach begin="1" end="${pq.prodotto.numBiglietti}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index eq pq.quantita}">
                                        <option class="qtaVal" value="${loop.index}" selected>${loop.index}</option>
                                    </c:when>

                                    <c:otherwise>
                                        <option class="qtaVal"  value="${loop.index}">${loop.index}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>


                <form action="gestione-acquisti" method="get" class="exDelete">
                    <input type="hidden" name="idE" value="${pq.prodotto.id}">
                    <input type="submit" class="bottonedecoratoblu" name="removeEventoFromCarrello" value="Rimuovi">
                </form>
            </div>
            <img src="${pq.prodotto.path}" >

        </div>
    </c:forEach>

</div>

<script>
    $(document).ready(function () {
        $(".quantita").change(function () {
            var idE=$(this).attr("id");
            var val=$("#"+idE+" option:selected").text();

            var url="update-carr-qta?idE="+idE+"&qta="+val; //nopn esiste ancora
            $.getJSON(url,function (data) {
                location.reload();
            });
        });
    });
</script>

</body>
</html>