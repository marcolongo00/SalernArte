package model.dao;

import model.entity.AmministratoreBean;

import java.util.List;

public interface AmministratoreDAO {// NON SERVE
    //mancano tipo retrievebyrichieste forse eccetera, vedi mano mano
    //metterei un retrieveByID
    List<AmministratoreBean> doRetrieveAll();
    AmministratoreBean doRetrieveByEmail(String email);
    AmministratoreBean doRetrieveByEmailPassword(String email, String password);
    void doSave(AmministratoreBean utente);
    void doUpdate(AmministratoreBean utente);
    void doDelete(int idAdmin);
}
