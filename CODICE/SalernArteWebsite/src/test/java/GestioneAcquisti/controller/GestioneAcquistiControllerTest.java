package GestioneAcquisti.controller;

import com.google.gson.Gson;
import gestioneAcquisti.utils.JSONAggiungiAlCarrello;
import gestioneAcquisti.utils.JSONUpdateQuantitaCarrello;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GestioneAcquistiControllerTest {
    public JSONAggiungiAlCarrello aggiungiAlCarrello;
    public JSONUpdateQuantitaCarrello updateQuantitaCarrello;
    private HttpServletRequest mockedRequest;
    private HttpServletResponse mockedResponse;
    private ServletContext mockedServletContext;
    private RequestDispatcher mockedDispatcher;
    private HttpSession mockedsession;
    private Gson gson;

/*
    @Before
    public void setUp(){
        aggiungiAlCarrello = new JSONAggiungiAlCarrello();
        updateQuantitaCarrello = new JSONUpdateQuantitaCarrello();
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedServletContext = Mockito.mock(ServletContext.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        mockedsession = Mockito.mock(HttpSession.class);
        gson = Mockito.mock(Gson.class);
        UtenteRegistratoBean utente = new UtenteBean();
        utente.setId(3);
        utente.setTipoUtente("utente");
        utente.setEmail("emaildiprova@gmail.com");
        utente.setPasswordHash("password",false);

        Mockito.when(mockedRequest.getSession()).thenReturn(mockedsession);
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(utente);
    }

    public void TC_3p1_1() throws Exception{

        Mockito.when(mockedRequest.getParameter("aggiungi-al-carrello")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idE")).thenReturn("2");
        Mockito.when(mockedRequest.getParameter("quantita")).thenReturn("0");
        IOException exception;
        exception = assertThrows(IOException.class, ()-> aggiungiAlCarrello.doPost(mockedRequest, mockedResponse));
        String message = "formato dati errato";
        assertEquals(message, exception.getMessage());
    }
    public void TC_3p1_2() throws Exception{
        Mockito.when(mockedRequest.getParameter("aggiungi-al-carrello")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("idE")).thenReturn("2");
        Mockito.when(mockedRequest.getParameter("quantita")).thenReturn("2");

        //CarrelloBean bean = (CarrelloBean) mockedsession.getAttribute("carrello");
        aggiungiAlCarrello.doPost(mockedRequest,mockedResponse);
        Mockito.verify(mockedsession).setAttribute("notificaAll", "Biglietti aggiunti al carrello");
    }

    public void TC_3p2_1() throws Exception{
        Mockito.when(mockedRequest.getParameter("update-carr-qta")).thenReturn("true");
        Mockito.when(mockedsession.getAttribute("carrello")).thenReturn("carrelloSessione");
        Mockito.when(mockedRequest.getParameter("idE")).thenReturn("2");
        Mockito.when(mockedRequest.getParameter("qta")).thenReturn("0");
        Exception e;
        e = assertThrows(Exception.class, () -> updateQuantitaCarrello.doPost(mockedRequest,mockedResponse));
        String message = "quantità non valida";
        assertEquals(message, e.getMessage());
    }

    public void TC_3p2_2() throws Exception{

        Mockito.when(mockedRequest.getParameter("update-carr-qta")).thenReturn("true");
        Mockito.when(mockedsession.getAttribute("carrello")).thenReturn("carrelloSessione");
        Mockito.when(mockedRequest.getParameter("idE")).thenReturn("2");
        Mockito.when(mockedRequest.getParameter("qta")).thenReturn("2");

        updateQuantitaCarrello.doPost(mockedRequest,mockedResponse);
        //boolean result = assertTrue(gson.fromJson("true", boolean));
      //  assertEquals(result,true);

    }

    @After
    public void tearDown(){
        aggiungiAlCarrello= null;
        updateQuantitaCarrello = null;
        mockedDispatcher = null;
        mockedResponse = null;
        mockedRequest = null;
        mockedServletContext = null;
        mockedsession = null;
    }
*/
}
