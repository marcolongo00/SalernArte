package GestioneUtente.service;

import autenticazione.service.AutenticazioneServiceImpl;
import model.dao.AmministratoreDAOImpl;
import model.dao.OrganizzatoreDAOImpl;
import model.dao.ScolarescaDAOImpl;
import model.dao.UtenteDAOImpl;
import model.entity.*;
import org.junit.*;
import java.sql.Date;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private static ScolarescaDAOImpl scolDAO = new ScolarescaDAOImpl();
    private static UtenteDAOImpl daoUser = new UtenteDAOImpl();
    private static OrganizzatoreDAOImpl daoOrg = new OrganizzatoreDAOImpl();
    private static AmministratoreDAOImpl daoAmm = new AmministratoreDAOImpl();
    private static UtenteBean user;
    private static OrganizzatoreBean org;
    private static AmministratoreBean amm;
    private static ScolarescaBean scolaresca;
    private static AutenticazioneServiceImpl serviceA;
    private static final String ISTITUTO = "ISS Gian Camillo Glorioso";
    private static final String PASSWORD = "Passworddiprova.10";

    /**  Arrivati a questo livello di Test, le classi DAO da cui la classe RegistrazioneService
     *   dipende sono già state testate. Di conseguenza verranno usate le classi DAO testate per
     *   l'inizializzazione delle componenti che ci servono per testare le operazioni della classe
     */
    @BeforeClass
    public static void startUp(){
        serviceA = new AutenticazioneServiceImpl(scolDAO, daoOrg, daoUser,daoAmm);

        scolaresca = new ScolarescaBean("emaildiprova@gmail.com", PASSWORD,ISTITUTO, false);
        user = new UtenteBean(0, "Marco", "Longo", "emaildimarco@gmail.com",PASSWORD, Date.valueOf("2000-01-01"), false);
        org = new OrganizzatoreBean(0, "IT17K0300203280648794858718", "Nome", "Cognome", "emailorg@gmail.com", PASSWORD, "Biografia Nome", Date.valueOf("2000-01-01"), false);
        amm = new AmministratoreBean("Amministratore", "CogAmm", "emailamm@gmail.com", PASSWORD, false);

        daoUser.doSave(user);
        daoAmm.doSave(amm);
        daoOrg.doSave(org);
        scolDAO.doSave(scolaresca);
    }
    /** Operazione di riferimento nel Test Plan: Login
     * Caso: Corretto
     * Metodo della classe service di riferimento: loginUtente(...)
     */
    @Test
    public void loginUtenteTest(){
        assertEquals(user.getId(), serviceA.loginUtente(user.getEmail(), PASSWORD, user.getTipoUtente()).getId());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo utente
     * Caso: Corretto
     * Metodo della classe service di riferimento: updateUtente(...)
     */
    @Test
    public void updateUtenteTest(){
        assertEquals(user.getId(), serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), user.getCognome(), user.getDataDiNascita(), user.getSesso()).getId());
    }
    /** Operazione di riferimento nel Test Plan: Modifica profilo scolaresca
     * Caso: Corretto
     * Metodo della classe service di riferimento: updateScolaresca(...)
     */
    @Test
    public void updateScolarescaTest(){
        UtenteRegistratoBean bean = serviceA.updateScolaresca(scolaresca, scolaresca.getEmail(), PASSWORD, ISTITUTO);
        assertEquals(scolaresca.getId(), bean.getId());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo organizzatore
     * Caso: Corretto
     * Metodo della classe service di riferimento: updateOrganizzatore(...)
     */
    @Test
    public void updateOrganizzatoreTest(){
        assertEquals(org.getId(),serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()).getId());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo amministratore
     * Caso: Corretto
     * Metodo della classe service di riferimento: updateAmministratore(...)
     */
    @Test
    public void updateAmministratoreTest(){
        assertEquals(amm.getId(), serviceA.updateAmministratore(amm, amm.getEmail(), PASSWORD, amm.getNome(), amm.getCognome()).getId());
    }

    @AfterClass
    public static void cleanUp(){
        serviceA = null;
        daoUser.doDelete(user.getId());
        daoAmm.doDelete(amm.getId());
        daoOrg.doDelete(org.getId());
        scolDAO.doDelete(scolaresca.getId());

    }
}
