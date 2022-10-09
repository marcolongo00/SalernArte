
<div id="logon" class="modal">
    <!-- il form class="logon-content" con la X per chiudere il login -->
    <div class="modalContainer">
        <div class="closeLogin">
            <span class="close" onclick="closeLoginRegistrazione()" title="Close Logon">&times;</span>
        </div>
        <form  action="autenticazione-controller" method="post" name="LoginForm">
            <!-- il div con i campi da compilare-->
            <div class="campi tipo">
                <p class="titolo"> Login: </p>
                <a id="Linkreg" > Non sei ancora iscritto? </a> <br>

                <label for="email"><b>email: </b></label> <br>
                <input type="text" id="email" name="email" required> <br>
                <label for="password"><b>Password: </b></label> <br>
                <input type="password" id="password" name="password"  required> <br>

                <input type="submit" id="acc" name="Accedi" value="Accedi"> <br>
            </div>
        </form>
    </div>
</div>