package model.dao;

import model.entity.BigliettoBean;

import java.util.List;

public interface BigliettoDAO {
    List<BigliettoBean> doRetrieveAllByEvento(int idEvento);
    void sellBiglietto(int idEvento, int quantita, int numFattura);
    void updatePrezzoBigliettoEvento(int idEvento, double costo);
    void doSave(int idEvento, double prezzo);
    double doRetrievePrezzoBigliettoByEvento(int idEvento);

}
