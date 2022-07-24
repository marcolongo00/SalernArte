package model.dao;

import model.entity.AcquistoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcquistoDAOImpl implements AcquistoDAO {
    @Override
    public List<AcquistoBean> doRetrieveListaAcquistiByIdUtente(int idUtente) {
        try(Connection conn= ConPool.getConnection()){
            List<AcquistoBean> acquisti= new ArrayList<>();
            PreparedStatement ps=conn.prepareStatement("SELECT * FROM Acquisto WHERE idUtente=?");
            ps.setInt(1,idUtente);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                AcquistoBean temp= new AcquistoBean();
                temp.setNumOrdine(rs.getInt("numOrdine"));
                temp.setTotale(rs.getDouble("totale"));
                temp.setData(rs.getDate("data"));
                temp.setIdUtente(idUtente);
                temp.setProdotti(rs.getString("prodotti"));
                acquisti.add(temp);
            }
            conn.close();
            ps.close();
            rs.close();
            return acquisti;
        }catch(SQLException e){
            throw  new RuntimeException(e);
        }
    }



    @Override
    public AcquistoBean doRetrieveAcquistoByNumOrdine(int numOrdine) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("SELECT * FROM Acquisto WHERE numOrdine=?");
            ps.setInt(1,numOrdine);
            ResultSet rs=ps.executeQuery();
            AcquistoBean temp=null;
            if(rs.next()){
                temp=new AcquistoBean();
                temp.setNumOrdine(numOrdine);
                temp.setProdotti(rs.getString("prodotti"));
                temp.setData(rs.getDate("data"));
                temp.setTotale(rs.getDouble("totale"));
                temp.setIdUtente(rs.getInt("idUtente"));
            }
            con.close();
            ps.close();
            rs.close();
            return temp;
        }catch(SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void doSave(AcquistoBean acquisto) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Acquisto(data,totale,idUtente,prodotti) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setDate(1,acquisto.getData());
            ps.setDouble(2,acquisto.getTotale());
            ps.setInt(3,acquisto.getIdUtente());
            ps.setString(4,acquisto.getProdotti());
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("INSERT error");
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int numOrdine=rs.getInt(1);
            acquisto.setNumOrdine(numOrdine);
            conn.close();
            ps.close();
            rs.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doUpdateProdottiByNumOrdine(int numOrdine, String prodotti) {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("UPDATE Acquisto SET prodotti=? WHERE numOrdine=?");
            ps.setString(1,prodotti);
            ps.setInt(2,numOrdine);

            if(ps.executeUpdate()!=1){
                throw new RuntimeException("SET Prodotti fattura error");
            }
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doDelete(int numOrdine) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Acquisto WHERE numOrdine=?");
            ps.setInt(1,numOrdine);
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("DELETE error");
            conn.close();
            ps.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
