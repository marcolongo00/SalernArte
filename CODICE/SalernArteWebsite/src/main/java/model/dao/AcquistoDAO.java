package model.dao;

import model.entity.AcquistoBean;

import java.util.List;

public interface AcquistoDAO {
List<AcquistoBean> doRetrieveListaAcquistiByIdUtente(int idUtente);
AcquistoBean doRetrieveAcquistoByNumOrdine(int numOrdine);
boolean doSave(AcquistoBean acquisto);
boolean doUpdateProdottiByNumOrdine(int numOrdine, String prodotti);
boolean setProdotti(int numAcquisto,String prodotti);
boolean doDelete(int numOrdine);

}
