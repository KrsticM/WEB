package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Product;
import beans.ShoppingCart;
import beans.User;
import dao.ProductDAO;
import dao.UserDAO;

public class AddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddProductServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException { 
    	System.out.println("Inicijalizovan AddProductServlet");
    	super.init();
    	ServletContext context = getServletContext();
    	String contextPath = context.getRealPath("");
    	// Dodaju se proizvodi u kontekst kako bi mogli servleti da rade sa njima
    	context.setAttribute("products", new ProductDAO(contextPath));
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 2: Implementirati listanje svih proizvoda
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) 
		{
			RequestDispatcher disp = request.getRequestDispatcher("/LoginServlet");
			disp.forward(request, response);
			return;
		}
		String itemIdString = request.getParameter("itemID");
		System.out.println("DEBUG: ItemID = " + itemIdString);
		
		ServletContext context = getServletContext();
		ProductDAO products = (ProductDAO) context.getAttribute("products");
		
		Product toAdd = products.findProduct(itemIdString);
		
		ShoppingCart cart = (ShoppingCart)session.getAttribute("cart");
		cart.addItem(toAdd, 1);
		
		context.setAttribute("cart", toAdd);
		
		System.out.println("One more item added!");
		
		RequestDispatcher disp = request.getRequestDispatcher("/JSP/addedToCart.jsp");
		disp.forward(request, response);
	}

}
