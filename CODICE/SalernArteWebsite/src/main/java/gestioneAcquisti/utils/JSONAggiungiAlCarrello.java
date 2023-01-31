package gestioneAcquisti.utils;

import com.google.gson.Gson;
import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "JSONAggiungiAlCarrello", urlPatterns = "/aggiungi-al-carrello")
public class JSONAggiungiAlCarrello extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UtenteRegistratoBean utente= (UtenteRegistratoBean) session.getAttribute("selezionato");
        CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");
        GestioneAcquistiService service= new GestioneAcquistiServiceImpl();

        boolean result=true;
        try{
            int quantita=Integer.parseInt(request.getParameter("quantita")); //se non sono numeri darà errore la parse
            int idE=Integer.parseInt(request.getParameter("idE"));
            carrello=service.aggiungiAlCarrello(idE,quantita,carrello,utente);
            session.setAttribute("carrello", carrello);
            request.getSession().setAttribute("notificaAll", "Biglietti aggiunti al carrello."); //per ora non ho la notifica
        }catch(Exception e){
            result=false;
        }
        String json=new Gson().toJson(result);
        response.getWriter().append(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
