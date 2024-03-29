package GestioneEventi.service;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.BigliettoDAO;
import model.dao.BigliettoDAOImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;

import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;
import org.junit.*;
import org.mockito.Mockito;
import org.springframework.mock.web.MockPart;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Implementa il testing di unità per la classe
 * GestioneEventiServiceImpl.
 * i nomi dei metodi di test faranno riferimento
 * ai TestCaseID riportati nel documenti di TestPlan
 *
 * visto che i nomi di metodi non possono comprendere punti
 * si seguirà la seguente regola:
 * un punto all'interno di un TESTCASEID verrà identificato con il carattere "p".
 *
 * esempio:
 *          TestCaseID: TC_2.1_1
 *          firma del metodo: TC_2p1_1
 * @author Alessia Della Pepa
 */
public class GestioneEventiServiceImplTest {
    private static GestioneEventiService serviceE;
    private static EventoDAO mockedEventoDao;
    private  static BigliettoDAO mockedBigliettoDao;
    private static OrganizzatoreBean organizzatore;
    private static File image;
    private static Date DATA_INIZIO_EVENTO;
    private static  Date DATA_FINE_EVENTO;
    private static Part FILE_PHOTO;
    private static EventoBean eventoDaModificare;

    private static  final int NUM_BIGLIETTI =5;
    private static final double PREZZO_BIGLIETTO= 5.5;
    private static final Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
    private static final String NOME_EVENTO="Evento di Test";
    private static final String TIPO_EVENTO="mostra";
    private static final String DESCRIZIONE_EVENTO="descrizione evento di Test";
    private static final String INDIRIZZO= "indirizzo di Test";
    private static final String SEDE="sede di Test";
    private static String query="nome evento*";

    private static final String pathReal= ".\\src\\main\\webapp\\immaginiEventi\\";
    @BeforeClass
    public static void startUp(){

        mockedEventoDao= Mockito.mock(EventoDAOImpl.class);
        mockedBigliettoDao=Mockito.mock(BigliettoDAOImpl.class);
        eventoDaModificare= new EventoBean(1,1,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/fotoSample.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        ArrayList<EventoBean> lista= new ArrayList<EventoBean>();
        lista.add(eventoDaModificare);
        Mockito.when(mockedEventoDao.doRetrieveByNomeOrDescrizione(Mockito.anyString())).thenReturn(lista);
        serviceE=new GestioneEventiServiceImpl(mockedEventoDao,mockedBigliettoDao);
        //creazione file foto da utilizzare per i test
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        image= new File(pathReal+"fotoSample.jpg");
        try {
            ImageIO.write(bi,"jpg",image);
            Path path= Paths.get(pathReal+"fotoSample.jpg");
            FILE_PHOTO= new MockPart("fotoSample.jpg","fotoSample.jpg", Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //creiamo i valori di data inizio e data fine di riferimento epr le classi di test considerando dinamicamente la data attual
        // al momento in cui viene effettuato il test ed aggiungendo 5 giorni dalla data attuale per la data di inizio
        //ed altri 10 giorni per la data di fine. creando in questo modo un evento successivo alla data odierna che dura 10 giorni
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());
        //inseriamo temporaneamente nel database le classi di cui abbiamo bisogno, come un organizzatore/amministratore per controllare varie operazioni
        organizzatore= new OrganizzatoreBean(1,2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);

    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il tipoEvento non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
    */
    @Test
    public void TC_2p1_1(){
        //forniamo i dati dell'evento
        String tipoEvento="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,tipoEvento,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato tipo evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il nome evento non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_2(){
        //forniamo i dati dell'evento
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),nome,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato nome evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la descrizione evento non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_3(){
        //forniamo i dati dell'evento
        String desc="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,desc,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato descrizione evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il file Foto Evento ha una grandezza superiore ai 15MB
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_4(){
        byte[] res = new byte[16*1024*1024];
        Part gronde=new MockPart("fotoSample.jpg","fotoSample.jpg", res);

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",gronde,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato file foto non corretto";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il file Foto Evento ha una dimensione maggiore di 1024x768 pixel
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_5() {
        //creazione file foto da utilizzare per i test
        BufferedImage bi= new BufferedImage(1080,1080,BufferedImage.TYPE_INT_RGB);
        File immagineGrande= new File(pathReal+"fotoSampleGrande.jpg");
        Part mockPart;
        try {
            ImageIO.write(bi,"jpg",immagineGrande);
            Path path= Paths.get(pathReal+"fotoSampleGrande.jpg");
            mockPart= new MockPart("fotoSampleGrande.jpg","fotoSampleGrande.jpg", Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSampleGrande.jpg",mockPart,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato file foto non corretto";
        immagineGrande.delete();
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il Numero Biglietti non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_6(){
        //forniamo i dati dell'evento
        int numBigl=0;
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,numBigl,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato numero biglietto e/o prezzo biglietti errato";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il Prezzo Biglietti non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_7(){
        //forniamo i dati dell'evento
        double prezzoBigl=-2;
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,prezzoBigl,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato numero biglietto e/o prezzo biglietti errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Inizio non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Ignore
    @Test
    public void TC_2p1_8(){
       /*questo test viene effettuato nella classe GestioneEventiControllerTest
       in quanto nel rispettivo metodo la stringa contenente la data è stata
       già convertita (all'interno del controller) in java.sql.Date
        */
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Inizio inserita è precedente o contemporanea alla Data Attuale
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_9(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_ATTUALE,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="impostazioni data inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Fine non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Ignore
    @Test
    public void TC_2p1_10(){
       /*questo test viene effettuato nella classe GestioneEventiControllerTest
       in quanto nel rispettivo metodo la stringa contenente la data è stata
       già convertita (all'interno del controller) in java.sql.Date
        */
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Data Fine inserita è precedente o contemporanea alla Data Inizio
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_11(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_INIZIO_EVENTO,INDIRIZZO,SEDE));
        String message="impostazioni data inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: la Sede non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_12(){
        //forniamo i dati dell'evento
        String sedeEv="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,sedeEv));
        String message="formato sede evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: l’indirizzo  non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_13(){
        //forniamo i dati dell'evento
        String indirizzoEv="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,indirizzoEv,SEDE));
        String message="formato indirizzo errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: Corretto
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
     */
    @Test
    public void TC_2p1_14(){
        //forniamo i dati dell'evento
        assertTrue(serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal + "fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il Tipo Evento non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_1(){
        //forniamo i dati dell'evento
        String tipoEvento="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,tipoEvento,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato tipo evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il Nome Evento non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_2(){
        //forniamo i dati dell'evento
         String nomeEvento="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,nomeEvento,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato nome evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Descrizione Evento non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_3(){
        //forniamo i dati dell'evento
         String descEvento="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,descEvento,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato descrizione evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il file Foto Evento ha una grandezza superiore ai 15MB
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_4(){
        byte[] res = new byte[16*1024*1024];
        Part immagineGrande=new MockPart("fotoSample.jpg","fotoSample.jpg", res);

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",immagineGrande,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato file foto non corretto";
        assertEquals(message,exception.getMessage());

     }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il file Foto Evento ha una dimensione maggiore di 1024x768 pixel
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_5(){
        //creazione file foto da utilizzare per i test
        BufferedImage bi= new BufferedImage(1080,1080,BufferedImage.TYPE_INT_RGB);
        File immagineGrande= new File(pathReal+"fotoSampleGrande.jpg");
        Part mockPart;
        try {
            ImageIO.write(bi,"jpg",immagineGrande);
            Path path= Paths.get(pathReal+"fotoSampleGrande.jpg");
            mockPart= new MockPart("fotoSampleGrande.jpg","fotoSampleGrande.jpg", Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSampleGrande.jpg",mockPart,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato file foto non corretto";
        immagineGrande.delete();
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il Numero Biglietti non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_6(){
        //forniamo i dati dell'evento
        int numBigl=0;
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,numBigl,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato numero biglietto e/o prezzo biglietti errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: il Prezzo Biglietti non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_7(){
        //forniamo i dati dell'evento
        double prezzoBigl=0;
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,prezzoBigl,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
        String message="formato numero biglietto e/o prezzo biglietti errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Data Inizio non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Ignore
    @Test
    public void TC_2p2_8(){
        /*questo test viene effettuato nella classe GestioneEventiControllerTest
       in quanto nel rispettivo metodo la stringa contenente la data è stata
       già convertita (all'interno del controller) in java.sql.Date
        */
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Data Fine non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Ignore
    @Test
    public void TC_2p2_9(){
        /*questo test viene effettuato nella classe GestioneEventiControllerTest
       in quanto nel rispettivo metodo la stringa contenente la data è stata
       già convertita (all'interno del controller) in java.sql.Date
        */
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Data Fine inserita è precedente o contemporanea alla Data Inizio
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_10(){
        //forniamo i dati dell'evento
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_INIZIO_EVENTO,INDIRIZZO,SEDE));
        String message="impostazioni data inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: la Sede non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_11(){
        //forniamo i dati dell'evento
        String sedeEv="";
         RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,sedeEv));
        String message="formato sede evento errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: l’indirizzo  non rispetta il formato
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_12(){
        //forniamo i dati dell'evento
        String indirizzoEv="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediModificaEvento(eventoDaModificare.getId(),organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,indirizzoEv,SEDE));
        String message="formato indirizzo errato";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: Corretto
     * Metodo della classe service di riferimento: boolean richiediModificaEvento(...);
     */
    @Test
    public void TC_2p2_13(){
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        //forniamo i dati dell'evento
        assertTrue(serviceE.richiediModificaEvento(1,organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,"",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
    }

    /** Operazione di riferimento nel Test Plan: Ricerca Evento
     * Caso: il Testo Ricerca non rispetta il formato
     * Metodo della classe service di riferimento: List<EventoBean> ricercaEventiByNomeOrDescrizione(String query)
     */
    @Test
    public void TC_2p3_1(){
        //forniamo i dati dell'evento
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.ricercaEventiByNomeOrDescrizione(""));
        String message="errore formato ricerca";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Ricerca Evento
     * Caso: Corretto
     * Metodo della classe service di riferimento: List<EventoBean> ricercaEventiByNomeOrDescrizione(String query)
     */
    @Test
    public void TC_2p3_2(){
        //forniamo i dati dell'evento
        assertNotNull(serviceE.ricercaEventiByNomeOrDescrizione(query));
    }

    /** Operazione di riferimento nei Requisiti Funzionali: visualizza richieste organizzatore
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       List<EventoBean> retrieveRichiesteInserimento(String tipoUtente)
     */
    @Test
    public void retrieveRichiesteInserimentoTestError(){
        List<EventoBean> listrichiesteIns= new ArrayList<>();
        Mockito.when(mockedEventoDao.doRetrieveAllRichiesteInserimento()).thenReturn(listrichiesteIns);
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.retrieveRichiesteInserimento("utente"));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());

    }
    /** Operazione di riferimento nei Requisiti Funzionali: visualizza richieste organizzatore
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       List<EventoBean> retrieveRichiesteModifica(String tipoUtente)
     */
    @Test
    public void retrieveRichiesteModificaTestError(){
        List<EventoBean> listrichiesteMod= new ArrayList<>();
        Mockito.when(mockedEventoDao.doRetrieveAllRichiesteModifiche()).thenReturn(listrichiesteMod);
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.retrieveRichiesteInserimento("utente"));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: visualizza Evento
     * Caso: evento inesistente
     * Metodo della classe service di riferimento:
     *       EventoBean retrieveEventoById(int idEvento);
     */
    @Test
    public void retrieveEventoByIdTestError(){
         Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(null);
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.retrieveEventoById(4));
        String message="Si è verificato un errore, l'evento selezionato non esiste ";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: visualizza Evento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       EventoBean retrieveEventoById(int idEvento);
     */
    @Test
    public void retrieveEventoByIdTest(){
        EventoBean bean= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathReal,DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(bean);
        assertNotNull(serviceE.retrieveEventoById(4));
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta inserimento
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       void attivaEvento(int idEvento, String tipoUtente)
     */
    @Test
    public void attivaEventoTestError(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.attivaEvento(eventoDaModificare.getId(),"utente"));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta inserimento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       void attivaEvento(int idEvento, String tipoUtente)
     */
    @Test
    public void attivaEventoTest(){
         assertTrue(serviceE.attivaEvento(eventoDaModificare.getId(),"amministratore"));
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta inserimento
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       boolean rimuoviEvento(int idEvento, UtenteRegistratoBean utente)
     */
    @Test
    public void rimuoviEventoTestError(){
        UtenteRegistratoBean bean= new ScolarescaBean("provaEmail@example.com","password","istituto",false);
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.rimuoviEvento(eventoDaModificare.getId(),bean));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta inserimento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean rimuoviEvento(int idEvento, UtenteRegistratoBean utente)
     */
    @Test
    public void rimuoviEventoTest(){
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        image= new File(pathReal+"fotoSampleRimuovi.jpg");
        try {
            ImageIO.write(bi,"jpg",image);
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EventoBean bean= new EventoBean(3,organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathReal+"fotoSampleRimuovi.jpg",DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(bean);
        assertTrue(serviceE.rimuoviEvento(bean.getId(),organizzatore));
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta modifica
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       boolean accettaModifica(int idEvento, String tipoUtente)
     */
    @Test
    public void accettaModificaTestError(){
        Mockito.when(mockedEventoDao.retrieveEventoFromidEventoModifica(Mockito.anyInt())).thenReturn(eventoDaModificare.getId());
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        Mockito.when(mockedBigliettoDao.doRetrievePrezzoBiglByRichiestaModifica(Mockito.anyInt())).thenReturn(4.5);

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.accettaModifica(eventoDaModificare.getId(),"utente"));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta modifica
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean accettaModifica(int idEvento, String tipoUtente)
     */
    @Test
    public void accettaModificaTest(){
        Mockito.when(mockedEventoDao.retrieveEventoFromidEventoModifica(Mockito.anyInt())).thenReturn(eventoDaModificare.getId());
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        Mockito.when(mockedBigliettoDao.doRetrievePrezzoBiglByRichiestaModifica(Mockito.anyInt())).thenReturn(4.5);

        assertTrue(serviceE.accettaModifica(eventoDaModificare.getId(),"amministratore"));
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta modifica
     * Caso: operazione non autorizzata
     * Metodo della classe service di riferimento:
     *       boolean rifiutaModifica(int idEvento, String tipoUtente)
     */
    @Test
    public void rifiutaModificaTestError(){
        Mockito.when(mockedEventoDao.retrieveEventoFromidEventoModifica(Mockito.anyInt())).thenReturn(eventoDaModificare.getId());
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        Mockito.when(mockedBigliettoDao.doRetrievePrezzoBiglByRichiestaModifica(Mockito.anyInt())).thenReturn(4.5);

        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.rifiutaModifica(eventoDaModificare.getId(),"utente"));
        String message="operazione non autorizzata";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta modifica
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean rifiutaModifica(int idEvento, String tipoUtente) )
     */
    @Test
    public void rifiutaModificaTest(){
        Mockito.when(mockedEventoDao.retrieveEventoFromidEventoModifica(Mockito.anyInt())).thenReturn(eventoDaModificare.getId());
        Mockito.when(mockedEventoDao.doRetrieveById(Mockito.anyInt())).thenReturn(eventoDaModificare);
        Mockito.when(mockedBigliettoDao.doRetrievePrezzoBiglByRichiestaModifica(Mockito.anyInt())).thenReturn(4.5);

        assertTrue(serviceE.rifiutaModifica(eventoDaModificare.getId(),"amministratore"));
    }




    @AfterClass
    public static void cleanUp(){
        serviceE=null;
        mockedEventoDao=null;
        mockedBigliettoDao=null;
        //rimozione immagine creata per le classi di test
        image.delete();
    }
}
