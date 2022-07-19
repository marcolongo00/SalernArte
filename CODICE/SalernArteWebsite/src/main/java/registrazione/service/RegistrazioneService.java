package registrazione.service;

import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public interface RegistrazioneService {
    //tutti ut registrato
    UtenteRegistratoBean registrazioneUtente(String gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita);
    UtenteRegistratoBean registrazioneScolaresca(String email,String passwordNoHash,String istituto);
    UtenteRegistratoBean registrazioneOrganizzatore(String gender,String iban,String nome,String cognome,String email,String passwordNoHash,String biografia,String azienda,Date dataDiNascita);
}
