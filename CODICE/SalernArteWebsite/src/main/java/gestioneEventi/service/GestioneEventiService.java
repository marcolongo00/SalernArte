package gestioneEventi.service;

import model.entity.EventoBean;

import javax.servlet.http.Part;
import java.sql.Date;
import java.util.List;

public interface GestioneEventiService {
    void richiediInserimentoEvento(int idOrganizzatore, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede);
    void attivaEvento(int idEvento,String tipoUtente);
    void rimuoviEvento(int idEvento,String tipoUtente);
    EventoBean retriveEventoById(int idEvento);
    List<EventoBean> retriveAllRichiesteEventi(String tipoUtente);
}
