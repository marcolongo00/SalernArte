package GestioneAcquisti.controller;

import gestioneAcquisti.controller.GestioneAcquistiController;
import model.dao.*;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class GestioneAcquistiControllerTest {
    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static ServletContext mockedServletContext;
    private static RequestDispatcher mockedDispatcher;
    private static HttpSession session;
    private static UtenteRegistratoDAO dao;
    private static EventoDAO eventoDAO = new EventoDAOImpl();
    private static CarrelloDAO carrelloDAO = new CarrelloDAOImpl();
    private static BigliettoDAO bigliettoDAO = new BigliettoDAOImpl();
    private static ServletConfig servletConfig = mock(ServletConfig.class);
    private static UtenteBean utente;
    private static CarrelloBean carrello;
    private static EventoBean evento;
    private static OrganizzatoreBean org;
    private static GestioneAcquistiController servlet = new GestioneAcquistiController();

    private static Date DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
    private static int QUANTITA = 1;
    private static double PREZZO_BIG = 13;

    @BeforeClass
    public static void setUp() throws ServletException {
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = mock(HttpServletRequest.class);
        mockedResponse = mock(HttpServletResponse.class);
        mockedServletContext = mock(ServletContext.class);
        mockedDispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        utente = new UtenteBean(1, "Marco", "Longo", "emailtest@gmail.com", "passTest", Date.valueOf("1978-08-10"), false);
        org = new OrganizzatoreBean(1, "IT17J0300203280772191565161", "Antonio", "Longo", "orgimailTestDAO@example.com", "password1", "bio", Date.valueOf("1995-07-15"), false);

        dao = new UtenteDAOImpl();
        dao.doSave(utente);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(org);

        evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/photo_2022-06-11_16-53-57.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        eventoDAO.doSave(evento);
        evento.setAttivo(true);
        eventoDAO.doUpdateAttivazioneEvento(evento.getId(), true);

        for(int i=0 ; i<evento.getNumBiglietti(); i++)
            bigliettoDAO.doSave(evento.getId(), 5.5);

        carrello = new CarrelloBean(utente.getId());

        when(mockedRequest.getSession()).thenReturn(session);
        when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);
        when(mockedRequest.getRequestDispatcher(anyString())).thenReturn(mockedDispatcher);
        when(session.getAttribute("selezionato")).thenReturn(utente);
        when(session.getAttribute("carrello")).thenReturn(carrello);
    }

    /* Operazione di riferimento nel Test Plan: Requisti funzionali
     * Caso: Corretto
     * Classe di riferimento: removeEventoFromCarrelloTestIntegrazione(...)
     * */
    @Test
    public void removeEventoFromCarrelloTestIntegrazione(){
        carrello.put(evento,QUANTITA,PREZZO_BIG);
        carrelloDAO.doSave(utente.getId(), carrello.get(evento.getId()));
        when(mockedRequest.getParameter("removeEventoFromCarrello")).thenReturn("true");
        when(mockedRequest.getParameter("idE")).thenReturn(evento.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(mockedRequest.getParameter("removeEventoFromCarrello")).thenReturn(null);
        verify(session).setAttribute("messaggio", "Rimozione evento dal carrello avvenuta con successo");
    }


    /* Operazione di riferimento nel Test Plan: Modifica Carrello
     * Caso: Corretto
     * Classe di riferimento: JSONUpdateQuantitaCarrello
     * */
    @Test
    public void finalizzaAcquistoTestIntegrazione(){
        carrello.put(evento,QUANTITA,PREZZO_BIG);
        carrelloDAO.doSave(utente.getId(), carrello.get(evento.getId()));
        when(mockedRequest.getParameter("finalizzaAcquisto")).thenReturn("true");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(mockedRequest.getParameter("finalizzaAcquisto")).thenReturn(null);
        verify(session).setAttribute("messaggio", "Acquisto avvenuto con successo");
    }


    @AfterClass
    public static void cleanUp() {
        mockedRequest = null;
        mockedResponse = null;
        mockedServletContext = null;
        mockedDispatcher = null;
        session = null;
        servlet = null;
        servletConfig = null;

        dao = new OrganizzatoreDAOImpl();
        dao.doDelete(org.getId());
        dao = new UtenteDAOImpl();
        dao.doDelete(utente.getId());

        bigliettoDAO = null;
        carrelloDAO = null;
        eventoDAO = null;
        dao = null;
    }
}
