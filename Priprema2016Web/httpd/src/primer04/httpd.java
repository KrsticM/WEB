package primer04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Jednostavan web server
 */
public class httpd {
	private static List<String> imena = new ArrayList<String>();
	private static List<String> prezimena = new ArrayList<String>();
	private static List<String> emails = new ArrayList<String>();
	private static List<Integer> gradovi = new ArrayList<Integer>();
	private static List<Double> krediti = new ArrayList<Double>();
	private static String imenaGradova[] = {
			"Novi Sad",
			"Ruma",
			"Kula"
	};
	
	
	public static void main(String args[]) throws IOException {
		int port = 80;

		@SuppressWarnings("resource")
		ServerSocket srvr = new ServerSocket(port);

		System.out.println("httpd running on port: " + port);
		Socket skt = null;

		while (true) {
			try {
				skt = srvr.accept();
				InetAddress addr = skt.getInetAddress();

				String resource = getResource(skt.getInputStream());

				if (resource.equals(""))
					resource = "pocetna";

				System.out.println("Request from " + addr.getHostName() + ": " + resource);

				sendResponse(resource, skt.getOutputStream());
				skt.close();
				skt = null;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	static String getResource(InputStream is) throws IOException {
		BufferedReader dis = new BufferedReader(new InputStreamReader(is));


		String s = null;
		
		try{ //resi onaj exception
			s = dis.readLine();
		}
		catch(java.net.SocketException e){
			return "";
		}
		
		if (s == null)
			s = "";
		
		System.out.println(s);

		String[] tokens = s.split(" ");

		// prva linija HTTP zahteva: METOD /resurs HTTP/verzija
		// obradjujemo samo GET metodu
		String method = tokens[0];
		if (!method.equals("GET")) {
			return null;
		}

		String rsrc = tokens[1];

		// izbacimo znak '/' sa pocetka
		rsrc = rsrc.substring(1);
		
		// ignorisemo ostatak zaglavlja
		//String s1;
		//while (!(s1 = dis.readLine()).equals(""))
			//System.out.println(s1);

		return rsrc;
	}

	static void sendResponse(String resource, OutputStream os) throws IOException {
		PrintStream ps = new PrintStream(os);

		// naredba?ime=uneto&prezime=uneto&
		
		String splited[] = resource.split("\\?");
		
		String naredba = splited[0];
		
		// Ako je resource jednak stringu koji se dobija prilikom submita forme
		// oblika resurs?parametar=vrednost&... npr: dodaj?ime=pera&prezime=peric
		// getParameter(resource) - vratice HashMap objekat gde su kljucevi parametri (npr. ime, prezime),
		// a vrednosti su jednake unetim vrednostima: mapa.get("ime") vraca "pera"
		
		/*File file = new File(resource);

		if (!file.exists()) {
			// ako datoteka ne postoji, vratimo kod za gresku
			ps.print("HTTP/1.0 404 File not found\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n<b>404 Нисам нашао:" + file.getName() + "</b>");
			// ps.flush();
			System.out.println("Could not find resource: " + file);
			return;
		}*/

		if(naredba.equals("pocetna"))
		{
			// ispisemo zaglavlje HTTP odgovora
			ps.print("HTTP/1.0 200 OK\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n");
			ps.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
					"<title>Priprema2016</title>\r\n" + 
					"</head>\r\n" + 
					"<body style=\"background-color:#e6ffe6\">\r\n" + 
					"	<font color=\"#009900\">\r\n" + 
					"		<form action=\"http://localhost/forma\">\r\n" + 
					"			<table border=\"1\">\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Ime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"ime\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Prezime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"prezime\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Email: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"email\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Grad: </td>\r\n" + 
					"					<td> <select name=\"grad\" style=\"width:220px\">\r\n" + 
					"							<option value=\"0\" selected>Novi Sad</option>\r\n" + 
					"							<option value=\"1\"> Ruma </option>\r\n" + 
					"							<option value=\"2\"> Kula </option>					\r\n" + 
					"						 </select>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Kredit: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"kredit\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>	\r\n" + 
					"				<tr>\r\n" + 
					"					<td> <input type=\"submit\" value=\"Dodaj\" name=\"dodaj\"/> </td> \r\n" + 
					"					<td> <input type=\"submit\" value=\"Snimi izmenu\" name=\"snimi\"/> \r\n" + 
					"						 <input type=\"submit\" value=\"Obriši\" name=\"obrisi\"/> \r\n" + 
					"						 <input type=\"reset\" value=\"Odustani\" name=\"odustani\"/>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"			</table>		\r\n" + 
					"		</form>\r\n" + 
					"		<h3> Trenutno registrovani korisnici: </h3>\r\n" + 
					"		\r\n");
					
					if(imena.size() > 0)
					{
						ps.print("<form action=\"izmeni\"><table border=\"1\">");
						for(int i=0; i<imena.size(); i++)
						{
							ps.print("<tr>"
									+ "<td>"
									+ imena.get(i)
									+ "</td>"
									+ "<td>"
									+ prezimena.get(i)
									+ "</td>"
									+ "<td>"
									+ emails.get(i)
									+ "</td>"
									+ "<td>"
									+ imenaGradova[gradovi.get(i)]
									+ "</td>"
									+ "<td>"
									+ krediti.get(i)
									+ "</td>"
									+ "<td>"
									+ "<input type=\"submit\" name=\"" + i + "\" value=\"Izmeni\"/>"
									+ "</td>"
									+ "<td>"
									+ "<a href=\"obrisi\\?"+ i +"\">Obriši</a>" 
									+ "</td>"
									+ "</tr>"
									);
						}
						ps.print("</table></form>");
					}
					else
					{
						ps.print("Nema trenutno registrovanih korisnika");
					}
					
					
					ps.print(
					"	</font>\r\n" + 
					"</body>\r\n" + 
					"</html>");

		}
		else if(naredba.equals("obrisi") || naredba.equals("obrisi/"))
		{
			System.out.println("Naredba brisanja!");
			int index = Integer.parseInt(splited[1]);
			imena.remove(index);
			prezimena.remove(index);
			emails.remove(index);
			gradovi.remove(index);
			krediti.remove(index);
			// ispisemo zaglavlje HTTP odgovora
			ps.print("HTTP/1.0 200 OK\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n");
			ps.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
					"<title>Priprema2016</title>\r\n" + 
					"</head>\r\n" + 
					"<body style=\"background-color:#e6ffe6\">\r\n" + 
					"	<font color=\"#009900\">\r\n" + 
					"		<form action=\"http://localhost/forma\">\r\n" + 
					"			<table border=\"1\">\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Ime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"ime\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Prezime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"prezime\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Email: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"email\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Grad: </td>\r\n" + 
					"					<td> <select name=\"grad\" style=\"width:220px\">\r\n" + 
					"							<option value=\"0\" selected>Novi Sad</option>\r\n" + 
					"							<option value=\"1\"> Ruma </option>\r\n" + 
					"							<option value=\"2\"> Kula </option>					\r\n" + 
					"						 </select>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Kredit: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"kredit\" style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>	\r\n" + 
					"				<tr>\r\n" + 
					"					<td> <input type=\"submit\" value=\"Dodaj\" name=\"dodaj\"/> </td> \r\n" + 
					"					<td> <input type=\"submit\" value=\"Snimi izmenu\" name=\"snimi\"/> \r\n" + 
					"						 <input type=\"submit\" value=\"Obriši\" name=\"obrisi\"/> \r\n" + 
					"						 <input type=\"reset\" value=\"Odustani\" name=\"odustani\"/>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"			</table>		\r\n" + 
					"		</form>\r\n" + 
					"		<h3> Trenutno registrovani korisnici: </h3>\r\n" + 
					"		\r\n");
					
					if(imena.size() > 0)
					{
						ps.print("<form action=\"izmeni\"><table border=\"1\">");
						for(int i=0; i<imena.size(); i++)
						{
							ps.print("<tr>"
									+ "<td>"
									+ imena.get(i)
									+ "</td>"
									+ "<td>"
									+ prezimena.get(i)
									+ "</td>"
									+ "<td>"
									+ emails.get(i)
									+ "</td>"
									+ "<td>"
									+ imenaGradova[gradovi.get(i)]
									+ "</td>"
									+ "<td>"
									+ krediti.get(i)
									+ "</td>"
									+ "<td>"
									+ "<input type=\"submit\" name=\"" + i + "\" value=\"Izmeni\"/>"
									+ "</td>"
									+ "<td>"
									+ "<a href=\"obrisi?"+ i +"\">Obriši</a>" 
									+ "</td>"
									+ "</tr>"
									);
						}
						ps.print("</table></form>");
					}
					else
					{
						ps.print("Nema trenutno registrovanih korisnika");
					}
					
					
					ps.print(
					"	</font>\r\n" + 
					"</body>\r\n" + 
					"</html>");
		}
		else if(naredba.equals("forma"))
		{
			ps.print("HTTP/1.0 200 OK\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n");
			
			HashMap<String,String> parametri = getParameter(splited[1]);
			
			int akcija = 0; // 1 - dodaj  2 - snimi 3 - obrisi
			
			String ime = "";
			String prezime = "";
			String email = "";
			int grad = 0;
			double kredit = 0.0;
			
			for (String key : parametri.keySet())
			{
				if(key.equals("dodaj"))
				{
					akcija = 1;
				}
				else if(key.equals("snimi"))
				{
					akcija = 2;					
				}
				else if(key.equals("obrisi"))
				{
					akcija = 3;
				}
				else if(key.equals("ime"))
				{
					ime = URLDecoder.decode(parametri.get(key));
				}
				else if(key.equals("prezime"))
				{
					prezime = URLDecoder.decode(parametri.get(key));
				}
				else if(key.equals("email"))
				{
					email = URLDecoder.decode(parametri.get(key));
				}
				else if(key.equals("grad"))
				{
					grad = Integer.parseInt(parametri.get(key));
				}
				else if(key.equals("kredit"))
				{
					kredit = Double.parseDouble(parametri.get(key));
				}
			}
			
			
			if(akcija == 1)
			{
				Boolean dodavanje = true;
				for(int i=0; i<emails.size(); i++)
				{
					if(emails.get(i).equals(email))
					{
						dodavanje = false;
					}					
				}
				if(dodavanje)
				{
					imena.add(ime);
					prezimena.add(prezime);
					emails.add(email);
					gradovi.add(grad);
					krediti.add(kredit);
				}
				ps.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
						"<html>\r\n" + 
						"<head>\r\n" + 
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
						"<title>Priprema2016</title>\r\n" + 
						"</head>\r\n" + 
						"<body style=\"background-color:#e6ffe6\">\r\n" + 
						"	<font color=\"#009900\">\r\n" + 
						"		<form action=\"http://localhost/forma\">\r\n" + 
						"			<table border=\"1\">\r\n" + 
						"				<tr>\r\n" + 
						"					<td> Ime: </td>\r\n" + 
						"					<td> <input type=\"text\" name=\"ime\" style=\"width:220px\"/> </td>\r\n" + 
						"				</tr>\r\n" + 
						"				<tr> \r\n" + 
						"					<td> Prezime: </td>\r\n" + 
						"					<td> <input type=\"text\" name=\"prezime\" style=\"width:220px\"/> </td>\r\n" + 
						"				</tr>\r\n" + 
						"				<tr>\r\n" + 
						"					<td> Email: </td>\r\n" + 
						"					<td> <input type=\"text\" name=\"email\" style=\"width:220px\"/> </td>\r\n" + 
						"				</tr>\r\n" + 
						"				<tr>\r\n" + 
						"					<td> Grad: </td>\r\n" + 
						"					<td> <select name=\"grad\" style=\"width:220px\">\r\n" + 
						"							<option value=\"0\" selected>Novi Sad</option>\r\n" + 
						"							<option value=\"1\"> Ruma </option>\r\n" + 
						"							<option value=\"2\"> Kula </option>					\r\n" + 
						"						 </select>\r\n" + 
						"					</td>\r\n" + 
						"				</tr>\r\n" + 
						"				<tr> \r\n" + 
						"					<td> Kredit: </td>\r\n" + 
						"					<td> <input type=\"text\" name=\"kredit\" style=\"width:220px\"/> </td>\r\n" + 
						"				</tr>	\r\n" + 
						"				<tr>\r\n" + 
						"					<td> <input type=\"submit\" value=\"Dodaj\" name=\"dodaj\"/> </td> \r\n" + 
						"					<td> <input type=\"submit\" value=\"Snimi izmenu\" name=\"snimi\"/> \r\n" + 
						"						 <input type=\"submit\" value=\"Obriši\" name=\"obrisi\"/> \r\n" + 
						"						 <input type=\"reset\" value=\"Odustani\" name=\"odustani\"/>\r\n" + 
						"					</td>\r\n" + 
						"				</tr>\r\n" + 
						"			</table>		\r\n" + 
						"		</form>\r\n" + 
						"		<h3> Trenutno registrovani korisnici: </h3>\r\n" + 
						"		\r\n");
						
						if(imena.size() > 0)
						{
							ps.print("<form action=\"izmeni\"><table border=\"1\">");
							for(int i=0; i<imena.size(); i++)
							{
								ps.print("<tr>"
										+ "<td>"
										+ imena.get(i)
										+ "</td>"
										+ "<td>"
										+ prezimena.get(i)
										+ "</td>"
										+ "<td>"
										+ emails.get(i)
										+ "</td>"
										+ "<td>"
										+ imenaGradova[gradovi.get(i)]
										+ "</td>"
										+ "<td>"
										+ krediti.get(i)
										+ "</td>"
										+ "<td>"
										+ "<input type=\"submit\" name=\"" + i + "\" value=\"Izmeni\"/>"
										+ "</td>"
										+ "<td>"
										+ "<a href=\"\\obrisi\\?"+ i +"\">Obriši</a>" 
										+ "</td>"
										+ "</tr>"
										);
							}
							ps.print("</table></form>");
						}
						else
						{
							ps.print("Nema trenutno registrovanih korisnika");
						}
						
						
						ps.print(
						"	</font>\r\n" + 
						"</body>\r\n" + 
						"</html>");
			}
			else if(akcija == 2)
			{
				
			}
			else if(akcija == 3)
			{
				
			}
			
		}
		else if(naredba.equals("izmeni"))
		{
			///izmeni?0=Izmeni
			String splitedAgain[] = splited[1].split("=");
			int index = Integer.parseInt(splitedAgain[0]);
			
			ps.print("HTTP/1.0 200 OK\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n");
			ps.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
					"<title>Priprema2016</title>\r\n" + 
					"</head>\r\n" + 
					"<body style=\"background-color:#e6ffe6\">\r\n" + 
					"	<font color=\"#009900\">\r\n" + 
					"		<form action=\"http://localhost/forma\">\r\n" + 
					"			<table border=\"1\">\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Ime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"ime\"  value=\"" + imena.get(index) + " \"style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Prezime: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"prezime\" value=\""+ prezimena.get(index) + " \"  style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Email: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"email\" value=\"" + emails.get(index) + " \"style=\"width:220px\" readonly/> </td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr>\r\n" + 
					"					<td> Grad: </td>\r\n" + 
					"					<td> <select name=\"grad\" style=\"width:220px\" >\r\n" + 
					"							<option value=\"0\" " + (gradovi.get(index) == 0 ? "selected" : "" ) + ">Novi Sad</option>\r\n" + 
					"							<option value=\"1\" " + (gradovi.get(index) == 1 ? "selected" : "" ) + "> Ruma </option>\r\n" + 
					"							<option value=\"2\" " + (gradovi.get(index) == 2 ? "selected" : "" ) + "> Kula </option>					\r\n" + 
					"						 </select>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"				<tr> \r\n" + 
					"					<td> Kredit: </td>\r\n" + 
					"					<td> <input type=\"text\" name=\"kredit\" value=\"" + krediti.get(index) + "\"style=\"width:220px\"/> </td>\r\n" + 
					"				</tr>	\r\n" + 
					"				<tr>\r\n" + 
					"					<td> <input type=\"submit\" value=\"Dodaj\" name=\"dodaj\"/> </td> \r\n" + 
					"					<td> <input type=\"submit\" value=\"Snimi izmenu\" name=\"snimi\"/> \r\n" + 
					"						 <input type=\"submit\" value=\"Obriši\" name=\"obrisi\"/> \r\n" + 
					"						 <input type=\"reset\" value=\"Odustani\" name=\"odustani\"/>\r\n" + 
					"					</td>\r\n" + 
					"				</tr>\r\n" + 
					"			</table>		\r\n" + 
					"		</form>\r\n" + 
					"		<h3> Trenutno registrovani korisnici: </h3>\r\n" + 
					"		\r\n");
					
					if(imena.size() > 0)
					{
						ps.print("<form action=\"izmeni\"><table border=\"1\">");
						for(int i=0; i<imena.size(); i++)
						{
							ps.print("<tr>"
									+ "<td>"
									+ imena.get(i)
									+ "</td>"
									+ "<td>"
									+ prezimena.get(i)
									+ "</td>"
									+ "<td>"
									+ emails.get(i)
									+ "</td>"
									+ "<td>"
									+ imenaGradova[gradovi.get(i)]
									+ "</td>"
									+ "<td>"
									+ krediti.get(i)
									+ "</td>"
									+ "<td>"
									+ "<input type=\"submit\" name=\"" + i + "\" value=\"Izmeni\"/>"
									+ "</td>"
									+ "<td>"
									+ "<a href=\"\\obrisi\\?"+ i +"\">Obriši</a>" 
									+ "</td>"
									+ "</tr>"
									);
						}
						ps.print("</table></form>");
					}
					else
					{
						ps.print("Nema trenutno registrovanih korisnika");
					}
					
					
					ps.print(
					"	</font>\r\n" + 
					"</body>\r\n" + 
					"</html>");
		}
		else
		{
			// nije nadjena akcija
			ps.print("HTTP/1.0 404 File not found\r\n"
					+ "Content-type: text/html; charset=UTF-8\r\n\r\n");
			ps.print("Nisam našao naredbu: " + naredba);
		}
		
		ps.flush();
	}
	
	
	/**
	 * Funkcija koja prima string zahtev oblika: <br/>
	 *  {@literal "resurs?parametar1=vrednost1&parametar2=vrednost2&...&parametarN=vrednostN"} <br/>
	 * I vraća HashMap objekat sa Key - Value parovima:
	 * <pre>
	 * 	{ 
	 * 		request : resurs,
	 * 		parametar1 : vrednost1,
	 * 		parametar2 : vrednost2,
	 * 		...
	 * 		parametarN : vrednostN
	 *        } 
	 * </pre>
	 * @param requestLine - String oblika {@literal "resurs?parametar1=vrednost1&parametar2=vrednost2&...&parametarN=vrednostN"}
	 * @return - HashMap&lt;String, String&gt; {parametar: vrednost}
	 */
	static HashMap<String, String> getParameter(String requestLine) {
		HashMap<String, String> retVal = new HashMap<String, String>();

		String request = requestLine.split("\\?")[0];
		retVal.put("request", request);
		String parameters = requestLine.substring(requestLine.indexOf("?") + 1);
		StringTokenizer st = new StringTokenizer(parameters, "&");
		while (st.hasMoreTokens()) {
			String key = "";
			String value = "";
			StringTokenizer pst = new StringTokenizer(st.nextToken(), "=");
			key = pst.nextToken();
			if (pst.hasMoreTokens())
				value = pst.nextToken();

			retVal.put(key, value);
		}

		return retVal;
	}
}
