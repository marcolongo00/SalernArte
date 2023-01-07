package GestioneEventi.service;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.BigliettoDAO;
import model.dao.BigliettoDAOImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import javax.servlet.http.Part;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
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

    @Before
    public void startUp(){
        serviceE=new GestioneEventiServiceImpl();
        mockedEventoDao= Mockito.mock(EventoDAOImpl.class);
        mockedBigliettoDao=Mockito.mock(BigliettoDAOImpl.class);
    }

    /** Operazione di riferimento nel Test Plan: Richiesta Inserimento Evento
     * Caso: il tipoEvento non rispetta il formato
     * Metodo della classe service di riferimento: void richiediInserimentoEvento(...)
    */
    @Test
    public void TC_2p1_1(){
        //forniamo i dati dell'evento
        int idOrg=5;
        int numBiglietti=3;
        double prezzobigl=4;
        Date dataInizio=Date.valueOf("2023-03-03");
        Date dataFine=Date.valueOf("2023-03-23");
        String nome="nome evento";
        String tipoEvento="";
        String descrizione="descrizione evento";
        String indirizzo= "indirizzo";
        String sede="sede";
        String pathcontext=""; //AAA
        Part filephoto=null; //AAA
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.richiediInserimentoEvento(idOrg,nome,tipoEvento,descrizione,pathcontext,filephoto,numBiglietti,prezzobigl,dataInizio,dataFine,indirizzo,sede));
        String message="formato tipo evento errato";
        assertEquals(message,exception.getMessage());
    }

    @After
    public void cleanUp(){
        serviceE=null;
        mockedEventoDao=null;
        mockedBigliettoDao=null;
    }
}
