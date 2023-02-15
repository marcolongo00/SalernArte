package GestioneUtente.service;

import autenticazione.service.AutenticazioneServiceImpl;
import model.dao.*;
import model.entity.*;
import org.junit.*;
import java.sql.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AutenticazioneServiceImplTest {
    private static ScolarescaDAOImpl scolDAO;
    private static UtenteDAOImpl daoUser;
    private static OrganizzatoreDAOImpl daoOrg;
    private static AmministratoreDAOImpl daoAmm;
    private static UtenteBean user;
    private static OrganizzatoreBean org;
    private static AmministratoreBean amm;
    private static ScolarescaBean scolaresca;
    private static AutenticazioneServiceImpl serviceA;
    private static final String ISTITUTO = "ISS Gian Camillo Glorioso";
    private static final String PASSWORD = "Passworddiprova.10";

    @BeforeClass
    public static void setUp()
    {
        scolDAO = mock(ScolarescaDAOImpl.class);
        daoUser = mock(UtenteDAOImpl.class);
        daoOrg = mock(OrganizzatoreDAOImpl.class);
        daoAmm = mock(AmministratoreDAOImpl.class);
        scolaresca = new ScolarescaBean("emaildiprova@gmail.com", PASSWORD,ISTITUTO, false);
        user = new UtenteBean(0, "Marco", "Longo", "emaildimarco@gmail.com",PASSWORD, Date.valueOf("2000-01-01"), false);
        org = new OrganizzatoreBean(0, "IT17K0300203280648794858718", "Nome", "Cognome", "emailorg@gmail.com", PASSWORD, "Biografia Nome", Date.valueOf("2000-01-01"), false);
        amm = new AmministratoreBean("Amministratore", "CogAmm", "emailamm@gmail.com", PASSWORD, false);

        when(scolDAO.doRetrieveById(1)).thenReturn(scolaresca);
        when(daoAmm.doRetrieveById(1)).thenReturn(amm);
        when(daoUser.doRetrieveById(1)).thenReturn(user);
        when(daoOrg.doRetrieveById(1)).thenReturn((OrganizzatoreBean) org);
        when(scolDAO.doRetrieveByEmailPassword(scolaresca.getEmail(), PASSWORD)).thenReturn(scolaresca);

        serviceA = new AutenticazioneServiceImpl(scolDAO, daoOrg, daoUser,daoAmm);
    }
    /*
    Metodo: loginUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Email non corretta o non valida
     */
    @Test
    public void TC_1p3_1()
    {
        assertNull(scolDAO.doRetrieveByEmailPassword(null, PASSWORD));
    }

    /*
    Metodo: loginUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Password non corretta o non valida
     */
    @Test
    public void TC_1p3_2()
    {
        assertNull(scolDAO.doRetrieveByEmailPassword(scolaresca.getEmail(), null));
    }

    /*
    Metodo: loginUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Corretto
         */
    @Test
    public void TC_1p3_3()
    {
        UtenteRegistratoBean bean = serviceA.loginUtente(scolaresca.getEmail(),PASSWORD, scolaresca.getTipoUtente());
        assertEquals(scolaresca.getId(), bean.getId());
    }

    /*
    Metodo: updateScolaresca(...)
    Classe: AutenticazioneServiceImpl
    Caso: Istituto non valido
         */
    @Test
    public void TC_1p8_1()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateScolaresca(scolaresca, scolaresca.getEmail(), PASSWORD, null));
        String message = "Istituto non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateScolaresca(...)
    Classe: AutenticazioneServiceImpl
    Caso: Email non valida
         */
    @Test
    public void TC_1p8_2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateScolaresca(scolaresca, null,PASSWORD, ISTITUTO));
        String message = "Email non valida.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateScolaresca(...)
    Classe: AutenticazioneServiceImpl
    Caso: Password non valida
         */
    @Test
    public void TC_1p8_3()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateScolaresca(scolaresca, scolaresca.getEmail(), "pass", ISTITUTO));
        String message = "Password non valida.";
        assertEquals(message, exception.getMessage());
    }


    //Il test Case TC_1.8.4 e 1.8.5 vengono testati nel controller

    /*
    Metodo: updateScolaresca(...)
    Classe: AutenticazioneServiceImpl
    Caso: Corretto
         */
    @Test
    public void TC_1p8_6()
    {
        UtenteRegistratoBean bean = serviceA.updateScolaresca(scolaresca, scolaresca.getEmail(), PASSWORD, ISTITUTO);
        assertEquals(scolaresca.getId(), bean.getId());
    }

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Nome non valido
         */
    @Test
    public void TC_1p2_1()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, user.getEmail(), PASSWORD, null, user.getCognome(), user.getDataDiNascita(),user.getSesso()));
        String message = "Nome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Cognome non valido
         */
    @Test
    public void TC_1p2_2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), null, user.getDataDiNascita(),user.getSesso()));
        String message = "Cognome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Sesso non valido
         */
    @Test
    public void TC_1p2_3()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), user.getCognome(), user.getDataDiNascita(),-1));
        String message = "dati per genere non corretti";
        assertEquals(message, exception.getMessage());
    }


    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Data di nascita uguale alla data odierna
    */
    @Test
    public void TC_1p2_5()
    {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), user.getCognome(), date,user.getSesso()));
        String message = "impostazioni data di nascita inserite non valide";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Data di nascita successiva alla data odierna
    */
    @Test
    public void TC_1p2_6()
    {
        Date date = Date.valueOf("2100-01-01");
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), user.getCognome(), date,user.getSesso()));
        String message = "impostazioni data di nascita inserite non valide";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Email non valida
         */
    @Test
    public void TC_1p2_7()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateUtente(user, null, PASSWORD, user.getNome(), user.getCognome(), user.getDataDiNascita(),user.getSesso()));
        String message = "Email non valida.";
        assertEquals(message, exception.getMessage());
    }

    // I test case da TC_1.2.8 a TC_1.2.11 vengono testati tramite controller

    /*
    Metodo: updateUtente(...)
    Classe: AutenticazioneServiceImpl
    Caso: Corretto
         */
    @Test
    public void TC_1p2_12()
    {
        assertEquals(user.getId(), serviceA.updateUtente(user, user.getEmail(), PASSWORD, user.getNome(), user.getCognome(), user.getDataDiNascita(), user.getSesso()).getId());
    }

    /*
    Metodo: updateAmministratore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Nome non valido
         */
    @Test
    public void TC_1p6_1()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateAmministratore(amm, amm.getEmail(), PASSWORD, null, amm.getCognome()));
        String message = "Nome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateAmministratore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Cognome non valido
         */
    @Test
    public void TC_1p6_2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateAmministratore(amm, amm.getEmail(), PASSWORD, amm.getNome(), null));
        String message = "Cognome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateAmministratore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Email non valido
         */
    @Test
    public void TC_1p6_3()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateAmministratore(amm, "email", PASSWORD, amm.getNome(), amm.getCognome()));
        String message = "Email non valida.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateAmministratore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Password non valido
         */
    @Test
    public void TC_1p6_4()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateAmministratore(amm, amm.getEmail(), "pass", amm.getNome(), amm.getCognome()));
        String message = "Password non valida.";
        assertEquals(message, exception.getMessage());
    }

    //I test Case TC_1.6.5 E TC_1.6.6 vengono controllati nel AutenticazioneController

    /*
    Metodo: updateAmministratore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Corretto
         */
    @Test
    public void TC_1p6_7()
    {
        assertEquals(amm.getId(), serviceA.updateAmministratore(amm, amm.getEmail(), PASSWORD, amm.getNome(), amm.getCognome()).getId());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Nome utente non valido
         */
    @Test
    public void TC_1p4_1()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, null, org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "Nome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Cognome non valido
         */
    @Test
    public void TC_1p4_2()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), null, org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "Cognome non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Sesso non valido
         */
    @Test
    public void TC_1p4_3()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), -1, org.getBiografia(), org.getIban()));
        String message = "dati per genere non corretti";
        assertEquals(message, exception.getMessage());
    }

    //Il test Case 1.4.4 relativo alla modifica profilo organizzatore viene testato nel controller

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Data di nascita uguale a quella odierna
         */
    @Test
    public void TC_1p4_5()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), new Date(Calendar.getInstance().getTimeInMillis()), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "impostazioni data di nascita inserite non valide";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Data di nascita successiva a quella odierna
         */
    @Test
    public void TC_1p4_6()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), Date.valueOf("2100-01-01"), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "impostazioni data di nascita inserite non valide";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Email non valida
         */
    @Test
    public void TC_1p4_7()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, "email", PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "Email non valida.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Password non valida
         */
    @Test
    public void TC_1p4_8()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), "pass", org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()));
        String message = "Password non valida.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: La biografia non valida
         */
    @Test
    public void TC_1p4_9()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), null, org.getIban()));
        String message = "Biografia non valido.";
        assertEquals(message, exception.getMessage());
    }

    //I test case TC_1.4.10 e TC_1.4.11 vengono testati nel controller

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: IBAN non valido
         */
    @Test
    public void TC_1p4_12()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class ,()-> serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), "Iban non valido"));
        String message = "Iban non valido.";
        assertEquals(message, exception.getMessage());
    }

    /*
    Metodo: updateOrganizzatore(...)
    Classe: AutenticazioneServiceImpl
    Caso: Corretto
    */
    @Test
    public void TC_1p4_13()
    {
        assertEquals(org.getId(),serviceA.updateOrganizzatore(org, org.getEmail(), PASSWORD, org.getNome(), org.getCognome(), org.getDataDiNascita(), org.getSesso(), org.getBiografia(), org.getIban()).getId());
    }

    @AfterClass
    public static void cleanUp()
    {
        serviceA = null;
        scolDAO = null;
        daoAmm = null;
        daoUser = null;
        daoOrg = null;
    }
}
