package autenticazione.service;

import model.dao.*;
import model.entity.*;

import java.sql.Date;
import java.util.*;

import model.entity.CarrelloBean.BigliettoQuantita;


public class AutenticazioneServiceImpl implements AutenticazioneService{
    private UtenteRegistratoDAO daoU;
    private AmministratoreDAOImpl daoAmm;
    private OrganizzatoreDAOImpl daoOrg;
    private UtenteDAOImpl daoUtente;
    private ScolarescaDAOImpl daoScol;
    private AcquistoDAO daoAcq;
    public AutenticazioneServiceImpl() {
        daoOrg = new OrganizzatoreDAOImpl();
        daoUtente = new UtenteDAOImpl();
        daoScol = new ScolarescaDAOImpl();
        daoAmm = new AmministratoreDAOImpl();
        daoAcq=new AcquistoDAOImpl();
    }

    public AutenticazioneServiceImpl(ScolarescaDAOImpl daoScol, OrganizzatoreDAOImpl daoOrg, UtenteDAOImpl daoUtente, AmministratoreDAOImpl daoAmm)
    {
        this.daoScol = daoScol;
        this.daoOrg = daoOrg;
        this.daoUtente = daoUtente;
        this.daoAmm = daoAmm;
        daoAcq=new AcquistoDAOImpl();
    }

    @Override
    public UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente) {
         if(tipoUtente!=null && tipoUtente.compareToIgnoreCase("utente")==0){
            return daoUtente.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente!=null && tipoUtente.compareToIgnoreCase("scolaresca")==0){
            return daoScol.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        else
        if(tipoUtente!=null && tipoUtente.compareToIgnoreCase("organizzatore")==0){
            return daoOrg.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente!=null && tipoUtente.compareToIgnoreCase("amministratore")==0){
            return daoAmm.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        return null;
    }
    @Override
    public List<UtenteRegistratoBean> retrieveAllUtentiSistema(UtenteRegistratoBean utenteLoggato) {
        if(utenteLoggato==null || utenteLoggato.getTipoUtente().compareToIgnoreCase("amministratore")!=0){
            throw new RuntimeException("operazione non autorizzata");
        }
        List<UtenteRegistratoBean> allUtenti= new ArrayList<>();
        allUtenti.addAll(daoUtente.doRetrieveAll());
        allUtenti.addAll(daoScol.doRetrieveAll());
        allUtenti.addAll(daoOrg.doRetrieveAll());
        allUtenti.addAll(daoAmm.doRetrieveAll());

        return  allUtenti;
    }

    @Override
    public List<AcquistoBean> retrieveListaOrdiniUtente(UtenteRegistratoBean utenteLoggato, int idUtente) {
        if(utenteLoggato==null || utenteLoggato.getTipoUtente().compareToIgnoreCase("organizzatore")==0){
            throw  new RuntimeException("Richiesta Illegale");
        }
        List<AcquistoBean> ordini= daoAcq.doRetrieveListaAcquistiByIdUtente(idUtente);

        Collections.reverse(ordini); //per avere la lista in ordine dal più recente
        return ordini;
    }


    public UtenteRegistratoBean updateUtente(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            //se non viene inserita la password non dve eessere cambiata e si inserisce la vecchia
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }else {
            //altrimenti controllare il formato password inserito
            if(!passwordNoHash.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{6,30}$")){
                throw new RuntimeException("Password non valida.");
            }
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Cognome non valido.");
        }
        Date dataAttuale = new Date(Calendar.getInstance().getTimeInMillis());
        //se il formato data non è corretto sarà il parse nell'areaUtentecontroller a dare errore
        if (dataDiNascita.after(dataAttuale) || dataDiNascita.toLocalDate().isEqual(dataAttuale.toLocalDate())) {
            throw new RuntimeException("impostazioni data di nascita inserite non valide");
        }
        if (gender != 0 && gender != 1 && gender != 2) {
            throw new RuntimeException("dati per genere non corretti");
        }
        UtenteBean utenteAggiornato = new UtenteBean(utenteLoggato.getId(), gender, nome, cognome, email, passwordNoHash, dataDiNascita, isHash);
        daoUtente.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }
    @Override
    public UtenteRegistratoBean updateAmministratore(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            //se non viene inserita la password non dve eessere cambiata e si inserisce la vecchia
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }else {
            //altrimenti controllare il formato password inserito
            if(!passwordNoHash.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{6,30}$")){
                throw new RuntimeException("Password non valida.");
            }
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Cognome non valido.");
        }
        AmministratoreBean utenteAggiornato = new AmministratoreBean(utenteLoggato.getId(),nome,cognome,email,passwordNoHash,isHash);
        daoAmm.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }

    @Override
    public UtenteRegistratoBean updateScolaresca(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String istituto) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            //se non viene inserita la password non dve eessere cambiata e si inserisce la vecchia
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }else {
            //altrimenti controllare il formato password inserito
            if(!passwordNoHash.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{6,30}$")){
                throw new RuntimeException("Password non valida.");
            }

        }
        if (istituto == null || istituto.isEmpty() || !istituto.matches("^[ a-zA-Z\u00C0-\u00ff]{1,100}$")) {
            throw new RuntimeException("Istituto non valido.");
        }
        ScolarescaBean utenteAggiornato = new ScolarescaBean(utenteLoggato.getId(),email,passwordNoHash,istituto,isHash);
        daoScol.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }

    @Override
    public UtenteRegistratoBean updateOrganizzatore(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender, String biografia, String iban) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            //se non viene inserita la password non dve eessere cambiata e si inserisce la vecchia
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }else {
            //altrimenti controllare il formato password inserito
            if(!passwordNoHash.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{6,30}$")){
                throw new RuntimeException("Password non valida.");
            }
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]{1,50}$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Cognome non valido.");
        }
        Date dataAttuale = new Date(Calendar.getInstance().getTimeInMillis());
        if (dataDiNascita.after(dataAttuale) || dataDiNascita.toLocalDate().isEqual(dataAttuale.toLocalDate())) {
            throw new RuntimeException("impostazioni data di nascita inserite non valide");
        }
        if (gender != 0 && gender != 1 && gender != 2) {
            throw new RuntimeException("dati per genere non corretti");
        }
        if (biografia == null || biografia.isEmpty()) {
            throw new RuntimeException("Biografia non valido.");
        }
        if (iban == null || iban.isEmpty() || !iban.matches("^(it|IT)[0-9]{2}[A-Za-z][0-9]{10}[0-9A-Za-z]{12}$")) {
            //regEx per iban italia
            throw new RuntimeException("Iban non valido.");
        }
        OrganizzatoreBean utenteAggiornato = new OrganizzatoreBean(utenteLoggato.getId(), gender, iban, nome, cognome, email, passwordNoHash, biografia,dataDiNascita,isHash);
        daoOrg.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }

    @Override
    public void recuperaPassword(String emailTo) {
        //DA FARE

    }
    public void eliminaProfiloUtente(UtenteRegistratoBean utenteLoggato){
        if(utenteLoggato==null) throw new RuntimeException("operazione non autorizzata");
        daoUtente.doDelete(utenteLoggato.getId());
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
                    }else{
                        daoCarr.doSave(utenteRegistratoBean.getId(),bi);
                    }
                }
        }
        return daoCarr.doRetrieveByIdUtente(utenteRegistratoBean.getId()) ;
    }
    public void applicaScontoScuola(CarrelloBean carrello){
        double scontoDaApplicare;
        double prezzoscontato;
        Collection<BigliettoQuantita> prodotti=carrello.getProdotti();
        for (BigliettoQuantita bi: prodotti) {
            scontoDaApplicare= bi.getPrezzoBigl()*30/100;
            prezzoscontato= bi.getPrezzoBigl()-scontoDaApplicare;
            bi.setPrezzoBigl(prezzoscontato);
        }
    }
}
