package model.dao;

import model.entity.AmministratoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;

public abstract class UtenteRegistratoDAOImpl implements UtenteRegistratoDAO{ //oppure con classe abstract
    public static String doRetriveTipoUtenteById(int idUtente){
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("SELECT tipoUtente FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,idUtente);
            ResultSet rs=ps.executeQuery();
            String tipoUtente=null;
            if(rs.next()){
                tipoUtente=rs.getString(1);
            }
            con.close();
            ps.close();
            rs.close();
            return tipoUtente;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static String doRetriveTipoUtenteByEmail(String email){
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("SELECT tipoUtente FROM UtenteRegistrato WHERE email=?");
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            String tipoUtente=null;
            if(rs.next()){
                tipoUtente=rs.getString(1);
            }else{
                throw new RuntimeException("Questa email non è associata a nessun account, ricarica e riprova");
            }

            con.close();
            ps.close();
            rs.close();
            return tipoUtente;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

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
