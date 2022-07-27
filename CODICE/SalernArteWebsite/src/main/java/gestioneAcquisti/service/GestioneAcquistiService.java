package gestioneAcquisti.service;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

public interface GestioneAcquistiService {
    CarrelloBean retrieveCarrelloUtente(UtenteRegistratoBean utente);
    boolean controlloElementiCarrello(CarrelloBean carrelloSessione,UtenteRegistratoBean utente);
    void svuotaCarrello(CarrelloBean carrello,UtenteRegistratoBean utente);
    void removeEventoFromCarrello(int idE,CarrelloBean carrello, UtenteRegistratoBean utente);
}
