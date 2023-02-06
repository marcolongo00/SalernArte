package gestioneEventi.service;

import model.dao.*;
import model.entity.CarrelloBean;
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
import model.entity.CarrelloBean.BigliettoQuantita;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

public class GestioneEventiServiceImpl implements GestioneEventiService{
    private EventoDAO daoEvento;
    private BigliettoDAO daoBiglietto;
    public GestioneEventiServiceImpl() {
        daoEvento= new EventoDAOImpl();
        daoBiglietto=new BigliettoDAOImpl();
    }
    public GestioneEventiServiceImpl(EventoDAO daoEv,BigliettoDAO daoBigl){
        daoEvento=daoEv;
        daoBiglietto=daoBigl;
    }

    public boolean richiediInserimentoEvento(int idOrganizzatore,String nome, String tipoEvento, String descrizione, String pathContext,Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede){
        try{
            Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
            if( dataFine.before(dataInizio) || dataFine.getTime()==dataInizio.getTime() || dataInizio.before(dataAttuale) || dataInizio.getTime()==dataAttuale.getTime()){
                throw  new RuntimeException("impostazioni data inserite non valide");
            }
            if(nome==null || !nome.matches("^[0-9°A-zÀ-ù ‘-]{1,50}$"))
                throw new RuntimeException("formato nome evento errato");

            if(descrizione==null || !descrizione.matches("^[0-9°A-zÀ-ù ‘-]{1,320}$"))
                throw new RuntimeException("formato descrizione evento errato");
            if(tipoEvento.compareToIgnoreCase("mostra")!=0 && tipoEvento.compareToIgnoreCase("teatro")!=0){
                throw new RuntimeException("formato tipo evento errato");
            }
            if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new RuntimeException("formato numero biglietto e/o prezzo biglietti errato");
            if(sede==null || !sede.matches("^[0-9°A-zÀ-ù ‘-]{1,100}$")){
                throw new RuntimeException("formato sede evento errato");
            }
            if(indirizzo==null || !indirizzo.matches("^[0-9°A-zÀ-ù ‘-]{2,30}$")){
                throw new RuntimeException("formato indirizzo errato");
            }
            String path = "./immaginiEventi/" + filePhoto.getSubmittedFileName();
            saveImage(filePhoto.getInputStream(),pathContext);

            EventoBean bean= new EventoBean(idOrganizzatore,dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(bean);
            creaBiglietti(numBiglietti,bean.getId(),prezzoBiglietto);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean richiediModificaEvento(int idEvDaModificare, UtenteRegistratoBean utenteLoggato, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede) {
        //controllo autorizzazione utente operazione
        controllaPermessiOrganizzatore(utenteLoggato);
        //controllo sul formato dei dati
         try{
             if(dataFine.before(dataInizio) || dataFine.getTime()==dataInizio.getTime() ){
                throw  new RuntimeException("impostazioni data inserite non valide");
             }
             if(nome==null || !nome.matches("^[0-9°A-zÀ-ù ‘-]{1,50}$"))
                 throw new RuntimeException("formato nome evento errato");

             if(descrizione==null || !descrizione.matches("^[0-9°A-zÀ-ù ‘-]{1,320}$"))
                 throw new RuntimeException("formato descrizione evento errato");

             if(tipoEvento.compareToIgnoreCase("mostra")!=0 && tipoEvento.compareToIgnoreCase("teatro")!=0){
                 throw new RuntimeException("formato tipo evento errato");
             }
             if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new RuntimeException("formato numero biglietto e/o prezzo biglietti errato");

             if(sede==null || !sede.matches("^[0-9°A-zÀ-ù ‘-]{1,100}$")){
                 throw new RuntimeException("formato sede evento errato");
             }
             if(indirizzo==null || !indirizzo.matches("^[0-9°A-zÀ-ù ‘-]{2,30}$")){
                 throw new RuntimeException("formato indirizzo errato");
             }
            //retrieve evento da modificare per fare alcuni controlli
            EventoBean eventoDaModificare= daoEvento.doRetrieveById(idEvDaModificare);
            //controlla se il path foto modifica esiste altrimenti prendi quello vecchio
            String path="";
            if(pathContext.isEmpty()){
                path=eventoDaModificare.getPath();
            }else{
                //se il path foto modifica esiste fai save photo
                path = "./immaginiEventi/" + filePhoto.getSubmittedFileName();
                saveImage(filePhoto.getInputStream(),pathContext);
            }

             // crea un evento temp con i dati modificati e id ovviamente diverso da quello orginario
            EventoBean newEvento= new EventoBean(utenteLoggato.getId(),dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(newEvento);
            //la creazione dei biglietti avverrà in accetta modifica da parte dell'admin
             // crea un'istanza di richiesta modifica
             daoEvento.doSaveRichiestaModificaEv(eventoDaModificare.getId(),newEvento.getId(),prezzoBiglietto);
            return true;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void creaBiglietti(int numBiglietti,int idEvento, double prezzoBiglietto){
        //creo tanti biglietti quanto il num inserito e li carico
        for (int i = 0; i < numBiglietti; i++) {
            daoBiglietto.doSave(idEvento,prezzoBiglietto);
        }
    }

    @Override
    public void attivaEvento(int idEvento, String tipoUtente) {
        if(tipoUtente.compareToIgnoreCase("amministratore")!=0){
            throw new RuntimeException("operazione non autorizzata"); //myexception
        }
        daoEvento.doUpdateAttivazioneEvento(idEvento,true);
    }

    public void rimuoviEvento(int idEvento, UtenteRegistratoBean utente){
        if(utente.getTipoUtente().compareToIgnoreCase("amministratore")!=0 && utente.getTipoUtente().compareToIgnoreCase("organizzatore")!=0){
            throw new RuntimeException("operazione non autorizzata"); //myexception
        }
        //nel acso di rifiuto inseirmento l'evento viene rimosso. nel caso di rifiuta modifica no,
        // si riporta allo stato di prima e si avvisa l'organizzatore
        //metodo chiamato anche se l'ìorganizzatore elimina l'evento
        EventoBean evDaEliminare= daoEvento.doRetrieveById(idEvento);
        deleteImage(evDaEliminare.getPath());
        daoEvento.doDelete(idEvento);
    }

    @Override
    public void accettaModifica(int idEvento, String tipoUtente) {
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException("operazione non autorizzata"); //myexception
        }
        //accetto al modifica poi trovo e cancello l'evento con i dati vecchi e in cascade si cancellerà la richiesta
        int idEventoPreModifica=daoEvento.retrieveEventoFromidEventoModifica(idEvento);
        daoEvento.doUpdateAttivazioneEvento(idEvento,true);
        EventoBean eventoPre=daoEvento.doRetrieveById(idEventoPreModifica);
        EventoBean eventoPost=daoEvento.doRetrieveById(idEvento);

        double nuovoPrezzoBiglietto=daoBiglietto.doRetrievePrezzoBiglByRichiestaModifica(eventoPost.getId());
        daoBiglietto.updatePrezzoBigliettoEvento(eventoPost.getId(),nuovoPrezzoBiglietto);

        daoBiglietto.doUpdateBigliettiModificaEvento(eventoPre,eventoPost);
        //attenzione al pathfoto se è diversp. da eliminare quello vecchio
        if(eventoPre.getPath().compareTo(eventoPost.getPath())!=0){
            deleteImage(eventoPre.getPath());
        }
        daoEvento.doDelete(idEventoPreModifica);

    }

    @Override
    public void rifiutaModifica(int idEvento, String tipoUtente) {
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException("operazione non autorizzata"); //myexception
        }
        //rifiuto la modifica quindi elimino temp e riporto evento originale ad attivo
        int idEventoPreModifica=daoEvento.retrieveEventoFromidEventoModifica(idEvento);
        daoEvento.doUpdateAttivazioneEvento(idEventoPreModifica,true);
        //se i path non coincidono vuol dire che è stata
        // salvata anche la foto dell'evento post modifica e va eliminata
        EventoBean eventoPre=daoEvento.doRetrieveById(idEventoPreModifica);
        EventoBean eventoPost=daoEvento.doRetrieveById(idEvento);
        if(eventoPre.getPath().compareTo(eventoPost.getPath())!=0){
            deleteImage(eventoPost.getPath());
        }
        daoEvento.doDelete(idEvento);
    }

    @Override
    public EventoBean retrieveEventoById(int idEvento) {
        EventoBean result= daoEvento.doRetrieveById(idEvento);
        if(result==null){
            throw new RuntimeException("Si è verificato un errore, l'evento selezionato non esiste ");
        }
        return result;
    }

    @Override
    public OrganizzatoreBean retriveBioOrganizzatore(int idOrg) {
        OrganizzatoreDAOImpl daoOrg=new OrganizzatoreDAOImpl();
        return daoOrg.doRetrieveById(idOrg);
    }

    @Override
    public List<EventoBean> retrieveEventiOrganizzatore(UtenteRegistratoBean utente){
        controllaPermessiOrganizzatore(utente);
        return daoEvento.doRetrieveByOrganizzatore(utente.getId());
    }

    @Override
    public List<EventoBean> retrieveRichiesteInserimento(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareToIgnoreCase("amministratore")!=0){
            throw  new RuntimeException("operazione non autorizzata"); // errore my exception
        }
        return daoEvento.doRetrieveAllRichiesteInserimento();
    }

    @Override
    public List<EventoBean> retrieveRichiesteModifica(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareToIgnoreCase("amministratore")!=0){
            throw  new RuntimeException("operazione non autorizzata"); // errore my exception
        }
        return daoEvento.doRetrieveAllRichiesteModifiche();
    }



    @Override
    public void checkQuantitaCarrello(EventoBean evento, CarrelloBean carrelloSessione) {
        if (carrelloSessione != null) {
            BigliettoQuantita biQta=carrelloSessione.get(evento.getId());
            if (biQta != null) {
                int qtaCarr= evento.getNumBiglietti()-biQta.getQuantita();
                    evento.setNumBiglietti(qtaCarr);
            }
        }
    }

    @Override
    public boolean checkScaduta(EventoBean evento) {
        Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
        if(evento.getDataFine().before(dataAttuale)){
            return true;
        }
        return  false;
    }

    @Override
    public double getPrezzoEvento(int idEvento) {
        List<EventoBean> eventiModifica = daoEvento.doRetrieveAllRichiesteModifiche();
        for (EventoBean e : eventiModifica) {
            if (e.getId() == idEvento) return daoBiglietto.doRetrievePrezzoBiglByRichiestaModifica(idEvento);
        }
        return daoBiglietto.doRetrievePrezzoBigliettoByEvento(idEvento);
    }
    public List<EventoBean> ricercaEventiByNomeOrDescrizione(String query){
        if(!query.matches("^[0-9°A-zÀ-ù ‘-]*\\*$")){
            throw new RuntimeException("errore formato ricerca");
        }
        return daoEvento.doRetrieveByNomeOrDescrizione(query);
    }
    public List<EventoBean> retrieveEventiByTipo(String tipoEvento){
        if(tipoEvento.compareToIgnoreCase("teatro")==0){
            return daoEvento.doRetrieveAllByTeatroAttiviNonScaduti();
        }else if(tipoEvento.compareToIgnoreCase("mostra")==0){
            return  daoEvento.doRetrieveAllByMostraAttiviNonScaduti();
        }else{
            throw new RuntimeException("operazione non riconoscita");
        }
    }
    public List<EventoBean> retrieveTuttiEventiNonScadutiAttivi(){
       return  daoEvento.doRetrieveAllEventiAttiviNonScaduti();
    }

    private boolean getTypeEvento(String tipoEvento){
        switch (tipoEvento){
            case "teatro": return true;
            case "mostra": return false;
            default: throw new RuntimeException("formato tipo evento errato");
        }
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

    private void controllaPermessiOrganizzatore(UtenteRegistratoBean utenteLoggato){
        if(utenteLoggato==null || utenteLoggato.getTipoUtente().compareToIgnoreCase("organizzatore")!=0){
            throw new RuntimeException("operazione non autorizzata");
        }
    }
    private void controllaPermessiAmministratore(UtenteRegistratoBean utenteLoggato){
        if(utenteLoggato==null || utenteLoggato.getTipoUtente().compareToIgnoreCase("amministratore")!=0){
            throw new RuntimeException("operazione non autorizzata");
        }
    }
}
