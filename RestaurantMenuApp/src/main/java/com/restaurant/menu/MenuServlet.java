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
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private SessionFactory factory;

    @Override
    public void init() throws ServletException {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MenuItem.class)
                .buildSessionFactory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("search");  // Get search query from request
        Session session = factory.getCurrentSession();
        List<MenuItem> menuItems;

        try {
            session.beginTransaction();
            if (searchQuery != null && !searchQuery.isEmpty()) {
                menuItems = session.createQuery("FROM MenuItem WHERE name LIKE :search", MenuItem.class)
                        .setParameter("search", "%" + searchQuery + "%")
                        .getResultList();
            } else {
                menuItems = session.createQuery("FROM MenuItem", MenuItem.class).getResultList();
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("[");
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            out.print("{");
            out.print("\"id\":" + item.getId() + ",");
            out.print("\"name\":\"" + item.getName() + "\",");
            out.print("\"description\":\"" + item.getDescription() + "\",");
            out.print("\"price\":" + item.getPrice());
            out.print("}");
            if (i < menuItems.size() - 1) {
                out.print(",");
            }
        }
        out.print("]");
    }

    @Override
    public void destroy() {
        factory.close();
    }
}
