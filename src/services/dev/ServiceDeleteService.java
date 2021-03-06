package services.dev;
import java.net.Socket;
import java.io.*;

import bri.Service;
import bri.ServiceDEV;
import bri.ServiceRegistry;
import personnes.Personne;

public class ServiceDeleteService implements Service {

	/**
	 * Supprime un service
	 * @param socket Le socket du developpeur
	 * @param dev Le developpeur client
	 */
	
    private final Socket client;
    private final Personne dev;
	
	public ServiceDeleteService(final Socket socket, Personne dev) {
        this.client = socket;
        this.dev = dev;
	}

	public void run() {
		try {
            System.out.println("Rentré dans ServiceAddService");
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			try {
                out.println("Veuillez entrer le nom de votre classe, \"stop\" pour revenir à l'accueil");
                String classeName = (String) in.readLine();
                if(classeName.equals(("stop"))) {
                    new ServiceDEV(client, dev).run();
                } 
                else {

                    ServiceRegistry.deleteService(classeName);
					new ServiceDEV(client, dev).run();
                }
                
            } catch (Exception e) {
                System.out.println(e);
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