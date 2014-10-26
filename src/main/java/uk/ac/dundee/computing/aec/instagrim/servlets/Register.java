/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.InstagrimTL.servlets;

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
import uk.ac.dundee.computing.aec.InstagrimTL.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.InstagrimTL.models.User;

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
        
        String first_name=request.getParameter("first_name");
        if (first_name == ""){
            error("Enter your first name to complete registration process", request, response);
        }
         else if (first_name.length() < 2 || first_name.length() > 15){
            error("Your name should not be less than 2 or more than 15 char long", request, response);
        }
        String last_name=request.getParameter("last_name");
        if (last_name == ""){
            error("Enter your last name to complete registration process", request, response);
        }
         else if (last_name.length() < 4 || last_name.length() > 15){
            error("Your last name should not be less than 4 or more than 15 char long", request, response);
        }
        
        if (password.equals(confirm_password)){
            User us=new User();
            us.setCluster(cluster);
            boolean checkReg = us.RegisterUser(username, password, confirm_password, email, first_name, last_name);
            
            if (checkReg == false){
                error("Registration failed, username already exists", request, response);
            }
            else{
                response.sendRedirect("/InstagrimTL");
            }
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
