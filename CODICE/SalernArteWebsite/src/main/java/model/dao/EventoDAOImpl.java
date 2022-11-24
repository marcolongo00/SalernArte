package model.dao;

import model.entity.EventoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventoDAOImpl implements EventoDAO{

    @Override
    public EventoBean doRetrieveById(int id) {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT * FROM Evento WHERE id=?");
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            EventoBean temp=null;
            if(rs.next()){
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                temp=new EventoBean(id,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo);
            }
            conn.close();
            ps.close();
            rs.close();
            return temp;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllEventiAttiviNonScaduti() {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento WHERE CURRENT_DATE() < dataFine AND attivo=true"); //attenzione modifica
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllEventiNonAttivi(){
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento WHERE CURRENT_DATE() < dataFine AND attivo=false"); //attenzione modifica
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllByTeatroAttiviNonScaduti() {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento WHERE CURRENT_DATE() < dataFine AND attivo=true AND tipo=true");//attenzione modifica
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllByMostraAttiviNonScaduti() {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento WHERE CURRENT_DATE() < dataFine AND attivo=true AND tipo=false");//attenzione modifica
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveByOrganizzatore(int idOrg) {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            PreparedStatement ps =conn.prepareStatement("SELECT * FROM Evento WHERE idOrganizzatore=? AND id not in(SELECT idEvento FROM RichiestaEvento)");
            ps.setInt(1,idOrg);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                int idEv=rs.getInt("id");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            ps.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveByNomeOrDescrizione(String against) {
        List<EventoBean> lista=new ArrayList<>();
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("select * from Evento where match(nome,descrizione) Against(? in boolean mode) and attivo=true");
            ps.setString(1,against);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            con.close();
            ps.close();
            rs.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllRichiesteInserimento() {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento as e LEFT JOIN RichiestaEvento as r on e.id=r.idEvento WHERE CURRENT_DATE() <= dataFine AND attivo=false AND r.idEvento IS NULL AND e.id not in (SELECT idEventoTemp FROM RichiestaEvento)");
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoBean> doRetrieveAllRichiesteModifiche() {
        try(Connection conn=ConPool.getConnection()){
            List<EventoBean> lista= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Evento WHERE CURRENT_DATE() <= dataFine AND attivo=false AND id in( select idEventoTemp from RichiestaEvento)");
            while(rs.next()){
                int idEv=rs.getInt("id");
                int idOrg=rs.getInt("idOrganizzatore");
                String nome=rs.getString("nome");
                boolean tipo=rs.getBoolean("tipo");
                String desc=rs.getString("descrizione");
                String path=rs.getString("pathFoto");
                int numBiglietti=rs.getInt("numBiglietti");
                Date dataInizio=rs.getDate("dataInizio");
                Date dataFine= rs.getDate("dataFine");
                String indirizzo= rs.getString("indirizzo");
                String sede=rs.getString("sede");
                boolean attivo=rs.getBoolean("attivo");

                lista.add(new EventoBean(idEv,idOrg,dataInizio,dataFine,nome,path,desc,indirizzo,sede,numBiglietti,tipo,attivo));
            }
            conn.close();
            st.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public void doSave(EventoBean evento) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Evento(idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) VALUES(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1,evento.getIdOrganizzatore());
            ps.setString(2,evento.getNome());
            ps.setBoolean(3,evento.isTipo());
            ps.setString(4,evento.getDescrizione());
            ps.setString(5,evento.getPath());
            ps.setInt(6,evento.getNumBiglietti());
            ps.setDate(7,evento.getDataInizio());
            ps.setDate(8,evento.getDataFine());
            ps.setString(9,evento.getIndirizzo());
            ps.setString(10,evento.getSede());
            ps.setBoolean(11,evento.isAttivo());

            if(ps.executeUpdate()!=1)
                throw new RuntimeException("INSERT error");
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            evento.setId(id);

            conn.close();
            ps.close();
            rs.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void doSaveRichiestaModificaEv(int idOldEvento, int idEventoModificato, double nuovoPrezzoBiglietto) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO RichiestaEvento(idEvento,idEventoTemp,nuovoPrezzoBiglietto) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1,idOldEvento);
            ps.setInt(2,idEventoModificato);
            ps.setDouble(3,nuovoPrezzoBiglietto);


            if(ps.executeUpdate()!=1)
                throw new RuntimeException("INSERT error");
            /*ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            evento.setId(id);
             */
            //mi assicuro che entrambi gli eventi non siano attivi, eprchè si riferiscono allo stesso
            //evento pre e post modifica che deve essere riattivato dall'admin
            doUpdateAttivazioneEvento(idOldEvento,false);
            doUpdateAttivazioneEvento(idEventoModificato,false);

            conn.close();
            ps.close();
           // rs.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void doUpdate(EventoBean evento) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("UPDATE Evento SET nome=?, tipo=?, descrizione=?, pathFoto=?, numBiglietti=?, dataInizio=?, dataFine=?, indirizzo=?,sede=? WHERE id=?");
            ps.setString(1,evento.getNome());
            ps.setBoolean(2,evento.isTipo());
            ps.setString(3,evento.getDescrizione());
            ps.setString(4,evento.getPath());
            ps.setInt(5,evento.getNumBiglietti());
            ps.setDate(6,evento.getDataInizio());
            ps.setDate(7,evento.getDataFine());
            ps.setString(8,evento.getIndirizzo());
            ps.setString(9,evento.getSede());
            ps.setInt(10,evento.getId());

            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE Mostra error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doUpdateNumBiglietti(int idEvento, int numBiglietti) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("UPDATE Evento SET numBiglietti=? WHERE id=?");
            ps.setInt(1,numBiglietti);
            ps.setInt(2,idEvento);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE numBiglietti error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doUpdateAttivazioneEvento(int idEvento, boolean attivo) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("UPDATE Evento SET attivo=? WHERE id=?");
            ps.setBoolean(1,attivo);
            ps.setInt(2,idEvento);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE attivazione error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(int idEvento) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Evento WHERE id=?");
            ps.setInt(1,idEvento);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("DELETE error");
            conn.close();
            ps.close();

        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    public int retieveEventoFromidEventoModifica(int idEventoTemp){
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT idEvento FROM RichiestaEvento WHERE idEventoTemp=?");
            ps.setInt(1,idEventoTemp);
            ResultSet rs=ps.executeQuery();
            EventoBean temp=null;
            int idEv=0;
            if(rs.next()){ //elimini il duplicato del'evento e poi rimuovi la richiesta
                idEv=rs.getInt("idEvento");
            }

            conn.close();
            rs.close();
            ps.close();
            return idEv;

        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }
}
