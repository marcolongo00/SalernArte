<%@ page import="model.entity.UtenteRegistratoBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Profilo Utente"/>
</jsp:include>
<div class="modalC" >
    <div class="modalContainerC" >
        <form name="reg" action="area-utente" method="post">
            <div class="flex datiC">
                <div >
                    <div class="allC">
                        <input type="email" name="email" id="email" oninput="" placeholder="Email" value="${selezionato.email}" required>
                        <p style="text-align: center">*La password deve contenere minimo 6 caratteri, una lettera maiuscola,minuscola e un numero.</p>

                        <input type="password" id="password" name="password" oninput=""placeholder="Password" >
                        <input type="password" name="passwordConferma" id="passConferma" oninput="" placeholder="ConfermaPassword" >

                    </div>
                    <c:if test="${(selezionato.tipoUtente=='organizzatore') or (selezionato.tipoUtente=='utente') or (selezionato.tipoUtente=='amministratore')}">
                        <div class="CutenteAndOrganizzatoreAndAdmin" >
                            <input type="text" id="nome" name="nome" oninput="" placeholder="Nome" value="${selezionato.nome}">
                            <input type="text" id="cognome" name="cognome" oninput="" placeholder="Cognome" value="${selezionato.cognome}">

                        </div>
                    </c:if>
                    <c:if test="${(selezionato.tipoUtente=='organizzatore') or (selezionato.tipoUtente=='utente')}">
                        <div class="CutenteAndOrganizzatore" >
                              <div class="dateContainer">
                                <label for="dataDiNascita">Data di nascita</label>
                                <input type="date" id="dataDiNascita"  name="dataDiNascita" value="${selezionato.dataDiNascita}">
                            </div>
                            <div class="CgenderContainer">
                                <c:choose>
                                    <c:when test="${selezionato.sesso==0}">
                                        <input type="radio" id="uomo" name="gender"  value="0" checked>
                                        <label for="uomo"><b>Uomo</b></label>

                                        <input type="radio" id="donna" name="gender" value="1">
                                        <label for="donna"><b>Donna</b></label>

                                        <input type="radio" id="altro" name="gender" value="2">
                                        <label for="altro"><b>Altro</b></label>
                                    </c:when>
                                    <c:when test="${selezionato.sesso==1}">
                                        <input type="radio" id="uomo" name="gender"  value="0">
                                        <label for="uomo"><b>Uomo</b></label>

                                        <input type="radio" id="donna" name="gender" value="1"checked>
                                        <label for="donna"><b>Donna</b></label>

                                        <input type="radio" id="altro" name="gender" value="2">
                                        <label for="altro"><b>Altro</b></label>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="radio" id="uomo" name="gender"  value="0">
                                        <label for="uomo"><b>Uomo</b></label>

                                        <input type="radio" id="donna" name="gender" value="1">
                                        <label for="donna"><b>Donna</b></label>

                                        <input type="radio" id="altro" name="gender" value="2" checked>
                                        <label for="altro"><b>Altro</b></label>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${selezionato.tipoUtente=='organizzatore'}">
                            <div class="Corganizzatore">
                                <input type="text" id="biografia" name="biografia"  placeholder="biografia" value="${selezionato.biografia}">
                                <input type="text" id="iban" name="iban"  placeholder="iban" value="${selezionato.iban}">
                            </div>
                        </c:when>
                        <c:when test="${selezionato.tipoUtente=='scolaresca'}">
                            <div class="Cscolaresca">
                                <input type="text" id="istituto" name="istituto"  placeholder="Istituto" value="${selezionato.istituto}">
                            </div>
                        </c:when>
                    </c:choose>
                </div>

            </div>
            <input type="submit" id="registrami"  name="updateProfilo" value="Modifica Profilo">
            <p class="tipo" id="messaggio"></p>

        </form>
        <form action="area-utente" method="post">
            <input class="bottonedecoratoblu" type="submit" name="eliminaProfilo" value="Elimina porfilo">
        </form>
    </div>
</div>
</body>
</html>
