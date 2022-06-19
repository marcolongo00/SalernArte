package model.dao;

import model.entity.BigliettoBean;

import java.util.List;

public interface BigliettoDAO {
    public List<BigliettoBean> doRetrieveAllByEvento(int idEvento);
    public void sellBiglietto(int idEvento, int quantita, int numFattura);
    public void updatePrezzoBigliettoEvento(int idEvento, double costo);
    public void doSave(int idEvento, double prezzo);

}
