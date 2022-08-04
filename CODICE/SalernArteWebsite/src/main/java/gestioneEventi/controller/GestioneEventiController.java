package gestioneEventi.controller;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.dao.OrganizzatoreDAOImpl;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.OrganizzatoreBean;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GestioneEventiController",urlPatterns = "/gestione-eventi")
@MultipartConfig
public class GestioneEventiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        GestioneEventiService serviceE=new GestioneEventiServiceImpl();
        if ( request.getParameter("detailsE")!=null) {
            int idE = Integer.parseInt(request.getParameter("idE"));

            EventoBean evento = serviceE.retriveEventoById(idE);
            CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");
            serviceE.checkQuantitaCarrello(evento,carrello);
            boolean alertScaduta=serviceE.checkScaduta(evento);
            double prezzoBiglietto=serviceE.getPrezzoEvento(evento.getId());
            request.setAttribute("selectedEvento", evento);
            request.setAttribute("alertScaduta",alertScaduta);
            request.setAttribute("prezzoBigl",prezzoBiglietto);

            String address = "/WEB-INF/gestioneEventi/EventoDetails.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("goToRichiestaEvento")!=null){
            String address = "/WEB-INF/gestioneEventi/RichiestaEvento.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("goToAllRichiesteEventi")!=null){
            String tipoUtente=(String) session.getAttribute("tipoUtente");
            List<EventoBean> richiesteEventi= serviceE.retriveAllRichiesteEventi(tipoUtente);
            
            request.setAttribute("richiesteEventi",richiesteEventi); //differenzia modifica
            String address = "/WEB-INF/gestioneEventi/AllRichiesteEventi.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("bioOrg")!=null){
            int idOrg=Integer.parseInt(request.getParameter("idOrganizzatore"));
            int idE = Integer.parseInt(request.getParameter("idE"));
            //fai in service
            OrganizzatoreDAOImpl daoOrg=new OrganizzatoreDAOImpl();
            OrganizzatoreBean org= daoOrg.doRetrieveById(idOrg);
            request.setAttribute("organizzatore",org);
            request.setAttribute("idE",idE);
            String address = "/WEB-INF/gestioneEventi/BioOrganizzatore.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
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
            String address="/WEB-INF/index.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("ricercaEventi")!=null){
            // nel service
            String query=request.getParameter("query")+"*";
            if(query.trim().length()>1){
                List<EventoBean> eventi=new EventoDAOImpl().doRetrieveByNomeOrDescrizione(query);
                request.setAttribute("eventi",eventi);
            }

            String address="WEB-INF/Ricerca.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
        if(request.getParameter("inviaRichiestaEvento")!=null){
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
        }else
            if(request.getParameter("accettaIns")!=null){
            //contorlla errori
            int idEvento= Integer.parseInt(request.getParameter("idEvento"));
            String tipoUtente=(String)session.getAttribute("tipoUtente");

            serviceE.attivaEvento(idEvento,tipoUtente);
            }else
                if(request.getParameter("rifiutaIns")!=null){
            //contorlla errori
            int idEvento= Integer.parseInt(request.getParameter("idEvento"));
            String tipoUtente=(String)session.getAttribute("tipoUtente");

            serviceE.rimuoviEvento(idEvento,tipoUtente);
                }
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/gestione-eventi") || address.trim().isEmpty()){
            address=".";
        }

        response.sendRedirect(address);
    }
    }
