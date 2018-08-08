package com.servlets;

import com.store.UserCache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchClientServlet extends HttpServlet {
    private final UserCache USER_CACHE = UserCache.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!request.getParameter("clientName").isEmpty() || !request.getParameter("petName").isEmpty() || !request.getParameter("kindOfPet").isEmpty())
            request.setAttribute("clients", USER_CACHE.searchClient(request.getParameter("clientName"), request.getParameter("petName"), request.getParameter("kindOfPet")));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/client/SearchClient.jsp");
        dispatcher.forward(request,response);
    }

    @Override
    public void destroy() {
        super.destroy();
        USER_CACHE.close();
    }
}
