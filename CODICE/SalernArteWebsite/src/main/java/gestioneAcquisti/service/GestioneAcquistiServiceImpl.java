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
    public boolean retrieveCarrelloAggiornato(UtenteRegistratoBean utente, CarrelloBean carrelloSessione) {
        if (carrelloSessione == null && utente==null) { //devo fare return carrello e gestire diversamente l'alert
            carrelloSessione = new CarrelloBean(); //asp gestione carrello se non c'è utente con l'id??
        }else{ //manca if utente esiste o non
            carrelloSessione=daoCarr.doRetrieveByIdUtente(utente.getId());
        }
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
                        eventiToRemove.add(idE);
                        if(utente!=null) //rimuovi anche dal carrello nel db
                            daoCarr.doDelete(utente.getId(),idE);
                    }else
                    if(temp.getNumBiglietti()==0 || temp.getNumBiglietti()<bi.getQuantita()){
                        alertCarrello=true;
                    }

                }
            }


        for(Integer id:eventiToRemove){
            carrelloSessione.remove(id); //rimuove solo quelli scaduti a livello di data
        }
        return alertCarrello;
    }


}
