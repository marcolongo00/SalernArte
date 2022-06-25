package gestioneEventi.service;

import java.sql.Date;

public interface GestioneEventiService {
    void richiediInserimentoEvento(int idOrganizzatore,String nome, String tipoEvento, String descrizione, String path, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede);
}
