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
import singleton.ConPool;

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
 * Implementa il testing di Integrazione per la classe
 * GestioneEventiServiceImpl con EventoDAOImpl e BigliettoDAOImpl
 * i nomi dei metodi di test faranno riferimento all'operazione
 * all'interno della classe da testare.
 *
 * si seguirà la seguente regola:
 * preso in considerazione un metodo della classe GestioneEventiServiceImpl che abbia firma:
 *              ReturnValue nomeMetodo( Type value, ...)
 *
 * il metodo all'interno di questa classe di test avrà come firma:
 *              void nomeMetodoTestIntegrazione()
 * nel caso di test di funzionalità di errore:
 *              void nomeMetodoTestIntegrazioneError()
 *
 * @author Alessia Della Pepa
 */
public class GestioneEventiServiceImplTestIntegrazione {
    private static GestioneEventiService serviceE;
    private static EventoDAO eventoDao;
    private  static BigliettoDAO bigliettoDao;
    private static OrganizzatoreBean organizzatore;
    private static File image;
    private static Date DATA_INIZIO_EVENTO;
    private static  Date DATA_FINE_EVENTO;
    private static Part FILE_PHOTO;
    private static EventoBean eventoTest;

    private static  final int NUM_BIGLIETTI =5;
    private static final double PREZZO_BIGLIETTO= 5.5;
    private static final Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
    private static final String NOME_EVENTO="Evento di Test";
    private static final String TIPO_EVENTO="mostra";
    private static final String DESCRIZIONE_EVENTO="descrizione evento di Test";
    private static final String INDIRIZZO= "indirizzo di Test";
    private static final String SEDE="sede di Test";
    private static final String pathReal= ".\\src\\main\\webapp\\immaginiEventi\\";
    private static  String pathInEvento;


    /**  Arrivati a questo livello di Test, le classi DAO da cui la classe GestioneEventiService
     *   dipende sono già state testate. Di conseguenza verranno usate le classi DAO testate per
     *   l'inizializzazione delle componenti che ci servono per testare le operazioni della classe
     */
    @BeforeClass
    public static void startUp(){
        serviceE=new GestioneEventiServiceImpl();
        eventoDao= new EventoDAOImpl();
        bigliettoDao= new BigliettoDAOImpl();

        //creiamo i valori di data inizio e data fine di riferimento epr le classi di test considerando dinamicamente la data attual
        // al momento in cui viene effettuato il test ed aggiungendo 5 giorni dalla data attuale per la data di inizio
        //ed altri 10 giorni per la data di fine. creando in questo modo un evento successivo alla data odierna che dura 10 giorni
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

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
        pathInEvento="./immaginiEventi/fotoSample.jpg";

        //inseriamo temporaneamente nel database le classi di cui abbiamo bisogno, come un organizzatore/amministratore per controllare varie operazioni
        /** Il testing della classe utenteDao (per tutti gli utenti) è stato svolto in parallelo ed indipendentemente
         *  da un altro membro del gruppo, per tanto non ci si affiderà all'utilizzo  dei Dao della piattaforma in
         *  questa classe ma verranno effettuate le operazioni necessarie tramite la scrittrua di query apposite
         */
        organizzatore= new OrganizzatoreBean(2,"IT32A0300203280389688236277","pippo","cognome","pippoOrganizzatore@example.com","pippoPassword01","biografia",Date.valueOf("1995-03-03"),false);
        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,organizzatore.getEmail());
            ps.setString(2,organizzatore.getPasswordHash());
            ps.setString(3,"organizzatore");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            organizzatore.setId(id);//inseriemnti in db di oprganizzatore/admin/evento etc

            PreparedStatement ps2=con.prepareStatement("INSERT INTO Organizzatore (id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps2.setInt(1,organizzatore.getId());
            ps2.setString(2,organizzatore.getNome());
            ps2.setString(3,organizzatore.getCognome());
            ps2.setString(4,organizzatore.getBiografia());
            ps2.setDate(5,organizzatore.getDataDiNascita());
            ps2.setInt(6,organizzatore.getSesso());
            ps2.setString(7,organizzatore.getIban());
            if(ps2.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Organizzatore error.");
            }

            con.close();
            ps.close();
            rs.close();
            ps2.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        eventoTest= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio",pathInEvento,"descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        eventoDao.doSave(eventoTest);

    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          boolean richiediInserimentoEvento(...)
     */
    @Test
    public void richiediInserimentoEventoTestIntegrazione(){
         assertTrue(serviceE.richiediInserimentoEvento(organizzatore.getId(),NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,pathReal+"fotoSample.jpg",FILE_PHOTO,NUM_BIGLIETTI,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
    }
    /** Operazione di riferimento nel Test Plan: Richiesta Modifica Evento
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          boolean richiediModificaEvento(...)
     */
   @Test
    public void richiediModificaEventoTestIntegrazione(){
        assertTrue(serviceE.richiediModificaEvento(eventoTest.getId(), organizzatore,NOME_EVENTO,TIPO_EVENTO,DESCRIZIONE_EVENTO,"",null,NUM_BIGLIETTI+10,PREZZO_BIGLIETTO,DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,INDIRIZZO,SEDE));
    }

    /** Operazione di riferimento nel Test Plan: Ricerca Evento
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          List<EventoBean> ricercaEventiByNomeOrDescrizione(String query)
     */
    @Test
    public void ricercaEventiByNomeOrDescrizioneTestIntegrazione(){
       java.util.List<EventoBean> result= serviceE.ricercaEventiByNomeOrDescrizione(NOME_EVENTO+"*");
        assertFalse(result.isEmpty());
    }

    /** Operazione di riferimento nei Requisiti Funzionali: visualizza Evento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       EventoBean retrieveEventoById(int idEvento);
     */
    @Test
    public void retrieveEventoByIdTestIntegrazione(){
        assertNotNull(serviceE.retrieveEventoById(eventoTest.getId()));
    }

    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta inserimento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       void attivaEvento(int idEvento, String tipoUtente)
     */
    @Test
    public void attivaEventoTestIntegrazione(){
        EventoBean bean= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathInEvento,DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        eventoDao.doSave(bean);
        boolean result= serviceE.attivaEvento(bean.getId(),"amministratore");
        assertTrue(result);
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta inserimento
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean rimuoviEvento(int idEvento, UtenteRegistratoBean utente)
     */
    @Test
    public void rimuoviEventoTestIntegrazione(){
        BufferedImage bi= new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        image= new File(pathReal+"fotoSampleRimuovi.jpg");
        try {
            ImageIO.write(bi,"jpg",image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EventoBean bean= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathReal+"fotoSampleRimuovi.jpg",DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        eventoDao.doSave(bean);
        boolean result=serviceE.rimuoviEvento(bean.getId(), organizzatore);
        assertTrue(result);
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin accetta richiesta modifica
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean accettaModifica(int idEvento, String tipoUtente)
     */
    @Test
    public void accettaModificaTestIntegrazione(){
       EventoBean bean= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathReal+"fotoSample.jpg",DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        eventoDao.doSave(bean);
        for (int i=0;i<NUM_BIGLIETTI; i++){
            bigliettoDao.doSave(bean.getId(),PREZZO_BIGLIETTO);
        }
        eventoDao.doUpdateAttivazioneEvento(bean.getId(),true);
        int idPre= bean.getId();
        bean.setDescrizione("nuova desc");
        eventoDao.doSave(bean);
        eventoDao.doSaveRichiestaModificaEv(idPre,bean.getId(),5.5);
        assertTrue(serviceE.accettaModifica(bean.getId(),"amministratore"));
    }
    /** Operazione di riferimento nei Requisiti Funzionali: Admin rifiuta richiesta modifica
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *       boolean rifiutaModifica(int idEvento, String tipoUtente) )
     */
    @Test
    public void rifiutaModificaTestIntegrazione(){
        EventoBean bean= new EventoBean(organizzatore.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,NOME_EVENTO,pathReal+"fotoSample.jpg",DESCRIZIONE_EVENTO,INDIRIZZO,SEDE,NUM_BIGLIETTI,true);
        eventoDao.doSave(bean);
        for (int i=0;i<NUM_BIGLIETTI; i++){
            bigliettoDao.doSave(bean.getId(),PREZZO_BIGLIETTO);
        }
        eventoDao.doUpdateAttivazioneEvento(bean.getId(),true);
        int idPre= bean.getId();
        bean.setDescrizione("nuova desc");
        eventoDao.doSave(bean);
        eventoDao.doSaveRichiestaModificaEv(idPre,bean.getId(),5.5);

        assertTrue(serviceE.rifiutaModifica(bean.getId(),"amministratore"));
    }

    @AfterClass
    public static void cleanUp(){
        serviceE=null;
        eventoDao=null;
        bigliettoDao=null;
         //rimozione immagine creata per le classi di test
        image.delete();
        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,organizzatore.getId());
            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("DELETE UTENTE  error.");
            }
            con.close();
            ps.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
