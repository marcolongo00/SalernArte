package gestioneEventi.service;

import model.dao.BigliettoDAO;
import model.dao.BigliettoDAOImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Calendar;

public class GestioneEventiServiceImpl implements GestioneEventiService{
    private EventoDAO daoEvento;
    private BigliettoDAO daoBiglietto;

    public GestioneEventiServiceImpl() {
        daoEvento= new EventoDAOImpl();
        daoBiglietto=new BigliettoDAOImpl();
    }

    public void richiediInserimentoEvento(int idOrganizzatore,String nome, String tipoEvento, String descrizione, String path, int numBiglietti, double prezzoBiglietto, Date dataInizio, Date dataFine, String indirizzo, String sede){
        try{
            Date dataAttuale= new Date(Calendar.getInstance().getTimeInMillis());
            if(dataFine.before(dataInizio) || dataInizio.before(dataAttuale)){
                throw  new Exception();
            }
            if(nome==null || nome.trim().length()==0 || descrizione==null || descrizione.trim().length()==0 )
                throw new Exception();

            if(numBiglietti <=0 || prezzoBiglietto<= 0)
                throw new Exception();

            //aggiungi all'init i path delle foto

            //gestisci path foto


            EventoBean bean= new EventoBean(idOrganizzatore,dataInizio,dataFine,nome,path,descrizione,indirizzo,sede,numBiglietti,getTypeEvento(tipoEvento));
            daoEvento.doSave(bean);
            //creo tanti biglietti quanto il num inserito e li carico
            for (int i = 0; i < numBiglietti; i++) {
                daoBiglietto.doSave(bean.getId(),prezzoBiglietto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private boolean getTypeEvento(String tipoEvento){
        if(tipoEvento.compareTo("teatro")==0)
            return true;
        else
            return false;
    }

    public static void saveImage(InputStream in,String path){
        File file=new File(path);
        try {
            Files.copy(in,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImage(String path){
        File file=new File(path);
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
