package gestioneAcquisti.service;

import model.dao.CarrelloDAO;
import model.dao.CarrelloDAOImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
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
    public GestioneAcquistiServiceImpl() {
        daoCarr=new CarrelloDAOImpl();
        eventoDao= new EventoDAOImpl();
    }


    @Override
    public CarrelloBean retrieveCarrelloUtente(UtenteRegistratoBean utente) { //forse non serve questa funzione
        if(utente.getTipoUtente().compareTo("utente")!=0 && utente.getTipoUtente().compareTo("scolaresca")!=0 ) {
            throw new RuntimeException("operazione non autorizzata");
        }else{
            return daoCarr.doRetrieveByIdUtente(utente.getId());
        }
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

       if(utente != null && utente.getTipoUtente().compareTo("utente")!=0 && utente.getTipoUtente().compareTo("scolaresca")!=0 ) {
            throw new RuntimeException("operazione non autorizzata");
        }else{
            daoCarr.doDelete(utente.getId(),idE);
        }
        carrello.remove(idE);
    }


}
