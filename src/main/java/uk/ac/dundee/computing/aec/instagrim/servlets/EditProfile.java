/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.InstagrimTL.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.InstagrimTL.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.InstagrimTL.models.User;
import uk.ac.dundee.computing.aec.InstagrimTL.stores.LoggedIn;

/**
 *
 * @author Fury
 */
@WebServlet(name = "editProfile", urlPatterns = {"/EditProfile",
                                                 "/EditProfile/*",
                                                 "/editprofile/",
                                                 "/editProfile/",
                                                 "/editProfile/*",
                                                 "/Editprofile/*"
                                                 })
public class EditProfile extends HttpServlet {

    Cluster cluster=null;
    private HashMap CommandsMap = new HashMap();
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    public EditProfile(){
        
        CommandsMap.put("EditProfile/*",1);
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
        // This is where it should now go..
        
         User u = new User();
       u.setCluster(cluster);
        System.out.println("Do Post");
        HttpSession session = request.getSession();
        
//        RequestDispatcher rd = request.getRequestDispatcher("/editProfile.jsp");
//       // request.setAttribute("info", info);
//        rd.forward(request, response);
        // You definately don't want that forward
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        if(lg==null){
            error("You're not logged in, " + "error", request, response);
        }
        String user = lg.getUsername();
        String email = "";
        String first_name = "";
        String last_name = "";
        boolean owner;
        email = (String) request.getParameter("new_email");
        first_name = (String) request.getParameter("new_first_name");
        last_name = (String) request.getParameter("new_last_name");
        
        owner = u.checkUserLogin(user, lg);
        if (owner == true){
            System.out.println("Owner is true..");

            if (first_name == ""){
                error("Enter your first name to complete editing process", request, response);
            }
             //   else if (first_name.length() < 2 || first_name.length() > 15){
             //       error("Your name should not be less than 2 or more than 15 char long", request, response);
             //   }
            if (last_name == ""){
                error("Enter your last name to complete editing process", request, response);
            }
           //  else if (last_name.length() < 4 || last_name.length() > 15){
            //    error("Your last name should not be less than 4 or more than 15 char long", request, response);
            //}
            //response.sendRedirect("/InstagrimTL/Profile" + user);
            System.out.println("Validation done without errors");
        }
        else{
            error("You can edit only your profile!", request, response);
        }
        System.out.println("Call edit profile method");
        u.editProfile(user, email, first_name, last_name);
        
        
        java.util.LinkedList<String> info = u.getProfile(user);
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
        request.setAttribute("info", info);
        rd.forward(request, response);       
        
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
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
        System.out.println("Error: " + mess);
        request.setAttribute("error", mess);
        RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);    
        return;     
    }
}
