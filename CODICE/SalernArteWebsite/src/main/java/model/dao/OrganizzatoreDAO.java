package model.dao;

import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface OrganizzatoreDAO {
    List<OrganizzatoreBean> doRetrieveAll();
    OrganizzatoreBean doRetrieveByEmailPassword(String email,String password); //per il login quindi cambia con email
    OrganizzatoreBean doSave(OrganizzatoreBean utente);
    void doUpdate(OrganizzatoreBean utente);
    OrganizzatoreBean doRetrieveByEmail(String email);
    void doDelete(int idUtente);
}
