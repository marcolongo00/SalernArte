package model.dao;

import model.entity.AcquistoBean;

import java.util.List;

public interface AcquistoDAO {
List<AcquistoBean> doRetrieveListaAcquistiByIdUtente(int idUtente);
AcquistoBean doRetrieveAcquistoByNumOrdine(int numOrdine);
void doSave(AcquistoBean acquisto);
void doUpdateProdottiByNumOrdine(int numOrdine, String prodotti);
void setProdotti(int numAcquisto,String prodotti);
void doDelete(int numOrdine);

}
