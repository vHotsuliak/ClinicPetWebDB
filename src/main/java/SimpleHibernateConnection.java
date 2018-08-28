import com.store.HibernateStorage;
import com.store.UserCache;

public class SimpleHibernateConnection {
    public static void main(String[] args) {
        final HibernateStorage hibernateStorage = new HibernateStorage();
        try {
            for(Object user : hibernateStorage.values())
            System.out.println("args = [" + user + "]");
        } finally {
            hibernateStorage.close();
        }
    }
}
