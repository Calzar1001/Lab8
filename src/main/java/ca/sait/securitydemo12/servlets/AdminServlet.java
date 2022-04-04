package ca.sait.securitydemo12.servlets;

import ca.sait.securitydemo12.models.Role;
import ca.sait.securitydemo12.models.User;
import ca.sait.securitydemo12.services.AccountService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // just by going to the login page the user is logged out :-) 

        getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Varibles to store user credientials 
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //Returns a user from the DB based on credientials
        AccountService as = new AccountService();
        User user = as.login(email, password);



        // If assisicoitaed user in the DB then send user back to the login page.
        if (user == null) {
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        /* Stores info about user across more then one page request.
         * I belive this is where the Authenticationfilter is called 
         * Get the user role
         */
        int roleId = user.getRole().getRoleId();
        
        HttpSession session = request.getSession();
        session.setAttribute("roleID", roleId);

        //This is the redirect based on the user information in the 
        if (user.getRole().getRoleId() == 1) {
            response.sendRedirect("admin");
        } else {
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);

        }
    }
}
