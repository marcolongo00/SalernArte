package registrazione.service;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;
import java.util.Collection;

public interface RegistrazioneService {
    //tutti ut registrato
    UtenteRegistratoBean registrazioneUtente(int gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita);
    UtenteRegistratoBean registrazioneScolaresca(String email,String passwordNoHash,String istituto);
    UtenteRegistratoBean registrazioneOrganizzatore(int gender,String iban,String nome,String cognome,String email,String passwordNoHash,String biografia,String azienda,Date dataDiNascita);
    void salvaCarrelloSessione(UtenteRegistratoBean utenteRegistrato, CarrelloBean carrelloSessione);
    void applicaScontoScuola(CarrelloBean carrello);
}
