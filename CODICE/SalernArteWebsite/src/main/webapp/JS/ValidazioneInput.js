

//da rifare

var borderOk='2px solid #29487d';
var borderNo='2px solid #f00';
var nome=false;
var cognome=false;
var email=false;
var username=false;
var password=false;
var passwordAtt=true;
var passwordNUOVA=true;

function validaNome(nomeForm,nomeSubmit) {
   var input=document.forms[nomeForm]['nome'];
    if(input.value.trim().length>0 && input.value.match(/^[ a-zA-Z\u00C0-\u00ff]+$/)){
        nome=true;
       input.style.border=borderOk;
    }else{
        input.style.border=borderNo;
        nome=false;
    }
    cambiaStato(nomeSubmit);
}


function validaCognome(nomeForm,nomeSubmit) {
    var input=document.forms[nomeForm]['cognome'];
    if(input.value.trim().length>0 && input.value.match(/^[ a-zA-Z\u00C0-\u00ff]+$/)){
        cognome=true;
        input.style.border=borderOk;
    }else{
        input.style.border=borderNo;
        cognome=false;
    }
    cambiaStato(nomeSubmit);
}


function validaEmail(nomeForm,nomeSubmit) {
    var input=document.forms[nomeForm]['email'];
    if(input.value.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w+)+$/) && input.value.trim().length<=100){
        //verifico se esite un utente con questa email
        var xmlHttpReq=new XMLHttpRequest();
        xmlHttpReq.onreadystatechange= function(){
            if (this.readyState == 4 && this.status == 200
                && this.responseText == '<ok/>') {
                email=true;
                input.style.border=borderOk;
            }else{
                input.style.border=borderNo;
                email=false;
            }
            cambiaStato(nomeSubmit);
        }
        xmlHttpReq.open("GET","VerificaEmail?email=" + encodeURIComponent(input.value),true);
        xmlHttpReq.send();
    }else{
        input.style.border=borderNo;
        email=false;
        cambiaStato(nomeSubmit);
    }
}




function validaUsername(nomeForm,nomeSubmit) {
    var input=document.forms[nomeForm]['username'];
    if(input.value.length>=2 && input.value.match(/^[0-9a-zA-Z]+$/) && input.value.length<=50){
        //verifico se esiste già un utente con questo username
        var xmlHttpReq = new XMLHttpRequest();
        xmlHttpReq.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200
                && this.responseText == '<ok/>') {
                username=true;
                input.style.border=borderOk;
            }else{
                input.style.border=borderNo;
                username=false;
            }
            cambiaStato(nomeSubmit);
        }
        xmlHttpReq.open("GET", "VerificaUsername?username="
            + encodeURIComponent(input.value), true);
        xmlHttpReq.send();

    }else{
        input.style.border=borderNo;
        username=false;
        cambiaStato(nomeSubmit);
    }

}

function validaPassword(nomeForm,nomeSubmit) {
    var inputpw=document.forms[nomeForm]['password'];
    var passConf=document.forms[nomeForm]['passwordConferma'];
    var pass=inputpw.value;
    if(pass.length>=6 && pass.toLowerCase()!=pass && pass.toUpperCase()!=pass && /[0-9]/.test(pass)){
        inputpw.style.border=borderOk;
        if(pass==passConf.value){
            passConf.style.border=borderOk;
            password=true;
        }else{
            passConf.style.border=borderNo;
            password=false;
        }
    }else{
        inputpw.style.border=borderNo;
        passConf.style.border=borderNo;
        password=false;
    }
    cambiaStato(nomeSubmit);
}


function validaPasswordAttuale(nomeForm,nomeSubmit) {
    var input=document.forms[nomeForm]['passwordAtt'];
        //verifico se la password inserita è davvero quella attuale
        var xmlHttpReq = new XMLHttpRequest();
        xmlHttpReq.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200
                && this.responseText == '<ok/>') {
                passwordAtt=true;
                input.style.border=borderOk;
            }else{
                input.style.border=borderNo;
                passwordAtt=false;
            }
            cambiaStato(nomeSubmit);
        }
        xmlHttpReq.open("GET", "VerificaPassAttuale?passwordAtt="
            + encodeURIComponent(input.value), true);
        xmlHttpReq.send();



    }

function validaPasswordNUOVA(nomeForm,nomeSubmit) {
    var inputpw=document.forms[nomeForm]['passwordNUOVA'];
    var pass=inputpw.value;
    if(pass.length>=6 && pass.toLowerCase()!=pass && pass.toUpperCase()!=pass && /[0-9]/.test(pass)){
        inputpw.style.border=borderOk;
            passwordNUOVA=true;
    }else{
        inputpw.style.border=borderNo;
        passwordNUOVA=false;
    }
    cambiaStato(nomeSubmit);
}

function checkFormAddQuadro() {
    return ($("#titoloQuadro").val().length > 0 && $("#prezzoQuadro").val().length>0 && $.isNumeric($("#prezzoQuadro").val()) && $("#prezzoQuadro").val()>0 && $("#descrizioneQ").val().length > 0 && $("input:file").get(0).files.length!=0)
}
function checkFormModQuadro() {
    return ($("#titoloModQ").val().length > 0 && $("#prezzoModQ").val().length>0 && $.isNumeric($("#prezzoModQ").val())  && $("#prezzoModQ").val()>0 && $("#descrizioneModQ").val().trim().length > 0)
}

function checkFormAddCategoria() {
    return ($("#nomecat").val().length > 0 && $("#descrizioneC").val().length>0 )
}

function checkForm() {
    if(!($("#emailCont").val().length > 0 && $("#emailCont").val().match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w+)+$/))) {
        return false;
    }
    return ( $("#oggetto").val().length > 0 && $("#messaggio2").val().length > 0);
}

var text=false;
var numBiglietti=false;
var prezzo=false;
var file=false;
var range=false;

function validaNotEmpty(nomeSubmit) {
    var tit=$("input#validaTitle").val();
    var de=$("textarea#validaDescr").val();
    var first,second;
    if(tit.trim().length>0){
        first=true;
        $("input#validaTitle").css("border",borderOk);
    }else{
        first=false;
        $("input#validaTitle").css("border",borderNo);
    }
    if(de.trim().length>0){
        second=true;
        $("textarea#validaDescr").css("border",borderOk);
    }else{
        second=false;
        $("textarea#validaDescr").css("border",borderNo);
    }
    if(first && second){
        text=true;
    }else{
        text=false;
    }
    cambiaStato(nomeSubmit);
}
function validaRangeDate2(nomeSubmit){
    var dataI=$("#inizio").val();
    var dataF=$("#fine").val();
    $(".fluidQuad").text("");
    if(Date.parse(dataI) && Date.parse(dataF) && new Date(dataI).getTime() < new Date(dataF).getTime()){
        var url="retrieve-quadri?inizio="+dataI+"&fine="+dataF;
            $.getJSON(url,function (data) {
                console.log(data);
                if(data.length >0){
                    range=true;
                    for(let i=0;i<data.length;i++){
                        var block="<div class=singleQuad> <input type='checkbox' name='quadEsposti' value="+data[i].cod+">"+
                            "<img src="+data[i].path+" alt='imgQuad' width='150' height='150' > </div>";
                        //controllare anche che almeno una casella è checked
                        $(".fluidQuad").append(block);
                    }
                    $("#message").text("Seleziona almeno un quadro da esporre:");
                }else{
                    range=false;
                    $("#message").text("Nessun quadro disponibile");
                }
                cambiaStato(nomeSubmit);
            });
    }else{
    $("#message").text("Nessun quadro disponibile");
        range=false;
    }
    cambiaStato(nomeSubmit);
}
function validaBigliettiAndPrezzo(nomeSubmit) {
    var biglietti=$("#validaBiglietti");
    var prezzoBi= $("#validaPrezzo");

    if($.isNumeric(biglietti.val()) && biglietti.val()>0){
        numBiglietti=true;
        biglietti.css("border",borderOk);
    }
    else{
        numBiglietti=false;
        biglietti.css("border",borderNo);
    }
    if( $.isNumeric(prezzoBi.val()) && prezzoBi.val()>0){
        prezzo=true;
        prezzoBi.css("border",borderOk);
    }
    else{
        prezzo=false;
        prezzoBi.css("border",borderNo);
    }

    cambiaStato(nomeSubmit);
}
function validaFile(nomeSubmit) {
    if($("input:file").get(0).files.length===0){
        file=false;
        $(".file").css("border",borderNo);
    }else{
        file=true;
        $(".file").css("border",borderOk);
    }

cambiaStato(nomeSubmit);
}


function cambiaStato(nomeSubmit){
    if(nomeSubmit=="registrami" && nome && cognome && email && username && password){
        document.getElementById(nomeSubmit).disabled=false;
        document.getElementById('messaggio').innerHTML="";
    }else
            if(nomeSubmit=="modUtente" && nome && cognome && email && passwordAtt && passwordNUOVA){
                document.getElementById(nomeSubmit).disabled=false;
                document.getElementById('messaggio').innerHTML="";
            }
        else
            if(nomeSubmit=="bottoneinvio" && username && messaggio && oggetto){
                document.getElementById(nomeSubmit).disabled=false;
                document.getElementById('messaggio').innerHTML="";
            }
            else
                if(nomeSubmit=="modConferma"&& text){
                    document.getElementById(nomeSubmit).disabled=false;
                    document.getElementById('messaggio').innerHTML="";
            }else
                if(nomeSubmit=="insertMSubmit" && text && range && numBiglietti && prezzo && file ) {
                    document.getElementById(nomeSubmit).disabled=false;
                    document.getElementById('messaggio').innerHTML="";
                }
            else {
                document.getElementById(nomeSubmit).disabled = true;
                document.getElementById('messaggio').innerHTML = "Verifica che tutti i campi siano blu.";
            }
        }

function validaPrezzo() {
    var prezzo= $("#prezzoQuadro");

    if( $.isNumeric(prezzo.val())){
        prezzo.css("border","1px solid rgba(160, 160, 159, 1)");
    }
    else{
        prezzo.css("border",borderNo);
    }
}
function validaFotoQuadro() {
    var foto= $("#fileQ");
    if($("input:file").get(0).files.length===0){
        foto.css("border",borderNo);
    }else{
        foto.css("border","1px solid rgba(160, 160, 159, 1)");
    }

}
//PER CONTATTI

var borderOkCont='1px solid rgba(160, 160, 159, 1)';

function validaOggettoAndTextmessage(nomeSubmit) {
    var input=document.forms['formMex'][nomeSubmit];
    if(input.value.trim().length>0 && input.value!=null){
        input.style.border=borderOkCont;
        $("#verificacampo").text("");
    }else{
        $("#verificacampo").text("Please, fill out all fields to proceed.");
    }
}



