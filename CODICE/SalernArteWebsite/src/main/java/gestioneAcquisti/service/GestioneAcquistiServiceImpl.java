package gestioneAcquisti.service;

import model.dao.*;
import model.entity.AcquistoBean;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.UtenteRegistratoBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import model.entity.CarrelloBean.BigliettoQuantita;

public class GestioneAcquistiServiceImpl implements GestioneAcquistiService{
    private CarrelloDAO daoCarr;
    private EventoDAO eventoDao;
    private BigliettoDAO bigliettoDAO;
    private AcquistoDAO acquistoDAO;
    public GestioneAcquistiServiceImpl() {
        daoCarr=new CarrelloDAOImpl();
        eventoDao= new EventoDAOImpl();
        bigliettoDAO= new BigliettoDAOImpl();
        acquistoDAO= new AcquistoDAOImpl();
    }

    public GestioneAcquistiServiceImpl(CarrelloDAO carrelloDAO, EventoDAO eventoDao, BigliettoDAO bigliettoDAO){
        this.daoCarr = carrelloDAO;
        this.eventoDao= eventoDao;
        this.bigliettoDAO= bigliettoDAO;
        acquistoDAO= new AcquistoDAOImpl();
    }

    @Override
    public CarrelloBean retrieveCarrelloUtente(UtenteRegistratoBean utente) { //forse non serve questa funzione
        checkAutorizzazioniUtente(utente);
        return daoCarr.doRetrieveByIdUtente(utente.getId());
    }

    @Override
    public boolean controlloElementiCarrello(CarrelloBean carrelloSessione,UtenteRegistratoBean utente) {

        boolean alertCarrello=false; //serve per allertare della presenza di prodotti non più disponibili nel
        // carrello senza automaticamente toglierli dal carrello

        List<Integer> eventiToRemove= new ArrayList<>(); //eventi passati
        Collection<BigliettoQuantita> prodotti= carrelloSessione.getProdotti();
        if(!prodotti.isEmpty()){
            Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
            for (BigliettoQuantita bi: prodotti) {
                int idE=bi.getProdotto().getId();
                EventoBean temp=eventoDao.doRetrieveById(idE);
                if(temp==null || temp.getDataFine().before(dataAttuale)){ //evento non esiste più o è scaduto
                    //eventiToRemove.add(idE); viene direttamente rimosso qui, testare
                    carrelloSessione.remove(idE);
                    daoCarr.doDelete(utente.getId(),idE); //so che l'utente è != null per precondizione
                }else
                if(temp.getNumBiglietti()==0 || temp.getNumBiglietti()<bi.getQuantita()){
                    alertCarrello=true;
                }
            }
        }
        //ok ma che succcede se l'evento non è attivo ma era già stato inserito in un carrello??? magari anche a livello di operazioni in cui si va a rendere non attivo un evento va cancellato anche nei carrelli utenti o bo
        /*for(Integer id:eventiToRemove){
            carrelloSessione.remove(id); //rimuove solo quelli scaduti a livello di data
        }*/
        return(alertCarrello || controlloEventiNonAttivi(carrelloSessione));
    }
    public boolean controlloEventiNonAttivi(CarrelloBean carrello){
       Collection<BigliettoQuantita> prodotti= carrello.getProdotti();
        for (BigliettoQuantita bi: prodotti) {
            if(!bi.getProdotto().isAttivo()){
                return true;
            }
        }
        return false;
    }
    @Override
    public void svuotaCarrello(CarrelloBean carrello, UtenteRegistratoBean utente) {
        checkAutorizzazioniUtente(utente);
        daoCarr.svuotaCarrello(utente.getId());
    }

    @Override
    public void removeEventoFromCarrello(int idE, CarrelloBean carrello, UtenteRegistratoBean utente) {
        // controllo che l'id sia corretto e che nel carrello sia presente il prodotto
        if(idE<= 0 || eventoDao.doRetrieveById(idE)==null || carrello.get(idE)==null)
            throw new RuntimeException("qualcosa è andato storto riprovare");
        checkAutorizzazioniUtente(utente);
        if(utente!=null)
            daoCarr.doDelete(utente.getId(),idE);
        carrello.remove(idE);
    }

    @Override
    public CarrelloBean aggiungiAlCarrello(int idE, int quantita, CarrelloBean carrelloSessione, UtenteRegistratoBean utente) {
        if(idE<= 0 || quantita<=0)
            throw new RuntimeException("formato dati errato");
        EventoBean evento= eventoDao.doRetrieveById(idE);
        Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
        //or non attivo)
        if(evento==null || evento.getDataFine().before(dataAttuale)) throw new RuntimeException("formato dati errato");
        checkAutorizzazioniUtente(utente);
        double prezzoBiglietto=bigliettoDAO.doRetrievePrezzoBigliettoByEvento(idE);
        if(utente!=null && utente.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
            double scontoDaApplicare= prezzoBiglietto*30/100;
            prezzoBiglietto-= scontoDaApplicare;
        }

       if(carrelloSessione==null){//può succedere solo se l'utente non è loggato, altirmenti avrei al più un carrello vuoto
           carrelloSessione=new CarrelloBean();
           carrelloSessione.put(evento,quantita, prezzoBiglietto);
       }else{ //vale l'ipotesi che il carrello in sessione sarà sempre aggiornato al momento dell'inserimento
           BigliettoQuantita biQta=carrelloSessione.get(idE);//recupera bigliettoQuantità di quella mostra
           if (biQta != null) { //se è presente aggiorna quantità
               int qtaTot=biQta.getQuantita()+quantita;
               if(qtaTot>evento.getNumBiglietti()){ //se si cerca di aggiungere più biglietti di quelli disponibili
                   throw new NumberFormatException();
               }
               biQta.setQuantita(qtaTot);
               if(utente!= null) //se l'utente è loggato aggiorno anche il Database
                   daoCarr.doUpdateQuantita(utente.getId(),biQta);

           } else { //se non sono mai stati aggiunti biglietti per quella mostra
               if(quantita>evento.getNumBiglietti()) throw new NumberFormatException();//se si cerca di aggiungere più biglietti di quelli disponibili
               carrelloSessione.put(evento,quantita, prezzoBiglietto);
               if(utente!=null )  //se l'utente è loggato aggiorno anche il Database
                   daoCarr.doSave(utente.getId(),carrelloSessione.get(idE));
           }
       }
        return carrelloSessione;
    }

    @Override
    public boolean updateQuantitaCarrello(int idE, int quantita, CarrelloBean carrelloSessione, UtenteRegistratoBean utente) {
        EventoBean evento= eventoDao.doRetrieveById(idE);
        if(evento==null) throw new RuntimeException("errore aggiornamento carrello");
        BigliettoQuantita biQta=carrelloSessione.get(idE);
        if(biQta==null ) throw new RuntimeException("errore aggiornamento carrello");
        if(quantita > evento.getNumBiglietti() || quantita<=0 ) throw new NumberFormatException("quantità non valida");
        biQta.setQuantita(quantita);
        if(utente!= null)
        {
            daoCarr.doUpdateQuantita(utente.getId(),biQta);
            return true;
        }
        else
            return false;
    }

    @Override
    public void acquistaProdotti(CarrelloBean carrelloSessione, UtenteRegistratoBean utente) {
        //controlla se i prodotti esistono ancora al momento dell'acquisto o se sono scaduti
        //fai con carrello nella sessione così puoi controllare se al momento
        // dell'acquisto ci sono prodotti già venduti

        if(utente==null || carrelloSessione==null || carrelloSessione.getProdotti().isEmpty() || controlloElementiCarrello(carrelloSessione,utente)){
            throw new RuntimeException("operazione non autorizzata");
        }else{
            checkAutorizzazioniUtente(utente);
        }

        //prendo la stringa dei prodotti e gli altri dati da salvare nell'acquisto
        String prodotti="";
        double prezzoTotale= carrelloSessione.getPrezzoTot();
        Date dataAttuale = new Date(Calendar.getInstance().getTimeInMillis());
        AcquistoBean acquisto=new AcquistoBean(utente.getId(),dataAttuale,prezzoTotale);
        acquistoDAO.doSave(acquisto);
        //la stringa con i prodotti verrà settata nel database successivamente
        for(BigliettoQuantita x: carrelloSessione.getProdotti()){
            EventoBean evento=x.getProdotto();
            int quantita=x.getQuantita();
            double prezzo= x.getPrezzoBigl(); //forse no
            //controllo che l'evento salvato in database sia disponibile
            EventoBean savedE=eventoDao.doRetrieveById(evento.getId());
            if(savedE==null || !savedE.isAttivo() || quantita==0 || savedE.getNumBiglietti()< quantita || savedE.getDataFine().before(dataAttuale)){
                //se c'è stato un errore cancello l'acquisto creato e lancio eccezione
                acquistoDAO.doDelete(acquisto.getNumOrdine());
                throw new RuntimeException("errore acquisto, ricaricare la pagina");
            }

            bigliettoDAO.sellBiglietto(evento.getId(),quantita,acquisto.getNumOrdine());
            //aggiorno anche num biglietti disponibili
            int bigliettiRimasti= evento.getNumBiglietti()-quantita;
            eventoDao.doUpdateNumBiglietti(evento.getId(),bigliettiRimasti);

            //metodo dao controllo biglietti carrelli e aggiorna quantità
            daoCarr.doUpdateQuantitaFromCarrelliAfterAcquisto(evento.getId(), bigliettiRimasti);
            prodotti+=evento.getNome()+"(qta:"+quantita+"), ";
        }

        //tolgo la virgola per l'ultimo prodotto
        int index=prodotti.lastIndexOf(",");
        prodotti=prodotti.substring(0,index)+";";

        acquistoDAO.setProdotti(acquisto.getNumOrdine(),prodotti);
        daoCarr.svuotaCarrello(utente.getId());

    }

    private void checkAutorizzazioniUtente(UtenteRegistratoBean utente){
        if(utente != null && utente.getTipoUtente().compareToIgnoreCase("utente")!=0 && utente.getTipoUtente().compareToIgnoreCase("scolaresca")!=0 ) {
            throw new RuntimeException("operazione non autorizzata");
        }
    }

}
