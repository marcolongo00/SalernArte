<div id="recuperaPwd" class="modal">
    <!-- il form class="logon-content" con la X per chiudere il login -->
    <div class="modalContainer" id="RecuperaPwdContainer">
        <div class="closeLogin">
            <span class="close" onclick="closeLoginRegistrazione()" title="Close Logon">&times;</span>
        </div>
        <form  action="autenticazione-controller" method="post" name="recuperaPwd">
            <!-- il div con i campi da compilare-->
            <div class="campi tipo">
                <p class="titolo"> Recupera Passord: </p>
                <a id="LinklogRecuperaPwd" > Torna al login </a> <br>
                <label for="email"><b>Inserisci la tua email: </b></label> <br>
                <input type="text" id="email" name="email" required> <br>
                <div class="informazioniRecPwd">Ti verrà inviata un'email con una nuova password epr il tuo account. <br> Ti consigliamo di cambiare la password al tuo prossimo accesso.</div>
                <input type="submit" id="acc" name="ConfermaCambioPwd" value="Conferma"> <br>
            </div>
        </form>
    </div>
</div>
