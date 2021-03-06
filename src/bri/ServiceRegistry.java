package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.lang.reflect.*;
import java.net.Socket;

public class ServiceRegistry {
	/**
	 * Gestionnaire de services
	 * @param socket Le socket du developpeur
	 * @param dev Le developpeur client
	 */

	static {
		servicesClasses = new Vector<Class <? extends Service>>();
	}
	private static Vector<Class<? extends Service>> servicesClasses;


	public static void addService(Class<? extends Service> classe, String classeName) throws Exception {
		synchronized(servicesClasses) {
			if (contains(classe))
				throw new Exception("Vous ne pouvez ajoutez deux fois le même service");
			if (normeBri(classe))
				servicesClasses.add(classe);
			System.out.println("size :" + servicesClasses.get(0).getSimpleName());
		}
	}

	public static void deleteService(String classeName) throws Exception {
		synchronized(servicesClasses) {
			Class<? extends Service> tmp = null;
			for(Class<? extends Service> c : servicesClasses) {
				if(c.getSimpleName().equals(classeName))
					tmp = c;
			}
			if(tmp != null) {
				servicesClasses.remove(tmp);
			}
		}
	}

	public static void majService(Class<? extends Service> classe, String classeName) throws Exception {
		synchronized(servicesClasses) {
			deleteService(classeName);
			addService(classe, classeName);
		}
	}

	public static Class<? extends Service> getServiceClass(int numService) {
		return servicesClasses.get(numService - 1);
	}

	public static String toStringue() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < servicesClasses.size(); i++) {
			synchronized (servicesClasses) {
				sb.append(servicesClasses.get(i).getSimpleName() + " [" + (i + 1) + "] | ");
			}
		}
		return sb.toString();
	}

	private static boolean contains(Class<?> service) {
		for (Class<? extends Service> class1 : servicesClasses) {
			if (class1.getName().equals(service.getName()))
				return true;
		}
		return false;
	}

	private static boolean normeBri(Class<?> service) throws Exception {
		return implementsServiceInterface(service) && isPublic(service) && isNotAbstract(service)
				&& hasPublicConstructorWithoutException(service) && hasSocketAttributeFinal(service)
				&& hasStaticToStringueWithoutException(service);

	}

	private static boolean implementsServiceInterface(Class<?> classe) throws Exception {
		for (Class<?> c : classe.getInterfaces()) {
			if (c.getSimpleName().equals("Service")) {
				return true;
			}
		}
		throw new Exception("N'implemente pas interface Service");
	}

	private static boolean isPublic(Class<?> classe) throws Exception {
		if (Modifier.isPublic(classe.getClass().getModifiers())) {
			return true;
		} else {
			throw new Exception("La classe n'est pas public");
		}
	}

	private static boolean isNotAbstract(Class<?> classe) throws Exception {
		if (!Modifier.isAbstract(classe.getModifiers())) {
			return true;
		} else {
			throw new Exception("La classe ne doit pas être abstract");
		}
	}

	private static boolean hasPublicConstructorWithoutException(Class<?> classe) throws Exception {
		for (Constructor<?> c : classe.getConstructors()) {
			if (c.getExceptionTypes().length == 0 && Modifier.isPublic(c.getModifiers())) {
				return true;
			}
		}
		throw new Exception("Pas de public constructeur sans exception");
	}

	private static boolean hasSocketAttributeFinal(Class<?> classe) throws Exception {
		for (Field f : classe.getDeclaredFields()) {
			if (f.getType().isAssignableFrom(Socket.class)) {
				if (Modifier.isFinal(f.getModifiers())) {
					if (Modifier.isPrivate(f.getModifiers())) {
						return true;
					}
				}
			}
		}
		throw new Exception("Pas de socket conforme");
	}

	private static boolean hasStaticToStringueWithoutException(Class<?> classe) throws Exception {
		for (Method m : classe.getMethods()) {
			if (Modifier.isStatic(m.getModifiers()) && m.getName().equals("toStringue")
					&& m.getAnnotatedExceptionTypes().length == 0) {
				return true;
			}
		}
		throw new Exception("Pas de toStringue static sans exception");
	}
}
