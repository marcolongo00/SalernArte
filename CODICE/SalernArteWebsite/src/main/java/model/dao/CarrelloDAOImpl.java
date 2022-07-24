package model.dao;

import model.entity.CarrelloBean;
import model.entity.EventoBean;
import singleton.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.entity.CarrelloBean.BigliettoQuantita;

public class CarrelloDAOImpl implements  CarrelloDAO{
    private double getPrezzoBiglietto(int idEvento){
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
    public CarrelloBean doRetrieveByIdUtente(int idUtente) {
        try(Connection conn= ConPool.getConnection()){
            CarrelloBean carrello= new CarrelloBean(idUtente);
            PreparedStatement ps= conn.prepareStatement("SELECT * FROM Evento as e JOIN Carrello as c on e.id=c.idevento WHERE c.idUtente=?");
            ps.setInt(1,idUtente);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                EventoBean temp= new EventoBean();
                int idE=rs.getInt("id");
                temp.setId(idE);
                temp.setIdOrganizzatore(rs.getInt("idOrganizzatore"));
                temp.setNome(rs.getString("nome"));
                temp.setTipo(rs.getBoolean("tipo"));
                temp.setDescrizione(rs.getString("descrizione"));
                temp.setPath(rs.getString("pathFoto"));
                temp.setNumBiglietti(rs.getInt("numBiglietti"));
                temp.setDataInizio(rs.getDate("dataInizio"));
                temp.setDataFine(rs.getDate("dataFine"));
                temp.setIndirizzo(rs.getString("indirizzo"));
                temp.setSede(rs.getString("sede"));
                temp.setAttivo(rs.getBoolean("attivo"));

                int quantita=rs.getInt("quantita");
                carrello.put(temp,quantita,getPrezzoBiglietto(idE));
            }
            conn.close();
            ps.close();
            rs.close();
            return carrello;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doSave(int idUtente, BigliettoQuantita carr) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Carrello(idUtente,idEvento,quantita) VALUES(?,?,?)");
            ps.setInt(1,idUtente);
            ps.setInt(2,carr.getProdotto().getId());
            ps.setInt(3, carr.getQuantita());
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("INSERT in Carrello error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doDelete(int idUtente, int idEvento) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Carrello WHERE idEvento=? AND idUtente=?");
            ps.setInt(1,idEvento);
            ps.setInt(2,idUtente);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("DELETE Evento from carrello error");
            conn.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void svuotaCarrello(int idUtente) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Carrello WHERE idUtente=?");
            ps.setInt(1,idUtente);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("svuota carrello error");
            conn.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doUpdateQuantita(int idUtente, BigliettoQuantita carr) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("UPDATE Carrello SET quantita=? WHERE idUtente=? AND idEvento=?");
            ps.setInt(1,carr.getQuantita());
            ps.setInt(2,idUtente);
            ps.setInt(3,carr.getProdotto().getId());
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE quantità carrello error");
            }
            conn.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doUpdateQuantitaFromCarrelliAfterAcquisto(int idEvento, int bigliettiRimasti) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("UPDATE Carrello SET quantita=? WHERE idEvento=? AND quantita > ?"); // dubbio su quantita altri prodotti
            ps.setInt(1,bigliettiRimasti);
            ps.setInt(2,idEvento);
            ps.setInt(3,bigliettiRimasti);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("UPDATE quantità carrello error");
            }
            conn.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
