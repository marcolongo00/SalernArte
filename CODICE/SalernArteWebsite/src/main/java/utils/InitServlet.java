package utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyInit",urlPatterns = "/MyInit",loadOnStartup = 0)
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        //set path per salvare immagini Eventi
       // getServletContext().setAttribute("pathNewEventi","C:\\Users\\aless\\Desktop\\SalernArte\\CODICE\\SalernArteWebsite\\src\\main\\webapp\\immaginiEventi\\");
        getServletContext().setAttribute("pathNewEventi",getServletContext().getRealPath("")+"immaginiEventi\\");

        super.init();
    }
}
