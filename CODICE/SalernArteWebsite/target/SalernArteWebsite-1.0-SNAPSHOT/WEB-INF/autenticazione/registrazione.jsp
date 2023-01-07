<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>
<div class="modal" id="modalRegistrazione">
    <div class="modalContainer" id="signInContainer">
        <div class="closeLogin">
            <span class="close" onclick="closeLoginRegistrazione()" title="Close Logon">&times;</span>
        </div>
        <p class="titolo" style="margin-bottom:5px">Sign In:</p>
        <div class="tipo">
            <a id="Linklog" > Sei già iscritto? </a> <br> <!-- attenzione ai link non so se vanno-->
        </div>

        <form name="reg" action="registrazione-controller" method="post">
            <div class="flex dati">
                <div >
                    <div class="radioTipoUtente">
                        <input type="radio" id="Utente" name="tipoUtente" onclick="showDatiCorrettiPerUtente()"  value="utente" checked>
                        <label for="Utente"><b>Utente</b></label>

                        <input type="radio" id="Scolaresca" name="tipoUtente" onclick="showDatiCorrettiPerUtente()" value="scolaresca">
                        <label for="Scolaresca"><b>Scolaresca</b></label>

                        <input type="radio" id="Organizzatore" name="tipoUtente" onclick="showDatiCorrettiPerUtente()" value="organizzatore">
                        <label for="Organizzatore"><b>Organizzatore</b></label>
                    </div>
                    <div class="all">
                        <input type="email" name="email" id="email" oninput="validaEmail('reg','registrami')" placeholder="Email" required>
                        <p style="text-align: center">*La password deve contenere minimo 6 caratteri, una lettera maiuscola,minuscola e un numero.</p>

                            <input type="password" id="password" name="password" oninput="validaPassword('reg','registrami')"placeholder="Password" required>
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
                            <input type="radio" id="uomo" name="gender"  value="0" required>
                            <label for="uomo"><b>Uomo</b></label>

                            <input type="radio" id="donna" name="gender" value="1">
                            <label for="donna"><b>Donna</b></label>

                            <input type="radio" id="altro" name="gender" value="2">
                            <label for="altro"><b>Altro</b></label>
                        </div>
                    </div>
                    <div class="scolaresca">
                        <input type="text" id="istituto" name="istituto"  placeholder="Istituto" >
                    </div>
                    <div class="organizzatore">
                        <input type="text" id="biografia" name="biografia"  placeholder="biografia" >
                        <input type="text" id="iban" name="iban"  placeholder="iban" >
                    </div>
                    <div class="regolamento">
                        Cliccando sul pulsante REGISTRA confermi di aver accettato le nostre norme sulla privacy
                    </div>
                </div>
            </div>
            <input type="submit" id="registrami"  name="registrazione" value="Registra">
            <p class="tipo" id="messaggio"></p>

        </form>
    </div>
</div>

<script>
    function showDatiCorrettiPerUtente(){
        var checked=document.querySelector('input[name="tipoUtente"]:checked').value;
        $(".utenteAndOrganizzatore").hide();
        $(".organizzatore").hide();
        $(".scolaresca").hide();

        //document.getElementById("provaJS").innerHTML
        //  = "hai selezionato: "+document.querySelector('input[name="tipoUtente"]:checked').value;

        if(checked=='utente'){
            $(".utenteAndOrganizzatore").show();
        }else if(checked=='scolaresca'){
            $(".scolaresca").show();

        }else if(checked=='organizzatore'){
            $(".utenteAndOrganizzatore").show();
            $(".organizzatore").show();
        }

    }
    $(document).ready(function () {
        showDatiCorrettiPerUtente();
    });
</script>
