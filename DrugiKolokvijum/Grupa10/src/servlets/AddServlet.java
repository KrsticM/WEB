package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Proizvod;
import dao.ProizvodiDAO;

/**
 * Servlet implementation class AddServlet
 */
@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		ServletContext ctx = getServletContext();
		ctx.setAttribute("proizvodi", new ProizvodiDAO());
		ctx.setAttribute("skriveniProizvodi", new ProizvodiDAO());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher disp = request.getRequestDispatcher("JSP/add.jsp");
		disp.forward(request, response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String proizvodjac = request.getParameter("proizvodjac");
		
		String tip = request.getParameter("tip");
		
		String model = request.getParameter("model");
		
		String cena = request.getParameter("cena");
		
		try
		{
			Double.parseDouble(cena);
		}
		catch(NumberFormatException e)
		{
			request.setAttribute("greska", "Morate uneti broj");
			doGet(request,response);
			return;
		}


		String punjac = request.getParameter("punjac");
		
		ServletContext ctx = getServletContext();
		ProizvodiDAO pdao = (ProizvodiDAO)ctx.getAttribute("proizvodi");
		
		pdao.getProizvodi().add(new Proizvod(proizvodjac, tip, model, cena));
		
		if(punjac != null) // ako je checkbox
		{
			String tipPunjac = "Dodatna oprema";			
			pdao.getProizvodi().add(new Proizvod(proizvodjac, tipPunjac, model, cena));
		}
		
		doGet(request,response);
		return;
		
	}

}
