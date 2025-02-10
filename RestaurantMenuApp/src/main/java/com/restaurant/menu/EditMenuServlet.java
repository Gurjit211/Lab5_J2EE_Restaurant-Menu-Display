package com.restaurant.menu;

import com.restaurant.menu.model.MenuItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/editMenu")
public class EditMenuServlet extends HttpServlet {

    private SessionFactory factory;

    @Override
    public void init() throws ServletException {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MenuItem.class)
                .buildSessionFactory();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("id"));
        String newName = request.getParameter("name");
        String newDescription = request.getParameter("description");
        double newPrice = Double.parseDouble(request.getParameter("price"));

        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            MenuItem itemToUpdate = session.get(MenuItem.class, itemId);
            if (itemToUpdate != null) {
                itemToUpdate.setName(newName);
                itemToUpdate.setDescription(newDescription);
                itemToUpdate.setPrice(newPrice);
                session.update(itemToUpdate);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void destroy() {
        factory.close();
    }
}
