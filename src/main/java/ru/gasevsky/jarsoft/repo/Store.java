//package ru.gasevsky.jarsoft.repo;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.function.Function;
//
//class Store {
//    private static final Logger log = LoggerFactory.getLogger(Store.class);
//
//    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//            .configure().build();
//    private final SessionFactory sf = new MetadataSources(registry)
//            .buildMetadata().buildSessionFactory();
//
//    Session openSession() {
//        return sf.openSession();
//    }
//
//    <T> List<T> findAll(Class<T> cl) {
//        return tx(session -> session.createQuery("from " + cl.getName(), cl).list());
//    }
//
//
//    <T> long save(T model) {
//        return tx(session -> (Long) session.save(model));
//    }
//
//    <T> void update(T model) {
//        tx(session -> {
//                    session.update(model);
//                    return null;
//                }
//        );
//    }
//
//    <T> void delete(Long id, Class<T> cl) {
//        T item = findById(id, cl);
//        tx(session -> {
//                    session.delete(item);
//                    return null;
//                }
//        );
//    }
//
//    @SuppressWarnings("unchecked")
//    <T> T findById(Long id, Class<T> cl) {
//        return tx(session -> (T) session.get(cl.getName(), id));
//    }
//
//    @SuppressWarnings("unchecked")
//    <T> T findBy(Object fieldValue, String fieldName, Class<T> cl) {
//        return tx(session ->
//                (T) session.createQuery("FROM " + cl.getSimpleName() + " WHERE " + fieldName + "=:field")
//                        .setParameter("field", fieldValue).uniqueResult()
//        );
//    }
//
//    <T> T tx(final Function<Session, T> command) {
//        T result = null;
//        Transaction tx = null;
//        try (Session session = sf.openSession()) {
//            tx = session.beginTransaction();
//            result = command.apply(session);
//            tx.commit();
//        } catch (final Exception e) {
//            e.printStackTrace();
//            if (tx != null)
//                tx.rollback();
//            throw e;
//        }
//        return result;
//    }
//
//
//    @SuppressWarnings("unchecked")
//    <T> T findOrCreate(Class<T> cl, T entity, String fieldName, Object fieldValue) {
//        return tx(session -> {
//            T result = (T) session.createQuery("FROM " + cl.getSimpleName() + " WHERE " + fieldName + "=:field")
//                    .setParameter("field", fieldValue).uniqueResult();
//            if (result == null) {
//                session.persist(entity);
//                result = entity;
//            }
//            return result;
//        });
//    }
//
//    <T> T persist(T entity) {
//        return tx(session -> {
//            session.persist(entity);
//            return entity;
//        });
//    }
//
//}
