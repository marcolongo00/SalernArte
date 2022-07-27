<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="${selectedEvento.nome}"/>
    <jsp:param name="stylesheet" value="CSS/DetailsEvento.css"/>
</jsp:include>
<c:if test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore') and (not selectedEvento.attivo)}">
    <div class="funzAdmin">
        <div id="adminButtons">
            <form action="gestione-eventi" method="get">
                <input type="hidden" class="bottonedecoratoblu" name="idEvento" value="${selectedEvento.id}">
                <input type="submit" class="bottonedecoratoblu" name="accettaIns" value="ACCETTA">
                <input type="submit" class="bottonedecoratoblu" name="rifiutaIns" value="RIFIUTA">
            </form>
        </div>
    </div>
</c:if>


<div class="contAll">

    <div class="imgContainer">
        <img src="${selectedevento.path}" alt="fotoEvento">
    </div>
    <div class="fluidDet">
        <div class="messageMod" id="messaggio"></div>
        <div class="mTitle">${selectedEvento.nome}</div>
        <a href="gestione-eventi?idE=${selectedEvento.id}&bioOrg=true&idOrganizzatore=${selectedEvento.idOrganizzatore}">Bio organizzatore </a>
        <strong> <div style="text-align: center" id="durataMostra"> Dal <fmt:formatDate value="${selectedEvento.dataInizio}" pattern="dd-MM-yyyy"/>  al <fmt:formatDate value="${selectedMostra.dataFine}" pattern="dd-MM-yyyy"/> </div></strong>
        <div class="mDescr">${selectedEvento.descrizione}</div>
        <strong><div style="text-align: center; margin-top:20px;" id="prezzoBiglietto">Costo biglietto: XXX €</div></strong>
        <c:choose>
            <c:when test="${selectedEvento.numBiglietti eq 0}">
                <h3 id="soldOut">SOLD OUT</h3>
            </c:when>
            <c:when test="${alertScaduta}">
                <h3 id="soldOut">Questa mostra è scaduta.</h3>
            </c:when>
            <c:otherwise>
                <div class="itemsCarr">
                    <div id="mFirstButton" >
                        <button id="firstButton" class="bottonedecoratoblu">ACQUISTA BIGLIETTI</button>
                    </div>
                    <div class="carrContainer">
                            <%--<div id="up">QTA:</div>--%>
                        <div id="carrQta">QUANTITA'</div>
                        <div id="barra">
                            <div class="qtaValue">0</div>
                            <div class="arrow">
                                <i class="fa fa-caret-down" ></i>
                                <i class="fa fa-caret-up" ></i>
                            </div>
                        </div>
                        <div class="allQta">
                            <c:forEach begin="0" end="${selectedEvento.numBiglietti}" varStatus="loop">
                                <div class="allSelected" >${loop.index}</div>
                            </c:forEach>
                        </div>
                        <div class="mButtons">
                            <form action="gestione-acquisti" method="get">
                                <input type="hidden" name="quantita" value="0">
                                <input type="hidden" name="idM" value="${selecteEvento.id}">
                                <input type="submit" class="bottonedecoratoblu" name="aggiungiAlCarrello" value="AGGIUNGI AL CARRELLO">
                            </form>
                            <button id="annulla" class="bottonedecoratoblu">ANNULLA</button>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>


<jsp:include page="../Footer.jsp" />
<script>
    $(document).ready(function () {
        $(".fa-caret-up, .carrContainer").hide();
        $(".arrow").click(function () {
            $(".allQta").slideToggle();
        });

        $("#firstButton").click(function () {
            $("input:submit").prop('disabled', true);
            $("#mFirstButton").hide();
            $(".carrContainer").css("display","flex");
        });

        $(".fa-caret-down").click(function () {
            $(".fa-caret-up").show();
            $(".fa-caret-down").hide();
        });

        $(".fa-caret-up").click(function () {
            $(".fa-caret-down").show();
            $(".fa-caret-up").hide();
            if($("#title").text()=="")  $("#title").html("QUANTITA'");
            if($(".qtaValue").text()==0){
                $("input:submit").prop('disabled', true);
            }
        });

        $(".allSelected").click(function () {
            $(".allQta, .fa-caret-up").hide();
            $(".fa-caret-down").show();
            var value=$(this).text();
            $(".qtaValue").html(value);
            $("input[name='quantita']").val(value);

            if(value==0){
                $("input:submit").prop('disabled', true);
            }
            else{
                $("input:submit").prop('disabled', false);
            }
        });

        $("#annulla").click(function () {
            $("#mFirstButton").show();
            $(".carrContainer").hide();
            $(".qtaValue").html(0);
            $("input[name='quantita']").val(0);
        });
        //button modifica
        var title= $(".mTitle").text();
        var descr=$(".mDescr").text();

        $("#modMostra").click(function () {
            $("#modMostra").hide();
            $("#modAnnulla,#modConferma").show();
            $("#firstButton").prop('disabled', true);
            $(".mTitle").html("<input type='text' class='mTitle' id='validaTitle' oninput='validaNotEmpty(\"modConferma\")' value='"+title+"'> ");
            $(".mDescr").html("<textarea  rows='10' class='mDescr' id='validaDescr' oninput='validaNotEmpty(\"modConferma\")' >"+descr+"</textarea>");
            validaNotEmpty("modConferma");
        });

        $("#modAnnulla").click(function () {
            $("#modMostra").show();
            $("#modAnnulla,#modConferma").hide();
            $("#firstButton").prop('disabled', false);
            $(".mTitle").text(title);
            $(".mDescr").text(descr);
            $("#messaggio").text("");
        });

        $("#modConferma").click(function () {
            //chiamata ajax
            var idM=$("input[name='idM']").val();
            var tit=$("input.mTitle").val();
            var de=$("textarea.mDescr").val();
            var url="update-mostra?idM="+idM+"&title="+tit+"&desc="+de;
            $.getJSON(url,function(data) {
                console.log(data);
                if(data){
                    title=tit;
                    descr=de;
                    $("#modMostra").show();
                    $("#modAnnulla,#modConferma").hide();
                    $("#firstButton").prop('disabled', false);
                    $(".mTitle").text(title);
                    $(".mDescr").text(descr);
                }else{
                    alert("Errore nella modifica, riprovare.");
                    location.reload();
                }
            });
        });
        var file=$("input:file");
        file.change(function (e) {
            $("#formImg").submit();
        });
    });
</script>
</body>
</html>
