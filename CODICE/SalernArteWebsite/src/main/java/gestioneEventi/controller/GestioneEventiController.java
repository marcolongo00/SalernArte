package gestioneEventi.controller;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "GestioneEventiController",urlPatterns = "/gestione-eventi")
@MultipartConfig
public class GestioneEventiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        GestioneEventiService serviceE=new GestioneEventiServiceImpl();
        if(request.getParameter("inviaRichiestaEvento")!=null){
            //OrganizzatoreBean organizzatore= (OrganizzatoreBean) session.getAttribute("selezionato"); //controllo sull'utente
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

            String path = "./immaginiEventi/" + filePhoto.getSubmittedFileName();
            String pathSave=getServletContext().getAttribute("pathNewEventi")+filePhoto.getSubmittedFileName();
            GestioneEventiServiceImpl.saveImage(filePhoto.getInputStream(),pathSave);

            String descrizione=request.getParameter("desc");
            String indirizzo=request.getParameter("indirizzo");
            String sede=request.getParameter("sede");

            serviceE.richiediInserimentoEvento(1,nome,tipoEvento,descrizione,path,numBiglietti,prezzo,dataInizio,dataFine,indirizzo,sede);

            //serviceE.richiediInserimentoEvento(organizzatore.getId(),nome,tipoEvento,descrizione,filePhoto,pathSave,numBiglietti,prezzo,dataInizio,dataFine,indirizzo,sede);

            String address=request.getHeader("referer");

            if(address==null || address.contains("/gestione-eventi") || address.trim().isEmpty()){
                address=".";
            }

            response.sendRedirect(address);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        GestioneEventiService serviceE=new GestioneEventiServiceImpl();
        EventoDAO eventoDAO=new EventoDAOImpl(); //sposta nel service
        if ( request.getParameter("detailsE")!=null) {
            int idE = Integer.parseInt(request.getParameter("idE"));
            EventoBean evento = eventoDAO.doRetrieveById(idE);//sposta nel service
            request.setAttribute("selectedEvento", evento);

            String address = "/WEB-INF/EventoDetails.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("goToRichiestaEvento")!=null){
            String address = "/WEB-INF/RichiestaEvento.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }

    }
    }
