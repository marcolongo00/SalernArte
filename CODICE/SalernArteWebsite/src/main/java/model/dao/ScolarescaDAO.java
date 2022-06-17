package model.dao;

import model.entity.ScolarescaBean;

import java.util.List;

public interface ScolarescaDAO {
    public List<ScolarescaBean> doRetrieveAll();
    public ScolarescaBean doRetrieveByEmail(String email);
    public ScolarescaBean doRetrieveByEmailPassword(String email, String password);
    public void doSave(ScolarescaBean utente);
    public void doUpdate(ScolarescaBean utente);
    public void doDelete(int idScuola);

}
