package autenticazione.service;

import model.dao.*;
import model.entity.*;

import java.sql.Date;
import java.util.*;

import model.entity.CarrelloBean.BigliettoQuantita;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
    @Override
    public UtenteRegistratoBean loginUtente(String email, String passwordNotHash, String tipoUtente) {
        //if null throw exception per tutti
        if(tipoUtente.compareToIgnoreCase("utente")==0){
            daoU=new UtenteDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareToIgnoreCase("scolaresca")==0){
            daoU=new ScolarescaDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }
        else
        if(tipoUtente.compareToIgnoreCase("organizzatore")==0){
            daoU=new OrganizzatoreDAOImpl();
            return daoU.doRetrieveByEmailPassword(email,passwordNotHash);
        }else
        if(tipoUtente.compareToIgnoreCase("amministratore")==0){
            daoU=new AmministratoreDAOImpl();
            return  daoU.doRetrieveByEmailPassword(email,passwordNotHash);
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

        Collections.reverse(ordini);
        return ordini;
    }


    public UtenteRegistratoBean updateUtente(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+)+$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Cognome non valido.");
        }
        Date dataAttuale = new Date(Calendar.getInstance().getTimeInMillis());
        if (dataDiNascita.after(dataAttuale)) {
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
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+)+$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
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
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+)+$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }
        if (istituto == null || istituto.isEmpty()) {
            throw new RuntimeException("Istituto non valido.");
        }
        ScolarescaBean utenteAggiornato = new ScolarescaBean(utenteLoggato.getId(),email,passwordNoHash,istituto,isHash);
        daoScol.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }

    @Override
    public UtenteRegistratoBean updateOrganizzatore(UtenteRegistratoBean utenteLoggato, String email, String passwordNoHash, String nome, String cognome, Date dataDiNascita, int gender, String biografia, String iban) {
        boolean isHash = false;
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+)+$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash.isEmpty()) {
            isHash = true;
            passwordNoHash = utenteLoggato.getPasswordHash();
        }
        if (nome == null || nome.isEmpty() || !nome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Nome non valido.");
        }

        if (cognome == null || cognome.isEmpty() || !cognome.matches("^[ a-zA-Z\u00C0-\u00ff]+$")) {
            //regEx per stringa senza numeri
            throw new RuntimeException("Cognome non valido.");
        }
        Date dataAttuale = new Date(Calendar.getInstance().getTimeInMillis());
        if (dataDiNascita.after(dataAttuale)) {
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
        UtenteBean utenteAggiornato = new UtenteBean(utenteLoggato.getId(), gender, nome, cognome, email, passwordNoHash, dataDiNascita, isHash);
        daoUtente.doUpdate(utenteAggiornato);
        return utenteAggiornato;
    }

    @Override
    public void recuperaPassword(String emailTo) {
        //no
        //String host = "smtp.gmail.com";
        String host="smtp.office365.com";
        String username = "alessia.dellapepa99";
        String password = "cH1cK.3UBU";
        Properties props = new Properties();
       // props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(props);


        try{
            MimeMessage msg = new MimeMessage(session);
            // set the message content here
            msg.setFrom(new InternetAddress("alessia.dellapepa99@gmail.com"));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(emailTo));
            msg.setSubject("test email");
            msg.setText("Hello CULO this is example of sending email  ");
            //Transport.send(msg, username, password);
            Transport.send(msg);
        }catch (Exception e){
            throw new RuntimeException(e);
        }


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
                    }
                    else
                        daoCarr.doSave(utenteRegistratoBean.getId(),bi);
                }
        }
        return daoCarr.doRetrieveByIdUtente(utenteRegistratoBean.getId()) ;
    }
}
