package model.dao;

import model.entity.AmministratoreBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmministratoreDAOImpl extends UtenteRegistratoDAOImpl{
    @Override
    public List<UtenteRegistratoBean> doRetrieveAll() {
        List<UtenteRegistratoBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM UtenteRegistrato JOIN Amministratore USING(id)");
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
    public void doSave(UtenteRegistratoBean utente) {
        super.doSave(utente);

        try(Connection con=ConPool.getConnection()){
            if(!(utente instanceof AmministratoreBean))
                throw  new RuntimeException();//myexception
            PreparedStatement ps=con.prepareStatement("INSERT INTO Amministratore(id,nome,cognome) VALUES(?,?,?)");
            ps.setInt(1,utente.getId());
            ps.setString(2,((AmministratoreBean)utente).getNome());
            ps.setString(3,((AmministratoreBean)utente).getCognome());

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT error.");
            }

            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Amministratore USING(id) WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            AmministratoreBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome= rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                scelto=new AmministratoreBean(id,nome,cognome,email,passwordHash,true);
            }else{
                throw new RuntimeException("Errore Autenticazione utente");
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
    public UtenteRegistratoBean doRetrieveByEmail(String email) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Amministratore USING(id) WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            AmministratoreBean scelto=null;
            if(rs.next()){
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
    public void doUpdate(UtenteRegistratoBean utente) {
        try(Connection con=ConPool.getConnection()){
            if(!(utente instanceof AmministratoreBean))
                throw  new RuntimeException();//myexception

            PreparedStatement ps=con.prepareStatement("UPDATE UtenteRegistrato JOIN Amministratore USING(id)  SET nome=?, cognome=?, email=?,passwordHash=? WHERE id=?");
            ps.setString(1,((AmministratoreBean)utente).getNome());
            ps.setString(2,((AmministratoreBean)utente).getCognome());
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
}
