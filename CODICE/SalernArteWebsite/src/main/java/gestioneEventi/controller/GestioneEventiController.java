package gestioneEventi.controller;

import model.dao.EventoDAOImpl;
import model.entity.EventoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GestioneEventiController",urlPatterns = "/GestioneEventiController")
public class GestioneEventiController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventoDAOImpl eventoDAO = new EventoDAOImpl();
        boolean details= Boolean.parseBoolean(request.getParameter("detailsE"));
        if ( details) {
            int idE = Integer.parseInt(request.getParameter("idE"));
            EventoBean evento = eventoDAO.doRetrieveById(idE);
            request.setAttribute("selectedEvento", evento);
        }
        String address = "/WEB-INF/EventoDetails.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    }
