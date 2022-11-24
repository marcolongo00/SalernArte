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
import model.entity.UtenteRegistratoBean;

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
                throw  new RuntimeException("impostazioni data inserite non valide");
            }
            if(nome==null || nome.trim().length()==0 || descrizione==null || descrizione.trim().length()==0 )
                throw new RuntimeException("formato campi di testo errato");

            if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new RuntimeException("formato numero biglietto e/o prezzo biglietti errato");

            if(indirizzo==null || indirizzo.trim().length()==0 || sede==null || sede.trim().length()==0){
                throw new RuntimeException("formato campi di testo errato");
            }
            String path = "./immaginiEventi/" + filePhoto.getSubmittedFileName();
            saveImage(filePhoto.getInputStream(),pathContext);

            EventoBean bean= new EventoBean(idOrganizzatore,dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(bean);
            creaBiglietti(numBiglietti,bean.getId(),prezzoBiglietto);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void richiediModificaEvento(int idEvDaModificare, UtenteRegistratoBean utenteLoggato, String nome, String tipoEvento, String descrizione, String pathContext, Part filePhoto, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede) {
        //controllo autorizzazione utente operazione
        controllaPermessiOrganizzatore(utenteLoggato);
        //controllo sul formato dei dati
         try{
             if(dataFine.before(dataInizio) ){
                throw  new RuntimeException("impostazioni data inserite non valide");
            }
            if(nome==null || nome.trim().length()==0 || descrizione==null || descrizione.trim().length()==0 )
                throw new RuntimeException("formato campi di testo errato");

            if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new RuntimeException("formato numero biglietto e/o prezzo biglietti errato");

            if(indirizzo==null || indirizzo.trim().length()==0 || sede==null || sede.trim().length()==0){
                throw new RuntimeException("formato campi di testo errato");
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

            //trasforma i tipi di dati che non sono in formato corretto come tipoEvento
            // crea un evento temp con i dati modificati e id ovviamente diverso da quello orginario
            EventoBean newEvento= new EventoBean(utenteLoggato.getId(),dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(newEvento);
            //la creazione dei biglietti avverrà in accetta modifica da parte dell'admin

             //crea un'istanza di richiesta modifica
             daoEvento.doSaveRichiestaModificaEv(eventoDaModificare.getId(),newEvento.getId(),prezzoBiglietto);

        }catch (Exception e){
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
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception
        }
        daoEvento.doUpdateAttivazioneEvento(idEvento,true);
    }

    public void rimuoviEvento(int idEvento, UtenteRegistratoBean utente){
        if(utente.getTipoUtente().compareToIgnoreCase("amministratore")!=0 && utente.getTipoUtente().compareToIgnoreCase("organizzatore")!=0){ //non so se comprende tutti i casi
            throw new RuntimeException("operazione non autorizzata"); //myexception
        }
        //nel acso di rifiuto inseirmento l'evento viene rimosso. nel caso di rifiuta modifica no,
        // si riporta allo stato di prima e si avvisa l'organizzatore
        //metodo chiamato anche se l'ìorganizzatore elimina l'evento
        daoEvento.doDelete(idEvento);
    }

    @Override
    public void accettaModifica(int idEvento, String tipoUtente) { //anche stessa  funzione con flag per accetta rifiuta
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception
        }
        //accetto al modifica poi trovo e cancello l'evento con i dati vecchi e in cascade si cancellerà la richiesta
        int idEventoPreModifica=daoEvento.retieveEventoFromidEventoModifica(idEvento);
        daoEvento.doUpdateAttivazioneEvento(idEvento,true);
        EventoBean eventoPre=daoEvento.doRetrieveById(idEventoPreModifica);
        EventoBean eventoPost=daoEvento.doRetrieveById(idEvento);

        double nuovoPrezzoBiglietto=daoBiglietto.doRetrievePrezzoBiglByRichiestaModifica(eventoPost.getId());
        daoBiglietto.updatePrezzoBigliettoEvento(eventoPost.getId(),nuovoPrezzoBiglietto);

        daoBiglietto.doUpdateBigliettiModificaEvento(eventoPre,eventoPost);
        daoEvento.doDelete(idEventoPreModifica);
        //attenzione al pathfoto se è diversp. da eliminare quello vecchio
    }

    @Override
    public void rifiutaModifica(int idEvento, String tipoUtente) {
        if(tipoUtente.compareTo("amministratore")!=0){
            throw new RuntimeException(); //myexception
        }
        //rifiuto la modifica quindi elimino temp e riporto evento originale ad attivo
        int idEventoPreModifica=daoEvento.retieveEventoFromidEventoModifica(idEvento);
        daoEvento.doUpdateAttivazioneEvento(idEventoPreModifica,true);
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
        return daoEvento.doRetrieveAllEventiNonAttivi(); // non va bene perchè ci sono i duplicati di quelli solo modifiche
    }

    @Override
    public List<EventoBean> retrieveEventiOrganizzatore(UtenteRegistratoBean utente){
        if(utente==null || utente.getTipoUtente().compareToIgnoreCase("Organizzatore")!=0){ //funzione a prte controllaPermessi(UtenteReg utente, String permesso);
            throw new RuntimeException("operazione non autorizzata"); //my exception
        }
        return daoEvento.doRetrieveByOrganizzatore(utente.getId());
    }

    @Override
    public List<EventoBean> retrieveRichiesteInserimento(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareTo("amministratore")!=0){
            throw  new RuntimeException(); // errore my exception
        }
        return daoEvento.doRetrieveAllRichiesteInserimento();
    }

    @Override
    public List<EventoBean> retrieveRichiesteModifica(String tipoUtente) {
        if(tipoUtente==null || tipoUtente.compareTo("amministratore")!=0){
            throw  new RuntimeException(); // errore my exception
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
