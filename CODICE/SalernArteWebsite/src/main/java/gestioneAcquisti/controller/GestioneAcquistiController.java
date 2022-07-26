package gestioneAcquisti.controller;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "GestioneAcquistiController",urlPatterns = "/gestione-acquisti")
public class GestioneAcquistiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestioneAcquistiService service= new GestioneAcquistiServiceImpl();
        HttpSession session = request.getSession();

        if(request.getParameter("goToCarrello")!=null){
            UtenteRegistratoBean utente= (UtenteRegistratoBean) session.getAttribute("selezionato");
            CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");
            boolean alertCarrello = service.retrieveCarrelloAggiornato(utente,carrello);
            session.setAttribute("carrello",carrello);
            request.setAttribute("alertCarrello",alertCarrello);
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
    }
}
