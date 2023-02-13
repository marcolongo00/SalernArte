package model.dao;

import model.entity.AmministratoreBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface AmministratoreDAO {
    List<UtenteRegistratoBean> doRetrieveAll();
    UtenteRegistratoBean doRetrieveByEmail(String email);
    UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password);
    boolean doSave(UtenteRegistratoBean utente);
    boolean doUpdate(UtenteRegistratoBean utente);
    boolean doDelete(int idAdmin);
}
