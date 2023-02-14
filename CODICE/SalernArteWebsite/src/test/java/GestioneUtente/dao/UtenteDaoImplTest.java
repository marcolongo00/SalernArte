package GestioneUtente.dao;

import model.dao.UtenteDAOImpl;
import model.dao.UtenteRegistratoDAO;
import model.entity.UtenteBean;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import singleton.ConPool;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UtenteDaoImplTest {

    private static UtenteRegistratoDAO daoUsr;

    private static UtenteBean utente;
    private static String passwordNotHash;

    @BeforeClass
    public static void startUp(){
        daoUsr =new UtenteDAOImpl();
        passwordNotHash="plutoUtente";
        utente = new UtenteBean(0, "Lucia", "Martino","luciamartino@gmail.com", passwordNotHash, new Date(1996, 10, 16),false);

        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,"utente");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Utente error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            utente.setId(id);
            PreparedStatement ps2=con.prepareStatement("INSERT INTO Utente (id,nome,cognome) VALUES(?,?,?)");
            ps2.setInt(1,utente.getId());
            ps2.setString(2,utente.getNome());
            ps2.setString(3,utente.getCognome());
            if(ps2.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT utente error.");
            }

            con.close();
            ps.close();
            rs.close();
            ps2.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /*
    Metodo: doRetrieveAll(...)
    Classe: UtenteDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveAllTest(){ assertNotNull(daoUsr.doRetrieveAll());}

    @Test
    /*
    Metodo: doSave(...)
    Classe: UtenteDAOImpl
    Caso: Corretto
     */
    public void doSaveTest(){
        UtenteBean bean1 = new UtenteBean(0, "Lucia", "Martino","luciamartino@gmail.com", passwordNotHash, new Date(1996, 10, 16),false);
        boolean result = daoUsr.doSave(bean1);
        daoUsr.doDelete(bean1.getId());
        assertTrue(result);
    }
    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: UtenteDAOImpl
    Caso: Password errata
     */
    @Test
    public void doRetrieveByEmailPasswordError2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> daoUsr.doRetrieveByEmailPassword(utente.getEmail(), null));
        String message = "Errore Autenticazione utente";
        assertEquals(message, exception.getMessage());
    }
    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: UtenteDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmailPassword(){
        assertEquals(utente.getId(), daoUsr.doRetrieveByEmailPassword(utente.getEmail(), passwordNotHash));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: UtenteDAOImpl
    Caso: Email errata
     */
    @Test
    public void doRetrieveByEmailError()
    {
        assertNull(daoUsr.doRetrieveByEmail(null));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: UtenteDAOImpl
    Caso: Email inesistente
     */
    @Test
    public void doRetrieveByEmailError1()
    {
        assertNull(daoUsr.doRetrieveByEmail("emailfake"));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: UtenteDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmail()
    {
        assertEquals(utente.getId(), daoUsr.doRetrieveByEmail(utente.getEmail()).getId());
    }

    /*
    Metodo: doUpdate(...)
    Classe: UtenteDAOImpl
    Caso: IdUtente errato o inesistente
     */
    @Test
    public void doUpdateError()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> daoUsr.doUpdate(utente));
        String message = "UPDATE Utente error.";
        assertEquals(message,exception.getMessage());
    }
    /*
    Metodo: doUpdate(...)
    Classe: UtenteDAOImpl
    Caso: Corretto
     */
    @Test
    public void doUpdate()
    {
        utente.setEmail("newemail@gmail.com");
        assertTrue(daoUsr.doUpdate(utente));
    }

    @AfterClass
    public static void cleanUp()
    {
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id = ?");
            ps.setInt(1,utente.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE UTENTE FAILED");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
