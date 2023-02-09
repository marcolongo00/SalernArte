package model.dao;


import model.entity.UtenteBean;

import java.util.List;

public interface UtenteDAO { // NON SERVE
    List<UtenteBean> doRetrieveAll();
    UtenteBean doRetrieveByEmailPassword(String email, String password);
    boolean doSave(UtenteBean utente);
    void doUpdate(UtenteBean utente);
    UtenteBean doRetrieveByEmail(String email);
    void doDelete(int idUtente);

}
