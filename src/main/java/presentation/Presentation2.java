package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Presentation2 {
    private static final String CONFIG_FILE = "config.txt";

    public static void main(String[] args) {
        try {
            String[] classNames = loadClassNames(CONFIG_FILE);

            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancie : " + dao.getClass().getName());

            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Metier instancie : " + metier.getClass().getName());

            injectDependency(metier, "setDao", dao);

            double resultat = metier.calcul();
            System.out.println("Resultats = " + resultat);
        } catch (ClassNotFoundException e) {
            System.err.println("Classe introuvable dans config.txt : " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("Constructeur ou methode setDao introuvable : " + e.getMessage());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Erreur pendant l'instanciation ou l'injection : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Impossible de lire " + CONFIG_FILE + " : " + e.getMessage());
        }
    }

    private static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (int i = 0; i < 2; i++) {
                String line = reader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    throw new IOException("ligne " + (i + 1) + " manquante ou vide dans " + fileName);
                }
                classNames[i] = line.trim();
            }
        }
        return classNames;
    }

    private static Object instantiateClass(String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }

    private static void injectDependency(Object target, String setterName, Object dependency)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> targetClass = target.getClass();
        Method setterMethod = targetClass.getMethod(setterName, dependency.getClass().getInterfaces()[0]);
        setterMethod.invoke(target, dependency);
    }
}
