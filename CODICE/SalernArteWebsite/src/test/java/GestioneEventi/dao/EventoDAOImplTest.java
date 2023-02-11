package GestioneEventi.dao;

import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import org.junit.*;
import singleton.ConPool;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Implementa il testing di unità per la classe
 *               EventoDAOImpl.
 * i nomi dei metodi di test faranno riferimento alla firma
 * dell'operazione di riferimento nella classe che stiamo testando
 *
 * esempio:
 *          firma metodo: boolean doSave(Object obj)
 *          metodo di test: void doSaveTest()
 * @author Alessia Della Pepa
 */
public class EventoDAOImplTest {
    private static OrganizzatoreBean org;
    private static EventoBean eventoTest;
    private static Date DATA_INIZIO_EVENTO;
    private static Date DATA_FINE_EVENTO;
    @BeforeClass
    public static void startUp(){
        org= new OrganizzatoreBean(0,"IT17J0300203280772191565161","pluto","prova","plutoprovaTestDAO@example.com","pluto","prova biografia", Date.valueOf("1999-07-21"),false);
        Calendar c= Calendar.getInstance();
        Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
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
            eventoTest= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
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
     *           EventoBean doRetrieveById(int id)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveByIdTest(){
        EventoDAO dao=new EventoDAOImpl();
        assertNotNull(dao.doRetrieveById(eventoTest.getId()));
    }
    /* Questo metodo testa l'operazione :
     *           EventoBean doRetrieveById(int id)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doRetrieveByIdTestError(){
        EventoDAO dao=new EventoDAOImpl();
        assertNull(dao.doRetrieveById(-1));
    }

    /* Questo metodo testa l'operazione :
     *           boolean doSave(EventoBean evento)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doSaveTest(){
         EventoDAO dao=new EventoDAOImpl();
         EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
         assertTrue(dao.doSave(evento));
    }

     /* Questo metodo testa l'operazione :
     *           boolean doSave(EventoBean evento)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doSaveTestError(){
        EventoBean evento= new EventoBean(org.getId(),null,null,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,true);
        EventoDAO dao=new EventoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doSave(evento));
        String message="INSERT evento error";
        assertEquals(message,exception.getMessage());
    }
    /* Questo metodo testa l'operazione :
     *           boolean doSaveRichiestaModificaEv(int idOldEvento, int idEventoModificato, double nuovoPrezzoBiglietto)
     *  della classe:
     *           EventoDAOImpl
     * */
     @Test
    public void doSaveRichiestaModificaEvTest(){
         EventoDAO dao=new EventoDAOImpl();
         EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
         dao.doSave(evento);
         int idold=evento.getId();
         evento.setNumBiglietti(evento.getNumBiglietti()+20);
         dao.doSave(evento);
         assertTrue(dao.doSaveRichiestaModificaEv(idold,evento.getId(),50));
    }
     /* Questo metodo testa l'operazione :
     *           boolean doSaveRichiestaModificaEv(int idOldEvento, int idEventoModificato, double nuovoPrezzoBiglietto)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void  doSaveRichiestaModificaEvTestError(){
        EventoDAO dao=new EventoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doSaveRichiestaModificaEv(eventoTest.getId(),-5,50));
        String message="INSERT Richiesta Modfica error";
        assertEquals(message,exception.getMessage());

    }
    /* Questo metodo testa l'operazione :
     *           boolean doUpdateAttivazioneEvento(int idEvento, boolean attivo)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doUpdateAttivazioneEventoTest(){
       EventoDAO dao=new EventoDAOImpl();
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        dao.doSave(evento);
        assertTrue(dao.doUpdateAttivazioneEvento(evento.getId(),true));
    }
   /* Questo metodo testa l'operazione :
     *           boolean doUpdateAttivazioneEvento(int idEvento, boolean attivo)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doUpdateAttivazioneEventoTestError(){
        EventoDAO dao=new EventoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doUpdateAttivazioneEvento(-5,true));
        String message="UPDATE attivazione error";
        assertEquals(message,exception.getMessage());
    }

    /* Questo metodo testa l'operazione :
     *           boolean doUpdateNumBiglietti(int idEvento, int numBiglietti)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doUpdateNumBigliettiTest(){
        EventoDAO dao=new EventoDAOImpl();
        assertTrue(dao.doUpdateNumBiglietti(eventoTest.getId(), eventoTest.getNumBiglietti()+10));
    }

    /* Questo metodo testa l'operazione :
     *           boolean doUpdateNumBiglietti(int idEvento, int numBiglietti)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doUpdateNumBigliettiTestError(){
        EventoDAO dao=new EventoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doUpdateNumBiglietti(-5,5));
        String message="UPDATE numBiglietti error";
        assertEquals(message,exception.getMessage());
    }

    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveByOrganizzatore(int idOrg)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveByOrganizzatoreTest(){
        //è stato slavato già almeno eventoTest in startUp
        EventoDAO dao=new EventoDAOImpl();
        List<EventoBean> result= dao.doRetrieveByOrganizzatore(org.getId());
        assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveByOrganizzatore(int idOrg)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idOrganizzatore inesistente
     * */
    @Test
    public void doRetrieveByOrganizzatoreTestError(){
        EventoDAO dao=new EventoDAOImpl();
        List<EventoBean> result= dao.doRetrieveByOrganizzatore(-1);
        assertTrue(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllEventiNonAttivi()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllEventiNonAttiviTest(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        dao.doSave(evento);
        List<EventoBean> result= dao.doRetrieveAllEventiNonAttivi();
        assertFalse(result.isEmpty());
    }
     /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllEventiAttiviNonScaduti()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllEventiAttiviNonScadutiTest(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,true);
        dao.doSave(evento);
        dao.doUpdateAttivazioneEvento(evento.getId(),true);
        List<EventoBean> result= dao.doRetrieveAllEventiAttiviNonScaduti();
        assertFalse(result.isEmpty());
    }
     /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllByTeatroAttiviNonScaduti()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllByTeatroAttiviNonScadutiTest(){
        EventoDAO dao=new EventoDAOImpl();
        boolean tipoTeatro=true;
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,tipoTeatro);
        dao.doSave(evento);
        dao.doUpdateAttivazioneEvento(evento.getId(),true);

        List<EventoBean> result= dao.doRetrieveAllByTeatroAttiviNonScaduti();
        assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllByMostraAttiviNonScaduti()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllByMostraAttiviNonScadutiTest(){
        EventoDAO dao=new EventoDAOImpl();
        boolean tipoMostra=false;
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeNuovo","pathNuovo","descrizione Test","indirizzo Test","sede Test",2,tipoMostra);
        dao.doSave(evento);
        dao.doUpdateAttivazioneEvento(evento.getId(),true);

        List<EventoBean> result= dao.doRetrieveAllByMostraAttiviNonScaduti();
        assertFalse(result.isEmpty());
    }
     /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveByNomeOrDescrizione(String against)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveByNomeOrDescrizioneTest(){
        EventoDAO dao=new EventoDAOImpl();
        List<EventoBean> result= dao.doRetrieveByNomeOrDescrizione("nome*");
        assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllRichiesteInserimento()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllRichiesteInserimentoTest(){
        //un evento non attivo che non si trova in richiesta evento
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
        dao.doSave(evento);
        List<EventoBean> result= dao.doRetrieveAllRichiesteInserimento();
        assertFalse(result.isEmpty());
    }
    /* Questo metodo testa l'operazione :
     *           List<EventoBean> doRetrieveAllRichiesteModifiche()
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doRetrieveAllRichiesteModificheTest(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean modifica= dao.doRetrieveById(eventoTest.getId());
        modifica.setNumBiglietti(eventoTest.getNumBiglietti()+20);
        dao.doSave(modifica);
        dao.doSaveRichiestaModificaEv(eventoTest.getId(),modifica.getId(),50);
        List<EventoBean> result= dao.doRetrieveAllRichiesteModifiche();
        assertFalse(result.isEmpty());
    }
     /* Questo metodo testa l'operazione :
     *           int retrieveEventoFromidEventoModifica(int idEventoTemp)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void retrieveEventoFromidEventoModificaTest(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento=new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
        dao.doSave(evento);
        int idold=evento.getId();
        evento.setNumBiglietti(evento.getNumBiglietti()+20);
        dao.doSave(evento);

        dao.doSaveRichiestaModificaEv(idold,evento.getId(),50);

        int idEvPreModifica= dao.retrieveEventoFromidEventoModifica(evento.getId());
        assertEquals(idEvPreModifica,idold);
    }
    /* Questo metodo testa l'operazione :
     *           boolean doUpdate(EventoBean evento)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doUpdateTest(){
        EventoDAO dao=new EventoDAOImpl();
        eventoTest.setDescrizione("nuova descrizione per Test");
       assertTrue(dao.doUpdate(eventoTest));
    }
     /* Questo metodo testa l'operazione :
     *           boolean doUpdate(EventoBean evento)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doUpdateTestError(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
        dao.doSave(evento);
        evento.setId(-1);
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doUpdate(evento));
        String message="UPDATE Mostra error";
        assertEquals(message,exception.getMessage());
    }
     /* Questo metodo testa l'operazione :
     *           boolean doDelete(int idEvento)
     *  della classe:
     *           EventoDAOImpl
     * */
    @Test
    public void doDeleteTest(){
        EventoDAO dao=new EventoDAOImpl();
        EventoBean evento= new EventoBean(org.getId(),DATA_INIZIO_EVENTO,DATA_FINE_EVENTO,"nomeTest","pathTest","descrizione Test","indirizzo Test","sede Test",2,false);
        dao.doSave(evento);
        assertTrue(dao.doDelete(evento.getId()));
    }
    /* Questo metodo testa l'operazione :
     *           boolean doDelete(int idEvento)
     *  della classe:
     *           EventoDAOImpl
     * nel caso di: errore con idEvento inesistente
     * */
    @Test
    public void doDeleteTestError(){
        EventoDAO dao=new EventoDAOImpl();
        RuntimeException exception;
        exception=assertThrows(RuntimeException.class,()-> dao.doDelete(-1));
        String message="DELETE error";
        assertEquals(message,exception.getMessage());
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
