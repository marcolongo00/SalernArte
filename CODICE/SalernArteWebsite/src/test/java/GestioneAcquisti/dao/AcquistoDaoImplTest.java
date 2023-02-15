package GestioneAcquisti.dao;

import model.dao.AcquistoDAO;
import model.dao.AcquistoDAOImpl;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import model.entity.AcquistoBean;
import model.entity.UtenteBean;
import org.junit.*;
import singleton.ConPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcquistoDaoImplTest {

    private static AcquistoBean acquisto;
    private boolean op = false;
    private static UtenteBean user;
    private static AcquistoDAO dao;
    private static List<AcquistoBean> list;
    @BeforeClass
    public static void startUp()
    {
        dao = new AcquistoDAOImpl();
        user = new UtenteBean(1, "Marco", "Longo", "emailuser@example.com", "passworduser", Date.valueOf("2000-02-23"), false);
        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3,"utente");

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT UTENTE REGISTRATO ERROR");
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO Utente(id,nome,cognome,dataDiNascita,sesso)VALUES (?,?,?,?,?)");
            ps2.setInt(1,user.getId());
            ps2.setString(2,user.getNome());
            ps2.setString(3,user.getCognome());
            ps2.setDate(4,user.getDataDiNascita());
            ps2.setInt(5,user.getSesso());

            if(ps2.executeUpdate() != 1)
                throw new RuntimeException("INSERT UTENTE ERROR");

            acquisto = new AcquistoBean(user.getId(), Date.valueOf("2020-04-10"), 89, "prodotti");

            PreparedStatement ps1 = con.prepareStatement("INSERT INTO Acquisto(data, totale, idUtente, prodotti)VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps1.setDate(1, acquisto.getData());
            ps1.setDouble(2,acquisto.getTotale());
            ps1.setInt(3,acquisto.getIdUtente());
            ps1.setString(4,acquisto.getProdotti());

            if(ps1.executeUpdate() != 1)
                throw new RuntimeException("INSERT ACQUISTO ERROR");

            ResultSet rs1 = ps1.getGeneratedKeys();
            rs1.next();
            acquisto.setNumOrdine(rs1.getInt(1));
            list = new ArrayList<AcquistoBean>();
            list.add(acquisto);

            con.close();
            rs.close();
            rs1.close();
            ps.close();
            ps1.close();
            ps2.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteAcquisto()
    {
        if(dao.doRetrieveAcquistoByNumOrdine(acquisto.getNumOrdine()) != null)
        {
            dao.doDelete(acquisto.getNumOrdine());
            return true;
        }
        else
            throw new RuntimeException(("deleteAcquisto failed"));
    }


    /*
    Metodo: doRetrieveListaAcquistiByIdUtente(...)
    Classe: AcquistoDAOImpl
    Caso: IdUtente errato o inesistente
     */
    @Test
    public void doRetrieveListaAcquistiByIdUtenteError()
    {
        assertNotEquals(list, dao.doRetrieveListaAcquistiByIdUtente(-1));
    }

    /*
    Metodo: doRetrieveListaAcquistiByIdUtente(...)
    Classe: AcquistoDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveListaAcquistiByIdUtente()
    {
      assertEquals(list.get(0).getIdUtente(), dao.doRetrieveListaAcquistiByIdUtente(user.getId()).get(0).getIdUtente());
    }

    /*
    Metodo: doRetrieveAcquistoByNumOrdine(...)
    Classe: AcquistoDAOImpl
    Caso: NumOrdine errato
     */
    @Test
    public void doRetrieveAcquistoByNumOrdineError()
    {
        assertNull(dao.doRetrieveAcquistoByNumOrdine(-1));
    }

    /*
    Metodo: doRetrieveAcquistoByNumOrdine(...)
    Classe: AcquistoDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveAcquistoByNumOrdine()
    {
        assertEquals(acquisto.getNumOrdine(), dao.doRetrieveAcquistoByNumOrdine(acquisto.getNumOrdine()).getNumOrdine());
    }

    /*
    Metodo: doSave(...)
    Classe: AcquistoDAOImpl
    Caso: IdUtente errato
     */
    @Test
    public void doSaveError()
    {
        int id = acquisto.getIdUtente();
        acquisto.setIdUtente(-1);
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> dao.doSave(acquisto));
        String message = "INSERT error";
        acquisto.setIdUtente(id);
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doSave(...)
    Classe: AcquistoDAOImpl
    Caso: Corretto
     */
    @Test
    public void doSave()
    {
        if(deleteAcquisto())
            assertTrue(dao.doSave(acquisto));
    }

    /*
    Metodo: setProdotti(...)
    Classe: AcquistoDAOImpl
    Caso: NumOrdine errato
     */
    @Test
    public void setProdottiError1()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> dao.setProdotti(-1, acquisto.getProdotti()));
        String message = "SET Prodotti fattura error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: setProdotti(...)
    Classe: AcquistoDAOImpl
    Caso: Prodotti errato
     */
    @Test
    public void setProdottiError2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> dao.setProdotti(acquisto.getNumOrdine(), null));
        String message = "SET Prodotti fattura error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doUpdateProdottiByNumOrdine(...)
    Classe: AcquistoDAOImpl
    Caso: Corretto
     */
    @Test
    public void setProdotti()
    {
        assertTrue(dao.setProdotti(acquisto.getNumOrdine(), acquisto.getProdotti()));
    }

    /*
    Metodo: doDelete(...)
    Classe: AcquistoDAOImpl
    Caso: NumOrdine errato
     */
    @Test
    public void doDeleteError()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> dao.doDelete(-1));
        String message = "DELETE error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doDelete(...)
    Classe: AcquistoDAOImpl
    Caso: Corretto
     */
    @Test
    public void doDelete()
    {
        AcquistoBean bean = new AcquistoBean(acquisto.getIdUtente(), acquisto.getData(), acquisto.getTotale(), acquisto.getProdotti());
        dao.doSave(bean);
        assertTrue(dao.doDelete(bean.getNumOrdine()));
    }

    @AfterClass
    public static void cleanUp()
    {
        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id = ?");
            ps.setInt(1, user.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE USER REG ERROR");

            con.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
