package autenticazione.service;

import model.entity.AcquistoBean;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;
import java.util.List;


public interface AutenticazioneService {
    UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente);
    List<UtenteRegistratoBean> retrieveAllUtentiSistema(UtenteRegistratoBean utenteLoggato );
    List<AcquistoBean> retrieveListaOrdiniUtente(UtenteRegistratoBean utenteLoggato, int idUtente);
    UtenteRegistratoBean updateUtente(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender);
    UtenteRegistratoBean updateAmministratore(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome);
    UtenteRegistratoBean updateScolaresca(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String istituto);
    UtenteRegistratoBean updateOrganizzatore(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender, String biografia, String iban);
    void recuperaPassword(String emailTo);
    void eliminaProfiloUtente(UtenteRegistratoBean utenteLoggato);
    CarrelloBean mergeCarrelloSessioneAndCarrelloDBAfterLogin(UtenteRegistratoBean utenteRegistratoBean, CarrelloBean carrelloSessione);
   }
