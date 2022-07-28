package model.dao;

import model.entity.BigliettoBean;
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
                temp.setFattura(rs.getInt("fattura"));

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
    public void sellBiglietto(int idEvento, int quantita, int numFattura) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement psMin=conn.prepareStatement("SELECT MIN(id) FROM Biglietto WHERE evento=? AND fattura is null");
            psMin.setInt(1,idEvento);
            ResultSet rsMin=psMin.executeQuery();
            int min=1;
            if(rsMin.next())
                min=rsMin.getInt(1);
            rsMin.close();
            psMin.close();
            PreparedStatement ps= conn.prepareStatement("UPDATE Biglietto SET fattura=? WHERE id=? AND evento=?");
            //inutile con quantita=0 che non dovrebbe proprio poter essere selezionata
            //aggiorna anche num biglietti in mostra
            for(int i=0;i<quantita;i++){
                ps.setInt(1,numFattura);
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
                throw new RuntimeException("UPDATE Mostra error");
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
}
