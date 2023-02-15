package model.dao;

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
            }

            con.close();
            ps.close();
            rs.close();
            return tipoUtente;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteRegistratoBean doRetrieveById(int id) {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM UtenteRegistrato WHERE id = ? ");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            UtenteRegistratoBean utente = null;

            if(rs.next())
            {
                utente = (UtenteRegistratoBean) rs.getObject(1);
            }
            else {
                throw new RuntimeException("Questo id non è presente nel database, riprova");
            }
            con.close();
            ps.close();
            rs.close();
            return utente;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doSave(UtenteRegistratoBean utente){//implementato poi sovrascritto dai figli
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,utente.getTipoUtente());

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
            return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean doDelete(int idUtente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM UtenteRegistrato WHERE id=?");
            ps.setInt(1,idUtente);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE UTENTE ERROR");
            }
            con.close();
            ps.close();
            return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
