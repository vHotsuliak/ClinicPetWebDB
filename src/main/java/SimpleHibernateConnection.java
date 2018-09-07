import com.store.HibernateStorage;
import com.store.UserCache;

import javax.persistence.EntityManager;

public class SimpleHibernateConnection {
    public static void main(String[] args) {
        final UserCache hibernateStorage = UserCache.getInstance();
        try {
            for(Object user : hibernateStorage.values())
            System.out.println("args = [" + user.toString() + "]");
        } finally {
            hibernateStorage.close();
        }
    }
}
