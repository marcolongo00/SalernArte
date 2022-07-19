package model.dao;

import model.entity.ScolarescaBean;

import java.util.List;

public interface ScolarescaDAO {// NON SERVE
    List<ScolarescaBean> doRetrieveAll();
    ScolarescaBean doRetrieveByEmail(String email);
    ScolarescaBean doRetrieveByEmailPassword(String email, String password);
    void doSave(ScolarescaBean utente);
    void doUpdate(ScolarescaBean utente);
    void doDelete(int idScuola);

}
