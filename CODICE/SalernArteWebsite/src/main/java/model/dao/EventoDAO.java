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

    EventoBean doRetrieveById(int id);
    List<EventoBean> doRetrieveAllEventiAttiviNonScaduti(); //attenzione duplicati per modifica
    List<EventoBean> doRetrieveAllEventiNonAttivi();
    List<EventoBean> doRetrieveAllByTeatroAttiviNonScaduti();
    List<EventoBean> doRetrieveAllByMostraAttiviNonScaduti();
    List<EventoBean> doRetrieveByOrganizzatore(int idOrg);
    List<EventoBean> doRetrieveByNomeOrDescrizione(String against); // per la ricerca
    List<EventoBean> doRetrieveAllRichiesteInserimento();
    List<EventoBean> doRetrieveAllRichiesteModifiche();
    int retieveEventoFromidEventoModifica(int idEventoTemp);

   // public List<EventoBean> doRetrieveAllRichiesteEventi();
   // potrebbe funzionare come doRetrieveAllRichieste e per ogni next della query fare doRetrieveById di evento
    void doSave (EventoBean evento);

    void doSaveRichiestaModificaEv(int idOldEvento, int idEventoModificato, double nuovoPrezzoBiglietto);
    void doUpdate(EventoBean evento);
    void doUpdateNumBiglietti( int idEvento, int numBiglietti);
    void doUpdateAttivazioneEvento(int idEvento,boolean attivo);
    void doDelete(int idEvento);

}
