package model.dao;

import model.entity.FatturaBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FatturaDAOImpl implements  FatturaDAO{
    @Override
    public List<FatturaBean> doRetrieveListaFattureByUtenteRegistrato(int idUtente) {
        try(Connection conn= ConPool.getConnection()){
            List<FatturaBean> fatture= new ArrayList<>();
            PreparedStatement ps=conn.prepareStatement("SELECT * FROM Fattura WHERE idUtente=? AND tipoUtente=true");
            ps.setInt(1,idUtente);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                FatturaBean temp= new FatturaBean();
                temp.setNumOrdine(rs.getInt("numOrdine"));
                temp.setTotale(rs.getDouble("totale"));
                temp.setData(rs.getDate("data"));
                temp.setIdUtente(idUtente);
                temp.setProdotti(rs.getString("prodotti"));
                temp.setTipoUtente(true);
                fatture.add(temp);
            }
            conn.close();
            ps.close();
            rs.close();
            return fatture;
        }catch(SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public List<FatturaBean> doRetrieveListaFattureByScolaresca(int idUtente) {
        try(Connection conn= ConPool.getConnection()){
            List<FatturaBean> fatture= new ArrayList<>();
            PreparedStatement ps=conn.prepareStatement("SELECT * FROM Fattura WHERE idUtente=? AND tipoUtente=false");
            ps.setInt(1,idUtente);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                FatturaBean temp= new FatturaBean();
                temp.setNumOrdine(rs.getInt("numOrdine"));
                temp.setTotale(rs.getDouble("totale"));
                temp.setData(rs.getDate("data"));
                temp.setIdUtente(idUtente);
                temp.setProdotti(rs.getString("prodotti"));
                temp.setTipoUtente(false);
                fatture.add(temp);
            }
            conn.close();
            ps.close();
            rs.close();
            return fatture;
        }catch(SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public FatturaBean doRetrieveFatturaByNumOrdine(int numOrdine) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("SELECT * FROM Fattura WHERE numOrdine=?");
            ps.setInt(1,numOrdine);
            ResultSet rs=ps.executeQuery();
            FatturaBean temp=null;
            if(rs.next()){
                temp=new FatturaBean();
                temp.setNumOrdine(numOrdine);
                temp.setProdotti(rs.getString("prodotti"));
                temp.setData(rs.getDate("data"));
                temp.setTipoUtente(rs.getBoolean("tipoUtente"));
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
    public void doSave(FatturaBean fattura) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Fattura(data,totale,idUtente,prodotti,tipoUtente) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setDate(1,fattura.getData());
            ps.setDouble(2,fattura.getTotale());
            ps.setInt(3,fattura.getIdUtente());
            ps.setString(4,fattura.getProdotti());
            ps.setBoolean(5,fattura.isTipoUtente());
            if(ps.executeUpdate()!=1)
                throw new RuntimeException("INSERT error");
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int numOrdine=rs.getInt(1);
            fattura.setNumOrdine(numOrdine);
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
            PreparedStatement ps=conn.prepareStatement("UPDATE Fattura SET prodotti=? WHERE numOrdine=?");
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
    public void doDeleteFattura(int numOrdine) {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps= conn.prepareStatement("DELETE FROM Fattura WHERE numOrdine=?");
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
