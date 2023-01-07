package GestioneAcquisti.controller;

import gestioneAcquisti.controller.GestioneAcquistiController;
import model.entity.UtenteRegistratoBean;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.Request;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GestioneAcquistiControllerTest {
    public GestioneAcquistiController servlet;
    private HttpServletRequest mockedRequest;
    private HttpServletResponse mockedResponse;
    private ServletContext mockedServletContext;
    private RequestDispatcher mockedDispatcher;
    private HttpSession session;
    private UtenteRegistratoBean utenteLoggato;

    @Before
    public void setUp(){
        servlet = new GestioneAcquistiController();
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedServletContext = Mockito.mock(ServletContext.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);
    }

    @After
    public void tearDown(){
        servlet = null;
        mockedDispatcher = null;
        mockedResponse = null;
        mockedRequest = null;
        mockedServletContext = null;
        session = null;
    }

}
