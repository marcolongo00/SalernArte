package GestioneAcquisti.service;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.dao.*;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteBean;
import org.junit.*;
import singleton.ConPool;
import java.sql.*;
import java.util.Calendar;

import static org.junit.Assert.*;

public class GestioneAcquistiServiceImplTestIntegrazione {
    private static GestioneAcquistiService serviceA;
    private static CarrelloDAO carrelloDAO;
    private static BigliettoDAO bigliettoDAO;
    private static AcquistoDAO acquistoDAO;
    private static EventoDAO eventoDAO;
    private static UtenteRegistratoDAO dao;
    private static Date DATA_INIZIO_EVENTO;
    private static Date DATA_FINE_EVENTO;
    private static UtenteBean user;
    private static EventoBean evento;
    private static OrganizzatoreBean org;
    private static CarrelloBean carrelloBean;
    private static int QUANTITA = 1;
    private static double PREZZO_BIG = 13;
    private static final Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());

    @BeforeClass
    public static void setUp()
    {
        carrelloDAO = new CarrelloDAOImpl();
        bigliettoDAO = new BigliettoDAOImpl();
        eventoDAO = new EventoDAOImpl();
        acquistoDAO = new AcquistoDAOImpl();
        serviceA = new GestioneAcquistiServiceImpl();

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        user = new UtenteBean(1, "Marco", "Longo", "emailtest@gmail.com", "passTest", Date.valueOf("1978-08-10"), false);
        org = new OrganizzatoreBean(1, "IT17J0300203280772191565161", "Antonio", "Longo", "orgimailTestDAO@example.com", "password1", "bio", Date.valueOf("1995-07-15"), false);

        dao = new UtenteDAOImpl();
        dao.doSave(user);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(org);

        evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/photo_2022-06-11_16-53-57.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        eventoDAO.doSave(evento);
        eventoDAO.doUpdateAttivazioneEvento(evento.getId(), true);
        evento.setAttivo(true);
        carrelloBean = new CarrelloBean(user.getId());
        carrelloBean.put(evento,QUANTITA,PREZZO_BIG);
        carrelloDAO.doSave(user.getId(), carrelloBean.get(evento.getId()));
    }

    @Before
    public void check()
    {
        CarrelloBean.BigliettoQuantita big = carrelloBean.get(evento.getId());
        if(big == null)
            carrelloBean.put(evento,QUANTITA,PREZZO_BIG);
        CarrelloBean bean = carrelloDAO.doRetrieveByIdUtente(user.getId());
        if(!(bean.contains(carrelloBean.get(evento.getId()))))
            carrelloDAO.doSave(user.getId(), carrelloBean.get(evento.getId()));
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void aggiungiAlCarrelloIntegrazione()
    {
        assertNotNull(serviceA.aggiungiAlCarrello(evento.getId(),QUANTITA,carrelloBean,user));
    }


    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void updateQuantitaCarrelloIntegrazione()
    {
        assertTrue(serviceA.updateQuantitaCarrello(evento.getId(),QUANTITA,carrelloBean,user));
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean controlloElementiCarrello(...)
     * */
    @Test
    public void controlloElementiCarrelloIntegrazione()
    {
        assertFalse(serviceA.controlloElementiCarrello(carrelloBean, user));
    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean removeEventoFromCarrello(...)
     * */
    @Test
    public void removeEventoFromCarrelloIntegrazione()
    {
        assertTrue(serviceA.removeEventoFromCarrello(evento.getId(), carrelloBean, user));

    }

    /* Operazione di riferimento nel Test Plan:
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean acquistaProdotti(...)
    * */
    @Test
    public void acquistaProdottiIntegrazione()
    {
        assertTrue(serviceA.acquistaProdotti(carrelloBean, user));
    }


    @AfterClass
    public static void cleanUp()
    {
        serviceA = null;
        eventoDAO = null;
        bigliettoDAO = null;
        carrelloDAO = null;

        dao = new UtenteDAOImpl();
        if(!(dao.doDelete(user.getId())))
            throw new RuntimeException("DELETE USER FAILED");
        dao = new OrganizzatoreDAOImpl();
        if(!(dao.doDelete(org.getId())))
            throw new RuntimeException("DELETE ORG FAILED");
    }
}
