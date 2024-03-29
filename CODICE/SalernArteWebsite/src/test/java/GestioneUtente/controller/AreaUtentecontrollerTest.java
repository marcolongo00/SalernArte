package GestioneUtente.controller;


import autenticazione.controller.AreaUtenteController;
import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import org.junit.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.*;


/**
 * Implementa il testing di Integrazione per la classe
 * AreaUtenteController.
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
 * @author Marco Longo
 */
public class AreaUtentecontrollerTest {
    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static ServletContext mockedServletContext;
    private static RequestDispatcher mockedDispatcher;
    private static HttpSession session;
    private static UtenteRegistratoDAO dao;
    private static ServletConfig servletConfig = mock(ServletConfig.class);
    private static AreaUtenteController servlet ;
    private static DateFormat df;
    private static final String NOME = "Marco", COGNOME = "Longo", EMAIL = "emailprova@gmail.com", PASSWORD = "Passworddiprova.10",
                                BIOGRAFIA = "Biografia", IBAN = "IT51Y0300203280575326347619", ISTITUTO = "ISS Gian Camillo Glorioso";
    private static final int GENDER = 0;
    private static final Date DATADINASCITA = Date.valueOf("2000-01-01");

    @Before
    public void setUp() throws ServletException {
        df = new SimpleDateFormat("yyyy-MM-dd");
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet= new AreaUtenteController();
        servlet.init(servletConfig);
        mockedRequest = mock(HttpServletRequest.class);
        mockedResponse = mock(HttpServletResponse.class);
        mockedServletContext = mock(ServletContext.class);
        mockedDispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);

        when(mockedRequest.getSession()).thenReturn(session);
        when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);
        when(mockedRequest.getRequestDispatcher(anyString())).thenReturn(mockedDispatcher);
        when(mockedRequest.getParameter("updateProfilo")).thenReturn("true");
    }

    /** Operazione di riferimento nel Test Plan: ModificaProfiloScolaresca
     * Caso: le password non coincidono
     */
    @Test
    public  void TC_1p8_4(){
        ScolarescaBean user = new ScolarescaBean(EMAIL,PASSWORD,ISTITUTO,false);
        dao = new ScolarescaDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD+"ddsd");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="Le password inserite non corrispondono";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloScolaresca
     * Caso: conferma password non valido
     */
    @Test
    public  void TC_1p8_5(){
        ScolarescaBean user = new ScolarescaBean(EMAIL,PASSWORD,ISTITUTO,false);
        dao = new ScolarescaDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn("ddsd");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="La password conferma non rispetta il formato";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloUtente
     * Caso: data di nascita non valida
     */
    @Test
    public  void TC_1p2_4(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        dao = new UtenteDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn("edsf");
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="la Data di Nascita non rispetta il formato";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloUtente
     * Caso: VecchiaPassword non valida
     */
    @Test
    public  void TC_1p2_8(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        dao = new UtenteDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn("shbf");
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="La password vecchia non è valida";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloUtente
     * Caso: Le password non coincidono
     */
    @Test
    public  void TC_1p2_10(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        dao = new UtenteDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD+"a");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="Le password inserite non corrispondono";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloUtente
     * Caso: conferma password non valido
     */
    @Test
    public  void TC_1p2_11(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        dao = new UtenteDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn("iyfsd");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        String message="La password conferma non rispetta il formato";
        verify(session).setAttribute("messaggio",message);
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloAmministratore
     * Caso: Le password non coincidono
     */
    @Test
    public  void TC_1p6_5(){
        AmministratoreBean user = new AmministratoreBean(NOME,COGNOME,EMAIL,PASSWORD,false);
        dao = new AmministratoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD+"a");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Le password inserite non corrispondono");
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloAmministratore
     * Caso: Il conferma password non è valido
     */
    @Test
    public  void TC_1p6_6(){
        AmministratoreBean user = new AmministratoreBean(NOME,COGNOME,EMAIL,PASSWORD,false);
        dao = new AmministratoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn("a");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","La password conferma non rispetta il formato");
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloOrganizzatore
     * Caso: Data di nascita non valida
     */
    @Test
    public  void TC_1p4_4(){
        OrganizzatoreBean user = new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD,BIOGRAFIA,DATADINASCITA,false);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn("");
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        when(mockedRequest.getParameter("iban")).thenReturn(IBAN);
        when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","la Data di Nascita non rispetta il formato");
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloOrganizzatore
     * Caso: Le password non coincidono
     */
    @Test
    public  void TC_1p4_10(){
        OrganizzatoreBean user = new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD,BIOGRAFIA,DATADINASCITA,false);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD+"a");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        when(mockedRequest.getParameter("iban")).thenReturn(IBAN);
        when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Le password inserite non corrispondono");
    }
    /** Operazione di riferimento nel Test Plan: ModificaProfiloOrganizzatore
     * Caso: Il conferma password non è valido
     */
    @Test
    public  void TC_1p4_11(){
        OrganizzatoreBean user = new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD,BIOGRAFIA,DATADINASCITA,false);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn("a");
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        when(mockedRequest.getParameter("iban")).thenReturn(IBAN);
        when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","La password conferma non rispetta il formato");
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo utente
     * Caso: Corretto
     */
    @Test
    public void updateProfiloTest(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        dao = new UtenteDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Update utente avvenuto con successo");
    }
    /** Operazione di riferimento nel Test Plan: Modifica Profilo scolaresca
     * Caso: Corretto
     */
    @Test
    public void updateScolarescaTest(){
        ScolarescaBean user = new ScolarescaBean(EMAIL,PASSWORD,ISTITUTO,false);
        dao = new ScolarescaDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Update scolaresca avvenuto con successo");
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore
     * Caso: Corretto
     */
    @Test
    public void updateOrganizzatoreTest(){
        OrganizzatoreBean user = new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD,BIOGRAFIA,DATADINASCITA,false);
        dao = new OrganizzatoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        when(mockedRequest.getParameter("iban")).thenReturn(IBAN);
        when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Update organizzatore avvenuto con successo");
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore
     * Caso: Corretto
     */
    @Test
    public void updateAmministratoreTest(){
        AmministratoreBean user = new AmministratoreBean(NOME,COGNOME,EMAIL,PASSWORD,false);
        dao = new AmministratoreDAOImpl();
        dao.doSave(user);

        when(session.getAttribute("selezionato")).thenReturn(user);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
        verify(session).setAttribute("messaggio","Update amministratore avvenuto con successo");
    }

    /**
     * Cleanup the environment.
     */
    @After
    public void tearDown(){
        mockedRequest = null;
        mockedResponse = null;
        mockedServletContext = null;
        mockedDispatcher = null;
        session = null;
        servlet = null;
        dao = null;
        servletConfig = null;
    }
}
