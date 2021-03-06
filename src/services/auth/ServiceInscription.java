package services.auth;

import java.net.Socket;
import java.io.*;

import bri.ClientRegistry;
import personnes.*;
import bri.ServiceDEV;
import bri.Service;

public class ServiceInscription implements Service {
	
	/**
	 * Inscrit une personne dev
	 * @param socket Le socket du developpeur
	 */
	private final Socket client;
	
	public ServiceInscription(final Socket socket) {
		this.client = socket;
	}

	public void run() {
		try {
			System.out.println("Rentré dans ServiceInscription");
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Identifiant : ");
			String id = (String) in.readLine();
			out.println("Mot de passe : ");
			String pass = (String) in.readLine();
			out.println("adresse ftp : ");
			String ftp = (String) in.readLine();

			try {
				Personne p = ClientRegistry.addDev(id, pass, ftp);
				new ServiceDEV(client, p).run();
			} catch(Exception e) {
				out.println("fail");
				e.printStackTrace();
			}
				
		}
		catch (IOException e) {
		}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}
	public void start() {
		(new Thread(this)).start();		
	}
}