package model.dao;

import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface UtenteRegistratoDAO {
    List<UtenteRegistratoBean> doRetrieveAll();
    UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password); //per il login quindi cambia con email
    UtenteRegistratoBean doSave(UtenteRegistratoBean utente);
    void doUpdate(UtenteRegistratoBean utente);
    UtenteRegistratoBean doRetrieveByEmail(String email);
    void doDelete(int idUtente);

}
