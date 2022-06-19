package model.dao;

import model.entity.EventoBean;

import java.util.List;

public interface EventoDAO {
    /*crea dao e bean per la richiesta evento
    * il dao di richiesta evento farà al retrieve richieste
    * quando viene creato un evento si geenra una richiesta
    * dodelete quando l'admin finalizza la richiesta
    * quando l'admin accetta un evento si farà doupdatevento di eventodao per settare a true l'evento
    * */

    public EventoBean doRetrieveById(int id);
    public List<EventoBean> doRetrieveAllEventiAttiviNonScaduti();
    public List<EventoBean> doRetrieveAllByTeatroAttiviNonScaduti();
    public List<EventoBean> doRetrieveAllByMostraAttiviNonScaduti();
    public List<EventoBean> doRetrieveByOrganizzatore(int idOrg);
    public List<EventoBean> doRetrieveByNomeOrDescrizione(String against); // per la ricerca

   // public List<EventoBean> doRetrieveAllRichiesteEventi();
   // potrebbe funzionare come doRetrieveAllRichieste e per ogni next della query fare doRetrieveById di evento
    public void doSave (EventoBean evento);
    public void doUpdate(EventoBean evento);
    public void doUpdateNumBiglietti( int idEvento, int numBiglietti);
    public void doDelete(int idEvento);

}
