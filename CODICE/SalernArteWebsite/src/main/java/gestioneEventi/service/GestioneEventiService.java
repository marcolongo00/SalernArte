package gestioneEventi.service;

import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.http.Part;
import java.sql.Date;
import java.util.List;

public interface GestioneEventiService {
    boolean richiediInserimentoEvento(int idOrganizzatore, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede);

    boolean richiediModificaEvento(int idEvDaModificare, UtenteRegistratoBean utenteLoggato, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede);

    boolean attivaEvento(int idEvento, String tipoUtente);
    boolean rimuoviEvento(int idEvento, UtenteRegistratoBean utente);
    EventoBean retrieveEventoById(int idEvento);
    OrganizzatoreBean retriveBioOrganizzatore(int idOrg);
    void checkQuantitaCarrello(EventoBean evento, CarrelloBean carrelloSessione);
    boolean checkScaduta(EventoBean evento);
    double getPrezzoEvento(int idEvento);
    List<EventoBean> retrieveRichiesteInserimento(String tipoUtente);
    List<EventoBean> retrieveRichiesteModifica(String tipoUtente);
    boolean accettaModifica(int idEvento, String tipoUtente);
    boolean rifiutaModifica(int idEvento, String tipoUtente);
    List<EventoBean> retrieveEventiOrganizzatore(UtenteRegistratoBean utente);
    List<EventoBean> ricercaEventiByNomeOrDescrizione(String query);
    List<EventoBean> retrieveEventiByTipo(String tipoEvento);
    List<EventoBean> retrieveTuttiEventiNonScadutiAttivi();
}
