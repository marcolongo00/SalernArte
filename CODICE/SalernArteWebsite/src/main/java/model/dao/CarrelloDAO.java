package model.dao;

import model.entity.CarrelloBean;
import model.entity.CarrelloBean.BigliettoQuantita;

public interface CarrelloDAO {
    CarrelloBean doRetrieveByIdUtente(int idUtente);
    void doSave(int idUtente, BigliettoQuantita carr);
    void doDelete(int idUtente, int idEvento);
    void svuotaCarrello(int idUtente);
    void doUpdateQuantita(int idUtente, BigliettoQuantita carr);
    void doUpdateQuantitaFromCarrelliAfterAcquisto(int idEvento,int bigliettiRimasti);

}
