package GestioneUtente.controller;
import model.dao.OrganizzatoreDAOImpl;
import model.dao.ScolarescaDAOImpl;
import model.dao.UtenteDAOImpl;
import model.dao.UtenteRegistratoDAO;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import org.junit.*;
import org.mockito.Mockito;
import registrazione.controller.RegistrazioneController;

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
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Implementa il testing di Integrazione per la classe
 * RegistrazioneController.
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

public class RegistrazioneControllerTest {
    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static ServletContext mockedServletContext;
    private static RequestDispatcher mockedDispatcher;
    private static HttpSession session;
    private static DateFormat df;
    private static final ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
    private static final RegistrazioneController servlet = new RegistrazioneController();

    private static  final int GENDER=1;
    private  static  final String NOME= "Pippo";
    private  static  final String COGNOME= "Cognome";
    private  static  final String EMAIL= "pippoEmailTest@gmail.com";
    private  static  final String PASSWORD_NOT_HASH= "Password.01";
    private  static  final Date DATA_DI_NASCITA= Date.valueOf("1999-08-04");
    private static  final String IBAN= "IT09I0300203280825518556348";
    private static final String BIOGRAFIA= "esampio biografia";
    private static  final String ISTITUTO= "nome istituto";

    private static  Date DATA_ATTUALE;

    @Before
    public void setUp() throws ServletException {
        df = new SimpleDateFormat("yyyy-MM-dd");
        DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getRequestDispatcher(Mockito.anyString())).thenReturn(mockedDispatcher);
        Mockito.when(mockedRequest.getParameter("registrazione")).thenReturn("true");
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: data di nascita non valida
     */
     @Test
    public void TC_1p1_4(){
         Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("utente");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);
         try {
             servlet.doPost(mockedRequest,mockedResponse);
         } catch (ServletException e) {
             throw new RuntimeException(e);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
          UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","la Data di Nascita non rispetta il formato");


    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: le password non coincidono
     */
     @Test
    public void TC_1p1_9(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("PasswordDiversa.2");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("utente");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

         try {
             servlet.doPost(mockedRequest,mockedResponse);
         } catch (ServletException e) {
             throw new RuntimeException(e);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         UtenteRegistratoDAO dao= new UtenteDAOImpl();
         UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
         if(bean!=null){
             dao.doDelete(bean.getId());
         }
         Mockito.verify(session).setAttribute("messaggio","Le due password inserite non corrispondono, riprovare");

    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: formato conferma password non valido
     */
    @Test
    public void TC_1p1_10(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("utente");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","La password conferma non rispetta il formato");

    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: data di nascita non valida
     */
    @Test
    public void TC_1p5_4(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("organizzatore");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);
        Mockito.when(mockedRequest.getParameter("iban")).thenReturn(IBAN);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","la Data di Nascita non rispetta il formato");

    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: le password non coincidono
     */
    @Test
    public void TC_1p5_10(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("PasswordDiversa.1");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("organizzatore");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);
        Mockito.when(mockedRequest.getParameter("iban")).thenReturn(IBAN);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","Le due password inserite non corrispondono, riprovare");

    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: il conferma password non è valido
     */
    @Test
    public void TC_1p5_11(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("organizzatore");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);
        Mockito.when(mockedRequest.getParameter("iban")).thenReturn(IBAN);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","La password conferma non rispetta il formato");

    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: le password non coincidono
     */
    @Test
    public void TC_1p7_4(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("PasswordDiversa.1");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("scolaresca");
        Mockito.when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        String message="Le due password inserite non corrispondono, riprovare";
        Mockito.verify(session).setAttribute("messaggio",message);
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: conferma password non valida
     */
     @Test
    public void TC_1p7_5(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("scolaresca");
        Mockito.when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

         try {
             servlet.doPost(mockedRequest,mockedResponse);
         } catch (ServletException e) {
             throw new RuntimeException(e);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         UtenteRegistratoDAO dao= new UtenteDAOImpl();
         UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
         if(bean!=null){
             dao.doDelete(bean.getId());
         }
         String message="La password conferma non rispetta il formato";
         Mockito.verify(session).setAttribute("messaggio",message);
    }

    /** Operazione di riferimento nel Test Plan: Registrazione utente
     * Caso: Corretto
     */

    @Test
    public void registrazioneUtenteTestIntegrazione(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("utente");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UtenteRegistratoDAO dao= new UtenteDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","registrazione utente andata a buon fine");
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: Corretto
     */

    @Test
    public void registrazioneOrganizzatoreTestIntegrazione(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("organizzatore");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn(NOME);
        Mockito.when(mockedRequest.getParameter("cognome")).thenReturn(COGNOME);
        Mockito.when(mockedRequest.getParameter("dataDiNascita")).thenReturn(df.format(DATA_DI_NASCITA));
        Mockito.when(mockedRequest.getParameter("gender")).thenReturn(GENDER+"");
        Mockito.when(mockedRequest.getParameter("biografia")).thenReturn(BIOGRAFIA);
        Mockito.when(mockedRequest.getParameter("iban")).thenReturn(IBAN);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UtenteRegistratoDAO dao= new OrganizzatoreDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","registrazione organizzatore andata a buon fine");

    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: Corretto
     */

    @Test
    public void registrazioneScolarescaTestIntegrazione(){
        Mockito.when(mockedRequest.getParameter("password")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("passwordConferma")).thenReturn(PASSWORD_NOT_HASH);
        Mockito.when(mockedRequest.getParameter("email")).thenReturn(EMAIL);
        Mockito.when(mockedRequest.getParameter("tipoUtente")).thenReturn("scolaresca");
        Mockito.when(mockedRequest.getParameter("istituto")).thenReturn(ISTITUTO);
        Mockito.when(session.getAttribute("carrello")).thenReturn(null);

        try {
            servlet.doPost(mockedRequest,mockedResponse);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UtenteRegistratoDAO dao= new ScolarescaDAOImpl();
        UtenteRegistratoBean bean= dao.doRetrieveByEmail(EMAIL);
        if(bean!=null){
            dao.doDelete(bean.getId());
        }
        Mockito.verify(session).setAttribute("messaggio","registrazione scolaresca andata a buon fine");

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
        //etc etc mancano dati
    }
}
