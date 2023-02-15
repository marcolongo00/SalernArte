package GestioneUtente.dao;

import model.dao.ScolarescaDAOImpl;
import model.dao.UtenteRegistratoDAO;
import model.entity.AmministratoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;
import org.junit.*;
import singleton.ConPool;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScolarescaDaoImplTest {
    private static ScolarescaBean scolaresca, bean;
    private static UtenteRegistratoDAO dao = new ScolarescaDAOImpl();

    @BeforeClass
    public static void setUp()
    {
        scolaresca = new ScolarescaBean("emailscolaresca@gmail.com", "passwordScol", "ISS Gian Camillo Glorioso", false);

        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps =con.prepareStatement("INSERT INTO UtenteRegistrato(email, passwordHash, tipoUtente) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,scolaresca.getEmail());
            ps.setString(2, scolaresca.getPasswordHash());
            ps.setString(3,"scolaresca");

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT USER SCOLARESCA FAILED");

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            scolaresca.setId(rs.getInt(1));

            PreparedStatement ps1 =con.prepareStatement("INSERT INTO Scolaresca(id,istituto) VALUES (?,?)");
            ps1.setInt(1,scolaresca.getId());
            ps1.setString(2,scolaresca.getIstituto());

            if(ps1.executeUpdate() != 1)
                throw new RuntimeException("INSERT SCOLARESCA FAILED");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bean = new ScolarescaBean(-1, scolaresca.getEmail(), scolaresca.getPasswordHash(), scolaresca.getIstituto(), false);
    }

    /*
    Metodo: doRetrieveAll(...)
    Classe: ScolarescaDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveAllTest()
    { List<UtenteRegistratoBean> result=dao.doRetrieveAll();
        assertFalse(result.isEmpty());
    }


    /*
    Metodo: doSave(...)
    Classe: ScolarescaDAOImpl
    Caso: Corretto
     */
    @Test
    public void doSaveTest()
    {
        ScolarescaBean bean1 = new ScolarescaBean("email@prova", "password", "istituto", false);
        boolean result = dao.doSave(bean1);
        dao.doDelete(bean1.getId());
        assertTrue(result);
    }


    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: ScolarescaDAOImpl
    Caso: Password errata
     */
    @Test
    public void doRetrieveByEmailPasswordError2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> dao.doRetrieveByEmailPassword(scolaresca.getEmail(), null));
        String message = "Errore Autenticazione utente";
        assertEquals(message,exception.getMessage());
    }

    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: ScolarescaDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmailPassword()
    {
        assertEquals(scolaresca.getId(),dao.doRetrieveByEmailPassword(scolaresca.getEmail(), "passwordScol").getId());
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: ScolarescaDAOImpl
    Caso: Email errata
     */
    @Test
    public void doRetrieveByEmailError()
    {
        assertNull(dao.doRetrieveByEmail(null));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: ScolarescaDAOImpl
    Caso: Email inesistente
     */
    @Test
    public void doRetrieveByEmailError1()
    {
        assertNull(dao.doRetrieveByEmail("emailfake"));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: ScolarescaDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmail()
    {
        assertEquals(scolaresca.getId(), dao.doRetrieveByEmail(scolaresca.getEmail()).getId());
    }

    /*
    Metodo: doUpdate(...)
    Classe: ScolarescaDAOImpl
    Caso: IdUtente errato o inesistente
     */
    @Test
    public void doUpdateError()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> dao.doUpdate(bean));
        String message = "UPDATE Scolaresca error.";
        assertEquals(message,exception.getMessage());
    }
    /*
    Metodo: doUpdate(...)
    Classe: ScolarescaDAOImpl
    Caso: Corretto
     */
    @Test
    public void doUpdate()
    {
        scolaresca.setEmail("newemail@gmail.com");
        assertTrue(dao.doUpdate(scolaresca));
    }

    @AfterClass
    public static void cleanUp()
    {
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id = ?");
            ps.setInt(1,scolaresca.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE SCOLARESCA FAILED");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
