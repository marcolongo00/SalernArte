package gestioneAcquisti.service;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

public interface GestioneAcquistiService {
    CarrelloBean retrieveCarrelloUtente(UtenteRegistratoBean utente);
    boolean controlloElementiCarrello(CarrelloBean carrelloSessione,UtenteRegistratoBean utente);
    boolean controlloEventiNonAttivi(CarrelloBean carrello);
    void svuotaCarrello(CarrelloBean carrello,UtenteRegistratoBean utente);
    boolean removeEventoFromCarrello(int idE,CarrelloBean carrello, UtenteRegistratoBean utente);
    CarrelloBean aggiungiAlCarrello(int idE,int quantita, CarrelloBean carrelloSessione,UtenteRegistratoBean utente);
    boolean updateQuantitaCarrello(int idE,int quantita, CarrelloBean carrelloSessione, UtenteRegistratoBean utente);
    boolean acquistaProdotti(CarrelloBean carrelloSessione, UtenteRegistratoBean utente);
}
