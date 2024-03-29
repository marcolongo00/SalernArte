package GestioneAcquisti.dao;

import model.dao.CarrelloDAO;
import model.dao.CarrelloDAOImpl;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteBean;
import org.junit.*;
import singleton.ConPool;
import java.sql.*;
import java.util.Calendar;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarrelloDaoImplTest {
    private static CarrelloDAO carrelloDAO;
    private String message;
    private static final String PASSWORD = "Password10";
    private static EventoBean evento;
    private static CarrelloBean carrello;
    private static UtenteBean user;
    private static OrganizzatoreBean org;
    private static CarrelloBean.BigliettoQuantita bigliettoQuantita;
    private static Date DATA_INIZIO_EVENTO, DATA_FINE_EVENTO, DATA_ATTUALE = new Date(Calendar.getInstance().getTimeInMillis());
    private static int NUM_BIGLIETTI = 10, QUANTITA = 3, PREZZO_BIG = 13;

    @BeforeClass
    public static void startUp()
    {
        user = new UtenteBean(1,"Marco", "Longo", "emaildiprova@example.com", PASSWORD, Date.valueOf("2000-02-23"), false);
        org = new OrganizzatoreBean(1, "IT17J0300203280772191565161", "Antonio", "Longo", "orgimailTestDAO@example.com", PASSWORD, "bio", Date.valueOf("1995-07-15"), false);
        carrelloDAO = new CarrelloDAOImpl();

        try(Connection conn = ConPool.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,org.getEmail());
            ps.setString(2,org.getPasswordHash());
            ps.setString(3,"organizzatore");

            if(ps.executeUpdate() !=1)
                throw new RuntimeException("INSERT Utente error.");

            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            org.setId(rs.getInt(1));

            PreparedStatement ps2=conn.prepareStatement("INSERT INTO Organizzatore (id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps2.setInt(1,org.getId());
            ps2.setString(2,org.getNome());
            ps2.setString(3,org.getCognome());
            ps2.setString(4,org.getBiografia());
            ps2.setDate(5,org.getDataDiNascita());
            ps2.setInt(6,org.getSesso());
            ps2.setString(7,org.getIban());

            if(ps2.executeUpdate() !=1)
                throw new RuntimeException("INSERT Organizzatore error.");

            Calendar c= Calendar.getInstance();
            c.setTime(DATA_ATTUALE);
            c.add(Calendar.DATE,5);
            DATA_INIZIO_EVENTO=new java.sql.Date(c.getTimeInMillis());
            c.add(Calendar.DATE,10);
            DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

            evento = new EventoBean(org.getId(),DATA_INIZIO_EVENTO, DATA_FINE_EVENTO, "nome-evento", "path test", "descrizione-evento", "indirizzo evento","sede evento",NUM_BIGLIETTI,false);

            PreparedStatement ps3=conn.prepareStatement("INSERT INTO Evento(idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) VALUES(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps3.setInt(1,evento.getIdOrganizzatore());
            ps3.setString(2,evento.getNome());
            ps3.setBoolean(3,evento.isTipo());
            ps3.setString(4,evento.getDescrizione());
            ps3.setString(5,evento.getPath());
            ps3.setInt(6,evento.getNumBiglietti());
            ps3.setDate(7,evento.getDataInizio());
            ps3.setDate(8,evento.getDataFine());
            ps3.setString(9,evento.getIndirizzo());
            ps3.setString(10,evento.getSede());
            ps3.setBoolean(11,evento.isAttivo());

            if(ps3.executeUpdate()!=1)
                throw new RuntimeException("INSERT evento error");
            ResultSet rs2=ps3.getGeneratedKeys();
            rs2.next();
            evento.setId(rs2.getInt(1));

            PreparedStatement ps4 = conn.prepareStatement("INSERT INTO UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps4.setString(1,user.getEmail());
            ps4.setString(2,user.getPasswordHash());
            ps4.setString(3,"utente");

            if(ps4.executeUpdate() !=1)
                throw new RuntimeException("INSERT Utente error.");

            ResultSet rs3=ps4.getGeneratedKeys();
            rs3.next();
            user.setId(rs3.getInt(1));

            PreparedStatement ps5 = conn.prepareStatement("INSERT INTO Utente(id,nome,cognome,dataDiNascita,sesso) VALUES (?,?,?,?,?)");
            ps5.setInt(1,user.getId());
            ps5.setString(2,user.getNome());
            ps5.setString(3,user.getCognome());
            ps5.setDate(4,user.getDataDiNascita());
            ps5.setInt(5,user.getSesso());

            if(ps5.executeUpdate()!= 1)
                throw new RuntimeException("INSERT Utente error");

            carrello = new CarrelloBean(user.getId());
            carrello.put(evento,QUANTITA,PREZZO_BIG);
            bigliettoQuantita = carrello.get(evento.getId());

            PreparedStatement ps6 = conn.prepareStatement("INSERT INTO Carrello(idUtente,idEvento,quantita)VALUES(?,?,?)");
            ps6.setInt(1,carrello.getIdUtente());
            ps6.setInt(2,bigliettoQuantita.getProdotto().getId());
            ps6.setInt(3, bigliettoQuantita.getQuantita());

            if(ps6.executeUpdate() !=1)
                throw new RuntimeException("INSERT Carrello error.");

            conn.close();
            ps.close();
            ps2.close();
            ps3.close();
            ps4.close();
            ps5.close();
            ps6.close();
            rs.close();
            rs2.close();
            rs3.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteCarrello()
    {
        CarrelloBean bean = carrelloDAO.doRetrieveByIdUtente(user.getId());
        if(bean!= null)
        {
            if(bean.get(evento.getId()).getProdotto().equals(evento))
            {
                carrelloDAO.doDelete(user.getId(), evento.getId());
                return true;
            }
            else
                return false;
        }
        else
            throw new RuntimeException(("deleteCarrello failed"));
    }

    @After
    public void insertCarrello()
    {
        CarrelloBean bean = carrelloDAO.doRetrieveByIdUtente(user.getId());
        if(!bean.contains(bigliettoQuantita))
            carrelloDAO.doSave(user.getId(), bigliettoQuantita);
    }

 
    /*
    Metodo: doRetrieveByIdUtente(...)
    Classe: CarrelloDAOImpl
    Caso: Id utente errato o inesistente
     */
    @Test
    public void doRetrieveByIdUtenteTestError()
    {
        assertNotEquals(carrello.getIdUtente(), carrelloDAO.doRetrieveByIdUtente(-1));
    }

    /*
    Metodo: doRetrieveByIdUtente(...)
    Classe: CarrelloDAOImpl
    Caso: Corretto
     */
    @Test
    public void doRetrieveByIdUtenteTest()
    {
        assertEquals(carrello.getIdUtente(), carrelloDAO.doRetrieveByIdUtente(user.getId()).getIdUtente());
    }

    /*
    Metodo: doSave(...)
    Classe: CarrelloDAOImpl
    Caso: Corretto
     */
    @Test
    public void doSaveTest(){
        if(deleteCarrello())
            assertTrue(carrelloDAO.doSave(user.getId(), bigliettoQuantita));
    }

    /*
    Metodo: doSave(...)
    Classe: CarrelloDAOImpl
    Caso: Utente inesistente o errato
     */
    @Test
    public void doSaveTestError(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doSave(-2, bigliettoQuantita));
        message = "INSERT in Carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doSave(...)
    Classe: CarrelloDAOImpl
    Caso: Evento errato o inesistente
     */
    @Test
    public void doSaveTestError2(){
        int idEv = bigliettoQuantita.getProdotto().getId();
        bigliettoQuantita.getProdotto().setId(-1);
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doSave(user.getId(),bigliettoQuantita));
        message = "INSERT in Carrello error";
        bigliettoQuantita.getProdotto().setId(idEv);
        assertEquals(message, exception.getMessage());
    }

    /*
        Metodo: doDelete(...)
        Classe: CarrelloDAOImpl
        Caso: Utente inesistente o errato
         */
    @Test
    public void doDeleteTestError(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doDelete(-2, evento.getId()));
        message = "DELETE Evento from carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
        Metodo: doDelete(...)
        Classe: CarrelloDAOImpl
        Caso: IdEvento errato o inesistente
         */
    @Test
    public void doDeleteTestError2(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doDelete(user.getId(), -1));
        message = "DELETE Evento from carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
        Metodo: doDelete(...)
        Classe: CarrelloDAOImpl
        Caso: Corretto
         */
    @Test
    public void doDeleteTest(){
        assertTrue(carrelloDAO.doDelete(user.getId(), evento.getId()));
    }

    /*
    Metodo: svuotaCarrello(...)
    Classe: CarrelloDAOImpl
    Caso: Utente inesistente
     */
    @Test
    public void svuotaCarrelloTestError(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.svuotaCarrello(-2));
        message = "svuota carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: svuotaCarrello(...)
    Classe: CarrelloDAOImpl
    Caso: Corretto
     */
    @Test
    public void svuotaCarrelloTest(){
        assertTrue(carrelloDAO.svuotaCarrello(user.getId()));
    }

    /*
    Metodo: doUpdateQuantita(...)
    Classe: CarrelloDAOImpl
    Caso: Utente inesistente o errato
     */
    @Test
    public void doUpdateQuantitàTestError(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doUpdateQuantita(-2, bigliettoQuantita));
        message = "UPDATE quantità carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doUpdateQuantita(...)
    Classe: CarrelloDAOImpl
    Caso: IdEvento errato
     */
    @Test
    public void doUpdateQuantitàTestError2(){
        int evCorretto = bigliettoQuantita.getProdotto().getId();
        bigliettoQuantita.getProdotto().setId(-1);
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, ()-> carrelloDAO.doUpdateQuantita(user.getId(), bigliettoQuantita));
        bigliettoQuantita.getProdotto().setId(evCorretto);
        message = "UPDATE quantità carrello error";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: doUpdateQuantita(...)
    Classe: CarrelloDAOImpl
    Caso: Corretto
     */
    @Test
    public void doUpdateQuantitàTest(){
        assertTrue(carrelloDAO.doUpdateQuantita(user.getId(), bigliettoQuantita));
    }

    @AfterClass
    public static void cleanUp()
    {
        try(Connection con=ConPool.getConnection()){

            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,org.getId());

            if(ps.executeUpdate()!=1)
                throw new RuntimeException("DELETE UTENTE ERROR");

            PreparedStatement ps1 =con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps1.setInt(1,user.getId());

            if(ps1.executeUpdate()!=1)
                throw new RuntimeException("DELETE CARRELLO ERROR");

            con.close();
            ps.close();
            ps1.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
