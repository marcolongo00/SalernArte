package model.dao;

import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface OrganizzatoreDAO {// NON SERVE
    List<OrganizzatoreBean> doRetrieveAll();
    OrganizzatoreBean doRetrieveByEmailPassword(String email,String password);
    void doSave(OrganizzatoreBean utente);
    void doUpdate(OrganizzatoreBean utente);
    OrganizzatoreBean doRetrieveByEmail(String email);
    void doDelete(int idUtente);
}
