package GestioneUtente.dao;

import model.dao.AmministratoreDAOImpl;
import model.dao.UtenteRegistratoDAO;
import model.entity.AmministratoreBean;
import model.entity.UtenteRegistratoBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import singleton.ConPool;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Implementa il testing di unità per la classe
 *               AmministratoreDAOImpl.
 * i nomi dei metodi di test faranno riferimento alla firma
 * dell'operazione di riferimento nella classe che stiamo testando
 *
 * esempio:
 *          firma metodo: boolean doSave(Object obj)
 *          metodo di test: void doSaveTest()
 * @author Alessia Della Pepa
 */

public class AmministratoreDaoImplTest {
    private static UtenteRegistratoDAO daoAmm;

    private static AmministratoreBean utente;
    private static String passwordNotHash;
    @BeforeClass
    public static void startUp(){
        daoAmm=new AmministratoreDAOImpl();
        passwordNotHash="plutoAdmin";
        utente=new AmministratoreBean("pluto","cognome","plutoadmin@example.com",passwordNotHash,false);

        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,"amministratore");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Utente error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            utente.setId(id);
             PreparedStatement ps2=con.prepareStatement("INSERT INTO Amministratore (id,nome,cognome) VALUES(?,?,?)");
            ps2.setInt(1,utente.getId());
            ps2.setString(2,utente.getNome());
            ps2.setString(3,utente.getCognome());
            if(ps2.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT amministratore error.");
            }

            con.close();
            ps.close();
            rs.close();
            ps2.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    /* Questo metodo testa l'operazione :
     *           List<UtenteRegistratoBean> doRetrieveAll();
     *  della classe:
     *           AmministratoreDAOImpl
     * */
     @Test
    public  void doRetrieveAllTest(){
        List<UtenteRegistratoBean> result=daoAmm.doRetrieveAll();
        assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           UtenteRegistratoBean doRetrieveByEmail(String email)
     *  della classe:
     *           AmministratoreDAOImpl
     * */
    @Test
    public  void doRetrieveByEmailTest(){
        AmministratoreBean result=(AmministratoreBean) daoAmm.doRetrieveByEmail(utente.getEmail());
        assertEquals(utente.getId(),result.getId());
    }
    /* Questo metodo testa l'operazione :
     *           UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password);
     *  della classe:
     *           AmministratoreDAOImpl
     * */
    @Test
    public  void doRetrieveByEmailPasswordTest(){
        AmministratoreBean result=(AmministratoreBean) daoAmm.doRetrieveByEmailPassword(utente.getEmail(), passwordNotHash);
        assertEquals(utente.getId(),result.getId());
    }
    /* Questo metodo testa l'operazione :
     *           boolean doSave(UtenteRegistratoBean utente);
     *  della classe:
     *           AmministratoreDAOImpl
     * */
    @Test
    public  void doSaveTest(){
        AmministratoreBean nuovo=new AmministratoreBean("pippo","cognome","pippoAdmin@hotmail.it","pippoAdmin",false);
       boolean result=daoAmm.doSave(nuovo);
       daoAmm.doDelete(nuovo.getId());
        assertTrue(result);

    }
    /* Questo metodo testa l'operazione :
     *           boolean doUpdate(UtenteRegistratoBean utente)
     *  della classe:
     *           AmministratoreDAOImpl
     * */
    @Test
    public  void doUpdateTest(){
        utente.setEmail("nuovaEmailPippo@example.com");
        assertTrue(daoAmm.doUpdate(utente));
    }

    @AfterClass
    public static void cleanUp(){
        daoAmm=null;
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,utente.getId());
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE UTENTE ERROR");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
