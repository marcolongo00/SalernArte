package GestioneUtente.dao;

import model.dao.UtenteRegistratoDAO;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import org.junit.BeforeClass;
import singleton.ConPool;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class UtenteDaoImplTest {
    private static UtenteBean utente;

    private static class UtenteRegistratoDaoImpl implements UtenteRegistratoDAO {
            @Override
            public List<UtenteRegistratoBean> doRetrieveAll() {
                return null;
            }

            @Override
            public UtenteRegistratoBean doRetrieveByEmailPassword(String email, String password) {
                return null;
            }

            @Override
            public boolean doSave(UtenteRegistratoBean utente) {
                return false;
            }

            @Override
            public void doUpdate(UtenteRegistratoBean utente) {

            }

            @Override
            public UtenteRegistratoBean doRetrieveByEmail(String email) {
                return null;
            }

            @Override
            public void doDelete(int idUtente) {

            }

            @Override
            public UtenteRegistratoBean doRetrieveById(int id) {
                // Implementazione del metodo
                return null;
            }
        }

    }

    private static UtenteRegistratoBean org;

    private static UtenteRegistratoBean utenteTest;

    public static UtenteRegistratoBean getUtenteTest() {
        UtenteRegistratoBean utenteRegistratoTest = null;
        return utenteRegistratoTest;
    }

    public static void setUtenteTest(UtenteBean utenteTest) {
        UtenteDaoImplTest.utente = utenteTest;
    }
    @BeforeClass
    public static void startUp(){
        org= new UtenteRegistratoBean( );

        Calendar c= Calendar.getInstance();
        Date DATA_ATTUALE= new Date(Calendar.getInstance().getTimeInMillis());
        c.setTime(DATA_ATTUALE);

        try(Connection con= ConPool.getConnection()){
            PreparedStatement ps=con.prepareStatement("insert into UtenteRegistrato(email,passwordHash,tipoUtente)VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,org.getEmail());
            ps.setString(2,org.getPasswordHash());
            ps.setString(3,"utenteRegistrato");

            if(ps.executeUpdate() !=1)
            {
                throw new RuntimeException("INSERT UtenteRegistrato error.");
            }
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            int id=rs.getInt(1);
            org.setId(id);

            con.close();
            ps.close();
            rs.close();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }


}
