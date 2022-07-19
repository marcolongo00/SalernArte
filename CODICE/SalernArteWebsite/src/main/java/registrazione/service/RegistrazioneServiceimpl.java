package registrazione.service;

import model.dao.*;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;

public class RegistrazioneServiceimpl implements RegistrazioneService{
    private UtenteRegistratoDAO daoU;

    public RegistrazioneServiceimpl() {
    }

    @Override
    public UtenteRegistratoBean registrazioneUtente(String gender, String nome, String cognome, String email, String passwordNoHash, Date dataDiNascita) {
        //controlli sui dati
        daoU=new UtenteDAOImpl();
        UtenteRegistratoBean result= new UtenteBean(RegistrazioneServiceimpl.getNumberGender(gender),nome,cognome,email,passwordNoHash,dataDiNascita,false);
        daoU.doSave(result);

        return result;
    }

    @Override
    public UtenteRegistratoBean registrazioneScolaresca(String email, String passwordNoHash, String istituto) {
        //controlli sui dati
        daoU=new ScolarescaDAOImpl();
        UtenteRegistratoBean result= new ScolarescaBean(email,passwordNoHash,istituto,false);
        daoU.doSave(result);
        return result;
    }

    @Override
    public UtenteRegistratoBean registrazioneOrganizzatore(String gender, String iban, String nome, String cognome, String email, String passwordNoHash, String biografia, String azienda, Date dataDiNascita) {
        //controlli sui dati
        daoU=new OrganizzatoreDAOImpl();
        UtenteRegistratoBean result= new OrganizzatoreBean(RegistrazioneServiceimpl.getNumberGender(gender),iban,nome,cognome,email,passwordNoHash,biografia,dataDiNascita,false);
        daoU.doSave(result);
        return result;
    }

    private static int getNumberGender(String gender){ //serve l'inverso
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
