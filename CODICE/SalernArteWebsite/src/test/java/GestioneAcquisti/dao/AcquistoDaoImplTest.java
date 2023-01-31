package GestioneAcquisti.dao;

import model.dao.BigliettoDAO;
import model.dao.CarrelloDAO;
import model.dao.EventoDAO;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.util.Calendar;

public class AcquistoDaoImplTest {
    private EventoDAO eventoDAO;
    private BigliettoDAO bigliettoDAO;
    private CarrelloDAO carrelloDAO;
    private String message;
    private EventoBean bean;
    private CarrelloBean.BigliettoQuantita bigliettoQuantita;
    private static Date DATA_INIZIO_EVENTO;
    private static Date DATA_FINE_EVENTO;
    private static Date DATA_ATTUALE;
    private int idutente, idE;

    @BeforeAll
    public void startUp()
    {
        idutente = 1;
        idE = 2;
        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new java.sql.Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        bean = new EventoBean(1,idutente,DATA_INIZIO_EVENTO, DATA_FINE_EVENTO, "nome-evento", "./immaginiEventi/photo_2022-06-11_16-53-57.jpg", "descrizione-evento", "indirizzo evento","sede evento",5,false);
        bigliettoQuantita = new CarrelloBean.BigliettoQuantita(bean, 5, 10);
    }

    //metodo DAO relativo al EventoDAOImpl
    @Test
    public void doRetrieveById()
    {
        assertNotNull(eventoDAO.doRetrieveById(idE));
    }

    //metodo DAO relativo al BigliettoDAOImpl
    @Test
    public void doRetrievePrezzoBigliettoByEvento()
    {
        idE = 2;
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> bigliettoDAO.doRetrievePrezzoBigliettoByEvento(idE));
        message = "Prezzo biglietto null";
        assertEquals(message, exception.getMessage());
    }

    //metodo DAO relativo al CarrelloDAOImpl
    @Test
    public void doUpdateQuantita()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> carrelloDAO.doUpdateQuantita(idutente, bigliettoQuantita));
        message = "UPDATE quantità carrello error";
        assertEquals(message, exception.getMessage());
    }

    //metodo DAO relativo al CarrelloDAOImpl
    @Test
    public void doSave()
    {
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class,() -> carrelloDAO.doSave(idutente, bigliettoQuantita));
        message = "INSERT in Carrello error";
        assertEquals(message, exception.getMessage());
    }

    @AfterAll
    public void cleanUp()
    {
        bean = null;
        bigliettoQuantita = null;
    }

}
