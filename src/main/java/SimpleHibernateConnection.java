import com.models.Client;
import com.models.pets.Pet;
import com.store.HibernateStorage;

import javax.persistence.EntityManager;

public class SimpleHibernateConnection {
    public static void main(String[] args) {
        final HibernateStorage hibernateStorage = new HibernateStorage();
        try {
            for(Object user : hibernateStorage.values())
            System.out.println("args = [" + user.toString() + "]");
            EntityManager entityManager = hibernateStorage.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(new Client(0,"test", new Pet("pet")));
            entityManager.getTransaction().commit();
            entityManager.close();
        } finally {
            hibernateStorage.close();
        }
    }
}
