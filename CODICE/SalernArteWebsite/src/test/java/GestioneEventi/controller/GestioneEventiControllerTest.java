package GestioneEventi.controller;

import gestioneEventi.controller.GestioneEventiController;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Implementa il testing di unità per la classe
 * GestioneEventiController.
 * i nomi dei metodi di test faranno riferimento
 * ai TestCaseID riportati nel documenti di TestPlan
 *
 * visto che i nomi di emtodi non possono comprendere punti
 * si seguirà la seguente regola:
 * un punto all'interno di un TESTCASEID verrà identificato con il carattere "p".
 *
 * esempio:
 *          TestCaseID: TC_2.1_1
 *          firma del metodo: TC_2p1_1
 * @author Alessia Della Pepa
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GestioneEventiControllerTest {
    public GestioneEventiController servlet;
    private HttpServletRequest mockedRequest;
    private HttpServletResponse mockedResponse;
    private ServletContext mockedServletContext;
    private RequestDispatcher mockedDispatcher;
    private HttpSession session;
    private UtenteRegistratoBean utenteLoggato ;
    MockMultipartFile fileFoto;
    /**
     * Parameters declaration.
     */
    private static final String NOME = "EventoTennis"; //etc

    /**
     *  Setting up the enviroment.
     */
    @Before
    public void setUp(){
//Servlet, mockedRequest, mockedResponse and Session instantiation.
        servlet = new GestioneEventiController();
        mockedRequest = Mockito.mock(HttpServletRequest.class);
        mockedResponse = Mockito.mock(HttpServletResponse.class);
        mockedServletContext = Mockito.mock(ServletContext.class);
        mockedDispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);
        //l'utente verrà settato prima dell'operazione da testare perchè
        // a seconda dell'operazione cambierà il tipo di utente registrato
        // e verranno testati conseguentemente anche i permessi di accesso
        //UtenteRegistratoBean ut=new OrganizzatoreBean(5,0,"IT17J0300203280772191565161","pluto","prova","plutoprova@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);
        UtenteRegistratoBean ut=new OrganizzatoreBean(5,0,"IT17J0300203280772191565161","pluto","prova","plutoprova@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);
         //forniamo i parametri per il nostro caso di test

        fileFoto =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        "immagine di copertina".getBytes());
        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);

    }

    /**
     * Operazione di riferimento: Richiesta Inserimento Evento
     * Caso: il tipoEvento non rispetta il formato
     */
    @Test
   public void TC_2p1_1() throws Exception {
        //non finito
         //forniamo i parametri per il nostro caso di test
        /*this.mockMvc.perform(MockMvcRequestBuilders.multipart("/gestione-eventi")
                        .file(fileFoto)
                .param("inviaRichiestaEvnto","true")).andExpect(view().name("redirect:/pagina"));
       */
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        File image= new File("C:\\Users\\aless\\Desktop\\SalernArte\\CODICE\\SalernArteWebsite\\src\\main\\webapp\\immaginiEventi\\provaCreazione.jpg");
        ImageIO.write(bi,"jpg",image);
        image.delete();
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("dataInizio")).thenReturn("2023-03-03");
        Mockito.when(mockedRequest.getParameter("dataFine")).thenReturn("2023-03-23");
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn("nomeEvento");
            Mockito.when(mockedRequest.getParameter("tipoEvento")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("numBiglietti")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzo")).thenReturn("4");
        try {
            Mockito.when(mockedRequest.getPart("path")).thenReturn(null);
        } catch (ServletException e) {
            throw new IOException("path non valido");
        }

        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn("");
        Mockito.when(mockedRequest.getParameter("desc")).thenReturn("descrizione evento");
        Mockito.when(mockedRequest.getParameter("indirizzo")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sede")).thenReturn("sede evento");
        IOException exception;
        exception= assertThrows(IOException.class,()-> servlet.doPost(mockedRequest,mockedResponse));
        String message="path non valido";
        assertEquals(message,exception.getMessage());

    }
    /**
     * Cleanup the environment.
     */
    @AfterAll
    public void tearDown(){
        servlet = null;
        mockedRequest = null;
        mockedResponse = null;
        mockedServletContext = null;
        mockedDispatcher = null;
        session = null;
        //etc etc mancano dati
    }

}
