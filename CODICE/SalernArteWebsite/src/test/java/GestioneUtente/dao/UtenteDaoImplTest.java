package GestioneUtente.dao;


import model.dao.UtenteDAO;

import model.entity.UtenteBean;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;


public class UtenteDaoImplTest {

    public static UtenteBean utenteTest;
    private static UtenteBean org;


    public static UtenteBean getUtenteTest() {
        return utenteTest;
    }

    public static void setUtenteTest(UtenteBean utenteTest) {
        UtenteDaoImplTest.utenteTest = utenteTest;
    }
    @BeforeClass
    public static void startUp() {
        org = new UtenteBean();

    }


    @Test
    public void doSaveTest(){
        UtenteDAO dao= new UtenteDAO() {
            @Override
            public List<UtenteBean> doRetrieveAll() {
                return null;
            }

            @Override
            public UtenteBean doRetrieveByEmailPassword(String email, String password) {
                return null;
            }

            @Override
            public boolean doSave(UtenteBean utenteRegistrato) {
                return false;
            };

            @Override
            public void doUpdate(UtenteBean utenteRegistrato) {

            }

            @Override
            public UtenteBean doRetrieveByEmail(String email) {
                return null;
            }

            @Override
            public void doDelete(int idUtenteRegistrato) {

            }
        };

    }



}
