package registrazione.service;

import autenticazione.service.AutenticazioneServiceImpl;
import model.dao.*;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public class RegistrazioneServiceimpl implements RegistrazioneService{
    private UtenteRegistratoDAO daoUtenteRegistrato;
    private OrganizzatoreDAO daoOrganizzatore;
    private ScolarescaDAO daoScolaresca;

    public RegistrazioneServiceimpl() {
        daoUtenteRegistrato=new UtenteRegistratoDAOImpl();
        daoOrganizzatore= new OrganizzatoreDAOImpl();
        daoScolaresca= new ScolarescaDAOImpl();
    }

    @Override
    public UtenteRegistratoBean registrazioneUtente(String gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita) {
        //controlli sui dati
        UtenteRegistratoBean newUtente=new UtenteRegistratoBean(RegistrazioneServiceimpl.getNumberGender(gender),nome,cognome,email,passwordNoHash,dataDiNascita,false);
        return daoUtenteRegistrato.doSave(newUtente);
    }

    @Override
    public ScolarescaBean registrazioneScolaresca(String email, String passwordNoHash, String istituto) {
        //controlli sui dati
        ScolarescaBean newUtente= new ScolarescaBean(email,passwordNoHash,istituto,false);
        return daoScolaresca.doSave(newUtente);
    }

    @Override
    public OrganizzatoreBean registrazioneOrganizzatore(String gender, String iban, String nome, String cognome, String email, String passwordNoHash, String biografia, String azienda, Date dataDiNascita) {
        OrganizzatoreBean newUtente=new OrganizzatoreBean(RegistrazioneServiceimpl.getNumberGender(gender),iban,nome,cognome,email,passwordNoHash,biografia,azienda,dataDiNascita,false);
        return daoOrganizzatore.doSave(newUtente);
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
