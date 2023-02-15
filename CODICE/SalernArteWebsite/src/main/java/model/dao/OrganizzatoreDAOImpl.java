package model.dao;

import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizzatoreDAOImpl extends UtenteRegistratoDAOImpl{
    @Override
    public List<UtenteRegistratoBean> doRetrieveAll() {
        List<UtenteRegistratoBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id)");
            while(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String email= rs.getString("email");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String iban= rs.getString("iban");
                lista.add(new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,dataNascita,true));
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
    public boolean doSave(UtenteRegistratoBean utente) {
        super.doSave(utente);

        try(Connection con=ConPool.getConnection()){
            if(!(utente instanceof OrganizzatoreBean)){ //controllata anche nelle service ma contorllo in più qui
                throw new RuntimeException();//myexception
            }

            PreparedStatement ps=con.prepareStatement("INSERT INTO Organizzatore (id,nome,cognome,biografia,dataDiNascita,sesso,iban) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1,utente.getId());
            ps.setString(2,((OrganizzatoreBean)utente).getNome());
            ps.setString(3,((OrganizzatoreBean)utente).getCognome());
            ps.setString(4,((OrganizzatoreBean)utente).getBiografia());
            ps.setDate(5,((OrganizzatoreBean)utente).getDataDiNascita());
            ps.setInt(6,((OrganizzatoreBean)utente).getSesso());
            ps.setString(7,((OrganizzatoreBean)utente).getIban());

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT error.");
            }

            con.close();
            ps.close();
            return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id) WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            OrganizzatoreBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String iban= rs.getString("iban");
                scelto=new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,dataNascita,true);
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
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id) WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            OrganizzatoreBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String iban= rs.getString("iban");
                scelto=new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,dataNascita,true);
            }

            con.close();
            stm.close();
            rs.close();
            return scelto;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public OrganizzatoreBean doRetrieveById(int id) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id) WHERE id=?");
            stm.setInt(1,id);
            ResultSet rs=stm.executeQuery();
            OrganizzatoreBean scelto=null;
            if(rs.next()){
                String email= rs.getString("email");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String passwordHash=rs.getString("passwordHash");
                String bio=rs.getString("biografia");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                String iban= rs.getString("iban");
                scelto=new OrganizzatoreBean(id,sesso,iban,nome,cognome,email,passwordHash,bio,dataNascita,true);
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
    public boolean doUpdate(UtenteRegistratoBean utente) {
        try(Connection con=ConPool.getConnection()){
            if(!(utente instanceof OrganizzatoreBean)){
                throw  new RuntimeException(); //myexception
            }

            PreparedStatement ps=con.prepareStatement("UPDATE UtenteRegistrato JOIN Organizzatore USING(id) SET nome=?, cognome=?, email=?,passwordHash=?, biografia=?, dataDiNascita=?, sesso=?, iban=? WHERE id=?");
            ps.setString(1,((OrganizzatoreBean)utente).getNome());
            ps.setString(2,((OrganizzatoreBean)utente).getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setString(5,((OrganizzatoreBean)utente).getBiografia());
            ps.setDate(6,((OrganizzatoreBean)utente).getDataDiNascita());
            ps.setInt(7,((OrganizzatoreBean)utente).getSesso());
            ps.setString(8,((OrganizzatoreBean)utente).getIban());
            ps.setInt(9,utente.getId());
            if(ps.executeUpdate() < 1)
            {
                throw new RuntimeException("UPDATE Organizzatore error.");
            }
            con.close();
            ps.close();
            return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
