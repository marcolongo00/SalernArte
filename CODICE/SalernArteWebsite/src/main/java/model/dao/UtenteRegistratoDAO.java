package model.dao;

import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface UtenteRegistratoDAO {
    public List<UtenteRegistratoBean> doRetrieveAll();
    public UtenteRegistratoBean doRetrieveByEmailPassword(String email,String password); //per il login quindi cambia con email
    public void doSave(UtenteRegistratoBean utente);
    public void doUpdate(UtenteRegistratoBean utente);
    public UtenteRegistratoBean doRetrieveByEmail(String email);
    public void doDelete(UtenteRegistratoBean utente);

}
