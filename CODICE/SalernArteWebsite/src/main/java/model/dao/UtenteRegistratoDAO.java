package model.dao;

import model.entity.UtenteRegistratoBean;

import java.util.List;

public interface UtenteRegistratoDAO {
    List<UtenteRegistratoBean> doRetrieveAll(); //abstract
    UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password); //abstract
    boolean doSave(UtenteRegistratoBean utente);//implementato poi sovrascritto dai figli
    boolean doUpdate(UtenteRegistratoBean utente);//implementato poi sovrascritto dai figli
    UtenteRegistratoBean doRetrieveByEmail(String email);
    boolean doDelete(int idUtente);//implementato

    UtenteRegistratoBean doRetrieveById(int id);
}
