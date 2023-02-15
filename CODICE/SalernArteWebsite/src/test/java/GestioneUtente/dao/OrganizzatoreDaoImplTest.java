package GestioneUtente.dao;


import model.dao.OrganizzatoreDAOImpl;
import model.dao.UtenteRegistratoDAO;
import model.entity.OrganizzatoreBean;

import model.entity.UtenteRegistratoBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import singleton.ConPool;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganizzatoreDaoImplTest {

    private static UtenteRegistratoDAO daoOrg;


    private static OrganizzatoreBean utente;

    private static String passwordNotHash;

    @BeforeClass
    public static void startUp(){
        daoOrg =new OrganizzatoreDAOImpl();
        passwordNotHash="plutoOrganizzatore";
        utente=new OrganizzatoreBean(1, "IT56W0300203280959939195461", "Lucia", "Martino", "luciamartino@gmail.com", passwordNotHash, "Ciao sono un organizzatore",Date.valueOf("1998-10-10"), false);

        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,"organizzatore");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Utente error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            utente.setId(id);
            PreparedStatement ps2=con.prepareStatement("INSERT INTO Organizzatore (id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps2.setInt(1,utente.getId());
            ps2.setString(2,utente.getNome());
            ps2.setString(3,utente.getCognome());
            ps2.setString(4,utente.getBiografia());
            ps2.setDate(5,utente.getDataDiNascita());
            ps2.setInt(6,utente.getSesso());
            ps2.setString(7,utente.getIban());
            if(ps2.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Organizzatore error.");
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
    Classe: OrganizzatoreDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveAllTest(){
        List<UtenteRegistratoBean> result=daoOrg.doRetrieveAll();
        assertFalse(result.isEmpty());
    }

    @Test
    /*
    Metodo: doSave(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Corretto
     */
    public void doSaveTest(){
        OrganizzatoreBean bean1 = new OrganizzatoreBean(1, "IT56W0300203280959939195461", "Lucia", "Martino", "luciamartino3@gmail.com", passwordNotHash, "Ciao sono un organizzatore",Date.valueOf("1998-10-10"), false);
        boolean result = daoOrg.doSave(bean1);
        daoOrg.doDelete(bean1.getId());
        assertTrue(result);
    }
    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Password errata
     */
    @Test
    public void doRetrieveByEmailPasswordError2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> daoOrg.doRetrieveByEmailPassword(utente.getEmail(), null));
        String message = "Errore Autenticazione utente";
        assertEquals(message, exception.getMessage());
    }
    /*
    Metodo: doRetrieveByEmailPassword(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmailPassword(){
        assertEquals(utente.getId(), daoOrg.doRetrieveByEmailPassword(utente.getEmail(), passwordNotHash).getId());
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Email errata
     */
    @Test
    public void doRetrieveByEmailError()
    {
        assertNull(daoOrg.doRetrieveByEmail(null));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Email inesistente
     */
    @Test
    public void doRetrieveByEmailError1()
    {
        assertNull(daoOrg.doRetrieveByEmail("emailfake"));
    }

    /*
    Metodo: doRetrieveByEmail(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByEmail()
    {
        assertEquals(utente.getId(), daoOrg.doRetrieveByEmail(utente.getEmail()).getId());
    }

    /*
    Metodo: doUpdate(...)
    Classe: OrganizzatoreDAOImpl
    Caso: IdUtente errato o inesistente
     */
    @Test
    public void doUpdateError()
    {
        OrganizzatoreBean bean1 = new OrganizzatoreBean(-1,1, "IT56W0300203280959939195461", "Lucia", "Martino", "luciamartino3@gmail.com", passwordNotHash, "Ciao sono un organizzatore",Date.valueOf("1998-10-10"), false);

        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> daoOrg.doUpdate(bean1));
        String message = "UPDATE Organizzatore error.";
        assertEquals(message,exception.getMessage());
    }
    /*
    Metodo: doUpdate(...)
    Classe: OrganizzatoreDAOImpl
    Caso: Corretto
     */
    @Test
    public void doUpdate()
    {
        utente.setEmail("newemail@gmail.com");
        assertTrue(daoOrg.doUpdate(utente));
    }

    @AfterClass
    public static void cleanUp()
    {
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id = ?");
            ps.setInt(1,utente.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE ORGANIZZATORE FAILED");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}