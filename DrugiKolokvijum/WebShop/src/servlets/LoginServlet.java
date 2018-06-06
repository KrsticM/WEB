package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ShoppingCart;
import beans.User;
import dao.UserDAO;

/***
 * Servlet zadužen za login. Èita podatke o korisniku iz zahteva i dodaje ga u sesiju ako su kredencijali ispravni.
 * @author Lazar
 *
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	ServletContext context = getServletContext();
    	String contextPath = context.getRealPath("");
    	// Dodaju se korisnici u kontekst kako bi mogli servleti da rade sa njima
    	context.setAttribute("users", new UserDAO(contextPath));
    }
    /***
     * Preusmerava korisnika na login stranicu.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher disp = request.getRequestDispatcher("/JSP/login.jsp");
    	disp.forward(request, response);
    }
    
    /***
     * Prihvata korisnièko ime i lozinku iz forme i pokušava da uloguje korisnika. 
     * Pri neuspešnom loginu preusmerava korisnika nazad na login stranicu, sa porukom greške.
     */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// TODO 1: Implementirati login
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	
    	ServletContext context = getServletContext();
    	
    	UserDAO users = (UserDAO) context.getAttribute("users");
    	User user = users.find(username, password);
    	
    	if (user == null) 
    	{
	    	request.setAttribute("err", "Pogrešno korisničko ime / lozinka");
	    	doGet(request, response);
	    	return;
    	}
    	HttpSession session = request.getSession();
    	ShoppingCart cart = new ShoppingCart();
    	session.setAttribute("user", user);
    	session.setAttribute("cart", cart);
    	
    	user.setLogged(true);
    	response.sendRedirect("ProductServlet");
    	
    	
	}
    

}
