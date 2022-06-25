package autenticazione.service;

import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public class AutenticazioneServiceImpl implements AutenticazioneService{
    private UtenteRegistratoDAO daoUtenteRegistrato;
    private OrganizzatoreDAO daoOrganizzatore;
    private ScolarescaDAO daoScolaresca;
    private AmministratoreDAO daoAdmin;


    public AutenticazioneServiceImpl() {
        daoUtenteRegistrato=new UtenteRegistratoDAOImpl();
        daoOrganizzatore= new OrganizzatoreDAOImpl();
        daoScolaresca= new ScolarescaDAOImpl();
        daoAdmin= new AmministratoreDAOImpl();

    }

    @Override
    public Object loginUtente(String email, String passwordNotHash, String tipoUtente) {
        //if null throw exception per tutti
        if(tipoUtente.compareTo("utenteRegistrato")==0){
            return daoUtenteRegistrato.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareTo("scolaresca")==0){
            return daoScolaresca.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        else
        if(tipoUtente.compareTo("organizzatore")==0){
            return daoOrganizzatore.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareTo("amministratore")==0){
            return  daoAdmin.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        return null;
    }



}
