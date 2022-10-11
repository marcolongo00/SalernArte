<%@ page import="model.entity.UtenteRegistratoBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../Header.jsp">
    <jsp:param name="pageTitle" value="Profilo Utente"/>
</jsp:include>

    <h2 class="cell">Modifica il tuo account SalernArte</h2>
    <div class="slideshow-container">
        <form class="slideshow-container" action="area-utente?updateProfilo=true" method="post">
            <c:if test="${(selezionato.tipoUtente == 'Organizzatore') or (selezionato.tipoUtente == 'Utente') or (selezionato.tipoUtente == 'Amministratore')}">
            <div class ="mySlides fade">
                <span class="cell">Nome:</span>
                <label class="field cell">
                    <input type="text" name="nome" value="${selezionato.nome}">
                </label>
                <span class="cell">Cognome:</span>
                <label class="field cell">
                    <input type="text" name="cognome" value="${selezionato.cognome}">
                </label>

                <span class="cell">Email:</span>
                <label class="field cell">
                    <input type="email" name="email" value="${selezionato.email}">
                </label>
                <span class="cell">Password:</span>
                <label class="field cell">
                    <input type="password" name="password">
                </label>
                <span class="cell">Conferma password:</span>
                <label class="field cell">
                    <input type="password" name="conferma-password">
                </label>

                <c:if test="${(selezionato.tipoUtente == 'Utente') or (selezionato.tipoUtente == 'Organizzatore')}">
                    <span class="cell">Data di nascita:</span>
                    <label class="field cell">
                        <input type="date" name="data" value="${selezionato.dataDiNascita}">
                    </label>

                    <span class="cell">Sesso:</span>
                    <form>
                        <input type="radio" id="uomo" name="sesso" value="uomo">
                        <label for="uomo">Uomo</label>
                        <input type="radio" id="donna" name="sesso" value="donna">
                        <label for="donna">Donna</label>
                        <input type="radio" name="sesso" id="non specificato" value="non specificato">
                        <label for="non specificato">Non specificato</label>
                    </form>
                </c:if>
                <c:if test="${selezionato.tipoUtente == 'Organizzatore'}">
                    <span class="cell">Biografia:</span>
                    <label class="field cell">
                        <input type="textarea" name="bio" rows="30" cols="30" value="${selezionato.biografia}">
                    </label>

                    <span class="cell">IBAN:</span>
                    <label class="field cell">
                        <input type="text" name="iban" value="${selezionato.iban}">
                    </label>
                </c:if>
                <c:if test="${selezionato.tipoUtente == 'Scolaresca'}">

                    <span class="cell">Email:</span>
                    <label class="field cell">
                        <input type="email" name="email" value="${selezionato.email}">
                    </label>

                    <span class="cell">Password:</span>
                    <label class="field cell">
                        <input type="password" name="password">
                    </label>
                    <span class="cell">Conferma Password:</span>
                    <label class="field cell">
                        <input type="password" name="conferma-password">
                    </label>
                    <span class="cell">Istituto</span>
                    <label class="field cell">
                        <input type="text" name = "istituto" value="${selezionato.istituto}">
                    </label>
                </c:if>
                </c:if>
                <button type="submit" class="btn cell">MODIFICA</button>
            </div>
        </form>
    </div>
</body>
</html>
