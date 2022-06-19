package model.dao;

import model.entity.FatturaBean;

import java.util.List;

public interface FatturaDAO {
public List<FatturaBean> doRetrieveListaFattureByUtenteRegistrato(int idUtente);
public List<FatturaBean> doRetrieveListaFattureByScolaresca(int idUtente);
public FatturaBean doRetrieveFatturaByNumOrdine(int numOrdine);
public void doSave(FatturaBean fattura);
public void doUpdateProdottiByNumOrdine(int numOrdine, String prodotti);
public void doDeleteFattura(int numOrdine);

}
