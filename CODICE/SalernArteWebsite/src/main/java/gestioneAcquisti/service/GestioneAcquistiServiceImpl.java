package gestioneAcquisti.service;

import model.dao.*;
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
    public GestioneAcquistiServiceImpl() {
        daoCarr=new CarrelloDAOImpl();
        eventoDao= new EventoDAOImpl();
        bigliettoDAO= new BigliettoDAOImpl();
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
                    //eventiToRemove.add(idE);
                    carrelloSessione.remove(idE);
                    daoCarr.doDelete(utente.getId(),idE); //so che l'utente è != null per precondizione
                }else
                if(temp.getNumBiglietti()==0 || temp.getNumBiglietti()<bi.getQuantita()){
                    alertCarrello=true;
                }
            }
        }

        /*for(Integer id:eventiToRemove){
            carrelloSessione.remove(id); //rimuove solo quelli scaduti a livello di data
        }*/
        return alertCarrello;
    }

    @Override
    public void svuotaCarrello(CarrelloBean carrello, UtenteRegistratoBean utente) {
        if(utente.getTipoUtente().compareTo("utente")!=0 && utente.getTipoUtente().compareTo("scolaresca")!=0 ) {
            throw new RuntimeException("operazione non autorizzata");
        }else{
            daoCarr.svuotaCarrello(utente.getId());
        }
    }

    @Override
    public void removeEventoFromCarrello(int idE, CarrelloBean carrello, UtenteRegistratoBean utente) {
        // controllo che l'id sia corretto e che nel carrello sia presente il prodotto
        if(idE<= 0 || eventoDao.doRetrieveById(idE)==null || carrello.get(idE)==null)
            throw new RuntimeException("qualcosa è andato storto ripsovare");
        checkAutorizzazioniUtente(utente);
        if(utente!=null)
            daoCarr.doDelete(utente.getId(),idE);
        carrello.remove(idE);
    }

    @Override
    public CarrelloBean aggiungiAlCarrello(int idE, int quantita, CarrelloBean carrelloSessione, UtenteRegistratoBean utente) {
        if(idE<= 0 || quantita<=0)   throw new RuntimeException("formato dati errato");
        EventoBean evento= eventoDao.doRetrieveById(idE);
        Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
        if(evento==null || evento.getDataFine().before(dataAttuale)) throw new RuntimeException("formato dati errato");
        checkAutorizzazioniUtente(utente);

       if(carrelloSessione==null){//può succedere solo se l'utente non è loggato, altirmenti avrei al più un carrello vuoto
           carrelloSessione=new CarrelloBean();
           carrelloSessione.put(evento,quantita, bigliettoDAO.doRetrievePrezzoBigliettoByEvento(idE));
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
               carrelloSessione.put(evento,quantita, bigliettoDAO.doRetrievePrezzoBigliettoByEvento(idE));
               if(utente!=null )  //se l'utente è loggato aggiorno anche il Database
                   daoCarr.doSave(utente.getId(),carrelloSessione.get(idE));
           }
       }
        return carrelloSessione;
    }

    private void checkAutorizzazioniUtente(UtenteRegistratoBean utente){
        if(utente != null && utente.getTipoUtente().compareTo("utente")!=0 && utente.getTipoUtente().compareTo("scolaresca")!=0 ) {
            throw new RuntimeException("operazione non autorizzata");
        }
    }

}
