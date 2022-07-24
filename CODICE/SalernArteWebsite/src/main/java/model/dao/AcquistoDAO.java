package model.dao;

import model.entity.AcquistoBean;

import java.util.List;

public interface AcquistoDAO {
public List<AcquistoBean> doRetrieveListaAcquistiByIdUtente(int idUtente);
public AcquistoBean doRetrieveAcquistoByNumOrdine(int numOrdine);
public void doSave(AcquistoBean acquisto);
public void doUpdateProdottiByNumOrdine(int numOrdine, String prodotti);
public void doDelete(int numOrdine);

}
