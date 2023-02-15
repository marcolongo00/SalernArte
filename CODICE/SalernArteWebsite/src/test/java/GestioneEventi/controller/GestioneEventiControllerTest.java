package GestioneEventi.controller;

import gestioneEventi.controller.GestioneEventiController;
import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import org.junit.*;
import org.mockito.Mockito;
import org.springframework.mock.web.MockPart;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Implementa il testing di Integrazione per la classe
 * GestioneEventiController.
 * i nomi dei metodi di test che faranno riferimento
 * ai TestCaseID riportati nel documenti di TestPlan avranno questa struttura:
 *
 * un punto all'interno di un TESTCASEID verrà identificato con il carattere "p".
 *
 * esempio:
 *          TestCaseID: TC_2.1_1
 *          firma del metodo: TC_2p1_1
 *
 *  i restanti metodi di test faranno riferimento all'operazione da testare.
 *  si seguirà la seguente regola:
 *
 *  - nome operazione da testare
 *  il metodo all'interno di questa classe di test avrà come firma:
 *              void nomeOperazioneTestIntegrazione()
 *  nel caso di test di funzionalità di errore:
 *              void nomeOperazioneTestIntegrazioneError()
 *
 * @author Alessia Della Pepa
 */

public class GestioneEventiControllerTest {
    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static ServletContext mockedServletContext;
    private static RequestDispatcher mockedDispatcher;
    private static File image;
    private static Part FILE_PHOTO;
    private static String dataI;
    private static String dataF;
    private static Date DATA_INIZIO_EVENTO;
    private static  Date DATA_FINE_EVENTO;
    private static Date DATA_ATTUALE;
    private static final String pathReal= ".\\src\\main\\webapp\\immaginiEventi\\";
    private static  String pathInEvento;
    private static HttpSession session;
    private static DateFormat df;
    private static final ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
    private static final  GestioneEventiController servlet = new GestioneEventiController();

    @BeforeClass
    public static void setUp() throws ServletException {
        df = new SimpleDateFormat("yyyy-MM-dd");
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedServletContext = Mockito.mock(ServletContext.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);
        //l'utente verrà settato prima dell'operazione da testare perchè
        // a seconda dell'operazione cambierà il tipo di utente registrato
        // e verranno testati conseguentemente anche i permessi di accesso
        //UtenteRegistratoBean ut=new OrganizzatoreBean(5,0,"IT17J0300203280772191565161","pluto","prova","plutoprova@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);

        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);
        Mockito.when(mockedRequest.getRequestDispatcher(Mockito.anyString())).thenReturn(mockedDispatcher);
        //inizializzazione date da usate per test eventi
        DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);

        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        //dataI= "2023-05-05";
        dataI= df.format(DATA_INIZIO_EVENTO);
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());
        //dataF="2023-06-01";
        dataF=df.format(DATA_FINE_EVENTO);
        //creazione immagine che verrà passata nelle request
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        image=new File(pathReal+"fotoSample.jpg");
        try {
            ImageIO.write(bi,"jpg",image);
            Path path= Paths.get(pathReal+"fotoSample.jpg");
            FILE_PHOTO= new MockPart("fotoSample.jpg","fotoSample.jpg", Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pathInEvento="./immaginiEventi/fotoSample.jpg";

    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Inizio non rispetta il formato
     */
    @Test
    public void TC_2p1_8(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("dataInizio")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("dataFine")).thenReturn(dataF);
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvento")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("numBiglietti")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzo")).thenReturn("4");
        try {
            Mockito.when(mockedRequest.getPart("path")).thenReturn(FILE_PHOTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);
        Mockito.when(mockedRequest.getParameter("desc")).thenReturn("descrizione evento");
        Mockito.when(mockedRequest.getParameter("indirizzo")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sede")).thenReturn("sede evento");


        RuntimeException exception;
        exception= assertThrows(RuntimeException.class,()-> servlet.doPost(mockedRequest,mockedResponse));
        daoOrg.doDelete(ut.getId());
        String message="la Data Inizio non rispetta il formato";
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn(null);
        assertEquals(message,exception.getMessage());

    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Fine non rispetta il formato
     */
     @Test
    public void TC_2p1_10(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("dataInizio")).thenReturn(dataI);
        Mockito.when(mockedRequest.getParameter("dataFine")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvento")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("numBiglietti")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzo")).thenReturn("4");
        try {
            Mockito.when(mockedRequest.getPart("path")).thenReturn(FILE_PHOTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);
        Mockito.when(mockedRequest.getParameter("desc")).thenReturn("descrizione evento");
        Mockito.when(mockedRequest.getParameter("indirizzo")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sede")).thenReturn("sede evento");


        RuntimeException exception;
        exception= assertThrows(RuntimeException.class,()-> servlet.doPost(mockedRequest,mockedResponse));
        daoOrg.doDelete(ut.getId());
        String message="la Data Fine non rispetta il formato";
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn(null);

        assertEquals(message,exception.getMessage());

    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Data Inizio non rispetta il formato
     */

    @Test
    public void TC_2p2_8(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //salva evento che verrà modificato
        EventoDAO daoEv= new EventoDAOImpl();
        EventoBean evento=new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        daoEv.doSave(evento);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEventoPreChange")).thenReturn(""+evento.getId());
        Mockito.when(mockedRequest.getParameter("titoloEvMod")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvMod")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("descrizioneMod")).thenReturn("descrizione evento");
        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);

        try {
            Mockito.when(mockedRequest.getPart("pathMod")).thenReturn(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        Mockito.when(mockedRequest.getParameter("dataInizioEvMod")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("dataFineEvMod")).thenReturn(dataF);
        Mockito.when(mockedRequest.getParameter("numBigliettiEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzoBigliettoEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("indirizzoEvMod")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sedeEvMod")).thenReturn("sede evento");


        RuntimeException exception;
        exception= assertThrows(RuntimeException.class,()-> servlet.doPost(mockedRequest,mockedResponse));
        daoOrg.doDelete(ut.getId());
        String message="la Data Inizio non rispetta il formato";
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn(null);
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Data Fine non rispetta il formato
     */
    @Test
    public void TC_2p2_9(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //salva evento che verrà modificato
        EventoDAO daoEv= new EventoDAOImpl();
        EventoBean evento=new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        daoEv.doSave(evento);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEventoPreChange")).thenReturn(""+evento.getId());
        Mockito.when(mockedRequest.getParameter("titoloEvMod")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvMod")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("descrizioneMod")).thenReturn("descrizione evento");
        try {
            Mockito.when(mockedRequest.getPart("pathMod")).thenReturn(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);

        Mockito.when(mockedRequest.getParameter("dataInizioEvMod")).thenReturn(dataI);
        Mockito.when(mockedRequest.getParameter("dataFineEvMod")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("numBigliettiEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzoBigliettoEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("indirizzoEvMod")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sedeEvMod")).thenReturn("sede evento");


        RuntimeException exception;
        exception= assertThrows(RuntimeException.class,()-> servlet.doPost(mockedRequest,mockedResponse));
        daoOrg.doDelete(ut.getId());
        String message="la Data Fine non rispetta il formato";
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn(null);

        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: corretto
     */
    @Test
    public void richiestaInserimentoEventoTestIntegrazione(){
         //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("dataInizio")).thenReturn(dataI);
        Mockito.when(mockedRequest.getParameter("dataFine")).thenReturn(dataF);
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvento")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("numBiglietti")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzo")).thenReturn("4");
        try {
            Mockito.when(mockedRequest.getPart("path")).thenReturn(FILE_PHOTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);
        Mockito.when(mockedRequest.getParameter("desc")).thenReturn("descrizione evento");
        Mockito.when(mockedRequest.getParameter("indirizzo")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sede")).thenReturn("sede evento");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daoOrg.doDelete(ut.getId());
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn(null);
        //??
         Mockito.verify(mockedRequest).setAttribute("messaggio","esecuzione andata a buon fine");
      // String result= mockedRequest.getParameter("messaggio");
       // mockedRequest.setAttribute("messaggio",null);
        //assertEquals("esecuzione andata a buon fine", result);
    }
    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: corretto
     */
    @Test
    public  void richiestaModificaEventoTestIntegrazione(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //salva evento che verrà modificato
        EventoDAO daoEv= new EventoDAOImpl();
        EventoBean evento=new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        daoEv.doSave(evento);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEventoPreChange")).thenReturn(""+evento.getId());
        Mockito.when(mockedRequest.getParameter("titoloEvMod")).thenReturn("nomeEvento");
        Mockito.when(mockedRequest.getParameter("tipoEvMod")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("descrizioneMod")).thenReturn("descrizione evento");
        try {
            Mockito.when(mockedRequest.getPart("pathMod")).thenReturn(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn(pathReal);

        Mockito.when(mockedRequest.getParameter("dataInizioEvMod")).thenReturn(dataI);
        Mockito.when(mockedRequest.getParameter("dataFineEvMod")).thenReturn(dataF);
        Mockito.when(mockedRequest.getParameter("numBigliettiEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzoBigliettoEvMod")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("indirizzoEvMod")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sedeEvMod")).thenReturn("sede evento");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daoOrg.doDelete(ut.getId());
        Mockito.when(mockedRequest.getParameter("richiestaModEventoOrg")).thenReturn(null);
        //??
        Mockito.verify(mockedRequest).setAttribute("messaggio","esecuzione richeistaModifcia andata a buon fine");
    }
    /** Operazione di riferimento nel Test Plan: Ricerca Eventi
     * Caso: corretto
     */
    @Test
    public  void ricercaEventiTestIntegrazione(){
        //salviamo l'utente che compie l'azione
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        daoOrg.doSave(ut);
        //salva evento che verrà modificato
        EventoDAO daoEv= new EventoDAOImpl();
        EventoBean evento=new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        daoEv.doSave(evento);
        //simuliamo con mockito i parametro passati alla classe controller attraverso la request
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(null);
        Mockito.when(mockedRequest.getParameter("ricercaEventi")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("query")).thenReturn("nomeNuovo");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daoOrg.doDelete(ut.getId());
        Mockito.when(mockedRequest.getParameter("ricercaEventi")).thenReturn(null);
        //??
        Mockito.verify(mockedRequest).setAttribute("messaggio","esecuzione ricerca andata a buon fine");
    }
    /** Operazione di riferimento nei Requisiti Funzionali: visualizza Evento
     * Caso: Corretto
     */
    @Test
    public void visualizzaEventoTestIntegrazione(){
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        EventoDAO daoEv= new EventoDAOImpl();
        BigliettoDAO daoBigl= new BigliettoDAOImpl();
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        daoOrg.doSave(ut);
        EventoBean bean= new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome",pathReal+"fotoSample.jpg","descrizione","indirizzo","sede",3,true);
        daoEv.doSave(bean);
        daoEv.doUpdateAttivazioneEvento(bean.getId(), true);
        for (int i=0; i<3;i++){
            daoBigl.doSave(bean.getId(),5.5);
        }
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        Mockito.when(mockedRequest.getParameter("detailsE")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idE")).thenReturn(bean.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daoOrg.doDelete(ut.getId());
        Mockito.when(mockedRequest.getParameter("detailsE")).thenReturn(null);
        Mockito.verify(mockedRequest).setAttribute("prezzoBigl",5.5);
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta inserimento
     * Caso: Corretto
     */
    @Test
    public void accettaRichiestaInserimentoTestIntegrazione(){
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        AmministratoreDAOImpl daoAmm= new AmministratoreDAOImpl();
        EventoDAO daoEv= new EventoDAOImpl();
        BigliettoDAO daoBigl= new BigliettoDAOImpl();
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        daoOrg.doSave(ut);
        EventoBean bean= new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome",pathReal+"fotoSample.jpg","descrizione","indirizzo","sede",3,true);
        daoEv.doSave(bean);
        for (int i=0; i<3;i++){
            daoBigl.doSave(bean.getId(),5.5);
        }

        AmministratoreBean beanAmm= new AmministratoreBean("nome","cognome","provaEmail@email.com","Password.1",false);
        daoAmm.doSave(beanAmm);
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(beanAmm);
        Mockito.when(mockedRequest.getParameter("accettaIns")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEvento")).thenReturn(bean.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        daoOrg.doDelete(ut.getId());
        daoAmm.doDelete(beanAmm.getId());
        Mockito.when(mockedRequest.getParameter("accettaIns")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","attivazione evento avvenuta con successo");

    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta inserimento
     * Caso: Corretto
     */
    @Test
    public void rifiutaInserimentoTestIntegrazione(){
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        AmministratoreDAOImpl daoAmm= new AmministratoreDAOImpl();
        EventoDAO daoEv= new EventoDAOImpl();
        BigliettoDAO daoBigl= new BigliettoDAOImpl();
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        daoOrg.doSave(ut);

        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        image= new File(pathReal+"fotoSampleRimuovi.jpg");
        try {
            ImageIO.write(bi,"jpg",image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EventoBean bean= new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome",pathReal+"fotoSampleRimuovi.jpg","descrizione","indirizzo","sede",3,true);
        daoEv.doSave(bean);
        for (int i=0; i<3;i++){
            daoBigl.doSave(bean.getId(),5.5);
        }

        AmministratoreBean beanAmm= new AmministratoreBean("nome","cognome","provaEmail@email.com","Password.1",false);
        daoAmm.doSave(beanAmm);

        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(beanAmm);
        Mockito.when(mockedRequest.getParameter("rifiutaIns")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEvento")).thenReturn(bean.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        daoOrg.doDelete(ut.getId());
        daoAmm.doDelete(beanAmm.getId());

        Mockito.when(mockedRequest.getParameter("rifiutaIns")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","rimozione evento avvenuta con successo");
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta modifica
     * Caso: Corretto
     */
    @Test
    public void accettaModificaTestIntegrazione(){
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        AmministratoreDAOImpl daoAmm= new AmministratoreDAOImpl();
        EventoDAO daoEv= new EventoDAOImpl();
        BigliettoDAO daoBigl= new BigliettoDAOImpl();
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        daoOrg.doSave(ut);
        EventoBean bean=  new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome",pathReal+"fotoSample.jpg","descrizione","indirizzo","sede",3,true);
        daoEv.doSave(bean);
        for (int i=0; i<3;i++){
            daoBigl.doSave(bean.getId(),5.5);
        }
        daoEv.doUpdateAttivazioneEvento(bean.getId(), true);
        int idPre= bean.getId();
        bean.setDescrizione("nuova desc");
        daoEv.doSave(bean);
        daoEv.doSaveRichiestaModificaEv(idPre,bean.getId(),5.5);

        AmministratoreBean beanAmm= new AmministratoreBean("nome","cognome","provaEmail@email.com","Password.1",false);
        daoAmm.doSave(beanAmm);

        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(beanAmm);
        Mockito.when(mockedRequest.getParameter("accettaMod")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEvento")).thenReturn(bean.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        daoOrg.doDelete(ut.getId());
        daoAmm.doDelete(beanAmm.getId());

        Mockito.when(mockedRequest.getParameter("accettaMod")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","modifica evento accettata con successo");
    }

    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta modifica
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean rifiutaModifica(int idEvento, String tipoUtente) )
     */
    @Test
    public void rifiutaModificaTestIntegrazione(){
        OrganizzatoreDAOImpl daoOrg= new OrganizzatoreDAOImpl();
        AmministratoreDAOImpl daoAmm= new AmministratoreDAOImpl();
        EventoDAO daoEv= new EventoDAOImpl();
        BigliettoDAO daoBigl= new BigliettoDAOImpl();
        UtenteRegistratoBean ut=new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        daoOrg.doSave(ut);
        EventoBean bean=  new EventoBean(ut.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome",pathReal+"fotoSample.jpg","descrizione","indirizzo","sede",3,true);
        daoEv.doSave(bean);
        for (int i=0; i<3;i++){
            daoBigl.doSave(bean.getId(),5.5);
        }
        daoEv.doUpdateAttivazioneEvento(bean.getId(), true);
        int idPre= bean.getId();
        bean.setDescrizione("nuova desc");
        daoEv.doSave(bean);
        daoEv.doSaveRichiestaModificaEv(idPre,bean.getId(),5.5);

        AmministratoreBean beanAmm= new AmministratoreBean("nome","cognome","provaEmail@email.com","Password.1",false);
        daoAmm.doSave(beanAmm);

        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(beanAmm);
        Mockito.when(mockedRequest.getParameter("rifiutaMod")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idEvento")).thenReturn(bean.getId()+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daoOrg.doDelete(ut.getId());
        daoAmm.doDelete(beanAmm.getId());
        Mockito.when(mockedRequest.getParameter("rifiutaMod")).thenReturn("true");
        Mockito.verify(session).setAttribute("messaggio","modifica evento rifiutata con successo");
    }
    /**
     * Cleanup the environment.
     */
    @AfterClass
    public static void tearDown(){
        mockedRequest = null;
        mockedResponse = null;
        mockedServletContext = null;
        mockedDispatcher = null;
        session = null;
        FILE_PHOTO=null;
        //rimozione immagine creata per le classi di test
        image.delete();

        //etc etc mancano dati
    }

}
