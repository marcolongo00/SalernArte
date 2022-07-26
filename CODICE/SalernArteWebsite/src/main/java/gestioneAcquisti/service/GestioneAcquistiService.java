package gestioneAcquisti.service;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

public interface GestioneAcquistiService {
    boolean retrieveCarrelloAggiornato(UtenteRegistratoBean utente, CarrelloBean carrelloSessione);//ritorna un boolean che avvisa l'utente se ci sono elementi da rimuovere nel carrello
}
