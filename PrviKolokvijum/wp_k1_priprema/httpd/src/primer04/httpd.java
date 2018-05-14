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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 * Jednostavan web server
 */
public class httpd {
	
	private static String[] gradoviImena = {
			"Novi Sad",
			"Milići",
			"Kula ili Vrbas isto je to",
			"London",
			"Madrid",
			"Ruma",
			"Melenci"
	};
	private static List<String> naziviKlubova = new ArrayList<String>();
	private static List<Integer> gradovi = new ArrayList<Integer>();
	private static List<Boolean> aktivni = new ArrayList<Boolean>();
	private static List<Integer> bodovi = new ArrayList<Integer>();
	
	public static void main(String args[]) throws IOException {
		int port = 80;		
		@SuppressWarnings("resource")
		ServerSocket srvr = new ServerSocket(port);

		System.out.println("httpd running on port: " + port);
		System.out.println("document root is: " + new File(".").getAbsolutePath() + "\n");

		Socket skt = null;

		while (true) {
			try {
				skt = srvr.accept();
				InetAddress addr = skt.getInetAddress();

				String resource = getResource(skt.getInputStream());

				if (resource.equals(""))
					resource = "index.html";

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
		String s = dis.readLine();
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
		String s1;
		while (!(s1 = dis.readLine()).equals(""))
			System.out.println(s1);

		return rsrc;
	}

	static void sendResponse(String resource, OutputStream os) throws IOException {
		PrintStream ps = new PrintStream(os);		
		// Ako je resource jednak stringu koji se dobija prilikom submita forme
		// oblika resurs?parametar=vrednost&... npr: dodaj?ime=pera&prezime=peric
		// getParameter(resource) - vratice HashMap objekat gde su kljucevi parametri (npr. ime, prezime),
		// a vrednosti su jednake unetim vrednostima: mapa.get("ime") vraca "pera"
		
		String splited[] = resource.split("\\?");
	    String naredba = splited[0];
	    
	    

	    if(naredba.equals("dodaj"))
	    {
	    	
	        ps.print("HTTP/1.0 200 OK\r\n"
	               + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	        
	        HashMap<String,String> parametri = getParameter(resource);

	        for (String key : parametri.keySet()) {
	            if(key.equals("naziv"))
	            {
	            	naziviKlubova.add(parametri.get(key));
	            }
	            else if(key.equals("grad"))
	            {
	            	gradovi.add(Integer.parseInt(parametri.get(key)));
	            }
	        }
	        if(parametri.size() >= 3)
	        {
	        	aktivni.add(true);
	        }
	        else
	        {
	        	aktivni.add(false);
	        }
	        bodovi.add(0);	        
	        
	        ps.print("<h1 style=\"color:blue;\">Tabela</h1>");
	        
	        ps.print("<form action=\"izmeniid\">");
	    	ps.print("<table border=\"1\">");
	    	
	    	ps.print("<tr><th>#</th><th>Klub</th><th>Bodovi</th><th>Akcije</th></tr>");
	    	for(int i=0; i<naziviKlubova.size(); i++)
	    	{	    		 
	    		 ps.print("<tr><td>");
	    		 ps.print(i+1 + "</td>");
	    		 ps.print("<td>");    		 
	    		 int grad = gradovi.get(i);
	    		 ps.print(naziviKlubova.get(i) + " " + gradoviImena[grad]);
	    		 ps.print("</td>");
	    		 ps.print("<td>");
	    		 ps.print(bodovi.get(i));
	    		 ps.print("</td>");
	    		 ps.print("<td>");
	    		 ps.print("<input type=\"submit\" name=\"" + i + "\" value=\"Izmena podataka\" />\r\n" + 
	    		 		"	</td>");
	    	 }	    	 
	    	 ps.print("</table>");  
	    	 ps.print("</form>");
	        
	        ps.print("<a href=\"dodajopet\">Dodaj novi klub</a><br/>");
	        ps.print("<a href=\"prikazivodeci\">Prikaži vodeći klub</a><br/><br/>");
	        
	        ps.print("<h1 style=\"color:blue;\">Upis bodova</h1>");
	        
	        // Tabela za unos bodova
	        ps.print("<form action=\"unesibodove\">");
	    	ps.print("<table>");
	    	
	    	ps.print("<tr><td>Klub</td>");
	    	ps.print("<td><select name=\"klub\">");
	    	int grad=0;
	    	for(int i=0; i< naziviKlubova.size(); i++)
	    	{
	    		grad = gradovi.get(i);	    		 
	    		ps.print("<option value=\"" + i + "\">" + naziviKlubova.get(i) + " " + gradoviImena[grad] + "</option>");
	    	}
	    	ps.print("</select></td><tr>");
	    	ps.print("<tr><td>Bodovi</td>");
	    	ps.print("<td> <input type=\"text\" name=\"bodovi\" /> </td></tr>");
	    	ps.print("<tr><td><input type=\"submit\" value=\"Unesi\" /></td></tr>");
	    	ps.print("</table></form>");
	    }
	    else if(naredba.equals("dodajopet"))
	    {
	    	 ps.print("HTTP/1.0 200 OK\r\n"
	                 + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	    	 ps.print("<h1 style=\"color:red;\">Unos novog kluba</h1>\r\n" + 
	    	  		"	<form action=\"dodaj\">\r\n" + 
	    	  		"		<table>\r\n" + 
	    	  		"			<tr>\r\n" + 
	    	  		"				<td> <h4>Naziv:<h4> </td>\r\n" + 
	    	  		"				<td> <input type=\"text\" name=\"naziv\" /> </td>\r\n" + 
	    	  		"			</tr>	\r\n" + 
	    	  		"			<tr>\r\n" + 
	    	  		"				<td> <h4>Grad:<h4> </td>\r\n" + 
	    	  		"				<td> <select name=\"grad\">\r\n" + 
	    	  		"						<option value=\"0\" selected>Novi Sad</option>\r\n" + 
	    	  		"						<option value=\"1\">Milići</option>\r\n" + 
	    	  		"						<option value=\"2\">Kula ili Vrbas isto je to</option>\r\n" + 
	    	  		"						<option value=\"3\">London</option>\r\n" + 
	    	  		"						<option value=\"4\">Madrid</option>\r\n" + 
	    	  		"						<option value=\"5\">Ruma</option>\r\n" + 
	    	  		"						<option value=\"6\">Melenci</option>\r\n" + 
	    	  		"					</select>\r\n" + 
	    	  		"				</td>\r\n" + 
	    	  		"			</tr>	\r\n" + 
	    	  		"			<tr>\r\n" + 
	    	  		"				<td> <h4>Aktivan:<h4> </td>\r\n" + 
	    	  		"				<td> <input type=\"checkbox\" name=\"aktivan\" value=\"jeste\"/></td>\r\n" + 
	    	  		"			</tr>	\r\n" + 
	    	  		"		</table>\r\n" + 
	    	  		"		<input type=\"submit\" value=\"Dodaj\" />\r\n" + 
	    	  		"	</form>");
	    }
	    else if(naredba.equals("prikazivodeci"))
	    {	    	 
	    	 ps.print("HTTP/1.0 200 OK\r\n"
	                 + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	    	 ps.print("<h5>Trenutno vodeći klub: </h5>");
	    	 int max = 0;
	    	 String vodeci = "Ne postoji!";
	    	 int grad=0;
	    	 for(int i=0; i< naziviKlubova.size(); i++)
	    	 {
	    		 if(bodovi.get(i) > max)
	    		 {
	    			 max = bodovi.get(i);
	    			 grad = gradovi.get(i);
	    			 vodeci = naziviKlubova.get(i) + " " + gradoviImena[grad];    			 
	    		 }
	    	 }
	    	 ps.print(vodeci);
	    }
	    else if(naredba.equals("izmeniid"))
	    {	
	    	//HashMap<String,String> parametriIzmene = getParameter(resource);
	    	String splitedAgain[] = splited[1].split("=");
	    	String kljuc = splitedAgain[0];
	    	int kljucIndex = Integer.parseInt(kljuc);
	    	
	        ps.print("HTTP/1.0 200 OK\r\n"
	               + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	        ps.print("<h1>Izmena podataka</h1>");
	        ps.print("<form action=\"izmeniklub\">");
	    		ps.print("<table>");
	    		
	    		ps.print("<tr>\r\n" + 
	    				"				<td> <h4>Naziv:<h4> </td>\r\n" + 
	    				"				<td> <input type=\"text\" name=\"naziv\" value=\"" + naziviKlubova.get(kljucIndex)  + "\"/></td>\r\n" + 
	    				"			</tr>	\r\n" + 
	    				"			<tr>\r\n" + 
	    				"				<td> <h4>Grad:<h4> </td>\r\n" + 
	    				"				<td> <select name=\"grad\">\r\n" + 
	    				"						<option value=\"0\"" + (gradovi.get(kljucIndex) == 0 ? "selected" : " ") + ">Novi Sad</option>\r\n" + 
	    				"						<option value=\"1\"" + (gradovi.get(kljucIndex) == 1 ? "selected" : " ") + ">Milići</option>\r\n" + 
	    				"						<option value=\"2\"" + (gradovi.get(kljucIndex) == 2 ? "selected" : " ") + ">Kula ili Vrbas isto je to</option>\r\n" + 
	    				"						<option value=\"3\"" + (gradovi.get(kljucIndex) == 3 ? "selected" : " ") + ">London</option>\r\n" + 
	    				"						<option value=\"4\"" + (gradovi.get(kljucIndex) == 4 ? "selected" : " ") + ">Madrid</option>\r\n" + 
	    				"						<option value=\"5\"" + (gradovi.get(kljucIndex) == 5 ? "selected" : " ") + ">Ruma</option>\r\n" + 
	    				"						<option value=\"6\"" + (gradovi.get(kljucIndex) == 6 ? "selected" : " ") + ">Melenci</option>\r\n" + 
	    				"					</select>\r\n" + 
	    				"				</td>\r\n" + 
	    				"			</tr>	\r\n" + 
	    				"			<tr>\r\n" + 
	    				"				<td> <h4>Aktivan:<h4> </td>\r\n" + 
	    				"				<td> <input type=\"checkbox\" name=\"aktivan\" value=\"jeste\" " + (aktivni.get(kljucIndex) ? "checked" : "") + " /></td>\r\n" + 
	    				"			</tr>	");
	    		ps.print("</table>");
	    		ps.print("<input type=\"submit\" value=\"Izmeni\" name=\"" + kljucIndex + "\"/>"); 
	    		ps.print("</form>");
	        
	    }
	    else if(naredba.equals("izmeniklub"))
	    {
	        ps.print("HTTP/1.0 200 OK\r\n"
	               + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	        HashMap<String,String> parametri = getParameter(resource);

	        int index = 0;
	        for (Entry<String, String> entry : parametri.entrySet()) {
	            String key = entry.getKey();
	            String value = entry.getValue();
	            if(value.equals("Izmeni"))	            		
	            {
	            	index = Integer.parseInt(key);
	            	break;
	            }	            
	        }
	        
	        for (String key : parametri.keySet()) 
	        {
	            if(key.equals("naziv"))
	            {
	            	naziviKlubova.set(index, parametri.get(key));
	            }
	            else if(key.equals("grad"))
	            {
	            	gradovi.set(index, Integer.parseInt(parametri.get(key)));
	            }
		        
	        }	        

	        if(parametri.size() >= 4)
	        {
	        	aktivni.set(index, true);
	        }
	        else
	        {
	        	aktivni.set(index, false);
	        }	               
	        
	        ps.print("<h1 style=\"color:blue;\">Tabela</h1>");
	        
	        ps.print("<form action=\"izmeniid\">");
	    	ps.print("<table border=\"1\">");
	    	
	    	ps.print("<tr><th>#</th><th>Klub</th><th>Bodovi</th><th>Akcije</th></tr>");
	    	for(int i=0; i<naziviKlubova.size(); i++)
	    	{	    		 
	    		 ps.print("<tr><td>");
	    		 ps.print(i+1 + "</td>");
	    		 ps.print("<td>");    		 
	    		 int grad = gradovi.get(i);
	    		 ps.print(naziviKlubova.get(i) + " " + gradoviImena[grad]);
	    		 ps.print("</td>");
	    		 ps.print("<td>");
	    		 ps.print(bodovi.get(i));
	    		 ps.print("</td>");
	    		 ps.print("<td>");
	    		 ps.print("<input type=\"submit\" name=\"" + i + "\" value=\"Izmena podataka\" />\r\n" + 
	    		 		"	</td>");
	    	 }	    	 
	    	 ps.print("</table>");  
	    	 ps.print("</form>");
	        
	        ps.print("<a href=\"dodajopet\">Dodaj novi klub</a><br/>");
	        ps.print("<a href=\"prikazivodeci\">Prikaži vodeći klub</a><br/><br/>");
	        
	        ps.print("<h1 style=\"color:blue;\">Upis bodova</h1>");
	        
	        // Tabela za unos bodova
	        ps.print("<form action=\"unesibodove\">");
	    	ps.print("<table>");
	    	
	    	ps.print("<tr><td>Klub</td>");
	    	ps.print("<td><select name=\"klub\">");
	    	int grad=0;
	    	for(int i=0; i< naziviKlubova.size(); i++)
	    	{
	    		grad = gradovi.get(i);	    		 
	    		ps.print("<option value=\"" + i + "\">" + naziviKlubova.get(i) + " " + gradoviImena[grad] + "</option>");
	    	}
	    	ps.print("</select></td><tr>");
	    	ps.print("<tr><td>Bodovi</td>");
	    	ps.print("<td> <input type=\"text\" name=\"bodovi\" /> </td></tr>");
	    	ps.print("<tr><td><input type=\"submit\" value=\"Unesi\" /></td></tr>");
	    	ps.print("</table></form>");	        
	    }
	    else if(naredba.equals("unesibodove"))
	    {
	    	HashMap<String,String> parametri = getParameter(resource);
	    	
	    	int index=0;
	    	int bodovii=0;
	    	for (String key : parametri.keySet()) 
	        {
	            if(key.equals("klub"))
	            {
	            	index = Integer.parseInt(parametri.get(key));
	            }
	            else if(key.equals("bodovi"))
	            {
	            	bodovii = Integer.parseInt(parametri.get(key));
	            }
		        
	        }	      
	    	bodovi.set(index, bodovi.get(index) + bodovii);
	    	 ps.print("<h1 style=\"color:blue;\">Tabela</h1>");
		        
		        ps.print("<form action=\"izmeniid\">");
		    	ps.print("<table border=\"1\">");
		    	
		    	ps.print("<tr><th>#</th><th>Klub</th><th>Bodovi</th><th>Akcije</th></tr>");
		    	for(int i=0; i<naziviKlubova.size(); i++)
		    	{	    		 
		    		 ps.print("<tr><td>");
		    		 ps.print(i+1 + "</td>");
		    		 ps.print("<td>");    		 
		    		 int grad = gradovi.get(i);
		    		 ps.print(naziviKlubova.get(i) + " " + gradoviImena[grad]);
		    		 ps.print("</td>");
		    		 ps.print("<td>");
		    		 ps.print(bodovi.get(i));
		    		 ps.print("</td>");
		    		 ps.print("<td>");
		    		 ps.print("<input type=\"submit\" name=\"" + i + "\" value=\"Izmena podataka\" />\r\n" + 
		    		 		"	</td>");
		    	 }	    	 
		    	 ps.print("</table>");  
		    	 ps.print("</form>");
		        
		        ps.print("<a href=\"dodajopet\">Dodaj novi klub</a><br/>");
		        ps.print("<a href=\"prikazivodeci\">Prikaži vodeći klub</a><br/><br/>");
		        
		        ps.print("<h1 style=\"color:blue;\">Upis bodova</h1>");
		        
		        // Tabela za unos bodova
		        ps.print("<form action=\"unesibodove\">");
		    	ps.print("<table>");
		    	
		    	ps.print("<tr><td>Klub</td>");
		    	ps.print("<td><select name=\"klub\">");
		    	int grad=0;
		    	for(int i=0; i< naziviKlubova.size(); i++)
		    	{
		    		grad = gradovi.get(i);	    		 
		    		ps.print("<option value=\"" + i + "\">" + naziviKlubova.get(i) + " " + gradoviImena[grad] + "</option>");
		    	}
		    	ps.print("</select></td><tr>");
		    	ps.print("<tr><td>Bodovi</td>");
		    	ps.print("<td> <input type=\"text\" name=\"bodovi\" /> </td></tr>");
		    	ps.print("<tr><td><input type=\"submit\" value=\"Unesi\" /></td></tr>");
		    	ps.print("</table></form>");	        
	    }
	    else
	    {
	        ps.print("HTTP/1.0 404 File not found\r\n"
	                + "Content-type: text/html; charset=UTF-8\r\n\r\n<b>404 Нисам нашао наредбу:"
	                + naredba + "</b>");           
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
