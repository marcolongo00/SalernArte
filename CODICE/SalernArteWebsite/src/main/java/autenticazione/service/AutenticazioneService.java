package autenticazione.service;

import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public interface AutenticazioneService {
    UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente);
    //Object loginUtente(String email, String passwordNotHash, String tipoUtente);
   }
