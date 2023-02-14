package GestioneUtente.service;

import autenticazione.service.AutenticazioneService;
import autenticazione.service.AutenticazioneServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import registrazione.service.RegistrazioneService;
import registrazione.service.RegistrazioneServiceimpl;

/**
 * Implementa il testing di Integrazione per la classe
 * AutenticazioneServiceImpl con XXX DAOImpl e  XXX DAOImpl ...
 * i nomi dei metodi di test faranno riferimento all'operazione
 * all'interno della classe da testare.
 *
 * si seguirà la seguente regola:
 * preso in considerazione un metodo della classe AutenticazioneServiceImpl che abbia firma:
 *              ReturnValue nomeMetodo( Type value, ...)
 *
 * il metodo all'interno di questa classe di test avrà come firma:
 *              void nomeMetodoTestIntegrazione()
 * nel caso di test di funzionalità di errore:
 *              void nomeMetodoTestIntegrazioneError()
 *
 * @author Marco Longo
 */
public class AutenticazioneServiceImplTestIntegrazione {
    private static AutenticazioneService serviceE;
    /**  Arrivati a questo livello di Test, le classi DAO da cui la classe RegistrazioneService
     *   dipende sono già state testate. Di conseguenza verranno usate le classi DAO testate per
     *   l'inizializzazione delle componenti che ci servono per testare le operazioni della classe
     */
    @BeforeClass
    public static void startUp(){

        serviceE=new AutenticazioneServiceImpl();
    }
    /** Operazione di riferimento nel Test Plan:
     * Caso:
     * Metodo della classe service di riferimento:
     *
     */
    @Test
    public void nomeOperazioneTestIntegrazione(){

    }
    @AfterClass
    public static void cleanUp(){

        serviceE=null;
    }
}
