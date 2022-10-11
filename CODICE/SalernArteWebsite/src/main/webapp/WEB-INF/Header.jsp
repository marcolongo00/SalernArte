<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://kit.fontawesome.com/a076d05399.js"></script> <%--per le icone--%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle}</title>
    <c:forEach items="${paramValues.get('stylesheet')}" var="par">
        <link rel="stylesheet" type="text/css" href="${par}">
    </c:forEach>

    <link rel="stylesheet" type="text/css" href="CSS/Mycss.css">
    <link rel="stylesheet" type="text/css" href="CSS/Footer.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="jQuery/myjQuery.js"></script>
    <script src="JS/ricerca.js"></script>
    <!-- velidazione input javascript-->

    <script src="JS/ValidazioneInput.js"></script>

</head>
<body>
<div class="container">
    <a class="header" href="index.html">SalernArte</a>
    <div class="items2">
        <!-- responsive menu column-->
        <div class="items2Responsive">
            <div class="container2" id="menuIntegrate"><!-- serve per l'icona del menu responsive-->
                <div class="bar1"></div> <!-- serve per l'icona del menu responsive-->
                <div class="bar2"></div><!-- serve per l'icona del menu responsive-->
                <div class="bar3"></div><!-- serve per l'icona del menu responsive-->
            </div>
        </div>
    </div>

    <div class="items3">
        <div class="items2Icon">

            <div class="showmenu">
                <ul style="list-style-type: none">
                    <c:choose>
                        <c:when test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore')}">
                            <li><a href="gestione-eventi?goToAllRichiesteEventi=true" > Richieste eventi</a></li>
                            <hr>
                            <li><a href="gestione-eventi?goToRichiesteInserimento=true" > RICHIESTE INSERIMENTO EVENTI</a></li>
                            <hr>
                            <li><a href="gestione-eventi?goToRichiesteModifica=true" > RICHIESTE MODIFICHE EVENTI</a></li>
                            <hr>
                            <li><a href="area-utente?listaUtenti=true" > LISTA UTENTI </a></li>
                            <hr>
                        </c:when>
                        <c:when test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='organizzatore')}">
                            <li><a href="gestione-eventi?goToRichiestaEvento=true">Richiedi evento</a></li>
                            <hr>
                            <li><a href="gestione-eventi?goToEventiOrganizzatore=true">I tuoi eventi</a></li>
                            <hr>
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                    </c:choose>
                    <li> <a href="area-utente?goToProfilo=true" > PROFILO UTENTE </a> </li>
                    <hr>
                    <li> <a href="autenticazione-controller?logout=true">Log out</a> </li>
                </ul>
            </div>
            <div id="login">
                <c:choose>
                    <c:when test="${sessionScope.selezionato !=null}">
                        <div class="hidemenu">
                            <i class="fas fa-user-tie spostaAdmin" style="font-size: 23px;color: #605E5E;"></i>
                            <i class="fa fa-chevron-down" aria-hidden="true" style="font-size: 23px;color: #605E5E;"></i>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a class="sopraIcon" ><i class="far fa-user" style="font-size: 23px;color: #605e5e; display: flex;"></i></a>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${(sessionScope.tipoUtente=='utente') or (sessionScope.tipoUtente=='scolaresca') or (sessionScope.selezionato==null)}">
                <a class="sopraIcon2" id="null" href="gestione-acquisti?goToCarrello=true"><i class="fa fa-shopping-cart" aria-hidden="true" style="font-size: 23px;color: #605E5E"></i></a>
            </c:if>
            <a class="sopraIcon2"><i class="fa fa-search" id="showbarracerca" style="font-size: 23px;color: #605E5E"></i></a>
            <!-- fine divIcon-->
        </div>

        <div class="iconresponsive">
            <div class="cercaresponsive">
                <form action="gestione-eventi" method="get">
                    <input type="text" name="query" list="ricerca-datalist" placeholder="Ricerca" id="ricercaresp" onkeyup="ricerca(this.value)" value="">
                    <datalist id="ricerca-datalist2"></datalist>
                </form>
            </div>

            <div id="loginresp">
                <c:choose>
                    <c:when test="${sessionScope.selezionato !=null}">
                        <a class="sopraIcon hidemenu">Account</a>
                        <div class="showmenu">
                            <ul style="list-style-type: none;">
                                <c:choose>
                                    <c:when test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='amministratore')}">
                                        <li><a href="gestione-eventi?goToAllRichiesteEventi=true" > Richieste eventi</a></li>
                                        <hr>
                                        <li><a href="gestione-eventi?goToRichiesteInserimento=true" > RICHIESTE INSERIMENTO EVENTI</a></li>
                                        <hr>
                                        <li><a href="gestione-eventi?goToRichiesteModifica=true" > RICHIESTE MODIFICHE EVENTI</a></li>
                                        <hr>
                                        <li><a href="area-utente?listaUtenti=true" > LISTA UTENTI </a></li>
                                        <hr>
                                    </c:when>
                                    <c:when test="${(sessionScope.selezionato !=null) and (sessionScope.tipoUtente=='organizzatore')}">
                                        <li><a href="gestione-eventi?goToRichiestaEvento=true">Richiedi evento</a></li>
                                        <hr>
                                        <li><a href="gestione-eventi?goToEventiOrganizzatore=true">I tuoi eventi</a></li>
                                        <hr>
                                    </c:when>
                                     <c:otherwise>
                                    </c:otherwise>
                                </c:choose>
                                 <li> <a href="area-utente?goToProfilo=true" > PROFILO UTENTE </a>  </li>
                                <hr>
                                <li> <a href="autenticazione-controller?logout=true">Log out</a> </li>
                            </ul>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <a class="sopraIcon">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${(sessionScope.tipoUtente=='utente') or (sessionScope.tipoUtente=='scolaresca') or (sessionScope.selezionato==null)}">
                <a class="sopraIcon2" href="gestione-acquisti?goToCarrello=true">Carrello</a>
            </c:if>
        </div>
                <c:choose>
                    <c:when test="${active=='teatro'}">
                        <a href="gestione-eventi?goToTipoEventi=teatro" class="uno" id="uno" style="color: #2F2E2E;">Eventi Teatro</a>
                    </c:when>
                    <c:otherwise>
                        <a href="gestione-eventi?goToTipoEventi=teatro" class="uno" id="uno">Eventi Teatro</a>
                    </c:otherwise>
                </c:choose>



                <c:choose>
                    <c:when test="${active=='mostra'}">
                        <a href="gestione-eventi?goToTipoEventi=mostra" class="due" id="due" style="color: #2F2E2E;">Eventi Mostre</a>
                    </c:when>
                    <c:otherwise>
                        <a href="gestione-eventi?goToTipoEventi=mostra" class="due" id="due">Eventi Mostre</a>
                    </c:otherwise>
                </c:choose>
        <!--active anche degli altri elementi del menu che roa non abbiamo-->
        <%-- <c:choose>
             <c:when test="${param.active=='activeContact'}">
                 <a href="Contact" class="quattro" id="quattro" style="color: #2F2E2E;">Contact</a>
             </c:when>
             <c:otherwise>
                 <a href="Contact" class="quattro" id="quattro">Contact</a>
             </c:otherwise>
         </c:choose>--%>
    </div>
</div>

<div class="togglecerca">
    <form action="gestione-eventi" method="post">
        <input type="text" name="query" list="ricerca-datalist" placeholder="Esplora" onkeyup="ricerca(this.value)" value="">
        <datalist id="ricerca-datalist"></datalist>
        <input type="submit" id="search" name="ricercaEventi" value="Search">
    </form>
</div>

<div id="loginForm">
    <c:if test="${sessionScope.selezionato ==null}">
        <!-- il div della finestra login -->
        <jsp:include page="autenticazione/login.jsp"/>
        <jsp:include page="autenticazione/registrazione.jsp"/>
    </c:if>
</div>

<script>
    function closeLoginRegistrazione(){
        $("form[name='LoginForm']").trigger("reset");
        $("form[name='reg']").trigger("reset");
        $("#logon").hide();
        $("#modalRegistrazione").hide();
        //validaAll("insertMSubmit"); valida tutti i campi per registrazione
    }


    $(document).ready(function () {
        $(".sopraIcon").click( function() {
            $("#logon").css("display","flex"); // Show the #logon div
        });

        //va sicuramente aggiustato per gli id della registrazione nostra
        $("#Linkreg").click(function () {
            closeLoginRegistrazione();
            $("#username, #nome, #cognome, #email, #password, #passConferma").css("border","1px solid rgba(160, 160, 159, 1)");
            $("#modalRegistrazione").css("display","flex");

        });
        $("#Linklog").click(function () {
            closeLoginRegistrazione();
            $("#logon").css("display","flex");
        });
        // Get the modal
        var modal=document.getElementById("logon");

        // When the user clicks anywhere outside of the modal, close it
        window.onclick =function (event) {
            if(event.target==modal){
                modal.style.display="none";
            }
        }

        $(".togglecerca").css("display","none");
        $("#showbarracerca").click(function () {
            $(".togglecerca").toggle();
            if($(".togglecerca").is(':visible')){
                $(".items2Icon").css("right","206px");
            }else{
                $(".items2Icon").css("right","10px");
            }
        });
    });
</script>
