package registrazione.service;

import model.dao.*;
import model.entity.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;

public class RegistrazioneServiceimpl implements RegistrazioneService{
    private UtenteRegistratoDAO daoU;

    public RegistrazioneServiceimpl() {
    }

    @Override
    public UtenteRegistratoBean registrazioneUtente(int gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita) {
        //controlli sui dati
        daoU=new UtenteDAOImpl();
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash==null || !passwordNoHash.matches("^(?=.*[az])(?=.*[AZ])(?=.*\\d)[a-zA-Z\\d]{6,30}$" )) {
            throw new RuntimeException("Password non valida.");
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
        if (dataDiNascita.after(dataAttuale)) {
            throw new RuntimeException("impostazioni data di nascita inserite non valide");
        }
        if (gender != 0 && gender != 1 && gender != 2) {
            throw new RuntimeException("dati per genere non corretti");
        }
        UtenteRegistratoBean result= new UtenteBean(gender,nome,cognome,email,passwordNoHash,dataDiNascita,false);
        daoU.doSave(result);

        return result;
    }

    @Override
    public UtenteRegistratoBean registrazioneScolaresca(String email, String passwordNoHash, String istituto) {
        daoU=new ScolarescaDAOImpl();
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash==null || !passwordNoHash.matches("^(?=.*[az])(?=.*[AZ])(?=.*\\d)[a-zA-Z\\d]{6,30}$" )) {
            throw new RuntimeException("Password non valida.");
        }
        if (istituto == null || istituto.isEmpty()  || !istituto.matches("^[ a-zA-Z\u00C0-\u00ff]{1,100}$")) {
            throw new RuntimeException("Istituto non valido.");
        }
        UtenteRegistratoBean result= new ScolarescaBean(email,passwordNoHash,istituto,false);
        daoU.doSave(result);
        return result;
    }

    @Override
    public UtenteRegistratoBean registrazioneOrganizzatore(int gender, String iban, String nome, String cognome, String email, String passwordNoHash, String biografia, String azienda, Date dataDiNascita) {
        daoU=new OrganizzatoreDAOImpl();
        if (email == null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+){1,100}$")) {
            throw new RuntimeException("Email non valida.");
        }
        if (passwordNoHash==null || !passwordNoHash.matches("^(?=.*[az])(?=.*[AZ])(?=.*\\d)[a-zA-Z\\d]{6,30}$" )) {
            throw new RuntimeException("Password non valida.");
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
        if (dataDiNascita.after(dataAttuale)) {
            throw new RuntimeException("impostazioni data di nascita inserite non valide");
        }
        if (gender != 0 && gender != 1 && gender != 2) {
            throw new RuntimeException("dati per genere non corretti");
        }
        if (biografia == null || biografia.isEmpty()) {
            //batsa che abbia almeno un carattere, stringa not empty
            throw new RuntimeException("Biografia non valido.");
        }
        if (iban == null || iban.isEmpty() || !iban.matches("^(it|IT)[0-9]{2}[A-Za-z][0-9]{10}[0-9A-Za-z]{12}$")) {
            //regEx per iban italia
            throw new RuntimeException("Iban non valido.");
        }
        UtenteRegistratoBean result= new OrganizzatoreBean(gender,iban,nome,cognome,email,passwordNoHash,biografia,dataDiNascita,false);
        daoU.doSave(result);
        return result;
    }
    public void salvaCarrelloSessione(UtenteRegistratoBean utenteRegistrato, CarrelloBean carrelloSessione){
        CarrelloDAO daoCarr= new CarrelloDAOImpl();
        if(carrelloSessione!=null && !carrelloSessione.getProdotti().isEmpty()){
            //CarrelloBean saved=daoCarr.doRetrieveByIdUtente(utenteRegistratoBean.getId());
            Collection<CarrelloBean.BigliettoQuantita> prodotti=carrelloSessione.getProdotti();
            for (CarrelloBean.BigliettoQuantita bi: prodotti) {
                daoCarr.doSave(utenteRegistrato.getId(),bi);
            }
        }
    }
    public void applicaScontoScuola(CarrelloBean carrello){
        double scontoDaApplicare;
        double prezzoscontato;
        Collection<CarrelloBean.BigliettoQuantita> prodotti=carrello.getProdotti();
        for (CarrelloBean.BigliettoQuantita bi: prodotti) {
            scontoDaApplicare= bi.getPrezzoBigl()*30/100;
            prezzoscontato= bi.getPrezzoBigl()-scontoDaApplicare;
            bi.setPrezzoBigl(prezzoscontato);
        }
    }

}
