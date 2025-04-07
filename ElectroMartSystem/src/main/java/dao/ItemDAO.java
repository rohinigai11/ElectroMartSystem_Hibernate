package dao;


import model.Item;
import util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class ItemDAO {
    // No need to create SessionFactory here anymore
    public void saveItem(Item item) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error saving employee: " + e.getMessage());
        }
    }

    public List<Item> getAllItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Item", Item.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching items: " + e.getMessage());
        }
    }

    public Item getItem(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Item.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching item: " + e.getMessage());
        }
    }

    public void updateItem(Item item) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error updating item: " + e.getMessage());
        }
    }

    public void deleteItem(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Item item = session.get(Item.class, id);
            if (item != null) {
                session.remove(item);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting item: " + e.getMessage());
        }
    }
}