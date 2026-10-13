package dao;

/**
 * Deuxieme implementation concrete de l'interface IDao.
 * Simule une autre source de donnees.
 */
public class DaoImpl2 implements IDao {

    @Override
    public double getValue() {
        System.out.println("[DAO2] Recuperation de la valeur depuis une autre source de donnees...");
        return 200.0;
    }
}
