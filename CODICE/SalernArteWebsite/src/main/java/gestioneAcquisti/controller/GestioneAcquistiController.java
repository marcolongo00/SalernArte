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
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


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
            boolean alertCarrello=false; //in teoria gestito, testare
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
            callDispatcher(request,response,address);
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
            callDispatcher(request,response,address);
        }
        if(request.getParameter("removeEventoFromCarrello")!=null){
            int idE= Integer.parseInt(request.getParameter("idE"));
            service.removeEventoFromCarrello(idE,carrello,utente);
            session.setAttribute("carrello", carrello);

            callReferer(request,response);
        }
        if(request.getParameter("datiCartaAcquisto")!=null){
            //go to dettagli carta first
            String address="WEB-INF/gestioneAcquisti/DettagliPagamentoCarta.jsp";
            callDispatcher(request,response,address);
        }
        String finalizzaAcquisto=request.getParameter("finalizzaAcquisto");
        if(finalizzaAcquisto!=null){
            //finto controllo dati carta!!!

            service.acquistaProdotti(carrello,utente);
            //session.setAttribute("notificaAll", "Acquisto completato");
            session.setAttribute("carrello", new CarrelloBean(utente.getId()));
            //se utente fosse null non si sarebbe potuto fare l'acquisto

            String address="WEB-INF/gestioneAcquisti/Acquistato.jsp";
            callDispatcher(request,response,address);
        }
    }
    private void callDispatcher(HttpServletRequest request, HttpServletResponse response,String address) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    private void callReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/gestione-acquisti") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
}
