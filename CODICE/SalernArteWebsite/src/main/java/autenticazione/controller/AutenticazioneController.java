package autenticazione.controller;

import model.dao.*;
import model.entity.AmministratoreBean;
import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AutenticazioneController",urlPatterns = "/autenticazione-controller")
public class AutenticazioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if(request.getParameter("goToLogin")!=null){
            String address="WEB-INF/login.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
        if(request.getParameter("Accedi")!=null){
            String email=request.getParameter("email");
            String password=request.getParameter("password");
            String tipoUtente=request.getParameter("tipoUtente");
            if(tipoUtente.compareTo("utenteRegistrato")==0){
                UtenteRegistratoDAO dao= new UtenteRegistratoDAOImpl();
                UtenteRegistratoBean bean=dao.doRetrieveByEmailPassword(email,password);
                if(bean != null){// if null throw exception ma ci pensiamo dopo
                    session.setAttribute("selezionato", bean);
                }
            }else
                if(tipoUtente.compareTo("scolaresca")==0){
                    ScolarescaDAO dao= new ScolarescaDAOImpl();
                    ScolarescaBean bean=dao.doRetrieveByEmailPassword(email,password);
                    if(bean != null){// if null throw exception ma ci pensiamo dopo
                        session.setAttribute("selezionato", bean);
                    }
                }
                else
                    if(tipoUtente.compareTo("organizzatore")==0){
                        OrganizzatoreDAO dao= new OrganizzatoreDAOImpl();
                        OrganizzatoreBean bean= dao.doRetrieveByEmailPassword(email,password);
                        if(bean != null){// if null throw exception ma ci pensiamo dopo
                            session.setAttribute("selezionato", bean);
                        }
                    }else
                        if(tipoUtente.compareTo("amministratore")==0){
                            AmministratoreDAO dao=new AmministratoreDAOImpl();
                            AmministratoreBean bean=dao.doRetrieveByEmailPassword(email,password);
                            if(bean != null){// if null throw exception ma ci pensiamo dopo
                                session.setAttribute("selezionato", bean);
                            }
                        }//aggiungi dati carrello appena disponibili
            String address="WEB-INF/index.jsp"; //non ottimale, usare il referer ma è un esperimento
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }

    }
}
