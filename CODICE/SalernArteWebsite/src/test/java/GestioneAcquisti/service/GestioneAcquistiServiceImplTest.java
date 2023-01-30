package GestioneAcquisti.service;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;

import model.entity.CarrelloBean;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;


public class GestioneAcquistiServiceImplTest {
    private static GestioneAcquistiService serviceA;

    @Before
    public void startUp(){
        serviceA= new GestioneAcquistiServiceImpl();
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
    * Caso: La quantità inserita non è valida
    * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
    * */
    @Test
    public void TC_3p1_1(){
    int idE = 2;
    int quantità = 0;
    CarrelloBean carrelloBean = new CarrelloBean();
    UtenteRegistratoBean utenteBean= new UtenteBean();
    utenteBean.setId(3);
    utenteBean.setTipoUtente("utente");
    utenteBean.setEmail("emaildiprova@gmail.com");
    utenteBean.setPasswordHash("password",false);
    RuntimeException exception;
    exception = assertThrows(RuntimeException.class ,()-> serviceA.aggiungiAlCarrello(idE,quantità,carrelloBean,utenteBean));
    String message = "formato dati errato";
    assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: La quantità inserita è valida
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void TC_3p1_2(){
        int idE = 2;
        int quantità = 2;
        CarrelloBean carrelloBean = new CarrelloBean();
        UtenteRegistratoBean utenteBean= new UtenteBean();
        utenteBean.setId(3);
        utenteBean.setTipoUtente("utente");
        utenteBean.setEmail("emaildiprova@gmail.com");
        utenteBean.setPasswordHash("password",false);
        assertNotNull(serviceA.aggiungiAlCarrello(idE,quantità,carrelloBean,utenteBean));
    }

    /* Operazione di riferimento nel Test Plan: Modifica al Carrello
     * Caso: La quantità inserita non è valida
     * Metodo della classe service di riferimento: void updateQuantitaCarrello(...)
     * */
    @Test
    public void TC_3p2_1(){
        int idE = 2;
        int quantità = 0;
        CarrelloBean carrelloBean = new CarrelloBean();
        UtenteRegistratoBean utenteBean= new UtenteBean();
        utenteBean.setId(3);
        utenteBean.setTipoUtente("utente");
        utenteBean.setEmail("emaildiprova@gmail.com");
        utenteBean.setPasswordHash("password",false);
        NumberFormatException exception;
        exception = assertThrows(NumberFormatException.class,() -> serviceA.updateQuantitaCarrello(idE,quantità,carrelloBean,utenteBean));
        String message = "quantità non valida";
        assertEquals(message, exception.getMessage());
    }

    /* Operazione di riferimento nel Test Plan: Modifica al Carrello
     * Caso: La quantità inserita è valida
     * Metodo della classe service di riferimento: void updateQuantitaCarrello(...)
     * */
    @Test
    public void TC_3p2_2(){
        int idE = 2;
        int quantità = 2;
        CarrelloBean carrelloBean = new CarrelloBean();
        UtenteRegistratoBean utenteBean= new UtenteBean();
        utenteBean.setId(3);
        utenteBean.setTipoUtente("utente");
        utenteBean.setEmail("emaildiprova@gmail.com");
        utenteBean.setPasswordHash("password",false);
        assertTrue(serviceA.updateQuantitaCarrello(idE,quantità,carrelloBean,utenteBean));
    }

    @After
    public void cleanUp(){
        serviceA=null;
    }
}
