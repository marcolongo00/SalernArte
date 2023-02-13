package GestioneUtente.dao;

import model.dao.OrganizzatoreDAO;

import model.entity.OrganizzatoreBean;

import org.junit.Test;

import java.util.List;

public class OrganizzatoreDaoImplTest {

    private static OrganizzatoreBean org;

    @Test
    public void doSaveTest() {
        OrganizzatoreDAO dao = new OrganizzatoreDAO() {
            @Override
            public List<OrganizzatoreBean> doRetrieveAll() {
                return null;
            }

            @Override
            public OrganizzatoreBean doRetrieveByEmailPassword(String email, String password) {
                return null;
            }

            @Override
            public void doSave(OrganizzatoreBean utente) {

            }

            @Override
            public void doUpdate(OrganizzatoreBean utente) {

            }

            @Override
            public OrganizzatoreBean doRetrieveByEmail(String email) {
                return null;
            }

            @Override
            public void doDelete(int idUtente) {

            }

        };
    }
}