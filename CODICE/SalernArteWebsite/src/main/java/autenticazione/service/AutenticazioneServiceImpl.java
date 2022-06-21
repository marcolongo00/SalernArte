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

    @Override
    public Object registrazioneUtente(String tipoUtente, String email, String password, String nome, String cognome, String dataDiNascita, String gender, String istituto, String biografia, String azienda, String iban) {
        //if null throw exception per tutti
        if(tipoUtente.compareTo("utenteRegistrato")==0){
            UtenteRegistratoBean newUtente=new UtenteRegistratoBean(AutenticazioneServiceImpl.getNumberGender(gender),nome,cognome,email,password,Date.valueOf(dataDiNascita),false);
            return daoUtenteRegistrato.doSave(newUtente);
        }else
        if(tipoUtente.compareTo("scolaresca")==0){
            ScolarescaBean newUtente= new ScolarescaBean(email,password,istituto,false);
            return daoScolaresca.doSave(newUtente);
        }
        else
        if(tipoUtente.compareTo("organizzatore")==0){
            OrganizzatoreBean newUtente=new OrganizzatoreBean(AutenticazioneServiceImpl.getNumberGender(gender),iban,nome,cognome,email,password,biografia,azienda,Date.valueOf(dataDiNascita),false);
            return daoOrganizzatore.doSave(newUtente);
        }
        return null;
    }

    private static int getNumberGender(String gender){
        if(gender.compareTo("uomo")==0){
            return 0;
        }else
            if(gender.compareTo("donna")==0){
                return 1;
        }else {
                return 2;
            }
    }


}
