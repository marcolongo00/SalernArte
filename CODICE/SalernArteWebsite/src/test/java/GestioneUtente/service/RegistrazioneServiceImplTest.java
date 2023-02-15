package GestioneUtente.service;

import model.dao.OrganizzatoreDAOImpl;
import model.dao.ScolarescaDAOImpl;
import model.dao.UtenteDAOImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.*;
import registrazione.service.RegistrazioneService;
import registrazione.service.RegistrazioneServiceimpl;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Implementa il testing di unità per la classe
 * RegistrazioneServiceImpl.
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
public class RegistrazioneServiceImplTest {

    private static RegistrazioneService service;
    private static UtenteDAOImpl mockedDAOUtente;
    private static ScolarescaDAOImpl mockedDAOScol;
    private static OrganizzatoreDAOImpl mockedDAOOrg;
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

    @BeforeClass
    public static void startUp(){
        DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        mockedDAOUtente=Mockito.mock(UtenteDAOImpl.class);
        mockedDAOScol= Mockito.mock(ScolarescaDAOImpl.class);
        mockedDAOOrg=Mockito.mock(OrganizzatoreDAOImpl.class);
        service= new RegistrazioneServiceimpl(mockedDAOOrg,mockedDAOUtente,mockedDAOScol);

    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: nome non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
     @Test
    public void TC_1p1_1(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,"",COGNOME,EMAIL,PASSWORD_NOT_HASH,DATA_DI_NASCITA));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: conome non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_2(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,NOME,"",EMAIL,PASSWORD_NOT_HASH,DATA_DI_NASCITA));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: sesso non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_3(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(5,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,DATA_DI_NASCITA));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: data di anscita non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Ignore
    @Test
    public void TC_1p1_4(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: Data di nascita uguale alla data odierna
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_5(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,DATA_ATTUALE));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: Data di nascita successiva alla data odierna
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_6(){
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        Date DATA_NEL_FUTURO=new Date(c.getTimeInMillis());
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,DATA_NEL_FUTURO));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: email non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_7(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,NOME,COGNOME,"sbagliata",PASSWORD_NOT_HASH,DATA_DI_NASCITA));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: password non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_8(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneUtente(GENDER,NOME,COGNOME,EMAIL,"password",DATA_DI_NASCITA));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: le password non coincidono
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Ignore
    @Test
    public void TC_1p1_9(){
        /** questo test viene effettuato all'interno della classe
         * RegistrazioneControllerTest
         */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: formato conferma password non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Ignore
    @Test
    public void TC_1p1_10(){
        /** questo test viene effettuato all'interno della classe
         * RegistrazioneControllerTest
         */
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneUtente(...)
     */
    @Test
    public void TC_1p1_11(){
        assertNotNull(service.registrazioneUtente(GENDER,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH, DATA_DI_NASCITA));
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: nome utente non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_1(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,"",COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: cognome non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_2(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,"",EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: sesso non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_3(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(-4,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: data di anscita non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Ignore
    @Test
    public void TC_1p5_4(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: data di nascita uguale alla data odierna
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_5(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_ATTUALE));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: data di nascita successiva alla data odierna
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_6(){
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        Date DATA_NEL_FUTURO=new Date(c.getTimeInMillis());
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_NEL_FUTURO));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: email non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_7(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,"",PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: password non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_8(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,"",BIOGRAFIA,DATA_DI_NASCITA));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: biografia non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_9(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,"",DATA_DI_NASCITA));
        String message="Biografia non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: le password non coincidono
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Ignore
    @Test
    public void TC_1p5_10(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: il conferma password non è valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Ignore
    @Test
    public void TC_1p5_11(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: iban non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_12(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneOrganizzatore(2,"neyeiwebd",NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));
        String message="Iban non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneOrganizzatore(...)
     */
    @Test
    public void TC_1p5_13(){
       assertNotNull(service.registrazioneOrganizzatore(2,IBAN,NOME,COGNOME,EMAIL,PASSWORD_NOT_HASH,BIOGRAFIA,DATA_DI_NASCITA));

    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: istituto non valido
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
     @Test
    public void TC_1p7_1(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneScolaresca(EMAIL,PASSWORD_NOT_HASH,""));
        String message="Istituto non valido.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: email non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
    @Test
    public void TC_1p7_2(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneScolaresca("email",PASSWORD_NOT_HASH,ISTITUTO));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: password non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
    @Test
    public void TC_1p7_3(){
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> service.registrazioneScolaresca(EMAIL,"Pass",ISTITUTO));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: le password non coincidono
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
    @Ignore
    @Test
    public void TC_1p7_4(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }
    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: conferma password non valida
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
    @Ignore
    @Test
    public void TC_1p7_5(){
        /** questo test viene effettuato nella classe Registrazione controller Test
         * */
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Scolaresca
     * Caso: Corretto
     * Metodo della classe service di riferimento:
     *          UtenteRegistratoBean registrazioneScolaresca(...)
     */
     @Test
    public void TC_1p7_6(){
        assertNotNull(service.registrazioneScolaresca(EMAIL,PASSWORD_NOT_HASH,ISTITUTO));
    }



    @AfterClass
    public static void cleanUp(){
        DATA_ATTUALE=null;
    }
}
