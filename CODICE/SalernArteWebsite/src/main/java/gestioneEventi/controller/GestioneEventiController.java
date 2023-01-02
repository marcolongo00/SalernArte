package gestioneEventi.controller;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.EventoDAOImpl;
import model.dao.OrganizzatoreDAOImpl;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;
import model.entity.UtenteRegistratoBean;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "GestioneEventiController",urlPatterns = "/gestione-eventi")
@MultipartConfig
public class GestioneEventiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistratoBean utenteLoggato= (UtenteRegistratoBean) session.getAttribute("selezionato");
        GestioneEventiService serviceE=new GestioneEventiServiceImpl();
        if ( request.getParameter("detailsE")!=null) {
            int idE = Integer.parseInt(request.getParameter("idE"));
            EventoBean evento = serviceE.retrieveEventoById(idE);
            if(utenteLoggato==null || utenteLoggato.getTipoUtente().compareToIgnoreCase("utente")==0 || utenteLoggato.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
                CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");
                serviceE.checkQuantitaCarrello(evento,carrello);
            }

            boolean alertScaduta=serviceE.checkScaduta(evento);
            double prezzoBiglietto=serviceE.getPrezzoEvento(evento.getId());

            if(request.getParameter("inserimento")!=null){
                request.setAttribute("ins",true);
            }else if(request.getParameter("modifica")!=null){
                request.setAttribute("modif",true);
            }
            if(utenteLoggato.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
                double scontoDaApplicare= prezzoBiglietto*30/100;
                double prezzoScontato= prezzoBiglietto-scontoDaApplicare;
                request.setAttribute("scontoScuola",prezzoScontato);
            }

            request.setAttribute("selectedEvento", evento);
            request.setAttribute("alertScaduta",alertScaduta);
            request.setAttribute("prezzoBigl",prezzoBiglietto);

            String address = "/WEB-INF/gestioneEventi/EventoDetails.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("goToEventiOrganizzatore")!=null){
            List<EventoBean> eventi=serviceE.retrieveEventiOrganizzatore(utenteLoggato);
            request.setAttribute("eventi",eventi);
            callDispatcher(request,response,"/WEB-INF/gestioneEventi/ListaEventiOrganizzatore.jsp");
        }
        if(request.getParameter("goToRichiestaEvento")!=null){
            //pagina per richiedere l'inserimento di un evento sulla
            // piattaforma da parte di un organizzatore di eventi
            String address = "/WEB-INF/gestioneEventi/RichiestaEvento.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("eliminaEv")!=null){
            int id= Integer.parseInt(request.getParameter("idE"));
            serviceE.rimuoviEvento(id,utenteLoggato);
            callDispatcher(request,response,"/index.html");
        }
        if(request.getParameter("goToRichiesteInserimento")!=null){
            if(utenteLoggato==null ){
                throw new RuntimeException("operazione non autorizzata");
            }
            List<EventoBean> richiesteEventi= serviceE.retrieveRichiesteInserimento(utenteLoggato.getTipoUtente());

            request.setAttribute("richiesteEventi",richiesteEventi);
            String address = "/WEB-INF/gestioneEventi/RichiesteInserimento.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("goToRichiesteModifica")!=null){
            if(utenteLoggato==null){
                throw new RuntimeException("operazione non autorizzata");
            }
            List<EventoBean> richiesteEventi= serviceE.retrieveRichiesteModifica(utenteLoggato.getTipoUtente());

            request.setAttribute("richiesteEventi",richiesteEventi);
            String address = "/WEB-INF/gestioneEventi/RichiesteModifiche.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("bioOrg")!=null){
            int idOrg=Integer.parseInt(request.getParameter("idOrganizzatore"));
            int idE = Integer.parseInt(request.getParameter("idE"));
            //fai in service
            OrganizzatoreBean org= serviceE.retriveBioOrganizzatore(idOrg);
            request.setAttribute("organizzatore",org);
            request.setAttribute("idE",idE);
            String address = "/WEB-INF/gestioneEventi/BioOrganizzatore.jsp";
            callDispatcher(request,response,address);
        }
        String goToTipo=request.getParameter("goToTipoEventi");
        if(goToTipo!=null){
            //in service
            EventoDAOImpl eventoDao=new EventoDAOImpl();
            List<EventoBean> eventi= null;

            if(goToTipo.compareTo("teatro")==0){
                eventi=eventoDao.doRetrieveAllByTeatroAttiviNonScaduti();
            }else{
                eventi=eventoDao.doRetrieveAllByMostraAttiviNonScaduti();
            }
            request.setAttribute("eventi",eventi);
            request.setAttribute("active",goToTipo);
            String address="/WEB-INF/index.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("ricercaEventi")!=null){
            String query=request.getParameter("query")+"*";
            if(query.trim().length()>1){
                List<EventoBean> eventi=serviceE.ricercaEventiByNomeOrDescrizione(query);
                request.setAttribute("eventi",eventi);
            }

            String address="WEB-INF/Ricerca.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("inviaRichiestaEvento")!=null){
            //RIVEDERE TUTTA OPERAZIONE
            OrganizzatoreBean organizzatore= (OrganizzatoreBean) session.getAttribute("selezionato"); //controllo sull'utente
            Date dataInizio=Date.valueOf(request.getParameter("dataInizio"));
            Date dataFine=Date.valueOf(request.getParameter("dataFine"));
            String nome=request.getParameter("nome");
            String tipoEvento=request.getParameter("tipoEvento");
            int numBiglietti=Integer.parseInt(request.getParameter("numBiglietti"));
            double prezzo=Double.parseDouble(request.getParameter("prezzo"));
            Part filePhoto=request.getPart("path");
            if (!(filePhoto != null && filePhoto.getSize()!=0 )) {
                throw new IOException();
            }
            if(ImageIO.read(filePhoto.getInputStream())== null) throw new IOException();


            String pathSave=getServletContext().getAttribute("pathNewEventi")+filePhoto.getSubmittedFileName();

            String descrizione=request.getParameter("desc");
            String indirizzo=request.getParameter("indirizzo");
            String sede=request.getParameter("sede");

            serviceE.richiediInserimentoEvento(organizzatore.getId(),nome,tipoEvento,descrizione,pathSave,filePhoto,numBiglietti,prezzo,dataInizio,dataFine,indirizzo,sede);
            //gestione biglietti
            //gestione richiesta modifica
            callReferer(request,response);
        }else
            if(request.getParameter("accettaIns")!=null){
            //contorlla errori
            int idEvento= Integer.parseInt(request.getParameter("idEvento"));
            String tipoUtente=(String)session.getAttribute("tipoUtente");

            serviceE.attivaEvento(idEvento,tipoUtente); // modifca nome operazione
                callReferer(request, response);
            }else
                if(request.getParameter("rifiutaIns")!=null){
            //contorlla errori
            int idEvento= Integer.parseInt(request.getParameter("idEvento"));

            serviceE.rimuoviEvento(idEvento,utenteLoggato);
                    callReferer(request, response);
                }else if(request.getParameter("accettaMod")!=null){
                    //contorlla errori
                    int idEvento= Integer.parseInt(request.getParameter("idEvento"));
                    String tipoUtente=(String)session.getAttribute("tipoUtente");

                    serviceE.accettaModifica(idEvento,tipoUtente);
                    callReferer(request, response);
                }else
                if(request.getParameter("rifiutaMod")!=null){
                    //contorlla errori
                    int idEvento= Integer.parseInt(request.getParameter("idEvento"));
                    String tipoUtente=(String)session.getAttribute("tipoUtente");

                    serviceE.rifiutaModifica(idEvento,tipoUtente);
                    callReferer(request, response);
                }

                if(request.getParameter("richiestaModEventoOrg")!=null){
                    int idEventoPreChange=Integer.parseInt(request.getParameter("idEventoPreChange"));
                    String titolo=request.getParameter("titoloEvMod");
                    String tipo=request.getParameter("tipoEvMod");
                    String descrizione=request.getParameter("descrizioneMod");
                    Part filePhoto=request.getPart("pathMod");
                    /*
                    NO CONTROLLO PERCHè SE NON SI CAMBIA LA FOTO NON SERVE,
                    SI PRENDERà IL PATH VECCHIO DELL'EVENTO E SI SALVERà QUELLO NEL CAMPO PATH DEL DB

                    if (!(filePhoto != null && filePhoto.getSize()!=0 )) {
                        throw new IOException();
                    }
                    if(ImageIO.read(filePhoto.getInputStream())== null) throw new IOException();
                    */
                    String pathSave;
                    if (filePhoto != null && filePhoto.getSize()!=0 ) {
                        pathSave=getServletContext().getAttribute("pathNewEventi")+filePhoto.getSubmittedFileName();
                    }else{
                        pathSave=""; //<------ inserire old pathsave
                    }
                    Date dataInizio=Date.valueOf(request.getParameter("dataInizioEvMod"));
                    Date dataFine=Date.valueOf(request.getParameter("dataFineEvMod"));
                    int numBiglietti=Integer.parseInt(request.getParameter("numBigliettiEvMod"));
                    double prezzo= Double.parseDouble(request.getParameter("prezzoBigliettoEvMod"));
                    String indirizzo= request.getParameter("indirizzoEvMod");
                    String sede=request.getParameter("sedeEvMod");

                    //operazioni con service
                    serviceE.richiediModificaEvento(idEventoPreChange,utenteLoggato,titolo,tipo,descrizione,pathSave,filePhoto,numBiglietti,prezzo,dataInizio,dataFine,indirizzo,sede);

                    callReferer(request,response);
                    //oppure alla pagina dell'evento quindi chiamerei l'altra oeprazione
                }

    }
    private void callDispatcher(HttpServletRequest request, HttpServletResponse response,String address) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    private void callReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/gestione-eventi") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
    }
