package model.dao;

import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScolarescaDAOImpl extends UtenteRegistratoDAOImpl{
    @Override
    public List<UtenteRegistratoBean> doRetrieveAll() {
        List<UtenteRegistratoBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM UtenteRegistrato JOIN Scolaresca USING(id) ");
            while(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String email=rs.getString("email");
                String istituto= rs.getString("istituto");
                lista.add(new ScolarescaBean(id,email,passwordHash,istituto,true));
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
           if(!(utente instanceof ScolarescaBean))
               throw new RuntimeException(); //myException
            PreparedStatement ps=con.prepareStatement("INSERT INTO Scolaresca (id,istituto) VALUES(?,?)");
            ps.setInt(1,utente.getId());
            ps.setString(2,((ScolarescaBean)utente).getIstituto());

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
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Scolaresca USING(id) WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            ScolarescaBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String istituto= rs.getString("istituto");
                scelto=new ScolarescaBean(id,email,passwordHash,istituto,true);
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
            PreparedStatement stm=con.prepareStatement("SELECT * FROM UtenteRegistrato JOIN Scolaresca USING(id) WHERE email=? ");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            ScolarescaBean scelto=null;
            if(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String istituto= rs.getString("istituto");
                scelto=new ScolarescaBean(id,email,passwordHash,istituto,true);
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
            if(!(utente instanceof ScolarescaBean)){ //controllata anche nelle service ma contorllo in più qui
                throw new RuntimeException();//myexception
            }

            PreparedStatement ps=con.prepareStatement("UPDATE UtenteRegistrato JOIN Scolaresca USING(id) SET email=?,passwordHash=?, istituto=? WHERE id=?");
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,((ScolarescaBean)utente).getIstituto());
            ps.setInt(4,utente.getId());
            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("UPDATE Scolaresca error.");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
