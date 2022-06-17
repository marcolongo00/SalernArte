package model.dao;

import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteRegistratoDAOImpl implements UtenteRegistratoDAO{
    @Override
    public List<UtenteRegistratoBean> doRetrieveAll() {
        List<UtenteRegistratoBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM UtenteRegistrato");
            while(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String email=rs.getString("email");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                lista.add(new UtenteRegistratoBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true));
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
    public UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            UtenteRegistratoBean scelto=null;
            if(rs.next()){
                scelto=new UtenteRegistratoBean();
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                scelto=new UtenteRegistratoBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true);

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
    public void doSave(UtenteRegistratoBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("INSERT INTO UtenteRegistrato (nome,cognome,email, passwordHash,dataDiNascita,sesso) VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setDate(5,utente.getDataDiNascita());
            ps.setInt(6,utente.getSesso());

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
    public void doUpdate(UtenteRegistratoBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("UPDATE UtenteRegistrato SET nome=?, cognome=?, email=?,passwordHash=?, dataDiNasciua=?, sesso=? WHERE id=?");
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setDate(5,utente.getDataDiNascita());
            ps.setInt(6,utente.getSesso());
            ps.setInt(7,utente.getId());
            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("UPDATE UTENTE error.");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteRegistratoBean doRetrieveByEmail(String email) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            UtenteRegistratoBean scelto=null;
            if(rs.next()){
                scelto=new UtenteRegistratoBean();
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                scelto=new UtenteRegistratoBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true);
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
