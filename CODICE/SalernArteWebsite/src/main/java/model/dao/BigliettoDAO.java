package model.dao;

import model.entity.BigliettoBean;
import model.entity.EventoBean;

import java.util.List;

public interface BigliettoDAO {
    List<BigliettoBean> doRetrieveAllByEvento(int idEvento);
    List<BigliettoBean> doRetrieveAllNonAcquistati();
    List<BigliettoBean> doRetrieveAllAcquistati();
    void sellBiglietto(int idEvento, int quantita, int numFattura);
    void updatePrezzoBigliettoEvento(int idEvento, double costo);
    void doSave(int idEvento, double prezzo);
    double doRetrievePrezzoBigliettoByEvento(int idEvento);
    double doRetrievePrezzoBiglByRichiestaModifica(int idEventoPostMod);
    void doUpdateBigliettiModificaEvento(EventoBean eventoPreModifica, EventoBean eventoModifica);
    void doDelete(int idEvento, int idBiglietto);


}
