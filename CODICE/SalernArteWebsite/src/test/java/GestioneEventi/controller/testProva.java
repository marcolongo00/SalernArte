package GestioneEventi.controller;

import model.entity.UtenteBean;
import model.entity.UtenteRegistratoBean;
import org.junit.Test;
import  static org.junit.Assert.assertTrue;
import  static org.junit.Assert.assertEquals;

public class testProva {

    @Test
    public  void nomeProva(){
        UtenteRegistratoBean utenteRegistratoBean= new UtenteBean();
        assertEquals("nulL",null,utenteRegistratoBean.getTipoUtente());
    }
}
