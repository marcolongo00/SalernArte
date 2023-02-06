package model.dao;

import model.entity.BigliettoBean;
import model.entity.EventoBean;

import java.util.List;

public interface BigliettoDAO {
    List<BigliettoBean> doRetrieveAllByEvento(int idEvento);
    List<BigliettoBean> doRetrieveAllNonAcquistati(int idEvento);
    List<BigliettoBean> doRetrieveAllAcquistati(int idEvento);
    boolean sellBiglietto(int idEvento, int quantita, int numFattura);
    boolean updatePrezzoBigliettoEvento(int idEvento, double costo);
    boolean doSave(int idEvento, double prezzo);
    double doRetrievePrezzoBigliettoByEvento(int idEvento);
    double doRetrievePrezzoBiglByRichiestaModifica(int idEventoPostMod);
    boolean doUpdateBigliettiModificaEvento(EventoBean eventoPreModifica, EventoBean eventoModifica);
    boolean doDelete(int idEvento, int idBiglietto);


}
