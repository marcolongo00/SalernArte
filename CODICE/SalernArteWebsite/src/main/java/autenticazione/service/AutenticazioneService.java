package autenticazione.service;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;


public interface AutenticazioneService {
    UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente);
    List<UtenteRegistratoBean> retriveAllUtentiSistema(String tipoUtente);
    void updateUtente(UtenteRegistratoBean utente);
    CarrelloBean mergeCarrelloSessioneAndCarrelloDBAfterLogin(UtenteRegistratoBean utenteRegistratoBean, CarrelloBean carrelloSessione);
   }
