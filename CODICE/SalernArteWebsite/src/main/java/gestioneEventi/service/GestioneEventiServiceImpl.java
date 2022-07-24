package gestioneEventi.service;

import model.dao.BigliettoDAO;
import model.dao.BigliettoDAOImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class GestioneEventiServiceImpl implements GestioneEventiService{
    private EventoDAO daoEvento;
    private BigliettoDAO daoBiglietto;

    public GestioneEventiServiceImpl() {
        daoEvento= new EventoDAOImpl();
        daoBiglietto=new BigliettoDAOImpl();
    }

    public void richiediInserimentoEvento(int idOrganizzatore,String nome, String tipoEvento, String descrizione, String pathContext,Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede){
        try{
            Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
            if(dataFine.before(dataInizio) || dataInizio.before(dataAttuale)){
                throw  new Exception();
            }
            if(nome==null || nome.trim().length()==0 || descrizione==null || descrizione.trim().length()==0 )
                throw new Exception();

            if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new Exception();


            String path = "./immaginiEventi/" + filePhoto.getSubmittedFileName();
            saveImage(filePhoto.getInputStream(),pathContext);

            EventoBean bean= new EventoBean(idOrganizzatore,dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(bean);
            //creo tanti biglietti quanto il num inserito e li carico
            for (int i = 0; i < numBiglietti; i++) {
                daoBiglietto.doSave(bean.getId(),prezzoBiglietto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void attivaEvento(int idEvento, String tipoUtente) {
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception
        }
        daoEvento.doUpdateAttivazioneEvento(idEvento,true);
    }

    public void rimuoviEvento(int idEvento, String tipoUtente){
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception
        }
        //nel acso di rifiuto inseirmento l'evento viene rimosso. nel caso di rifiuta modifica no,
        // si riporta allo stato di prima e si avvisa l'organizzatore
        daoEvento.doDelete(idEvento);
    }

    @Override
    public EventoBean retriveEventoById(int idEvento) {
        EventoBean result= daoEvento.doRetrieveById(idEvento);
        if(result==null){
            throw new RuntimeException(); //myException si è verificato un errore evento non esiste ciao
        }
        return result;
    }

    @Override
    public List<EventoBean> retriveAllRichiesteEventi(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareTo("amministratore")!=0){
            throw  new RuntimeException(); // errore my exception
        }
        //differenzia la modifica e gli altri eventi non attivi. non so ancora come
        return daoEvento.doRetrieveAllEventiNonAttivi();
    }

    private boolean getTypeEvento(String tipoEvento){
        if(tipoEvento.compareTo("teatro")==0)
            return true;
        else
            return false;
    }

    private static void saveImage(InputStream in,String path){
        File file=new File(path);
        try {
            Files.copy(in,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteImage(String path){
        File file=new File(path);
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
