<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="${selectedEvento.nome}"/>
    <jsp:param name="stylesheet" value="CSS/DetailsEvento.css"/>
</jsp:include>

<c:if test="${(sessionScope.selezionato !=null) and (selezionato.tipoUtente=='amministratore') and (not selectedEvento.attivo)}">
    <div class="funzAdmin">
        <div id="adminButtons">
            <form action="gestione-eventi" method="get">
                <input type="hidden" class="bottonedecoratoblu" name="idEvento" value="${selectedEvento.id}">
                <c:choose>
                    <c:when test="${ins}">
                        inserimento
                        <input type="submit" class="bottonedecoratoblu" name="accettaIns" value="ACCETTA">
                        <input type="submit" class="bottonedecoratoblu" name="rifiutaIns" value="RIFIUTA">
                    </c:when>
                    <c:when test="${modif}">
                        modifica
                        <input type="submit" class="bottonedecoratoblu" name="accettaMod" value="ACCETTA">
                        <input type="submit" class="bottonedecoratoblu" name="rifiutaMod" value="RIFIUTA">
                    </c:when>
                    <c:otherwise>
                        credo ci sia stato qualche errore, questa non è una richiesta
                    </c:otherwise>
                </c:choose>
            </form>
        </div>
    </div>
</c:if>

<c:choose>
    <c:when test="${not selectedEvento.attivo}">
        EVENTO NON ATTIVO
    </c:when>
    <c:when test="${(sessionScope.selezionato !=null) and (selezionato.tipoUtente=='organizzatore') and  selectedEvento.attivo}">
        <div class="funzOrg">
            <div id="orgButtons">
                <!- JS trsforma caselle in input->
                <div class="bottonedecoratoblu" id="modMostra" > Modifica evento</div>
                <div id="modAnnulla" class="bottonedecoratoblu">Annulla</div>
                <button  id="modConferma"  class="bottonedecoratoblu" >Conferma modifica</button>

                <form action="gestione-eventi" method="get" class="exDelete">
                    <input type="hidden" name="idE" value="${selectedEvento.id}">
                    <input type="submit" id="Elimina" name="eliminaEv" class="bottonedecoratoblu" value="Elimina">
                </form>
            </div>
        </div>
    </c:when>
</c:choose>
<form action="gestione-eventi" id="formPerModifica" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="idEventoPreChange" value="${selectedEvento.id}">
    <input type="hidden" name="richiestaModEventoOrg" value="true">
<div class="contAll">


    <div class="imgContainer">
       <div id="file-upload"> <input type="file"  name="pathMod" accept="image/*"> </div>
        <img src="${selectedEvento.path}" alt="fotoEvento">
    </div>
    <div class="fluidDet">
        <div class="messageMod" id="messaggio"></div><!- ci potrebbe essere attivo o non attivo->
        <div class="mTitle">${selectedEvento.nome}</div>
        <div class="tipoEvento">
            <div class="tipoValue">
                <c:choose>
                    <c:when test="${selectedEvento.tipo}">teatro</c:when>
                    <c:otherwise>mostra</c:otherwise>
                </c:choose>
            </div>

            <div id="barraTipo">
                <div class="tipoModVal">0</div>
                <div class="arrow">
                    <i class="fa fa-caret-down" ></i>
                    <i class="fa fa-caret-up" ></i>
                </div>
            </div>
            <div class="allTipoEv">
                    <div class="allTipoSelected" >mostra</div>
                    <div class="allTipoSelected" >teatro</div>
            </div>
        </div>
       <div class="evBio"> <a href="gestione-eventi?idE=${selectedEvento.id}&bioOrg=true&idOrganizzatore=${selectedEvento.idOrganizzatore}">Bio organizzatore </a> </div><!-usare nome organizzatore->
        <strong> <div style="text-align: center" id="durataMostra">
            Dal
            <div class="dataInizio"><fmt:formatDate value="${selectedEvento.dataInizio}" pattern="dd-MM-yyyy"/> </div>
            <c:set var="realValueDataInizio" value="${selectedEvento.dataInizio}" />
            al
            <div class="dataFine"><fmt:formatDate value="${selectedEvento.dataFine}" pattern="dd-MM-yyyy"/> </div>
            <c:set var="realValueDataFine" value="${selectedEvento.dataFine}" />
        </div></strong>
        <div class="containerIndirizzo">
            Indirizzo: <div class="indirizzoCont">${selectedEvento.indirizzo}</div>
        </div>
        <div class="containerSede">
            Sede: <div class="sedeCont">${selectedEvento.sede}</div>
        </div>
        <div class="mDescr">${selectedEvento.descrizione}</div>
        <div class="prezzoBiglContainer">Costo biglietto: <div class="prezzoBiglietto">${prezzoBigl}</div>€</div>

        <c:choose>
            <c:when test="${selectedEvento.numBiglietti eq 0}">
                <h3 id="soldOut">SOLD OUT</h3>
            </c:when>
            <c:when test="${alertScaduta}">
                <h3 id="soldOut">Questa mostra è scaduta.</h3>
            </c:when>
            <c:when test="${(selezionato.tipoUtente=='organizzatore') or (selezionato.tipoUtente=='amministratore')}">
                <div class="qtaOrgContainer">
                    QUANTITA':<div class="numBiglietti">${selectedEvento.numBiglietti}</div>
                </div>

            </c:when>
            <c:otherwise>
                <div class="itemsCarr">
                    <div id="mFirstButton" >
                        <div id="firstButton" class="bottonedecoratoblu">ACQUISTA BIGLIETTI</div>
                    </div>
                    <div class="carrContainer">
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
                            <div action="gestione-acquisti" method="get">
                                <div id="ConfermaAggAlCarrello" class="bottonedecoratoblu" name="aggiungiAlCarrello" >AGGIUNGI AL CARRELLO</div>
                            </div>
                            <div id="annulla" class="bottonedecoratoblu">ANNULLA</div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>


</div>

</form>


<jsp:include page="../Footer.jsp" />
<script>

    function logFormatoData(str){
        console.log(str);
    }
    $(document).ready(function () {
        $(".fa-caret-up, .carrContainer").hide();
        $("#barraTipo").hide();
        $(".arrow").click(function () { //cambiare
            $(".allQta").slideToggle();
            $(".allTipoEv").slideToggle();
        });

        $("#firstButton").click(function () {
            $("input:submit").prop('disabled', true);
            $("#mFirstButton").hide();
            $(".carrContainer").css("display","flex");
        });

        $(".fa-caret-down").click(function () { //cambiare
            $(".fa-caret-up").show();
            $(".fa-caret-down").hide();
        });

        $(".fa-caret-up").click(function () { //cambiare
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

        $(".allTipoSelected").click(function () {
            $(".allTipoEv, .fa-caret-up").hide();
            $(".fa-caret-down").show();
            var value=$(this).text();
            $(".tipoModVal").html(value);
        });

        $("#ConfermaAggAlCarrello").click(function (){
            var idE=$("input[name='idEventoPreChange']").val();
            //quantita
            var quantita=$(".qtaValue").text();
            var url="aggiungi-al-carrello?idE="+idE+"&quantita="+quantita;
            $.getJSON(url,function(data) {
                console.log(data);
                if(data){
                    //in teoria quello che viene fatto in annulla
                    //oppure location.reload
                    location.reload();
                }else{
                    alert("Errore nell' operazione, riprovare.");
                    location.reload();
                }
            });

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
        var tipoEv= $(".tipoValue").text();
        var indirizzo=$(".indirizzoCont").text();
        var sede=$(".sedeCont").text();
        var numBiglietti=$(".numBiglietti").text();
        var prezzoBiglietto=$(".prezzoBiglietto").text();
        var dataInizio=$(".dataInizio").text();
        var dataFine=$(".dataFine").text();
        $("#modMostra").click(function () {

            $("#modMostra").hide();
            $("#modAnnulla,#modConferma").show();
            $("input[type='file']").show();
            $(".mTitle").html("<input type='text' class='mTitle' name='titoloEvMod' id='validaTitle' oninput='validaNotEmpty(\"modConferma\")' value='"+title+"'> ");
            $(".indirizzoCont").html("<input type='text' class='indirizzoCont' name='indirizzoEvMod' id='validaIndirizzo' oninput='validaNotEmpty(\"modConferma\")' value='"+indirizzo+"'> ");
            $(".sedeCont").html("<input type='text' class='sedeCont' id='validaSede' name='sedeEvMod' oninput='validaNotEmpty(\"modConferma\")' value='"+sede+"'> ");
            $(".numBiglietti").html("<input type='number' min='0' step='1' class='numBigliettiCont' id='validaNumBiglietti' name='numBigliettiEvMod' oninput='validaNotEmpty(\"modConferma\")' value='"+numBiglietti+"'> ");
            $(".prezzoBiglietto").html("<input type='number'  class='prezzoBiglietto' id='validaPrezzoBiglietti' name='prezzoBigliettoEvMod' oninput='validaNotEmpty(\"modConferma\")' value='"+prezzoBiglietto+"'> ");

            $(".tipoModVal").html(tipoEv);
            $("#barraTipo").show();
            $(".tipoValue").hide();
            $(".mDescr").html("<textarea  rows='10' class='mDescr' name='descrizioneMod' id='validaDescr' oninput='validaNotEmpty(\"modConferma\")' >"+descr+"</textarea>");

            $(".dataInizio").html("<input type='date'  class='dataInizio' id='validaDataInizio' name='dataInizioEvMod' oninput='validaNotEmpty(\"modConferma\")' value='${realValueDataInizio}'> ");
            $(".dataFine").html("<input type='date'  class='dataInizio' id='validaDataInizio' name='dataFineEvMod' oninput='validaNotEmpty(\"modConferma\")' value='${realValueDataFine}'> ");

            //validaNotEmpty("modConferma");
        });

        $("#modAnnulla").click(function () {
            $("#modMostra").show();
            $("#modAnnulla,#modConferma").hide();
            $(".mTitle").text(title);
            $(".mDescr").text(descr);
            $(".indirizzoCont").text(indirizzo);
            $(".sedeCont").text(sede);
            $(".numBiglietti").text(numBiglietti);
            $(".prezzoBiglietto").text(prezzoBiglietto);
            $("#messaggio").text("");
            $(".tipoModVal").html(tipoEv);
            $("#barraTipo").hide();
            $(".tipoValue").show();
            $("input[type='file']").val(null);
            $("input[type='file']").hide();
            $(".dataInizio").text(dataInizio);
            $(".dataFine").text(dataFine);

        });

        $("#modConferma").click(function () {
            var tipoMod=  $(".tipoModVal").text();
            $(".tipoModVal").html("<input type='text'  name='tipoEvMod' value='"+tipoMod.trim()+"'> ");
           $("#formPerModifica").submit();
        });


    });
</script>
</body>
</html>
