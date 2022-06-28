package gestioneEventi.service;

import javax.servlet.http.Part;
import java.sql.Date;

public interface GestioneEventiService {
    void richiediInserimentoEvento(int idOrganizzatore, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede);
}
