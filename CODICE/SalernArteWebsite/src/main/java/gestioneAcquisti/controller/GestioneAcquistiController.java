package gestioneAcquisti.controller;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.dao.CarrelloDAOImpl;
import model.dao.EventoDAOImpl;
import model.entity.CarrelloBean;
import model.entity.EventoBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;


@WebServlet(name = "GestioneAcquistiController",urlPatterns = "/gestione-acquisti")
public class GestioneAcquistiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestioneAcquistiService service= new GestioneAcquistiServiceImpl();
        HttpSession session = request.getSession();
        UtenteRegistratoBean utente= (UtenteRegistratoBean) session.getAttribute("selezionato");
        CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");

        if(request.getParameter("goToCarrello")!=null){
            boolean alertCarrello=false;
            if(carrello==null && utente==null){
                carrello=new CarrelloBean();

            }else if(carrello==null && utente!=null){
                // non si può mai verificare eprchè se ho fatto il login il carrello non è null ma al più vuoto
                carrello=service.retrieveCarrelloUtente(utente);
            }else if(carrello!=null && utente!=null){
                alertCarrello=service.controlloElementiCarrello(carrello,utente);
            }

            session.setAttribute("carrello",carrello);
            request.setAttribute("alertCarrello",alertCarrello);
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
        if(request.getParameter("svuotaCarrello")!=null){
            if(utente==null){
                carrello=new CarrelloBean();
            }else{
                service.svuotaCarrello(carrello,utente);
                carrello=new CarrelloBean(utente.getId());
            }

            session.setAttribute("carrello",carrello);
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
        if(request.getParameter("removeEventoFromCarrello")!=null){
            int idE= Integer.parseInt(request.getParameter("idE"));
            service.removeEventoFromCarrello(idE,carrello,utente);
            session.setAttribute("carrello", carrello);

            String address=request.getHeader("referer");
            if(address==null || address.contains("/gestione-acquisti") || address.trim().isEmpty()){
                address=".";
            }

            response.sendRedirect(address);
        }
        if(request.getParameter("aggiungiAlCarrello")!=null){
            int quantita=Integer.parseInt(request.getParameter("quantita")); //se non sono numeri darà errore la parse
            int idE=Integer.parseInt(request.getParameter("idE"));
            carrello=service.aggiungiAlCarrello(idE,quantita,carrello,utente);
            request.getSession().setAttribute("notificaAll", "Biglietti aggiunti al carrello."); //per ora non ho la notifica
            session.setAttribute("carrello", carrello);

            String address=request.getHeader("referer");
            if(address==null || address.contains("/gestione-acquisti") || address.trim().isEmpty()){
                address=".";
            }

            response.sendRedirect(address);
        }
    }
}
