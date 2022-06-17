package model.dao;

import model.entity.AmministratoreBean;
import model.entity.ScolarescaBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmministratoreDAOImpl implements AmministratoreDAO{

    @Override
    public List<AmministratoreBean> doRetrieveAll() {
        List<AmministratoreBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM Amministratore");
            while(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome= rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String email=rs.getString("email");
                lista.add(new AmministratoreBean(id,nome,cognome,email,passwordHash,true));
            }
            con.close();
            stm.close();
            rs.close();
            return lista;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public AmministratoreBean doRetrieveByEmail(String email) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Amministratore WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            AmministratoreBean scelto=null;
            if(rs.next()){
                scelto=new AmministratoreBean();
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome= rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                scelto=new AmministratoreBean(id,nome,cognome,email,passwordHash,true);
            }

            con.close();
            stm.close();
            rs.close();
            return scelto;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public AmministratoreBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Amministratore WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            AmministratoreBean scelto=null;
            if(rs.next()){
                scelto=new AmministratoreBean();
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome= rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                scelto=new AmministratoreBean(id,nome,cognome,email,passwordHash,true);
            }
            con.close();
            stm.close();
            rs.close();
            return scelto;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doSave(AmministratoreBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("INSERT INTO Amministratore(nome,cognome,email, passwordHash) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            utente.setId(id); //cosa lo setto a fare se poi è void?

            con.close();
            ps.close();
            rs.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doUpdate(AmministratoreBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("UPDATE Amministratore SET nome=?, cognome=?, email=?,passwordHash=? WHERE id=?");
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setInt(5,utente.getId());
            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("UPDATE Amministratore error.");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(int idAdmin) {
        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM Amministratore WHERE id=?");
            ps.setInt(1,idAdmin);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE AMMINISTRATORE ERROR");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
