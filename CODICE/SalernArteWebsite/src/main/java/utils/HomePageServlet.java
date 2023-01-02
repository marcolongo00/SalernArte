package utils;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/index.html")
public class HomePageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //questa pagina sfrutterà il service relativo alla gestione eventi in quanto l'operazione
        //che ci serve applicare è un retrive di tutti gli eventi non scaduti ed attivi del sistema
        GestioneEventiService service= new GestioneEventiServiceImpl();

        List<EventoBean> eventi= service.retrieveTuttiEventiNonScadutiAttivi();
        request.setAttribute("eventi",eventi);
        String address="/WEB-INF/index.jsp";
        RequestDispatcher dispatcher=request.getRequestDispatcher(address);
        dispatcher.forward(request,response);
    }
}
