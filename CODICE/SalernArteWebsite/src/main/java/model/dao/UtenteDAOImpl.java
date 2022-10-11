package model.dao;

import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOImpl extends UtenteRegistratoDAOImpl {//prova extends e non interfaccia
    @Override
    public List<UtenteRegistratoBean> doRetrieveAll() {
        List<UtenteRegistratoBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM UtenteRegistrato JOIN Utente USING(id) ");
            while(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String email=rs.getString("email");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");

                lista.add(new UtenteBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true));
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
            if(!(utente instanceof UtenteBean)){ //controllata anche nelle service ma contorllo in più qui
                throw new RuntimeException();//myexception
            }

           PreparedStatement ps=con.prepareStatement("insert into Utente(id,nome,cognome,dataDiNascita,sesso) values(?,?,?,?,?)");

            ps.setInt(1,utente.getId());
            ps.setString(2,((UtenteBean)utente).getNome());
            ps.setString(3,((UtenteBean)utente).getCognome());
            ps.setDate(4,((UtenteBean)utente).getDataDiNascita());
            ps.setInt(5,((UtenteBean)utente).getSesso());

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
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Utente USING(id) WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            UtenteBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                scelto=new UtenteBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true);

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
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Utente USING(id) WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            UtenteBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                scelto=new UtenteBean(id,sesso,nome,cognome,email,passwordHash,dataNascita,true);
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
            if(!(utente instanceof UtenteBean)){ //controllata anche nelle service ma contorllo in più qui
                throw new RuntimeException();//myexception
            }

            PreparedStatement ps=con.prepareStatement("UPDATE UtenteRegistrato JOIN Utente USING(id) SET nome=?, cognome=?, email=?,passwordHash=?, dataDiNascita=?, sesso=? WHERE id=?");
            ps.setString(1,((UtenteBean)utente).getNome());
            ps.setString(2,((UtenteBean)utente).getCognome());
            ps.setString(3,utente.getEmail());
            ps.setString(4,utente.getPasswordHash());
            ps.setDate(5,((UtenteBean)utente).getDataDiNascita());
            ps.setInt(6,((UtenteBean)utente).getSesso());
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

}
