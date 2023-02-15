package GestioneUtente.service;

import gestioneEventi.service.GestioneEventiService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import registrazione.service.RegistrazioneService;
import registrazione.service.RegistrazioneServiceimpl;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Implementa il testing di Integrazione per la classe
 * RegistrazioneServiceImpl con UtenteDAOImpl, ScolarescaDAOImpl ed OrganizzatoreDAOImpl
 * i nomi dei metodi di test faranno riferimento all'operazione
 * all'interno della classe da testare.
 *
 * si seguirà la seguente regola:
 * preso in considerazione un metodo della classe RegistrazioneServiceImpl che abbia firma:
 *              ReturnValue nomeMetodo( Type value, ...)
 *
 * il metodo all'interno di questa classe di test avrà come firma:
 *              void nomeMetodoTestIntegrazione()
 * nel caso di test di funzionalità di errore:
 *              void nomeMetodoTestIntegrazioneError()
 *
 * @author Alessia Della Pepa
 */
public class RegistrazioneServiceImplTestIntegrazione {
    private static RegistrazioneService service;
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
    /**  Arrivati a questo livello di Test, le classi DAO da cui la classe RegistrazioneService
     *   dipende sono già state testate. Di conseguenza verranno usate le classi DAO testate per
     *   l'inizializzazione delle componenti che ci servono per testare le operazioni della classe
     */
    @BeforeClass
    public static void startUp(){
        DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        service=new RegistrazioneServiceimpl();
    }
    /** Operazione di riferimento nel Test Plan: Registrazione utente
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Ignore
    @Test
    public void registrazioneUtenteTestIntegrazione(){
        assertNotNull(service.registrazioneUtente(GENDER,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH, DATA_DI_NASCITA));
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
    @Ignore
    @Test
    public void registrazioneScolarescaTestIntegrazione(){
        assertNotNull(service.registrazioneScolaresca(EMAIL,PASSWORD_NOT_HASH,ISTITUTO));
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Ignore
    @Test
    public void registrazioneOrganizzatoreTestIntegrazione(){
        assertNotNull(service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
    }
    @AfterClass
    public static void cleanUp(){
        service=null;
    }
}
