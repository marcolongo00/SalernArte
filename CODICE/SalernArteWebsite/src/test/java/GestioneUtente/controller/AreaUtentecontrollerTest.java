package GestioneUtente.controller;


import autenticazione.controller.AreaUtenteController;
import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
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
    private static AreaUtenteController servlet = new AreaUtenteController();
    private static final String NOME = "Marco", COGNOME = "Longo", EMAIL = "emailprova@gmail.com", PASSWORD = "Passworddiprova.10",
                                BIOGRAFIA = "Biografia", IBAN = "IT51Y0300203280575326347619", ISTITUTO = "ISS Gian Camillo Glorioso";
    private static final int GENDER = 0;
    private static final Date DATADINASCITA = Date.valueOf("2000-01-01");

    @BeforeClass
    public static void setUp() throws ServletException {
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = mock(HttpServletRequest.class);
        mockedResponse = mock(HttpServletResponse.class);
        mockedServletContext = mock(ServletContext.class);
        mockedDispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);
        servlet = mock(AreaUtenteController.class);

        when(mockedRequest.getSession()).thenReturn(session);
        when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);
        when(mockedRequest.getRequestDispatcher(anyString())).thenReturn(mockedDispatcher);
        when(mockedRequest.getParameter("updateProfilo")).thenReturn("true");
    }
    /** Operazione di riferimento nel Test Plan: Modifica Profilo utente
     * Caso: Corretto
     */
    @Test
    public void updateProfiloTest(){
        UtenteBean user = new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD,DATADINASCITA,false);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(String.valueOf(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(String.valueOf(GENDER));

        dao = new UtenteDAOImpl();
        dao.doSave(user);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
    }
    /** Operazione di riferimento nel Test Plan: Modifica Profilo scolaresca
     * Caso: Corretto
     */
    @Test
    public void updateScolarescaTest(){
        ScolarescaBean user = new ScolarescaBean(EMAIL,PASSWORD,ISTITUTO,false);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);

        dao = new ScolarescaDAOImpl();
        dao.doSave(user);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore
     * Caso: Corretto
     */
    @Test
    public void updateOrganizzatoreTest(){
        OrganizzatoreBean user = new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD,BIOGRAFIA,DATADINASCITA,false);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        when(mockedRequest.getParameter("dataDiNascita")).thenReturn(String.valueOf(DATADINASCITA));
        when(mockedRequest.getParameter("gender")).thenReturn(String.valueOf(GENDER));
        when(mockedRequest.getParameter("iban")).thenReturn(IBAN);
        when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);

        dao = new OrganizzatoreDAOImpl();
        dao.doSave(user);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore
     * Caso: Corretto
     */
    @Test
    public void updateAmministratoreTest(){
        AmministratoreBean user = new AmministratoreBean(NOME,COGNOME,EMAIL,PASSWORD,false);
        when(mockedRequest.getParameter("password")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD);
        when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);

        dao = new AmministratoreDAOImpl();
        dao.doSave(user);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao.doDelete(user.getId());
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
        servlet = null;
        dao = null;
        servletConfig = null;
    }
}
