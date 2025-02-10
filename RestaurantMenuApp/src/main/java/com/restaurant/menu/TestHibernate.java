package com.restaurant.menu;

import com.restaurant.menu.model.MenuItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestHibernate {
    public static void main(String[] args) {
        // Create Hibernate SessionFactory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MenuItem.class)
                .buildSessionFactory();

        // Create session
        Session session = factory.getCurrentSession();

        try {
            // Create a new MenuItem
            MenuItem item = new MenuItem("Pasta Alfredo", "Creamy pasta with Alfredo sauce", 14.99);

            // Start transaction
            session.beginTransaction();

            // Save the menu item
            session.save(item);

            // Commit transaction
            session.getTransaction().commit();

            System.out.println("Menu Item saved successfully!");
        } finally {
            factory.close();
        }
    }
}
