package autenticazione.service;

import model.dao.*;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.entity.CarrelloBean.BigliettoQuantita;

public class AutenticazioneServiceImpl implements AutenticazioneService{
    private UtenteRegistratoDAO daoU;
    private AmministratoreDAOImpl daoAmm;
    private OrganizzatoreDAOImpl daoOrg;
    private UtenteDAOImpl daoUtente;
    private ScolarescaDAOImpl daoScol;
    public AutenticazioneServiceImpl() {
        daoOrg = new OrganizzatoreDAOImpl();
        daoUtente = new UtenteDAOImpl();
        daoScol = new ScolarescaDAOImpl();
        daoAmm = new AmministratoreDAOImpl();
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
        allUtenti.addAll(daoUtente.doRetrieveAll());
        allUtenti.addAll(daoScol.doRetrieveAll());
        allUtenti.addAll(daoOrg.doRetrieveAll());
        allUtenti.addAll(daoAmm.doRetrieveAll());

        return  allUtenti;
    }

    @Override
    public void updateUtente(UtenteRegistratoBean utente) {

        System.out.println(utente.getTipoUtente());

        if(utente.getTipoUtente().compareToIgnoreCase("amministratore") == 0)
        {
            daoAmm.doUpdate(utente);
        }
        else if(utente.getTipoUtente().compareToIgnoreCase("organizzatore") == 0)
        {
            daoOrg.doUpdate(utente);
        }
        else if(utente.getTipoUtente().compareToIgnoreCase("scolaresca") == 0)
        {
            daoScol.doUpdate(utente);
        }
        else
        {
            daoU.doUpdate(utente);
        }
    }

    @Override
    public CarrelloBean mergeCarrelloSessioneAndCarrelloDBAfterLogin(UtenteRegistratoBean utenteRegistratoBean, CarrelloBean carrelloSessione) {
        //da testare
        CarrelloDAO daoCarr= new CarrelloDAOImpl();
        if(carrelloSessione!=null && !carrelloSessione.getProdotti().isEmpty()){
            CarrelloBean saved=daoCarr.doRetrieveByIdUtente(utenteRegistratoBean.getId());
            Collection<BigliettoQuantita> prodotti=carrelloSessione.getProdotti();
                for (BigliettoQuantita bi: prodotti) {
                    if(saved.contains(bi)){
                        if(bi.getQuantita() != saved.get(bi.getProdotto().getId()).getQuantita())
                            daoCarr.doUpdateQuantita(utenteRegistratoBean.getId(),bi);
                    }
                    else
                        daoCarr.doSave(utenteRegistratoBean.getId(),bi);
                }
        }else{
                carrelloSessione=daoCarr.doRetrieveByIdUtente(utenteRegistratoBean.getId()) ;
        }
        return carrelloSessione;
    }
}
