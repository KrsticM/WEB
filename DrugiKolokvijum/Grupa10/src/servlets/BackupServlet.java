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
 * Servlet implementation class BackupServlet
 */
@WebServlet("/BackupServlet")
public class BackupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BackupServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext ctx = getServletContext();

		ProizvodiDAO originali = (ProizvodiDAO) ctx.getAttribute("proizvodi");

		ProizvodiDAO skriveniProizvodi = (ProizvodiDAO) ctx.getAttribute("skriveniProizvodi");

		for (int i = skriveniProizvodi.getProizvodi().size() - 1; i >= 0; i--) {

			originali.getProizvodi().add(skriveniProizvodi.getProizvodi().get(i));
			skriveniProizvodi.getProizvodi().remove(i);

		}

		ctx.setAttribute("proizvodi", originali);
		ctx.setAttribute("skriveniProizvodi", skriveniProizvodi);

		// Boolean filtrir = false;
		// request.setAttribute("fitrir", filtrir);
		RequestDispatcher disp = request.getRequestDispatcher("JSP/add.jsp");
		disp.forward(request, response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
