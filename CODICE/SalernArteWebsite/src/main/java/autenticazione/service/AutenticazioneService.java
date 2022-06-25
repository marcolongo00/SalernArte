package autenticazione.service;

import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public interface AutenticazioneService {
    Object loginUtente(String email, String passwordNotHash, String tipoUtente);
   }
