package dao;

/**
 * Implementation concrete de l'interface IDao.
 * Simule une recuperation de donnees depuis une source externe.
 */
public class DaoImpl implements IDao {

    @Override
    public double getValue() {
        System.out.println("[DAO] Recuperation de la valeur depuis la source de donnees...");
        return 100.0;
    }
}
