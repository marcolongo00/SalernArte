package model.dao;

import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface OrganizzatoreDAO {
    public List<OrganizzatoreBean> doRetrieveAll();
    public OrganizzatoreBean doRetrieveByEmailPassword(String email,String password); //per il login quindi cambia con email
    public void doSave(OrganizzatoreBean utente);
    public void doUpdate(OrganizzatoreBean utente);
    public OrganizzatoreBean doRetrieveByEmail(String email);
    public void doDelete(int idUtente);
}
