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
            ResultSet rs=stm.executeQuery("SELECT * FROM UTENTEREGISTRATO");
            while(rs.next()){
                int id= rs.getInt("id");
                String passwordHash=rs.getString("passwordHash");
                String nome= rs.getString("nome");
                String cognome=rs.getString("cognome");
                String email=rs.getString("email");
                Date dataNascita= rs.getDate("dataDiNascita");
                int sesso =rs.getInt("sesso");
                //CONTROLLA
                //lista.add(new Utente(user,passwordHash,nome,cognome,email,admin));
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
        return null;
    }

    @Override
    public void doSave(UtenteRegistratoBean utente) {

    }

    @Override
    public void doUpdate(UtenteRegistratoBean utente) {

    }

    @Override
    public UtenteRegistratoBean doRetrieveByEmail(String email) {
        return null;
    }

    @Override
    public void doDelete(UtenteRegistratoBean utente) {

    }
    //file vuoti per rispettare la struttura definita nei documenti ODD E SDD
}
