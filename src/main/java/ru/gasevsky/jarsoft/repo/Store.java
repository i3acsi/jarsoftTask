//package ru.gasevsky.jarsoft.repo;
//
//import lombok.AllArgsConstructor;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@AllArgsConstructor
//@Component
//class Store {
//    private final SessionFactory sessionfactory;
//
//    public <T> T tx(final Function<Session, T> command) {
//        T result = null;
//        Transaction tx = null;
//        try (Session session = sessionfactory.openSession()) {
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
//}
