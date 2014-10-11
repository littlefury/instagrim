/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
 import javax.swing.JOptionPane;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }




    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username=request.getParameter("username");
        if (username == ""){
            error("Enter username to register, please", request, response);
        }
        else if (username.length() < 4 || username.length() > 15){
            error("Username should not be less than 4 or more than 15 char long", request, response);
        }
        
        String password=request.getParameter("password");
        if (password == ""){
            error("Enter password to complete registration process", request, response);
        }
         else if (password.length() < 4 || password.length() > 15){
            error("Password should not be less than 4 or more than 15 char long", request, response);
        }
        
        String confirm_password=request.getParameter("confirm_password");
        String email=request.getParameter("email");
        
        
        if (password.equals(confirm_password)){
            User us=new User();
            us.setCluster(cluster);
            us.RegisterUser(username, password, confirm_password, email);
        
            response.sendRedirect("/Instagrim");
        }
        else
        {
            //redirect back to the error page
            error("Passports entered should match", request, response);
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private void error(String mess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", mess);
        RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);    
        return;     
    }
}
