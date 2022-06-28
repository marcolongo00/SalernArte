<%--
  Created by IntelliJ IDEA.
  User: aless
  Date: 21/06/2022
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Registrazione Page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<div class="modal" id="modalRegistrazione">
    <div class="modalContainer" id="signInContainer">
        <div class="closeLogin">
            <span class="close" onclick="closeLoginRegistrazione()" title="Close Logon">&times;</span>
        </div>
        <p class="titolo" style="margin-bottom:5px">Sign In:</p>
        <div class="tipo">
            <a  id="Linklog">Sei già iscritto?</a>
        </div>

        <form name="reg" action="registrazione-controller" method="post">
            <div class="flex dati">
                <div >
                    <input type="radio" id="Utente" name="tipoUtente" onclick="showDatiCorrettiPerUtente()"  value="utenteRegistrato">
                    <label for="Utente"><b>Utente</b></label>

                    <input type="radio" id="Scolaresca" name="tipoUtente" onclick="showDatiCorrettiPerUtente()" value="scolaresca">
                    <label for="Scolaresca"><b>Scolaresca</b></label>

                    <input type="radio" id="Organizzatore" name="tipoUtente" onclick="showDatiCorrettiPerUtente()" value="organizzatore">
                    <label for="Organizzatore"><b>Organizzatore</b></label>
                    <div class="all">
                        <input type="email" name="email" id="email" oninput="validaEmail('reg','registrami')" placeholder="Email" required>
                        <p style="text-align: center">*La password deve contenere minimo 6 caratteri, una lettera maiuscola,minuscola e un numero.</p>

                            <input type="password" style="margin-right: 5px" id="password" name="password" oninput="validaPassword('reg','registrami')"placeholder="Password" required>
                            <input type="password" name="passwordConferma" id="passConferma" oninput="validaPassword('reg','registrami')" placeholder="ConfermaPassword" required>

                    </div>
                    <div class="utenteAndOrganizzatore" >
                        <input type="text" id="nome" name="nome" oninput="validaNome('reg','registrami')" placeholder="Nome" >
                        <input type="text" id="cognome" name="cognome" oninput="validaCognome('reg','registrami')" placeholder="Cognome" >
                        <div class="dateContainer">
                            <label for="dataDiNascita">Data di nascita</label>
                            <input type="date" id="dataDiNascita"  name="dataDiNascita">
                        </div>
                        <div class="genderContainer">
                            <input type="radio" id="uomo" name="gender"  value="uomo">
                            <label for="uomo"><b>Uomo</b></label>

                            <input type="radio" id="donna" name="gender" value="donna">
                            <label for="donna"><b>Donna</b></label>

                            <input type="radio" id="altro" name=gender" value="altro">
                            <label for="altro"><b>Altro</b></label>
                        </div>
                    </div>
                    <div class="scolaresca">
                        <input type="text" id="istituto" name="istituto"  placeholder="Istituto" >
                    </div>
                    <div class="organizzatore">
                        <input type="text" id="biografia" name="biografia"  placeholder="biografia" >
                        <input type="text" id="azienda" name="azienda"  placeholder="azienda" >
                        <input type="text" id="iban" name="iban"  placeholder="iban" >
                    </div>
                </div>

            </div>
            <input type="submit" id="registrami"  name="registrazione" value="Registra">
            <p class="tipo" id="messaggio"></p>

        </form>
    </div>
</div>

</body>
</html>
<script>
function showDatiCorrettiPerUtente(){
    var checked=document.querySelector('input[name="tipoUtente"]:checked').value;
    $(".utenteAndOrganizzatore").hide();
    $(".organizzatore").hide();
    $(".scolaresca").hide();

    //document.getElementById("provaJS").innerHTML
      //  = "hai selezionato: "+document.querySelector('input[name="tipoUtente"]:checked').value;

    if(checked=='utenteRegistrato'){
        $(".utenteAndOrganizzatore").show();
    }else if(checked=='scolaresca'){
        $(".scolaresca").show();

    }else if(checked=='organizzatore'){
        $(".utenteAndOrganizzatore").show();
        $(".organizzatore").show();
    }




/*
    document.getElementById("provaJS").innerHTML
        = "hai selezionato: "+document.querySelector('input[name="tipoUtente"]:checked').value;
    for(i = 0; i < tipo.length; i++) {
        if(tipo[i].checked){

        }
           +tipo[i].value;
    }*/

}
</script>
