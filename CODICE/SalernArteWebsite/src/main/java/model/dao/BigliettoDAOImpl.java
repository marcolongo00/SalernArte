package model.dao;

import model.entity.BigliettoBean;
import model.entity.EventoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BigliettoDAOImpl implements BigliettoDAO{

    @Override
    public List<BigliettoBean> doRetrieveAllByEvento(int idEvento) {
        try(Connection conn=ConPool.getConnection()){
            List<BigliettoBean> biglietti= new ArrayList<>();
            PreparedStatement ps= conn.prepareStatement("SELECT * FROM Biglietto WHERE evento=?");
            ps.setInt(1,idEvento);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                BigliettoBean temp= new BigliettoBean();
                temp.setIdBiglietto(rs.getInt("id"));
                temp.setIdEvento(rs.getInt("evento"));
                temp.setPrezzo(rs.getDouble("costo"));
                temp.setNumAcquisto(rs.getInt("acquisto"));

                biglietti.add(temp);
            }
            conn.close();
            ps.close();
            rs.close();
            return biglietti;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BigliettoBean> doRetrieveAllNonAcquistati() {
        try(Connection conn=ConPool.getConnection()){
            List<BigliettoBean> biglietti= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Biglietto WHERE acquisto is null");
            while(rs.next()){
                BigliettoBean temp= new BigliettoBean();
                temp.setIdBiglietto(rs.getInt("id"));
                temp.setIdEvento(rs.getInt("evento"));
                temp.setPrezzo(rs.getDouble("costo"));
                temp.setNumAcquisto(rs.getInt("acquisto"));

                biglietti.add(temp);
            }
            conn.close();
            st.close();
            rs.close();
            return biglietti;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BigliettoBean> doRetrieveAllAcquistati() {
        try(Connection conn=ConPool.getConnection()){
            List<BigliettoBean> biglietti= new ArrayList<>();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Biglietto WHERE acquisto is not null");
            while(rs.next()){
                BigliettoBean temp= new BigliettoBean();
                temp.setIdBiglietto(rs.getInt("id"));
                temp.setIdEvento(rs.getInt("evento"));
                temp.setPrezzo(rs.getDouble("costo"));
                temp.setNumAcquisto(rs.getInt("acquisto"));

                biglietti.add(temp);
            }
            conn.close();
            st.close();
            rs.close();
            return biglietti;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sellBiglietto(int idEvento, int quantita, int numAcquisto) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement psMin=conn.prepareStatement("SELECT MIN(id) FROM Biglietto WHERE evento=? AND acquisto is null");
            psMin.setInt(1,idEvento);
            ResultSet rsMin=psMin.executeQuery();
            int min=1;
            if(rsMin.next())
                min=rsMin.getInt(1);
            rsMin.close();
            psMin.close();
            PreparedStatement ps= conn.prepareStatement("UPDATE Biglietto SET acquisto=? WHERE id=? AND evento=?");
            //inutile con quantita=0 che non dovrebbe proprio poter essere selezionata
            //aggiorna anche num biglietti in evento
            for(int i=0;i<quantita;i++){
                ps.setInt(1,numAcquisto);
                ps.setInt(2,min);
                ps.setInt(3,idEvento);
                min++;
                ps.addBatch();
            }
            ps.executeBatch();
            conn.close();
            ps.close();
        }catch(BatchUpdateException e1){
            throw new RuntimeException("batch sell biglietti error");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePrezzoBigliettoEvento(int idEvento, double costo) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("UPDATE Biglietto SET costo=? WHERE evento=?");
            ps.setDouble(1,costo);
            ps.setInt(2,idEvento);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE Evento error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doSave(int idEvento, double prezzo) {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Biglietto(evento,costo) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,idEvento);
            ps.setDouble(2,prezzo);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("INSERT error");
            conn.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public double doRetrievePrezzoBigliettoByEvento(int idEvento) {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT costo FROM Biglietto WHERE evento=?");
            ps.setInt(1,idEvento);
            ResultSet rs=ps.executeQuery();
            double costo=0;
            if(rs.next()){
                costo= rs.getDouble("costo");
            }
            conn.close();
            ps.close();
            rs.close();
            return costo;
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doUpdateBigliettiModificaEvento(EventoBean eventoPreModifica, EventoBean eventoModifica) {
        // e se è cambiato il prezzo dei biglietti? li devo modificare tutti . update prezzo è un'operazione che no ha bisogno di essere confermata dall'admin.
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("UPDATE Biglietto SET evento=? WHERE evento=?");
            ps.setInt(1,eventoModifica.getId());
            ps.setInt(2,eventoPreModifica.getId());
            if(ps.executeUpdate()<1){
                throw new RuntimeException("UPDATE Bigleitti evento error");
            }
            if(eventoModifica.getNumBiglietti()<eventoPreModifica.getNumBiglietti()){
                List<BigliettoBean> bigliettiNonAcquistati= doRetrieveAllAcquistati();
                if(eventoModifica.getNumBiglietti()<bigliettiNonAcquistati.size()) throw new RuntimeException("non puoi rendere disponibili meno biglietti di quelli già acquistati");
                int numDaEliminare=eventoPreModifica.getNumBiglietti()-eventoModifica.getNumBiglietti();
                doDeleteOnlyNonAcquistati(eventoModifica.getId(),numDaEliminare);

            }else if(eventoModifica.getNumBiglietti()>eventoPreModifica.getNumBiglietti()){
                int numDaCreare=eventoModifica.getNumBiglietti()-eventoPreModifica.getNumBiglietti();
                for (int i = 0; i < numDaCreare; i++) {
                    doSave(eventoModifica.getId(),doRetrievePrezzoBigliettoByEvento(eventoModifica.getId()));
                }
            }

            conn.close();
            ps.close();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(int idEvento, int idBiglietto) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Biglietto WHERE id=? and evento=?");
            ps.setInt(1,idBiglietto);
            ps.setInt(2,idEvento);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("DELETE error");
            conn.close();
            ps.close();

        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    private void doDeleteOnlyNonAcquistati(int idEvento,int quantita){
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement psMin=conn.prepareStatement("SELECT MIN(id) FROM Biglietto WHERE evento=? AND acquisto is null");
            psMin.setInt(1,idEvento);
            ResultSet rsMin=psMin.executeQuery();
            int min=1;
            if(rsMin.next())
                min=rsMin.getInt(1);

            for(int i=0;i<quantita;i++){
                doDelete(idEvento,min);
                min++;
            }
            conn.close();
            rsMin.close();
            psMin.close();
        }catch(BatchUpdateException e1){
            throw new RuntimeException("batch sell biglietti error");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
