package model.dao;

import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface UtenteRegistratoDAO {
    List<UtenteRegistratoBean> doRetrieveAll(); //abstract
    UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password); //abstract
    boolean doSave(UtenteRegistratoBean utente);//implementato poi sovrascritto dai figli
    void doUpdate(UtenteRegistratoBean utente);//implementato poi sovrascritto dai figli
    UtenteRegistratoBean doRetrieveByEmail(String email);
    void doDelete(int idUtente);//implementato

    UtenteRegistratoBean doRetrieveById(int id);
}
