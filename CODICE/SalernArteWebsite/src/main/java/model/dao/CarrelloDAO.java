package model.dao;

import model.entity.CarrelloBean;
import model.entity.CarrelloBean.BigliettoQuantita;

public interface CarrelloDAO {
    CarrelloBean doRetrieveByIdUtente(int idUtente);
    boolean doSave(int idUtente, BigliettoQuantita carr);
    boolean doDelete(int idUtente, int idEvento);
    boolean svuotaCarrello(int idUtente);
    boolean doUpdateQuantita(int idUtente, BigliettoQuantita carr);
    void doUpdateQuantitaFromCarrelliAfterAcquisto(int idEvento,int bigliettiRimasti);

}
