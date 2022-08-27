package gestioneAcquisti.utils;

import com.google.gson.Gson;
import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "JSONUpdateQuantitaCarrello",urlPatterns = "/update-carr-qta")
public class JSONUpdateQuantitaCarrello extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        GestioneAcquistiService service= new GestioneAcquistiServiceImpl();
        boolean result=true;// serviva per la notifica di errore, rivedi poi
        int idE=Integer.parseInt(request.getParameter("idE"));
        int qta=Integer.parseInt(request.getParameter("qta"));
        CarrelloBean carrelloSessione= (CarrelloBean) request.getSession().getAttribute("carrello");
        UtenteRegistratoBean utente= (UtenteRegistratoBean) request.getSession().getAttribute("selezionato");
          service.updateQuantitaCarrello(idE,qta,carrelloSessione,utente);
        request.getSession().setAttribute("carrello", carrelloSessione);

        String json=new Gson().toJson(result);
        response.getWriter().append(json);
    }
}
