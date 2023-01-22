package GestioneAcquisti.service;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;

import model.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class GestioneAcquistiServiceImplTest {
    private static GestioneAcquistiService serviceA;
    private static AcquistoDAO mockedAcquistoDao;

    @Before
    public void startUp(){
        serviceA= new GestioneAcquistiServiceImpl();
        mockedAcquistoDao= Mockito.mock(AcquistoDAOImpl.class);
    }
    @Test
    public void TC_3p1_1(){
        int idUser = 3;
        //CarrelloBean carrello = new CarrelloBean();
        //UtenteRegistratoBean utente = new UtenteRegistratoBean();
        int quantità = 0;
        RuntimeException exception;
        //exception = assertThrows(RuntimeException.class, () -> serviceA.updateQuantitaCarrello(idUser, quantità, ca));
    }

    @After
    public void cleanUp(){
        serviceA=null;
        mockedAcquistoDao = null;
    }
}
