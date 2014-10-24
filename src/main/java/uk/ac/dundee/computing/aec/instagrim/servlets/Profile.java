/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;
import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.servlet.http.Part;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Fury
 */
@WebServlet(name = "Profile", 
        urlPatterns = {"/Profile/*",
                        "/Profile"})

public class Profile extends HttpServlet{
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    public Profile(){
        super();
        CommandsMap.put("Profile/*",1);
    }
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        
        
      
                String user = args[2];
                DisplayProfile(user, request, response);
            
        
        
        
    }
    
    
    private void DisplayProfile(String user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User u = new User();
        u.setCluster(cluster);
        java.util.LinkedList<String> info = u.getProfile(user);
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
        request.setAttribute("info", info);
        rd.forward(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
        private void error(String mess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setAttribute("error", mess);
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response); 
        }
    


}
