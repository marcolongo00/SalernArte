package GestioneEventi.dao;


import model.dao.*;
import model.entity.*;
import org.junit.*;
import singleton.ConPool;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BigliettoDaoImplTest {
    private static OrganizzatoreBean org;
    private static EventoBean eventoTest;
    private static Date DATA_INIZIO_EVENTO;
    private static Date DATA_FINE_EVENTO;
    private static Date DATA_ATTUALE;
    private  static int numBigliettiTest= 10;

    @BeforeClass
    public static void startUp(){
        org= new OrganizzatoreBean(0,"IT17J0300203280772191565161","pluto","prova","plutoprovaTestDAO@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);
        Calendar c= Calendar.getInstance();
        DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        c.setTime(DATA_ATTUALE);
        c.add(Calendar.DATE,5);
        DATA_INIZIO_EVENTO=new Date(c.getTimeInMillis());
        c.add(Calendar.DATE,10);
        DATA_FINE_EVENTO= new Date(c.getTimeInMillis());

        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,org.getEmail());
            ps.setString(2,org.getPasswordHash());
            ps.setString(3,"organizzatore");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Utente error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            org.setId(id);//inseriemnti in db di oprganizzatore/admin/evento etc
            eventoTest= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",numBigliettiTest,false);
            PreparedStatement ps2=con.prepareStatement("INSERT INTO Organizzatore (id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps2.setInt(1,org.getId());
            ps2.setString(2,org.getNome());
            ps2.setString(3,org.getCognome());
            ps2.setString(4,org.getBiografia());
            ps2.setDate(5,org.getDataDiNascita());
            ps2.setInt(6,org.getSesso());
            ps2.setString(7,org.getIban());
            if(ps2.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT Organizzatore error.");
            }
            //evento per test
            PreparedStatement ps3=con.prepareStatement("INSERT INTO Evento(idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) VALUES(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps3.setInt(1,eventoTest.getIdOrganizzatore());
            ps3.setString(2,eventoTest.getNome());
            ps3.setBoolean(3,eventoTest.isTipo());
            ps3.setString(4,eventoTest.getDescrizione());
            ps3.setString(5,eventoTest.getPath());
            ps3.setInt(6,eventoTest.getNumBiglietti());
            ps3.setDate(7,eventoTest.getDataInizio());
            ps3.setDate(8,eventoTest.getDataFine());
            ps3.setString(9,eventoTest.getIndirizzo());
            ps3.setString(10,eventoTest.getSede());
            ps3.setBoolean(11,eventoTest.isAttivo());

            if(ps3.executeUpdate()!=1)
                throw new RuntimeException("INSERT evento error");
            ResultSet rs2=ps3.getGeneratedKeys();
            rs2.next();
            int idEv=rs2.getInt(1);
            eventoTest.setId(idEv);


            con.close();
            ps.close();
            rs.close();
            ps2.close();
            ps3.close();
            rs2.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    /* Questo metodo testa l'operazione :
    *           boolean doSave(int idEvento, double prezzo)
    *  della classe:
    *           BigliettoDAOImpl
    * */
    @Test
    public void doSaveTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        assertTrue(daoBigl.doSave(eventoTest.getId(),10));
    }
    /* Questo metodo testa l'operazione :
     *           boolean doSave(int idEvento, double prezzo)
     *  della classe:
     *           BigliettoDAOImpl
     *  nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doSaveTestError(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> daoBigl.doSave(-4,10));
        String message="INSERT error";
        assertEquals(message,exception.getMessage());
    }
    /* Questo metodo testa l'operazione :
     *           double doRetrievePrezzoBigliettoByEvento(int idEvento))
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void doRetrievePrezzoBigliettoByEventoTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        daoBigl.doSave(eventoTest.getId(),10);

        assertEquals(10,daoBigl.doRetrievePrezzoBigliettoByEvento(eventoTest.getId()),0.01);
    }
    /* Questo metodo testa l'operazione :
     *           boolean doUpdateBigliettiModificaEvento(EventoBean eventoPreModifica, EventoBean eventoModifica)
     *  della classe:
     *           BigliettoDAOImpl
     * nel caso di: quantita nuovi biglietti > quantita biglietti esistenti
     * */
    @Test
    public void doUpdateBigliettiModificaEventoTest1(){

        EventoDAO dao=new EventoDAOImpl();
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        int numBiglietti=10;
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",numBiglietti,false);
        dao.doSave(evento);
        for(int i=0; i<numBiglietti; i++){
            daoBigl.doSave(evento.getId(),10);
        }

        int idold=evento.getId();
        evento.setNumBiglietti(evento.getNumBiglietti()+20);
        dao.doSave(evento);
        dao.doSaveRichiestaModificaEv(idold,evento.getId(),50);
        EventoBean preModifica=dao.doRetrieveById(idold);
        assertTrue(daoBigl.doUpdateBigliettiModificaEvento(preModifica,evento));

    }
    /* Questo metodo testa l'operazione :
     *           boolean doUpdateBigliettiModificaEvento(EventoBean eventoPreModifica, EventoBean eventoModifica)
     *  della classe:
     *           BigliettoDAOImpl
     * nel caso di: quantita nuovi biglietti < quantita biglietti esistenti
     *                                      AND
     *              quantita nuovi biglietti >= quantia biglietti acquistati
     * */
    @Test
    public void doUpdateBigliettiModificaEventoTest2(){
        EventoDAO dao=new EventoDAOImpl();
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        int numBiglietti=10;
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",numBiglietti,false);
        dao.doSave(evento);
        for(int i=0; i<numBiglietti; i++){
            daoBigl.doSave(evento.getId(),10);
        }

        int idold=evento.getId();
        evento.setNumBiglietti(evento.getNumBiglietti()-2);
        dao.doSave(evento);
        dao.doSaveRichiestaModificaEv(idold,evento.getId(),50);
        EventoBean preModifica=dao.doRetrieveById(idold);
        assertTrue(daoBigl.doUpdateBigliettiModificaEvento(preModifica,evento));
    }
    /* Questo metodo testa l'operazione :
     *           double doRetrievePrezzoBiglByRichiestaModifica( int idEventoPostMod)
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void doRetrievePrezzoBiglByRichiestaModificaTest(){
        EventoDAO dao=new EventoDAOImpl();
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
        dao.doSave(evento);
        daoBigl.doSave(evento.getId(),10);

        int idold=evento.getId();
        evento.setNumBiglietti(evento.getNumBiglietti()+20);
        dao.doSave(evento);
        dao.doSaveRichiestaModificaEv(idold,evento.getId(),50);
        assertEquals(50,daoBigl.doRetrievePrezzoBiglByRichiestaModifica(evento.getId()),0.01);

    }
     /* Questo metodo testa l'operazione :
     *           List<BigliettoBean> doRetrieveAllByEvento(int idEvento)
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void doRetrieveAllByEventoTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        daoBigl.doSave(eventoTest.getId(),10);
        List<BigliettoBean> result=daoBigl.doRetrieveAllByEvento(eventoTest.getId());
        assertFalse(result.isEmpty());
    }
     /* Questo metodo testa l'operazione :
     *           List<BigliettoBean> doRetrieveAllNonAcquistati(int idEvento)
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void doRetrieveAllNonAcquistatiTest(){
       BigliettoDAO daoBigl=new BigliettoDAOImpl();
       daoBigl.doSave(eventoTest.getId(),10);
       List<BigliettoBean> result=daoBigl.doRetrieveAllNonAcquistati(eventoTest.getId());
       assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<BigliettoBean> doRetrieveAllAcquistati()
     *  della classe:
     *           BigliettoDAOImpl
     * */
   @Test
    public void doRetrieveAllAcquistatiTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        UtenteDAOImpl dao= new UtenteDAOImpl();
        daoBigl.doSave(eventoTest.getId(),10);

        UtenteBean utente= new UtenteBean(1,"nome","cognome","nomecognome@email.com","password",Date.valueOf("1999-12-12"),false);
        dao.doSave(utente);
        AcquistoBean acquisto=new AcquistoBean(utente.getId(),DATA_ATTUALE,12);
        AcquistoDAO acquistoDAO=new AcquistoDAOImpl();
        acquistoDAO.doSave(acquisto);
        daoBigl.sellBiglietto(eventoTest.getId(),1,acquisto.getNumOrdine());
        List<BigliettoBean> result=daoBigl.doRetrieveAllAcquistati(eventoTest.getId());
         dao.doDelete(utente.getId());
         assertFalse(result.isEmpty());


    }
    /* Questo metodo testa l'operazione :
     *           boolean sellBiglietto(int idEvento, int quantita, int numAcquisto)
     *  della classe:
     *           BigliettoDAOImpl
     * */
     @Test
    public void sellBigliettoTest(){
         BigliettoDAO daoBigl=new BigliettoDAOImpl();
         UtenteDAOImpl dao= new UtenteDAOImpl();
         daoBigl.doSave(eventoTest.getId(),10);

        UtenteBean utente= new UtenteBean(1,"nome","cognome","nomecognome@email.com","password",Date.valueOf("1999-12-12"),false);
        dao.doSave(utente);
        AcquistoBean acquisto=new AcquistoBean(utente.getId(),DATA_ATTUALE,12);
        AcquistoDAO acquistoDAO=new AcquistoDAOImpl();
        acquistoDAO.doSave(acquisto);
        boolean result=daoBigl.sellBiglietto(eventoTest.getId(),1,acquisto.getNumOrdine());
         dao.doDelete(utente.getId());
        assertTrue(result);

    }
    /* Questo metodo testa l'operazione :
     *           boolean updatePrezzoBigliettoEvento(int idEvento, double costo)
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void updatePrezzoBigliettoEventoTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        daoBigl.doSave(eventoTest.getId(),10);
        assertTrue(daoBigl.updatePrezzoBigliettoEvento(eventoTest.getId(),20));
    }
    /* Questo metodo testa l'operazione :
     *           boolean doDelete(int idEvento, int idBiglietto)
     *  della classe:
     *           BigliettoDAOImpl
     * */
    @Test
    public void doDeleteTest(){
        BigliettoDAO daoBigl=new BigliettoDAOImpl();
        daoBigl.doSave(eventoTest.getId(),10);
        List<BigliettoBean> result=daoBigl.doRetrieveAllByEvento(eventoTest.getId());

        assertTrue(daoBigl.doDelete(eventoTest.getId(),result.get(0).getIdBiglietto()));
    }


    @AfterClass
    public static void cleanUp(){
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,org.getId());
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE UTENTE ERROR");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
