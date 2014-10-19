package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.lang.Object;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.persistence.Convert;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*",
        "/Delete/",
        "/Delete/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("Delete", 4);

    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", request, response);
            return;
        }
        try {
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], request, response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2], request, response);
                break;
            case 4:
                DeleteImage(args[2], request, response);
                break;
            default:
                error("Bad Operator", request, response);
        }
        }
        catch (ArrayIndexOutOfBoundsException outofb) {
            error("ArrayIndexOutOfBoundsException", request, response);
        }
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }
    

    private void DisplayImage(int type,String Image, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
  
        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));
        try {
            p = tm.getPic(type,java.util.UUID.fromString(Image));
        }
        catch (IllegalArgumentException illarg) {
            error("IllegalArgumentException", request, response);
        }
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
    
    private void DeleteImage(String picid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        String user = lg.getUsername();

        boolean success;
        success = tm.deletePic(java.util.UUID.fromString(picid), user);  
        if (success == true)
        {
            RequestDispatcher view = request.getRequestDispatcher("/Images/" + user);
            view.forward(request, response);
        }
        else
        {
            error("Image was not found" + success, request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());
            // check that it is an image not more than 200mb
            String type = part.getContentType();
            // Internet media types
            if (!type.equalsIgnoreCase("image/jpeg")){
                error("Unable to upload file of this format", request, response);
            }
            else {
                
                long imSize = part.getSize();
                
                // 200mb max size
                if (209715200L < imSize){
                    error("Image that you are trying to upload is too big", request, response);
                }
                else {
                    String filename = part.getSubmittedFileName();
            
                    InputStream is = request.getPart(part.getName()).getInputStream();
                    int i = is.available();
                    HttpSession session=request.getSession();
                    LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
                    String username="majed";
                    if (lg.getlogedin()){
                        username=lg.getUsername();
                    }
            
                    if (i > 0) {
                        byte[] b = new byte[i + 1];
                        is.read(b);
                        System.out.println("Length : " + b.length);
                        PicModel tm = new PicModel();
                        tm.setCluster(cluster);
                        tm.insertPic(b, type, filename, username);
                
                        is.close();
                    }
                    RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
                    rd.forward(request, response);
                }
            }
        }
        

    }

    private void error(String mess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", mess);
        RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);
        /*
        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
        */
    }
}
