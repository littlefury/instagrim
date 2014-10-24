/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password, String confirm_password, String email, String first_name, String last_name){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
           
            EncodedPassword= sha1handler.SHA1(Password);
          
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        
        }
    
        Set<String> emailSet = new HashSet<String>();
        emailSet.add(email);
        
        Session session = cluster.connect("instagrim");
        PreparedStatement psCheck = session.prepare("select login from userprofiles where login =?");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,email,first_name,last_name) Values(?,?,?,?,?)");
       
        BoundStatement boundStatementCheck = new BoundStatement(psCheck);
        BoundStatement boundStatement = new BoundStatement(ps);
        
        ResultSet resultSet = session.execute( // this is where the query is executed
                boundStatementCheck.bind( // here you are binding the 'boundStatement'
                        username));
        
        if (!resultSet.isExhausted()){
            return false;
        }
        else{
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username, EncodedPassword,emailSet,first_name,last_name));
        }
        return true;
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
           
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
   
    
    return false;  
    }
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public java.util.LinkedList<String> getProfile(String user) {
        java.util.LinkedList<String> info = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select * from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        user));
        
        if (rs.isExhausted()) {
            System.out.println("No user returned");
            return null;
        } else {
            for (Row row : rs) {
              //  String ulog;
              //  ulog = row.getString("login");
              //  info.push(ulog);
                String login = row.getString("login");
                
                
                
                Set<String> email = row.getSet("email", String.class);
                String[] EmailArr = email.toArray(new String[0]);
                StringBuffer emailres = new StringBuffer();
                for (int i=0; i< EmailArr.length; i++){
                    emailres.append(EmailArr[i]);
                }
                String uemail = emailres.toString();
                String first_name = row.getString("first_name");
                String last_name = row.getString("last_name");
                info.push(user);
                info.push(uemail);
                info.push(first_name);
                info.push(last_name);
            }
        }
        return info;
       }
    
}

    
