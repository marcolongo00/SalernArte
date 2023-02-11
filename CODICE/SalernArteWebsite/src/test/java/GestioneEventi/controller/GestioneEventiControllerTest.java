package GestioneEventi.controller;

import gestioneEventi.controller.GestioneEventiController;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.web.multipart.MultipartFile;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import static org.junit.Assert.assertEquals;
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
 *  i restanti metodi di test faranno riferimento all'operazione all'interno della classe da testare.
 *  si seguirà la seguente regola:
 *
 *  preso in considerazione un metodo della classe GestioneEventiServiceImpl che abbia firma:
 *              ReturnValue nomeMetodo( Type value, ...)
 *
 *  il metodo all'interno di questa classe di test avrà come firma:
 *              void nomeMetodoTestIntegrazione()
 *  nel caso di test di funzionalità di errore:
 *              void nomeMetodoTestIntegrazioneError()
 *
 * @author Alessia Della Pepa
 */

public class GestioneEventiControllerTest {
    public static GestioneEventiController servlet;
    private static HttpServletRequest mockedRequest;
    private static HttpServletResponse mockedResponse;
    private static ServletContext mockedServletContext;
    private static RequestDispatcher mockedDispatcher;
    private static Date DATA_INIZIO_EVENTO;
    private static  Date DATA_FINE_EVENTO;
    private static final Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
    private static HttpSession session;
    private UtenteRegistratoBean utenteLoggato ;
    MockMultipartFile fileFoto;
    private static final String NOME = "EventoTennis"; //etc

    @BeforeClass
    public static void setUp(){
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

        Mockito.when(mockedRequest.getSession()).thenReturn(session);
        Mockito.when(mockedRequest.getServletContext()).thenReturn(mockedServletContext);

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        //save utente in db

    }
    @Ignore
    @Test
    public void TC_2p1_8(){

    }
    @Ignore
    @Test
    public void TC_2p1_10(){

    }
    @Ignore
    @Test
    public void TC_2p2_8(){

    }
    @Ignore
    @Test
    public void TC_2p2_10(){

    }
    /**
     * Operazione di riferimento: Richiesta Inserimento Evento
     * Caso: il tipoEvento non rispetta il formato
     */
   @Ignore
    @Test
   public void TC_2p1_1() throws Exception {
        UtenteRegistratoBean ut=new OrganizzatoreBean(1,0,"IT17J0300203280772191565161","pluto","prova","plutoprova@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);
        //forniamo i parametri per il nostro caso di test
        Mockito.when(mockedRequest.getSession().getAttribute("selezionato")).thenReturn(ut);
        /*
        //non finito
         //forniamo i parametri per il nostro caso di test
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/gestione-eventi")
                        .file(fileFoto)
                .param("inviaRichiestaEvnto","true")).andExpect(view().name("redirect:/pagina"));

         */
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        File image= new File("C:\\Users\\aless\\Desktop\\SalernArte\\CODICE\\SalernArteWebsite\\src\\main\\webapp\\immaginiEventi\\provaCreazione.jpg");
        ImageIO.write(bi,"jpg",image);
        //image.delete();
        DateFormat df = new SimpleDateFormat("yyyy/mm/dd");
        String text = df.format(DATA_ATTUALE);
        Mockito.when(mockedRequest.getParameter("inviaRichiestaEvento")).thenReturn("true");
        Mockito.when(mockedRequest.getParameter("dataInizio")).thenReturn(df.format(DATA_INIZIO_EVENTO));
        Mockito.when(mockedRequest.getParameter("dataFine")).thenReturn(df.format(DATA_FINE_EVENTO));
        Mockito.when(mockedRequest.getParameter("nome")).thenReturn("nomeEvento");
            Mockito.when(mockedRequest.getParameter("tipoEvento")).thenReturn("teatro");
        Mockito.when(mockedRequest.getParameter("numBiglietti")).thenReturn("4");
        Mockito.when(mockedRequest.getParameter("prezzo")).thenReturn("4");
        Path path= Paths.get("C:\\Users\\aless\\Desktop\\SalernArte\\CODICE\\SalernArteWebsite\\src\\main\\webapp\\immaginiEventi\\provaCreazione.jpg");

        try {
            Mockito.when(mockedRequest.getPart("path")).thenReturn(new MockPart("provaCrezione","provaCreazione.jpg",Files.readAllBytes(path)));
        } catch (ServletException e) {
            throw new IOException("path non valido");
        }

        Mockito.when(mockedServletContext.getAttribute("pathNewEventi")).thenReturn("C:\\Users\\aless\\Desktop\\SalernArte\\CODICE\\SalernArteWebsite\\src\\main\\webapp\\immaginiEventi\\");
        Mockito.when(mockedRequest.getParameter("desc")).thenReturn("descrizione evento");
        Mockito.when(mockedRequest.getParameter("indirizzo")).thenReturn("indirizzo evento");
        Mockito.when(mockedRequest.getParameter("sede")).thenReturn("sede evento");
        RuntimeException exception;
        exception= assertThrows(RuntimeException.class,()-> servlet.doPost(mockedRequest,mockedResponse));

        String message="path non valido";
        assertEquals(message,exception.getMessage());


    }
    /**
     * Cleanup the environment.
     */
    @AfterClass
    public static void tearDown(){
        servlet = null;
        mockedRequest = null;
        mockedResponse = null;
        mockedServletContext = null;
        mockedDispatcher = null;
        session = null;
        //etc etc mancano dati
    }

}
