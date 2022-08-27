package autenticazione.controller;

import autenticazione.service.AutenticazioneService;
import autenticazione.service.AutenticazioneServiceImpl;
import model.dao.*;
import model.entity.UtenteRegistratoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AreaUtenteController",urlPatterns = "/area-utente")
public class AreaUtenteController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService service=new AutenticazioneServiceImpl();
        if(request.getParameter("listaUtenti")!=null){
            //usa service
            String tipoUtente= (String) session.getAttribute("tipoUtente"); //retrive utente non tipoutente
            List<UtenteRegistratoBean> allUtenti= service.retriveAllUtentiSistema(tipoUtente);
            request.setAttribute("allUtenti",allUtenti);

            String address = "/WEB-INF/gestioneUtente/AllUtenti.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("goToProfilo")!=null){
            //nella jsp usa JS per vedere i dati del tipo di utente giusto

            String address = "/WEB-INF/gestioneUtente/ProfiloUtente.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
    }
}
