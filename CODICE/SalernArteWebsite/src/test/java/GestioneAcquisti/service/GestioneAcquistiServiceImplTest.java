package GestioneAcquisti.service;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;

import model.dao.*;
import model.entity.*;
import org.junit.*;
import org.mockito.Mockito;
import singleton.ConPool;

import java.sql.*;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class GestioneAcquistiServiceImplTest {
    private static GestioneAcquistiService serviceA;
    private static CarrelloDAO mockedcarrelloDAO;
    private static EventoDAO mockedeventoDAO;
    private static BigliettoDAO mockedbigliettoDAO;
    private static CarrelloBean carrelloBean;
    private static UtenteBean user;
    private static EventoBean evento;
    private static int QUANTITA = 3;
    private static double PREZZO_BIG = 13;
    private static Date DATA_INIZIO_EVENTO, DATA_FINE_EVENTO, DATA_ATTUALE = new Date(Calendar.getInstance().getTimeInMillis());

    @BeforeClass
    public static void startUp(){
        mockedbigliettoDAO = Mockito.mock(BigliettoDAO.class);
        mockedeventoDAO = Mockito.mock(EventoDAO.class);
        mockedcarrelloDAO = Mockito.mock(CarrelloDAO.class);

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        evento= new EventoBean(1,1,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/photo_2022-06-11_16-53-57.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        user = new UtenteBean(1, "Marco", "Longo", "emailtest@gmail.com", "passTest", Date.valueOf("1978-08-10"), false);

        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps4 = con.prepareStatement("INSERT INTO UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps4.setString(1,user.getEmail());
            ps4.setString(2,user.getPasswordHash());
            ps4.setString(3,"utente");

            if(ps4.executeUpdate() !=1)
                throw new RuntimeException("INSERT Utente error.");

            ResultSet rs3=ps4.getGeneratedKeys();
            rs3.next();
            user.setId(rs3.getInt(1));

            PreparedStatement ps5 = con.prepareStatement("INSERT INTO Utente(id,nome,cognome,dataDiNascita,sesso) VALUES (?,?,?,?,?)");
            ps5.setInt(1,user.getId());
            ps5.setString(2,user.getNome());
            ps5.setString(3,user.getCognome());
            ps5.setDate(4,user.getDataDiNascita());
            ps5.setInt(5,user.getSesso());

            if(ps5.executeUpdate()!= 1)
                throw new RuntimeException("INSERT Utente error");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        carrelloBean = new CarrelloBean(user.getId());
        carrelloBean.put(evento,QUANTITA,PREZZO_BIG);
        serviceA = new GestioneAcquistiServiceImpl(mockedcarrelloDAO,mockedeventoDAO,mockedbigliettoDAO);
        Mockito.when(mockedeventoDAO.doRetrieveById(1)).thenReturn(evento);

        Mockito.when(mockedcarrelloDAO.doUpdateQuantita(user.getId(), carrelloBean.get(evento.getId()))).thenReturn(true);
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
    * Caso: La quantità inserita non è valida
    * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
    * */
    @Test
    public void TC_3p1_1(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.aggiungiAlCarrello(evento.getId(),-1, carrelloBean, user));
        String message = "formato dati errato";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: La quantità inserita è valida
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void TC_3p1_2()
    {
        double prezzo = carrelloBean.get(evento.getId()).getPrezzoBigl();
        Mockito.when(mockedbigliettoDAO.doRetrievePrezzoBigliettoByEvento(1)).thenReturn(prezzo);
        CarrelloBean bean = serviceA.aggiungiAlCarrello(1,1,carrelloBean,user);
        assertNotNull(bean);
    }

    /* Operazione di riferimento nel Test Plan: Modifica al Carrello
     * Caso: La quantità inserita non è valida
     * Metodo della classe service di riferimento: void updateQuantitaCarrello(...)
     * */
    @Test
    public void TC_3p2_1(){
        double prezzo = carrelloBean.get(evento.getId()).getPrezzoBigl();
        Mockito.when(mockedbigliettoDAO.doRetrievePrezzoBigliettoByEvento(1)).thenReturn(prezzo);
        NumberFormatException exception;
        exception = assertThrows(NumberFormatException.class,() -> serviceA.updateQuantitaCarrello(evento.getId(),-1,carrelloBean,user));
        String message = "quantità non valida";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan: Modifica al Carrello
     * Caso: La quantità inserita è valida
     * Metodo della classe service di riferimento: void updateQuantitaCarrello(...)
     * */
    @Test
    public void TC_3p2_2(){
        assertTrue(serviceA.updateQuantitaCarrello(evento.getId(),QUANTITA,carrelloBean,user));
    }

    @AfterClass
    public static void cleanUp(){
        mockedeventoDAO = null;
        mockedcarrelloDAO = null;
        mockedbigliettoDAO = null;
        serviceA=null;

        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,user.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE USER FAILED");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
