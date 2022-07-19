package model.dao;

import model.entity.AmministratoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;

public abstract class UtenteRegistratoDAOImpl implements UtenteRegistratoDAO{ //oppure con classe abstract

    public void doSave(UtenteRegistratoBean utente){//implementato poi sovrascritto dai figli
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,"Utente");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            utente.setId(id);

            con.close();
            ps.close();
            rs.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void doDelete(int idUtente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,idUtente);
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
