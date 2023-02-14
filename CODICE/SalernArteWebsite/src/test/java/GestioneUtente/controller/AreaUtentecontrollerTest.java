package GestioneUtente.controller;


import autenticazione.controller.AreaUtenteController;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import registrazione.controller.RegistrazioneController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    private static final ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
    private static final AreaUtenteController servlet = new AreaUtenteController();

    @BeforeClass
    public static void setUp() throws ServletException {
        //Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet.init(servletConfig);
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedServletContext = Mockito.mock(ServletContext.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);
        Mockito.when(mockedRequest.getRequestDispatcher(Mockito.anyString())).thenReturn(mockedDispatcher);

    }
    /** Operazione di riferimento nel Test Plan:
     * Caso:
     */
    @Test
    public void nomeOperazioneTestIntegrazione(){

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
        //etc etc mancano dati
    }
}
