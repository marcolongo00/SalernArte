package model.dao;

import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizzatoreDAOImpl implements OrganizzatoreDAO{

    @Override
    public List<OrganizzatoreBean> doRetrieveAll() {
        List<OrganizzatoreBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM Organizzatore");
            while(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String email= rs.getString("email");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String azienda= rs.getString("azienda");
                String iban= rs.getString("iban");
                lista.add(new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,azienda,dataNascita,true));
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
    public OrganizzatoreBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Organizzatore WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            OrganizzatoreBean scelto=null;
            if(rs.next()){
                scelto=new OrganizzatoreBean();
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String azienda= rs.getString("azienda");
                String iban= rs.getString("iban");
                scelto=new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,azienda,dataNascita,true);
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
    public void doSave(OrganizzatoreBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("INSERT INTO Organizzatore (nome,cognome,email, passwordHash,biografia,dataDiNascita,sesso,azienda,iban) VALUES(?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setString(5,utente.getBiografia());
            ps.setDate(6,utente.getDataDiNascita());
            ps.setInt(7,utente.getSesso());
            ps.setString(8,utente.getAzienda());
            ps.setString(9,utente.getIban());

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
    public void doUpdate(OrganizzatoreBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("UPDATE Organizzatore SET nome=?, cognome=?, email=?,passwordHash=?, biografia=?, dataDiNasciua=?, sesso=?, azienda=?, iban=? WHERE id=?");
            ps.setString(1,utente.getNome());
            ps.setString(2,utente.getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setString(5,utente.getBiografia());
            ps.setDate(6,utente.getDataDiNascita());
            ps.setInt(7,utente.getSesso());
            ps.setString(8,utente.getAzienda());
            ps.setString(9,utente.getIban());
            ps.setInt(10,utente.getId());
            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("UPDATE Organizzatore error.");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrganizzatoreBean doRetrieveByEmail(String email) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Organizzatore WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            OrganizzatoreBean scelto=null;
            if(rs.next()){
                scelto=new OrganizzatoreBean();
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String azienda= rs.getString("azienda");
                String iban= rs.getString("iban");
                scelto=new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,azienda,dataNascita,true);
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
        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM Organizzatore WHERE id=?");
            ps.setInt(1,idUtente);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE ORGANIZZATORE ERROR");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
