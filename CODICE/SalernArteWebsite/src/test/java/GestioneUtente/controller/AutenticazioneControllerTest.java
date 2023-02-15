package GestioneUtente.controller;

import autenticazione.controller.AutenticazioneController;
import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Implementa il testing di Integrazione per la classe
 * AutenticazioneController.
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

public class AutenticazioneControllerTest {

    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static RequestDispatcher mockedDispatcher;
    private static HttpSession session;
    private static final ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
    private static final AutenticazioneController servlet = new AutenticazioneController();

    private static  final int GENDER=1;
    private  static  final String NOME= "Pippo";
    private  static  final String COGNOME= "Cognome";
    private  static  final String EMAIL= "pippoEmailTest@gmail.com";
    private  static  final String PASSWORD_NOT_HASH= "Password.01";
    private  static  final Date DATA_DI_NASCITA= Date.valueOf("1999-08-04");
    private static  final String IBAN= "IT09I0300203280825518556348";
    private static final String BIOGRAFIA= "esampio biografia";
    private static  final String ISTITUTO= "nome istituto";

    @BeforeClass
    public static void setUp() throws ServletException {
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getRequestDispatcher(Mockito.anyString())).thenReturn(mockedDispatcher);

    }
    /** Operazione di riferimento nel Test Plan: Login utente
     * Caso: Corretto
     */
    @Test
    public void loginUtenteTestIntegrazione(){
        mockedRequest.removeAttribute("messaggio");
        //salvimao l'utente che effettuerà il login
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteBean bean= new UtenteBean(GENDER,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,DATA_DI_NASCITA,false);
        dao.doSave(bean);
        //inizializiamo i valori della request
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dao.doDelete(bean.getId());
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","login utente andato a buon fine");
    }
    /** Operazione di riferimento nel Test Plan: Login Scolaresca
     * Caso: Corretto
     */
   @Test
    public void loginScolarescaTestIntegrazione(){
       mockedRequest.removeAttribute("messaggio");
        //salvimao l'utente che effettuerà il login
        UtenteRegistratoDAO dao= new ScolarescaDAOImpl();
        ScolarescaBean bean= new ScolarescaBean(EMAIL,PASSWORD_NOT_HASH,ISTITUTO,false);
         dao.doSave(bean);
        //inizializiamo i valori della request
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dao.doDelete(bean.getId());
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","login scolaresca andato a buon fine");
    }
    /** Operazione di riferimento nel Test Plan: Login Amministratore
     * Caso: Corretto
     */
    @Test
    public void loginAmministratoreTestIntegrazione(){
        mockedRequest.removeAttribute("messaggio");
        //salvimao l'utente che effettuerà il login
        UtenteRegistratoDAO dao= new AmministratoreDAOImpl();
        AmministratoreBean bean= new AmministratoreBean(NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,false);
        dao.doSave(bean);
        //inizializiamo i valori della request
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dao.doDelete(bean.getId());
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","login amministratore andato a buon fine");
    }
    /** Operazione di riferimento nel Test Plan: Login Organizzatore
     * Caso: Corretto
     */
    @Test
    public void loginOrganizzatoreTestIntegrazione(){
        mockedRequest.removeAttribute("messaggio");
        //salvimao l'utente che effettuerà il login
        UtenteRegistratoDAO dao= new OrganizzatoreDAOImpl();
        OrganizzatoreBean bean= new OrganizzatoreBean(GENDER,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA,false);
        dao.doSave(bean);
        //inizializiamo i valori della request
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);
        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dao.doDelete(bean.getId());
        Mockito.when(mockedRequest.getParameter("Accedi")).thenReturn(null);
        Mockito.verify(session).setAttribute("messaggio","login organizzatore andato a buon fine");
    }
    /**
     * Cleanup the environment.
     */
    @AfterClass
    public static void tearDown(){
        mockedRequest = null;
        mockedResponse = null;
        mockedDispatcher = null;
        session = null;
        //etc etc mancano dati
    }
}
