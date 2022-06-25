package registrazione.service;

import autenticazione.service.AutenticazioneServiceImpl;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public interface RegistrazioneService {

    UtenteRegistratoBean registrazioneUtente(String gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita);
    ScolarescaBean registrazioneScolaresca(String email,String passwordNoHash,String istituto);
    OrganizzatoreBean registrazioneOrganizzatore(String gender,String iban,String nome,String cognome,String email,String passwordNoHash,String biografia,String azienda,Date dataDiNascita);
}
