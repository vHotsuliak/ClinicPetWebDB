package com.servlets;

import com.models.Client;
import com.store.UserCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.models.CreatePet.createPet;

public class EditClientServlet extends HttpServlet {
    private final UserCache USER_CACHE = UserCache.getInstance();
    private Integer id;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.USER_CACHE.edit(new Client(this.id, request.getParameter("clientName"), createPet(request)));
        response.sendRedirect(String.format("%s%s", request.getContextPath(), "/client/view"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        id = Integer.valueOf(request.getParameter("id"));
        request.setAttribute("client", USER_CACHE.get(id));
        request.getRequestDispatcher("/views/client/EditClient.jsp").forward(request,response);
    }

    @Override
    public void destroy() {
        super.destroy();
        USER_CACHE.close();
    }
}
