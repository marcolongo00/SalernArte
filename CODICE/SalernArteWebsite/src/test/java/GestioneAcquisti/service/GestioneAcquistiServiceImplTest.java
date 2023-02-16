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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GestioneAcquistiServiceImplTest {
    private static GestioneAcquistiService serviceA;
    private static CarrelloDAO mockedcarrelloDAO;
    private static AcquistoDAO mockedacquistoDAO;
    private static EventoDAO mockedeventoDAO;
    private static BigliettoDAO mockedbigliettoDAO;
    private static UtenteDAOImpl daoUser;
    private static CarrelloBean carrelloBean;
    private static UtenteBean user;
    private static EventoBean evento;
    private static int QUANTITA = 3;
    private static double PREZZO_BIG = 13;
    private static Date DATA_INIZIO_EVENTO, DATA_FINE_EVENTO, DATA_ATTUALE = new Date(Calendar.getInstance().getTimeInMillis());

    @BeforeClass
    public static void startUp(){
        mockedbigliettoDAO = mock(BigliettoDAO.class);
        mockedeventoDAO = mock(EventoDAO.class);
        mockedcarrelloDAO = mock(CarrelloDAO.class);
        daoUser = mock(UtenteDAOImpl.class);
        mockedacquistoDAO = mock(AcquistoDAO.class);

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        evento= new EventoBean(1,1,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/photo_2022-06-11_16-53-57.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        user = new UtenteBean(1,1, "Marco", "Longo", "emailtest@gmail.com", "passTest", Date.valueOf("1978-08-10"), false);
        evento.setAttivo(true);
        when(daoUser.doRetrieveById(Mockito.anyInt())).thenReturn(user);
        carrelloBean = new CarrelloBean(user.getId());
        carrelloBean.put(evento,QUANTITA,PREZZO_BIG);
        serviceA = new GestioneAcquistiServiceImpl(mockedcarrelloDAO,mockedeventoDAO,mockedbigliettoDAO,mockedacquistoDAO);
        when(mockedeventoDAO.doRetrieveById(Mockito.anyInt())).thenReturn(evento);
        when(mockedcarrelloDAO.doRetrieveByIdUtente(user.getId())).thenReturn(carrelloBean);
    }

    @Before
    public void check()
    {
        CarrelloBean bean = new CarrelloBean(user.getId());
        CarrelloBean.BigliettoQuantita big = carrelloBean.get(evento.getId());
        if(big == null)
            carrelloBean.put(evento,QUANTITA,PREZZO_BIG);

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
        when(mockedbigliettoDAO.doRetrievePrezzoBigliettoByEvento(1)).thenReturn(prezzo);
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
        when(mockedbigliettoDAO.doRetrievePrezzoBigliettoByEvento(1)).thenReturn(prezzo);
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

    /* Operazione di riferimento nel Test Plan:
     * Caso: Carrello non corretto
     * Metodo della classe service di riferimento: controlloElementiCarrello(...)
     * */
    @Test
    public void controlloElementiCarrelloTestError1(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> serviceA.controlloElementiCarrello(null, user));
        String message = "Errore nel carrello";
        assertEquals(message, exception.getMessage());
    }
    /* Operazione di riferimento nel Test Plan:
     * Caso: Carrello non corretto
     * Metodo della classe service di riferimento: controlloElementiCarrello(...)
     * */
    @Test
    public void controlloElementiCarrelloTest(){
        assertFalse(serviceA.controlloElementiCarrello(carrelloBean, user));
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Idevento errato
     * Metodo della classe service di riferimento: removeEventoFromCarrello(...)
     * */
    @Test
    public void removeEventoFromCarrelloError1(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> serviceA.removeEventoFromCarrello(-1, carrelloBean,user));
        String message = "qualcosa è andato storto riprovare";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Carrello errato o inesistente
     * Metodo della classe service di riferimento: removeEventoFromCarrello(...)
     * */
    @Test
    public void removeEventoFromCarrelloError2(){
        CarrelloBean bean = new CarrelloBean();
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> serviceA.removeEventoFromCarrello(evento.getId(), bean,user));
        String message = "qualcosa è andato storto riprovare";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Corretto
     * Metodo della classe service di riferimento: removeEventoFromCarrello(...)
     * */
    @Test
    public void removeEventoFromCarrello(){
        assertTrue(serviceA.removeEventoFromCarrello(evento.getId(), carrelloBean, user));
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Carrello errato o inesistente
     * Metodo della classe service di riferimento: acquistaProdotti(...)
     * */
    @Test
    public void acquistaProdottiTestError1(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> serviceA.acquistaProdotti(null, user));
        String message = "operazione non autorizzata";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Utente errato o inesistente
     * Metodo della classe service di riferimento: acquistaProdotti(...)
     * */
    @Test
    public void acquistaProdottiTestError2(){
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> serviceA.acquistaProdotti(carrelloBean, null));
        String message = "operazione non autorizzata";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Corretto
     * Metodo della classe service di riferimento: acquistaProdotti(...)
     * */
    @Test
    public void acquistaProdottiTest(){
        assertTrue(serviceA.acquistaProdotti(carrelloBean, user));
    }

    @AfterClass
    public static void cleanUp(){
        mockedeventoDAO = null;
        mockedcarrelloDAO = null;
        mockedbigliettoDAO = null;
        serviceA=null;

    }
}
