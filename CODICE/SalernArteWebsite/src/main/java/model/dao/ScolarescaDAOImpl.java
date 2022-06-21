package model.dao;

import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;
import singleton.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScolarescaDAOImpl implements  ScolarescaDAO{
    @Override
    public List<ScolarescaBean> doRetrieveAll() {
        List<ScolarescaBean> lista=new ArrayList<>();
        try(Connection con= ConPool.getConnection()){
            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM Scolaresca");
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
    public ScolarescaBean doRetrieveByEmail(String email) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Scolaresca WHERE email=?");
            stm.setString(1,email);
            ResultSet rs=stm.executeQuery();
            ScolarescaBean scelto=null;
            if(rs.next()){
                scelto=new ScolarescaBean();
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
    public ScolarescaBean doRetrieveByEmailPassword(String email, String password) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement stm=con.prepareStatement("SELECT * FROM Scolaresca WHERE email=? AND passwordHash=SHA1(?)");
            stm.setString(1,email);
            stm.setString(2,password);
            ResultSet rs=stm.executeQuery();
            ScolarescaBean scelto=null;
            if(rs.next()){
                scelto=new ScolarescaBean();
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
    public ScolarescaBean doSave(ScolarescaBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("INSERT INTO Scolaresca (email, passwordHash,istituto) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,utente.getIstituto());

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
            return  utente;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doUpdate(ScolarescaBean utente) {
        try(Connection con=ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("UPDATE Scolaresca SET email=?,passwordHash=?, istituto=? WHERE id=?");
            ps.setString(1,utente.getEmail());
            ps.setString(2,utente.getPasswordHash());
            ps.setString(3,utente.getIstituto());
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

    @Override
    public void doDelete(int idScuola) {
        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("DELETE FROM Scolaresca WHERE id=?");
            ps.setInt(1,idScuola);
            if(ps.executeUpdate()!=1){
                throw new RuntimeException("DELETE SCOLARESCA ERROR");
            }
            con.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
