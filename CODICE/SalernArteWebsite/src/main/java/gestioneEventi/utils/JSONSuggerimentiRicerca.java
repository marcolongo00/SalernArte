package gestioneEventi.utils;

import gestioneEventi.service.GestioneEventiService;
import gestioneEventi.service.GestioneEventiServiceImpl;
import model.dao.EventoDAO;
import model.dao.EventoDAOImpl;
import model.entity.EventoBean;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "JSONSuggerimentiRicerca",urlPatterns = "/JSONSuggerimentiRicerca")
public class JSONSuggerimentiRicerca extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         GestioneEventiService service= new GestioneEventiServiceImpl();
        String query=request.getParameter("query")+"*";
        List<EventoBean> eventi= new ArrayList<>();
        if(query.trim().length()>1){
            eventi=service.ricercaEventiByNomeOrDescrizione(query);
        }

        JSONArray ricercaTOTJSON= new JSONArray();
        for(EventoBean e:eventi){
            JSONArray evento=new JSONArray();
            evento.put(e.getNome());
            ricercaTOTJSON.put(evento);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(ricercaTOTJSON.toString());
    }
}
