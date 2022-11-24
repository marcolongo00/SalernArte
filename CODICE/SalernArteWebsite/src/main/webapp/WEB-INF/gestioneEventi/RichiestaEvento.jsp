
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Richiesta Evento</title>
</head>
<body>
<form action="gestione-eventi" method="post"  enctype="multipart/form-data">
    <div class="dati">
        <div id="range">
            <div class="dateContainer">
                <label for="inizio">Data inizio:</label>
                <input type="date" id="inizio"  name="dataInizio">
            </div>
            <div class="dateContainer">
                <label for="fine">Data fine:</label>
                <input type="date" id="fine"  name="dataFine">
            </div>
        </div>
    </div>
    <div class="flex dati">
        <div class="sinistra">
            <input type="text" id='validaTitle' name="nome"  placeholder="Nome">
            <div id="radioTypeEvento">
                <input type="radio" id="mostraType" name="tipoEvento" value="mostra">
                <label for="mostraType">Mostra</label>
                <input type="radio" id="teatroType" name="tipoEvento" value="teatro">
                <label for="teatroType">Teatro</label>
            </div>

            <input type="number"  name="numBiglietti" id='validaBiglietti' placeholder="Numero Biglietti">
            <input type="number" step="any" name="prezzo" id='validaPrezzo'  placeholder="Prezzo">

                <input type="file" id="fileM" name="path" accept="image/*" >

        </div>
        <div class="destra">
            <textarea  name="desc" rows='10' class='mDescr' id='validaDescr'  placeholder="Descrizione"></textarea>
            <input type="text" name="indirizzo" placeholder="indirizzo">
            <input type="text" name="sede" placeholder="sede">
        </div>
    </div>
    <input type="submit" id="insertMSubmit" name="inviaRichiestaEvento" value="PROCEDI">
</form>

</body>
</html>
