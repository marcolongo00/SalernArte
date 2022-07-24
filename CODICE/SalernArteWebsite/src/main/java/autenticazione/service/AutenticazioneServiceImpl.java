package autenticazione.service;

import model.dao.*;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;

import java.util.ArrayList;
import java.util.List;

public class AutenticazioneServiceImpl implements AutenticazioneService{
    private UtenteRegistratoDAO daoU;


    public AutenticazioneServiceImpl() {
    }

    @Override
    public UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente) {
        //if null throw exception per tutti
        if(tipoUtente.compareTo("utente")==0){
            daoU=new UtenteDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareTo("scolaresca")==0){
            daoU=new ScolarescaDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        else
        if(tipoUtente.compareTo("organizzatore")==0){
            daoU=new OrganizzatoreDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareTo("amministratore")==0){
            daoU=new AmministratoreDAOImpl();
            return  daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        return null;
    }

    @Override
    public List<UtenteRegistratoBean> retriveAllUtentiSistema(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception utente non autorizzato
        }
        List<UtenteRegistratoBean> allUtenti= new ArrayList<>();
        allUtenti.addAll(new UtenteDAOImpl().doRetrieveAll());
        allUtenti.addAll(new ScolarescaDAOImpl().doRetrieveAll());
        allUtenti.addAll(new OrganizzatoreDAOImpl().doRetrieveAll());
        allUtenti.addAll(new AmministratoreDAOImpl().doRetrieveAll());

        return  allUtenti;
    }
}
