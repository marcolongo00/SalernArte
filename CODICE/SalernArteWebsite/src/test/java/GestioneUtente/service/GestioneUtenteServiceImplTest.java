package GestioneUtente.service;


import autenticazione.service.AutenticazioneService;

import autenticazione.service.AutenticazioneServiceImpl;

import model.dao.UtenteDAO;
import model.dao.UtenteDAOImpl;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Part;
import java.io.File;
import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GestioneUtenteServiceImplTest {

        private static AutenticazioneService serviceE;
        private  static UtenteDAO mockedUtenteDao;
        private static OrganizzatoreBean organizzatore;
        private static UtenteRegistratoBean utenteRegistrato;
        private static Date DATA_DI_NASCITA;

        private static final String NOME_UTENTE="Lucia";
        private static final String COGNOME_UTENTE="Martino";
        private static final String NOME_ORGANIZZATORE="Antonio";
        private static final String COGNOME_ORGANIZZATORE="Martini";
        private static final String NOME_ADMIN="Marco";
        private static final String COGNOME_ADMIN="Marte";
        private static final int gender =1;
        //private static final Date DATA_DI_NASCITA= new Date(Calendar.getInstance().getTimeInMillis());
        private static final String EMAIL_UTENTE="prova@gmail.com";
        private static final String EMAIL_ORGANIZZATORE="prova01@gmail.com";
        private static final String EMAIL_ADMIN="prova02@gmail.com";
        private static final String PASSWORD="Lucia98";
        private static final String CONFERMA_PASSWORD="Lucia98";
        private static final String VECCHIA_PASSWORD="Lucia98";
        private static final String NUOVA_PASSWORD="Lucia98";
        private static final String IBAN_ORGANIZZATORE="IT89Y0300203280338293944286";
        private static final String BIOGRAFIA="Ciao mi chiamo Antonio e sono un organizzatore";


    /** La nuova password coincide con la password ?
        */

       private static final String CONFERMA_NUOVA_PASSWORD="Lucia98";

    private static AutenticazioneService serviceE;
    private static UtenteDAO mockedUtenteDao;

    @Before
    public void startUp(){
        serviceE=new AutenticazioneServiceImpl();
        mockedUtenteDao= Mockito.mock(UtenteDAOImpl.class);
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.1 che sara' TC_1p1_1
     * Caso: il nome è errato
     */
   @Ignore
    @Test
    public void TC_1p1_1(){
        //forniamo i dati per la registrazione dell'utente
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.2 che sara' TC_1p1_2
     * Caso: il cognome è errato
     */
    @Test
    public void TC_1p1_2(){
        //forniamo i dati per la registrazione dell'utente
        String cognome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.3 che sara' TC_1p1_3
     * Caso: il sesso è errato
     */
    @Test
    public void TC_1p1_3(){
        //forniamo i dati per la registrazione dell'utente
        int gender="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.4 che sara' TC_1p1_4
     * Caso: la data di nascita non è corretta
     */
    @Test
    public void TC_1p1_4(){
        //forniamo i dati per la registrazione dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.5 che sara' TC_1p1_5
     * Caso: la data di nascita è uguale alla data odierna
     */
    @Test
    public void TC_1p1_5(){
        //forniamo i dati per la registrazione dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.6 che sara' TC_1p1_6
     * Caso: la data di nascita successiva alla data odierna
     */
    @Test
    public void TC_1p1_6(){
        //forniamo i dati per la registrazione dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.7 che sara' TC_1p1_7
     * Caso: email non valida
     */
    @Test
    public void TC_1p1_7(){
        //forniamo i dati per la registrazione dell'utente
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.8 che sara' TC_1p1_8
     * Caso: la password non rispetta il formato
     */

    @Test
    public void TC_1p1_8(){
        //forniamo i dati per la registrazione dell'utente
        String password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.9 che sara' TC_1p1_9
     * Caso: le password non coincidono
     * ??? la password non valida da lo stesso messaggio delle 2 password che non coincidono ?
     */

    @Test
    public void TC_1p1_9(){
        //forniamo i dati per la registrazione dell'utente
        String password="";
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.10 che sara' TC_1p1_10
     * Caso: conferma password non rispetta il formato
     */

    @Test
    public void TC_1p1_10(){
        //forniamo i dati per la registrazione dell'utente
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Utente 1.1.11 che sara' TC_1p1_11
     * Caso: Corretto
     */
    @Test
    public void TC_1p1_11(){
        //forniamo i dati per la registrazione dell'utente
        assertTrue(serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.1 che sara' TC_1p2_1
     * Caso: il nome è errato
     */
    @Test
    public void TC_1p2_1(){
        //forniamo i dati per la modifica dell'utente
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.2 che sara' TC_1p2_2
     * Caso: il cognome è errato
     */
    @Test
    public void TC_1p2_2(){
        //forniamo i dati per la modifica dell'utente
        String cognome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.3 che sara' TC_1p2_3
     * Caso: il sesso è errato
     */
    @Test
    public void TC_1p2_3(){
        //forniamo i dati per la modifica dell'utente
        int gender="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.4 che sara' TC_1p2_4
     * Caso: la data di nascita non è corretta
     */
    @Test
    public void TC_1p2_4(){
        //forniamo i dati per la modifica dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.5 che sara' TC_1p2_5
     * Caso: la data di nascita è uguale alla data odierna
     */
    @Test
    public void TC_1p2_5(){
        //forniamo i dati per la modifica dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.6 che sara' TC_1p2_6
     * Caso: la data di nascita successiva alla data odierna
     */
    @Test
    public void TC_1p2_6(){
        //forniamo i dati per la modifica dell'utente
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.7 che sara' TC_1p2_7
     * Caso: email non valida
     */
    @Test
    public void TC_1p2_7(){
        //forniamo i dati per la modifica dell'utente
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.8 che sara' TC_1p2_8
     * Caso: la vecchia password non è valida
     */

    @Test
    public void TC_1p2_8(){
        //forniamo i dati per la modifica dell'utente
        String vecchia_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , VECCHIA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.9 che sara' TC_1p2_9
     * Caso: la nuova password non è valida
     */

    @Test
    public void TC_1p2_9(){
        //forniamo i dati per la modifica dell'utente
        String nuova_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , NUOVA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.10 che sara' TC_1p2_10
     * Caso: le password non coincidono
     */

    @Test
    public void TC_1p2_10(){
        //forniamo i dati per la modifica dell'utente
        String vecchia_password="";
        String nuova_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD, VECCHIA_PASSWORD , NUOVA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.11 che sara' TC_1p2_11
     * Caso: conferma nuova password
     */

    @Test
    public void TC_1p2_11(){
        //forniamo i dati per la modifica dell'utente
        String conferma_nuova_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD, , CONFERMA_NUOVA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica profilo Utente 1.2.12 che sara' TC_1p2_12
     * Caso: Corretto
     */
    @Test
    public void TC_1p2_12(){
        //forniamo i dati per la modifica dell'utente
        assertTrue(serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_NUOVA_PASSWORD));
    }

    /** Operazione di riferimento nel Test Plan: Login 1.3.1 che sara' TC_1p3_1
     * Caso: email non valida
     */
    @Test
    public void TC_1p3_1(){
        //forniamo i dati per il login
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Login 1.3.2 che sara' TC_1p3_2
     * Caso: la password non rispetta il formato
     */
    @Test
    public void TC_1p3_2(){
        //forniamo i dati per il login
        String password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Login 1.3.3 che sara' TC_1p3_3
     * Caso: Corretto
     */
    @Test
    public void TC_1p3_3(){
        //forniamo i dati per il login
        assertTrue(serviceE.updateUtente(NOME_UTENTE , COGNOME_UTENTE , gender , DATA_DI_NASCITA , EMAIL_UTENTE , PASSWORD , CONFERMA_PASSWORD));
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.1 che sara' TC_1p4_1
     * Caso: il nome è errato
     */
    @Test
    public void TC_1p4_1(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.2 che sara' TC_1p4_2
     * Caso: il cognome è errato
     */
    @Test
    public void TC_1p4_2(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String cognome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.3 che sara' TC_1p4_3
     * Caso: il sesso è errato
     */
    @Test
    public void TC_1p4_3(){
        //forniamo i dati per la modifica del profilo Organizzatore
        int gender="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.4 che sara' TC_1p4_4
     * Caso: la data di nascita non è corretta
     */
    @Test
    public void TC_1p4_4(){
        //forniamo i dati per la modifica del profilo Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.5 che sara' TC_1p4_5
     * Caso: la data di nascita è uguale alla data odierna
     */
    @Test
    public void TC_1p4_5(){
        //forniamo i dati per la modifica del profilo Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.6 che sara' TC_1p4_6
     * Caso: la data di nascita successiva alla data odierna
     */
    @Test
    public void TC_1p4_6(){
        //forniamo i dati per la modifica del profilo Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.7 che sara' TC_1p4_7
     * Caso: email non valida
     */
    @Test
    public void TC_1p4_7(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE ,BIOGRAFIA  , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.8 che sara' TC_1p4_8
     * Caso: la password non rispetta il formato
     */

    @Test
    public void TC_1p4_8(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.9 che sara' TC_1p4_9
     * Caso: La biografia non è valida
     */
    @Test
    public void TC_1p4_9(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String biografia="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Biografia non valido.";
        assertEquals(message,exception.getMessage());
        }



    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.10 che sara' TC_1p4_10
     * Caso: le password non coincidono
     */

    @Test
    public void TC_1p4_10(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String password="";
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.11 che sara' TC_1p4_11
     * Caso: conferma nuova password
     */

    @Test
    public void TC_1p4_11(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.12 che sara' TC_1p4_12
     * Caso: l'IBAN è errato
     */
    @Test
    public void TC_1p4_12(){
        //forniamo i dati per la modifica del profilo Organizzatore
        String iban="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Iban non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Organizzatore 1.4.13 che sara' TC_1p4_13
     * Caso: Corretto
     */
    @Test
    public void TC_1p4_13(){
        //forniamo i dati per la modifica del profilo Organizzatore
        assertTrue(serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.1 che sara' TC_1p5_1
     * Caso: il nome è errato
     */
    @Test
    public void TC_1p5_1(){
        //forniamo i dati per la registrazione Organizzatore
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.2 che sara' TC_1p5_2
     * Caso: il cognome è errato
     */
    @Test
    public void TC_1p5_2(){
        //forniamo i dati per la registrazione Organizzatore
        String cognome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.3 che sara' TC_1p5_3
     * Caso: il sesso è errato
     */
    @Test
    public void TC_1p5_3(){
        //forniamo i dati per la registrazione Organizzatore
        int gender="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.4 che sara' TC_1p5_4
     * Caso: la data di nascita non è corretta
     */
    @Test
    public void TC_1p5_4(){
        //forniamo i dati per la registrazione Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.5 che sara' TC_1p5_5
     * Caso: la data di nascita è uguale alla data odierna
     */
    @Test
    public void TC_1p5_5(){
        //forniamo i dati per la registrazione Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.6 che sara' TC_1p5_6
     * Caso: la data di nascita successiva alla data odierna
     */
    @Test
    public void TC_1p5_6(){
        //forniamo i dati per la registrazione Organizzatore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.7 che sara' TC_1p5_7
     * Caso: email non valida
     */
    @Test
    public void TC_1p5_7(){
        //forniamo i dati per la registrazione Organizzatore
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE ,BIOGRAFIA  , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.8 che sara' TC_1p5_8
     * Caso: la password non rispetta il formato
     */

    @Test
    public void TC_1p5_8(){
        //forniamo i dati per la registrazione Organizzatore
        String password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.9 che sara' TC_1p5_9
     * Caso: La biografia non è valida
     */
    @Test
    public void TC_1p5_9(){
        //forniamo i dati per la registrazione Organizzatore
        String biografia="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Biografia non valido.";
        assertEquals(message,exception.getMessage());
    }



    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.10 che sara' TC_1p5_10
     * Caso: le password non coincidono
     */

    @Test
    public void TC_1p5_10(){
        //forniamo i dati per la registrazione Organizzatore
        String password="";
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.11 che sara' TC_1p5_11
     * Caso: conferma nuova password
     */

    @Test
    public void TC_1p5_11(){
        //forniamo i dati per la registrazione Organizzatore
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.12 che sara' TC_1p5_12
     * Caso: l'IBAN è errato
     */
    @Test
    public void TC_1p5_12(){
        //forniamo i dati per la registrazione Organizzatore
        String iban="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
        String message="Iban non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Registrazione Organizzatore 1.5.13 che sara' TC_1p5_13
     * Caso: Corretto
     */
    @Test
    public void TC_1p5_13(){
        //forniamo i dati per la registrazione Organizzatore
        assertTrue(serviceE.updateOrganizzatore(NOME_ORGANIZZATORE , COGNOME_ORGANIZZATORE , gender , DATA_DI_NASCITA , IBAN_ORGANIZZATORE , BIOGRAFIA , EMAIL_ORGANIZZATORE , PASSWORD , CONFERMA_PASSWORD));
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.1 che sara' TC_1p6_1
     * Caso: il nome è errato
     */
    @Test
    public void TC_1p6_1(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String nome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Nome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.2 che sara' TC_1p6_2
     * Caso: il cognome è errato
     */
    @Test
    public void TC_1p6_2(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String cognome="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Cognome non valido.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.3 che sara' TC_1p6_3
     * Caso: il sesso è errato
     */
    @Test
    public void TC_1p6_3(){
        //forniamo i dati per la modifica del Profilo Amministratore
        int gender="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="dati per genere non corretti";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.4 che sara' TC_1p6_4
     * Caso: la data di nascita non è corretta
     */
    @Test
    public void TC_1p6_4(){
        //forniamo i dati per la modifica del Profilo Amministratore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.5 che sara' TC_1p6_5
     * Caso: la data di nascita è uguale alla data odierna
     */
    @Test
    public void TC_1p6_5(){
        //forniamo i dati per la modifica del Profilo Amministratore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.6 che sara' TC_1p6_6
     * Caso: la data di nascita successiva alla data odierna
     */
    @Test
    public void TC_1p6_6(){
        //forniamo i dati per la modifica del Profilo Amministratore
        Date="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="impostazioni data di nascita inserite non valide";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.7 che sara' TC_1p6_7
     * Caso: email non valida
     */
    @Test
    public void TC_1p6_7(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String email="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Email non valida.";
        assertEquals(message,exception.getMessage());
    }


    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.8 che sara' TC_1p6_8
     * Caso: la password non rispetta il formato
     */

    @Test
    public void TC_1p6_8(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.9 che sara' TC_1p6_9
     * Caso: le password non coincidono
     */

    @Test
    public void TC_1p6_9(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String password="";
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.10 che sara' TC_1p6_10
     * Caso: conferma nuova password
     */

    @Test
    public void TC_1p6_10(){
        //forniamo i dati per la modifica del Profilo Amministratore
        String conferma_password="";
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
        String message="Password non valida.";
        assertEquals(message,exception.getMessage());
    }

    /** Operazione di riferimento nel Test Plan: Modifica Profilo Amministratore 1.6.11 che sara' TC_1p6_11
     * Caso: Corretto
     */
    @Test
    public void TC_1p6_11(){
        //forniamo i dati per la modifica del Profilo Amministratore
        assertTrue(serviceE.updateAmministratore(NOME_ADMIN , COGNOME_ADMIN , gender , DATA_DI_NASCITA , EMAIL_ADMIN , PASSWORD , CONFERMA_PASSWORD));
    }


    @After
    public void cleanUp(){
        serviceE=null;
        mockedUtenteDao=null;
    }
}







