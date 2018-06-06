package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/***
 * Servlet za odjavljivanje i poništavanje sesije
 * @author Lazar
 *
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 4: Implementirati logout
		// getSession(false) ne kreira novu sesiju ako ona ne postoji
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("user") != null) 
		{
			session.invalidate();
		}
		response.sendRedirect("LoginServlet");
	}

}
