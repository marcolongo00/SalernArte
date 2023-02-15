package GestioneAcquisti.service;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.dao.*;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import singleton.ConPool;
import java.sql.*;
import java.util.Calendar;

import static org.junit.Assert.*;

public class GestioneAcquistiServiceImplTestIntegrazione {
    private static GestioneAcquistiService serviceA;
    private static CarrelloDAO carrelloDAO;
    private static BigliettoDAO bigliettoDAO;
    private static EventoDAO eventoDAO;
    private static Date DATA_INIZIO_EVENTO;
    private static Date DATA_FINE_EVENTO;
    private static UtenteBean user;
    private static EventoBean evento;
    private static OrganizzatoreBean org;
    private static CarrelloBean carrelloBean;
    private static int QUANTITA = 1;
    private static double PREZZO_BIG = 13;
    private static final Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());

    @BeforeClass
    public static void setUp()
    {
        carrelloDAO = new CarrelloDAOImpl();
        bigliettoDAO = new BigliettoDAOImpl();
        eventoDAO = new EventoDAOImpl();
        serviceA = new GestioneAcquistiServiceImpl(carrelloDAO,eventoDAO,bigliettoDAO);

        Calendar c= Calendar.getInstance();
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        user = new UtenteBean(1, "Marco", "Longo", "emailtest@gmail.com", "passTest", Date.valueOf("1978-08-10"), false);
        org = new OrganizzatoreBean(1, "IT17J0300203280772191565161", "Antonio", "Longo", "orgimailTestDAO@example.com", "password1", "bio", Date.valueOf("1995-07-15"), false);


        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1,org.getEmail());
            ps1.setString(2,org.getPasswordHash());
            ps1.setString(3,"organizzatore");

            if(ps1.executeUpdate() !=1)
                throw new RuntimeException("INSERT Utente ORG error.");

            ResultSet rs=ps1.getGeneratedKeys();
            rs.next();
            org.setId(rs.getInt(1));

            PreparedStatement ps2=con.prepareStatement("INSERT INTO Organizzatore(id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps2.setInt(1,org.getId());
            ps2.setString(2,org.getNome());
            ps2.setString(3,org.getCognome());
            ps2.setString(4,org.getBiografia());
            ps2.setDate(5,org.getDataDiNascita());
            ps2.setInt(6,org.getSesso());
            ps2.setString(7,org.getIban());

            if(ps2.executeUpdate()!= 1)
                throw new RuntimeException("INSERT Organizzatore error");

            PreparedStatement ps3 = con.prepareStatement("INSERT INTO UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps3.setString(1,user.getEmail());
            ps3.setString(2,user.getPasswordHash());
            ps3.setString(3,"utente");

            if(ps3.executeUpdate() !=1)
                throw new RuntimeException("INSERT UtenteReg error.");

            ResultSet rs2=ps3.getGeneratedKeys();
            rs2.next();
            user.setId(rs2.getInt(1));

            PreparedStatement ps4 = con.prepareStatement("INSERT INTO Utente(id,nome,cognome,dataDiNascita,sesso) VALUES (?,?,?,?,?)");
            ps4.setInt(1,user.getId());
            ps4.setString(2,user.getNome());
            ps4.setString(3,user.getCognome());
            ps4.setDate(4,user.getDataDiNascita());
            ps4.setInt(5,user.getSesso());

            if(ps4.executeUpdate()!= 1)
                throw new RuntimeException("INSERT Utente error");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nome vecchio","./immaginiEventi/photo_2022-06-11_16-53-57.jpg","descrizione vecchia", "indirizzo vecchio","sede vecchia",5,false);
        eventoDAO.doSave(evento);

        carrelloBean = new CarrelloBean(user.getId());
        carrelloBean.put(evento,QUANTITA,PREZZO_BIG);
        carrelloDAO.doSave(user.getId(), carrelloBean.get(evento.getId()));
    }

    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void aggiungiAlCarrelloIntegrazione()
    {
        assertNotNull(serviceA.aggiungiAlCarrello(evento.getId(),QUANTITA,carrelloBean,user));
    }


    /* Operazione di riferimento nel Test Plan: Aggiungi al Carrello
     * Caso: Corretto
     * Metodo della classe service di riferimento: CarrelloBean aggiungiAlCarrello(...)
     * */
    @Test
    public void updateQuantitaCarrelloIntegrazione()
    {
        assertTrue(serviceA.updateQuantitaCarrello(evento.getId(),QUANTITA,carrelloBean,user));
    }

    @AfterClass
    public static void cleanUp()
    {
        serviceA = null;
        eventoDAO = null;
        bigliettoDAO = null;
        carrelloDAO = null;

        try(Connection con = ConPool.getConnection())
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,user.getId());

            if(ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE USER FAILED");
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id = ?");
            ps1.setInt(1,org.getId());

            if(ps1.executeUpdate() != 1)
                throw new RuntimeException("DELETE ORG FAILED");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
