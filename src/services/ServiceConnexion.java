package services;

import java.net.Socket;
import java.io.*;
import bri.ServiceDEV;
import bri.ServeurBRi;
import bri.Service;
import personnes.*;

public class ServiceConnexion implements Service {

	private final Socket client;
	
	public ServiceConnexion(final Socket socket) {
		this.client = socket;
	}

	public void run() {
		try {
			System.out.println("Rentré dans ServiceConnexion");
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Identifiant : ");
			String id = (String) in.readLine();
			out.println("Mot de passe : ");
			String pass = (String) in.readLine();

			try {
				Personne p = ServeurBRi.getDev(id, pass);
				new ServiceDEV(client, p).run();
			} catch(Exception e) {
				out.println(e.toString());
			}
				
		}
		catch (IOException e) {
		}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Connexion";
	}

		// lancement du service
		public void start() {
			(new Thread(this)).start();		
		}
}