package model.dao;

import model.entity.EventoBean;

import java.util.List;

public interface EventoDAO {

    EventoBean doRetrieveById(int id);
    List<EventoBean> doRetrieveAllEventiAttiviNonScaduti(); //attenzione duplicati per modifica
    List<EventoBean> doRetrieveAllEventiNonAttivi();
    List<EventoBean> doRetrieveAllByTeatroAttiviNonScaduti();
    List<EventoBean> doRetrieveAllByMostraAttiviNonScaduti();
    List<EventoBean> doRetrieveByOrganizzatore(int idOrg);
    List<EventoBean> doRetrieveByNomeOrDescrizione(String against); // per la ricerca
    List<EventoBean> doRetrieveAllRichiesteInserimento();
    List<EventoBean> doRetrieveAllRichiesteModifiche();
    int retrieveEventoFromidEventoModifica(int idEventoTemp);
    boolean doSave (EventoBean evento);

    boolean doSaveRichiestaModificaEv(int idOldEvento, int idEventoModificato, double nuovoPrezzoBiglietto);
    boolean doUpdate(EventoBean evento);
    boolean doUpdateNumBiglietti( int idEvento, int numBiglietti);
    boolean doUpdateAttivazioneEvento(int idEvento,boolean attivo);
    boolean doDelete(int idEvento);

}
