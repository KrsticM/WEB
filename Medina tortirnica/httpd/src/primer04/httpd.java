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
import java.util.StringTokenizer;

/**
 * Jednostavan web server
 */
public class httpd {
	private static List<String> Usernames = new ArrayList<String>();
	private static List<String> Passwords = new ArrayList<String>();
	private static List<String> Genders = new ArrayList<String>();
	private static List<String> Bdays = new ArrayList<String>();
	
	//za narucivanje
	private static List<String> imena = new ArrayList<String>();
	private static List<String> brojeviTel = new ArrayList<String>();
	private static List<String> boje = new ArrayList<String>();
	private static List<String> poruka = new ArrayList<String>();
	
	private static String[] vrsteImena = {
			"Bomba",
			"Milka",
			"Rafaelo",
			"Snikers",
			"Jafa",
			"Keks torta",
			"Pesak",
		
	};
	private static List<Integer> vrsteTorte = new ArrayList<Integer>();
	
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
	    
	    if(naredba.equals("login"))
	    {
	    	
	        ps.print("HTTP/1.0 200 OK\r\n"
	               + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	        
	        HashMap<String,String> parametri = getParameter(resource);
	        // treba pretraziti i videti da li postoji korisnik kome odgovara dati 
	        // username i password
	        
	        // username=novi&password=novi
	        String username="";
	        String password="";
	        
	        for (String key : parametri.keySet()) {
	            if(key.equals("username"))
	            {
	            	username = parametri.get(key);
	            }
	            else if(key.equals("password"))
	            {
	            	password = parametri.get(key);
	            }
	        }
	        System.out.println("Uneti username: " + username + " password: " + password);
	        Boolean uspesnoLogovanje = false;
	        for(int i=0; i<Usernames.size(); i++)
	        {
	        	if(Usernames.get(i).equals(username))
	        	{
	        		if(Passwords.get(i).equals(password))
	        		{
	        			uspesnoLogovanje = true;
	        		}
	        	}
	        }
	        if(uspesnoLogovanje)
	        {
	        	ps.print("<body>\r\n" + 
	        			"\r\n" + 
	        			"<center> \r\n" + 
	        			"	<h3 style=\"color:blue;\"> Medina tortionica </h3> \r\n" + 
	        			"\r\n" + 
	        			"<form action=\"naruci\">\r\n" + 
	        			"	<table>\r\n" + 
	        			"		<tr>\r\n" + 
	        			"			<td> Vaše ime: </td>\r\n" + 
	        			"			<td> <input type=\"text\" name=\"ime\"/> </td>\r\n" + 
	        			"			<td> </td>\r\n" + 
	        			"		</tr>\r\n" + 
	        			"		<tr>\r\n" + 
	        			"			<td> Vaš broj telefona: </td>\r\n" + 
	        			"			<td> <input type=\"text\" name=\"brtel\"/> </td>\r\n" + 
	        			"			<td> </td>\r\n" + 
	        			"		</tr>\r\n" + 
	        			"			<td> Boja vaše torte: </td>\r\n" + 
	        			"			<td> <input type=\"color\" name=\"boja\"/> </td>\r\n" + 
	        			"			<td> </td>\r\n" + 
	        			"		</tr>\r\n" + 
	        			"		<tr>\r\n" + 
	        			"			<td> Vrsta torte: </td>\r\n" + 
	        			"			<td> \r\n" + 
	        			"			    <select name=\"vrsta\">\r\n" + 
	        			"					<option value=\"0\" selected>Bomba</option>\r\n" + 
	        			"					<option value=\"1\">Milka</option>\r\n" + 
	        			"					<option value=\"2\">Rafaelo</option>\r\n" + 
	        			"					<option value=\"3\">Snikers</option>\r\n" + 
	        			"					<option value=\"4\">Jafa</option>\r\n" + 
	        			"					<option value=\"5\">Keks torta</option>\r\n" + 
	        			"					<option value=\"6\">Pesak</option>\r\n" + 
	        			"				</select>\r\n" + 
	        			"			</td>\r\n" + 
	        			"		</tr>\r\n" + 
	        			"		<tr>\r\n" + 
	        			"			<td> Poruka za Medu: </td>\r\n" + 
	        			"			<!--td> <input style=\"height:200px;\" type=\"text\" name=\"poruka\"/></td-->\r\n" + 
	        			"			<td> <textarea name=\"poruka\" cols=\"22\" rows = \"10\"> </textarea> </td>\r\n" + 
	        			"		<tr>\r\n" + 
	        			"			<td> </td>\r\n" + 
	        			"			<td> <input type=\"submit\" value=\"Naruči\"/> <input type=\"reset\" value=\"Odustani\"/> </td>\r\n" + 
	        			"			<td> </td>\r\n" + 
	        			"		</tr>\r\n" + 
	        			"	</table>\r\n" + 
	        			"</form>\r\n" + 
	        			"\r\n" + 
	        			"\r\n" + 
	        			"\r\n" + 
	        			"</center>\r\n" + 
	        			"</body>");
	        	
	        }
	        else
	        {
	        	ps.print("<h1>Logovanje neuspesno!</h1>");
	        }
	    }
	    else if(naredba.equals("register"))
	    {	    	
	    	HashMap<String,String> parametri = getParameter(resource);
	    	int kodRegistracije = 0; // uspesno registrovan
	    	//username=dsada&password=dsada&repeatpassword=sda&gender=male&bday=&robot=on
	    	
	    	String username = "";
	    	String password = "";
	    	String repeatpassword = "";
	    	String gender = "None";
	    	String bday = "None";
	    	String robot = "";
	    	
	    	for (String key : parametri.keySet())
	    	{
	    		if(key.equals("username"))
	    		{
	    			username = parametri.get(key);
	    		}
	    		else if(key.equals("password"))
	    		{
	    			password = parametri.get(key);	    			
	    		}
	    		else if(key.equals("repeatpassword"))
	    		{
	    			repeatpassword = parametri.get(key);
	    		}
	    		else if(key.equals("gender"))
	    		{
	    			gender = parametri.get(key);
	    		}
	    		else if(key.equals("bday"))
	    		{
	    			bday = parametri.get(key);
	    		}
	    		else if(key.equals("robot"))
	    		{
	    			robot = parametri.get(key);
	    		}
	    	}
	    	if(robot.equals("on"))
	    	{
	    		// proveri jos passworde
	    		if(password.equals(repeatpassword))
	    		{
	    			ps.print("HTTP/1.0 200 OK\r\n"
				               + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	    			// zapravo dodaj korisnika
	    			Usernames.add(username);
	    			Passwords.add(password);
	    			Genders.add(gender);
	    			Bdays.add(bday);	    
	    			// kada se registruje nek mu prikaze login stranicu
	    			ps.print("<body>\r\n" + 
	    					"\r\n" + 
	    					"<center> \r\n" + 
	    					"	<h3 style=\"color:blue;\"> Login </h3> \r\n" + 
	    					"\r\n" + 
	    					"<form action=\"http://localhost/login\">\r\n" + 
	    					"	<table>\r\n" + 
	    					"		<tr>\r\n" + 
	    					"			<td> Username: </td>\r\n" + 
	    					"			<td> <input type=\"text\" name=\"username\"/> </td>\r\n" + 
	    					"			<td> </td>\r\n" + 
	    					"		</tr>\r\n" + 
	    					"		<tr>\r\n" + 
	    					"			<td> Password: </td>\r\n" + 
	    					"			<td> <input type=\"password\" name=\"password\"/> </td>\r\n" + 
	    					"			<td> </td>\r\n" + 
	    					"		</tr>\r\n" + 
	    					"		<tr>\r\n" + 
	    					"			<td> </td>\r\n" + 
	    					"			<td> <input type=\"submit\" value=\"Login\"/> <input type=\"reset\" value=\"Reset\"/> </td>\r\n" + 
	    					"			<td> </td>\r\n" + 
	    					"		</tr>\r\n" + 
	    					"	</table>\r\n" + 
	    					"</form>\r\n" + 
	    					"\r\n" + 
	    					"\r\n" + 
	    					"\r\n" + 
	    					"</center>\r\n" + 
	    					"</body>");
	    		}
	    		else
	    		{
	    			ps.print("HTTP/1.0 404 File not found\r\n"
			                + "Content-type: text/html; charset=UTF-8\r\n\r\n");
		    		ps.print("<b> Passwords must be same! </b>");
	    		}
		    	
	    		
	    	}
	    	else
	    	{
	    		ps.print("HTTP/1.0 404 File not found\r\n"
		                + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	    		ps.print("<b> Sorry, but only humans :) </b>");
	    	}
	    	
	    	
	    	
	    }
	    else if(naredba.equals("naruci"))
	    {
	    	//naruci?ime=miki&brtel=064&boja=%23ffffff&vrsta=4&poruka=+Cao+Medo%0D%0A
	    	 ps.print("HTTP/1.0 200 OK\r\n" + "Content-type: text/html; charset=UTF-8\r\n\r\n");
	    	
	    	 HashMap<String,String> parametri = getParameter(resource);
	    	 for(String key : parametri.keySet()) {
	    		 if(key.equals("ime"))
		            {
		            	imena.add(parametri.get(key));
		            }
		            else if(key.equals("brtel"))
		            {
		            	brojeviTel.add(parametri.get(key));
		            }else if(key.equals("boja"))
		            {
		            	boje.add(parametri.get(key));
		            }else if(key.equals("vrsta"))
		            {
		            	vrsteTorte.add(Integer.parseInt(parametri.get(key)));
		            }else if(key.equals("poruka"))
		            {
		            	String porukaa = parametri.get(key);
		            	porukaa = porukaa.replace("+", " ");
		            	System.out.println(porukaa);
		            	porukaa = porukaa.replace("%0D%0A", "<br/>");	
		            	System.out.println(porukaa);
		            	poruka.add(porukaa);
		            }
		        }
	    	 
	    	  	ps.print("<h1 style=\"color:blue;\">Naručene torte</h1>");
		        
		        ps.print("<form action=\"izmeniid\">");
		    	ps.print("<table border=\"1\">");
		    	
		    	ps.print("<tr>"
		    			+ "<th>#</th>"
		    			+ "<th>Ime</th>"
		    			+ "<th>Broj telefona</th>"
		    			+ "<th>Vrsta torte</th>"
		    			+ "<th>Poruka za Medu</th>"
		    			+ "<th>Obrisi</th></tr>");
		    	for(int i=0; i<imena.size(); i++)
		    	{	    		 
		    		 ps.print("<tr><td>");
		    		 ps.print(i+1 + "</td>");
		    		 ps.print("<td>");    		
		    		 ps.print(imena.get(i));
		    		 ps.print("</td>"); 
		    		 ps.print("<td>");    		
		    		 ps.print(brojeviTel.get(i));
		    		 ps.print("</td>"); 
		    		 
		    		 ps.print("<td>");    
		    		 int vrsta = vrsteTorte.get(i);
		    		 ps.print(vrsteImena[vrsta]);
		    		 ps.print("</td>");
		    		 ps.print("<td>");
		    		 ps.print(poruka.get(i));
		    		 ps.print("</td>");
		    		 
		    		 ps.print("<td>");
		    		 ps.print("<input type=\"submit\" name=\"" + i + "\" value=\"Obrisite narucenu tortu\" />\r\n" + 
		    		 		"	</td>");
		    	 }	    	 
		    	 ps.print("</table>");  
		    	 ps.print("</form>");
	    	
	    }
	    else
	    {
	    	System.out.println(naredba);
	        ps.print("HTTP/1.0 404 File not found Content-type: text/html; charset=UTF-8\r\n\r\n");
	        		
	        ps.print("<b> Nisam pronasao naredbu: </b>" + naredba);      
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
