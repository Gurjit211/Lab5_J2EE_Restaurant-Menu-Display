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

@WebServlet("/addMenu")
public class AddMenuServlet extends HttpServlet {

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
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        MenuItem newItem = new MenuItem(name, description, price);

        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(newItem);
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        response.sendRedirect("menu.jsp");
    }

    @Override
    public void destroy() {
        factory.close();
    }
}
