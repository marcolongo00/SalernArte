package autenticazione.service;

import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public interface AutenticazioneService {
    //non so se creare l'utente generico per davvero
    Object loginUtente(String email, String passwordNotHash, String tipoUtente);
    Object registrazioneUtente(String tipoUtente, String email, String password, String nome, String cognome, String dataDiNascita,String gender,String istituto,String biografia,String azienda, String iban);
}
