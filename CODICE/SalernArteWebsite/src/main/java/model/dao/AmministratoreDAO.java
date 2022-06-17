package model.dao;

import model.entity.AmministratoreBean;

import java.util.List;

public interface AmministratoreDAO {
    //mancano tipo retrievebyrichieste forse eccetera, vedi mano mano
    //metterei un retrieveByID
    public List<AmministratoreBean> doRetrieveAll();
    public AmministratoreBean doRetrieveByEmail(String email);
    public AmministratoreBean doRetrieveByEmailPassword(String email, String password);
    public void doSave(AmministratoreBean utente);
    public void doUpdate(AmministratoreBean utente);
    public void doDelete(int idAdmin);
}
