package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import static javax.swing.Spring.height;
import static javax.swing.Spring.width;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void insertPic(byte[] b, String type, String name, String user) {
        try {
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }
    
    public boolean deletePic(java.util.UUID picid, String user){
        try{
            Session session = cluster.connect("instagrim");

            PreparedStatement psTime = session.prepare("SELECT interaction_time, user FROM pics WHERE picid = ?");
            PreparedStatement psDeletePic = session.prepare("DELETE FROM pics WHERE picid = ?");
            PreparedStatement psDeletePicList = session.prepare("DELETE FROM userpiclist WHERE user = ? AND pic_added = ?");
            BoundStatement bsTime = new BoundStatement(psTime);
            BoundStatement bsDeletePic = new BoundStatement(psDeletePic);
            BoundStatement bsDeletePicList = new BoundStatement(psDeletePicList);
            
            ResultSet rs = session.execute(bsTime.bind(picid));
            Date dateAdd = new Date();
            String owner = "";
            if (rs.isExhausted()) 
            {
                session.close();
                return false;
            } 
            else 
            {
                for (Row row : rs) 
                {
                    dateAdd = row.getDate("interaction_time");
                    owner = row.getString("user");
                }
            }
            if (owner.equals(user))
            {
                session.execute(bsDeletePic.bind(picid));
                session.execute(bsDeletePicList.bind(user, dateAdd));
                session.close();
                return true;
            }
            session.close();
            return false;
        }
        catch(Exception ex){
           return false;
        }      
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] redPic(byte[] byteArray, String type){
        byte[] imageInByte = null;
        try{
            // I suspect type should be types[1].... ;)
            String types[]=Convertors.SplitFiletype(type);
            System.out.println("Filter.. type: " + type);
            InputStream input = new ByteArrayInputStream(byteArray);
            BufferedImage original = ImageIO.read(input); // dimensions width x height, black on transparent

            for (int x = 0; x < original.getWidth(); x++)
            {
                for (int y = 0; y < original.getHeight(); y++)
                {
                    int rgba = original.getRGB(x,y);
                    Color col = new Color (rgba, true);
                    col = new Color (col.getRed(), col.getGreen() - (col.getGreen()/2), col.getBlue() - (col.getBlue()/2));
                    original.setRGB(x, y, col.getRGB());
                }
            }
//            int width = original.getWidth();
//            int height = original.getHeight();
//            // create red image
//            BufferedImage redVersion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            // I also suspect this is actually drawing the image.. which is wrong..-.-
            
//            Graphics2D g = (Graphics2D) redVersion.getGraphics();
//            g.setColor(Color.red);
//            g.fillRect(0, 0, width, height);
//
//            // paint original with composite
//            g.setComposite(AlphaComposite.DstIn);
//            g.drawImage(original, 0, 0, width, height, 0, 0, width, height, null);
            
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(original, types[1], bs);
            bs.flush();
            imageInByte = bs.toByteArray();

            bs.close();
            input.close();
        }
        catch(IOException et){
            
        }
        return imageInByte;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);

            }
        }
        return Pics;
    }

    public Pic getPic(int image_type, java.util.UUID picid) throws IOException {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE || image_type == Convertors.MAKE_RED) {
                System.out.println("Getting image...");
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE || image_type == Convertors.MAKE_RED) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        
        if(image_type == Convertors.MAKE_RED){
            System.out.println("Applying make red...");
            byte[] tp = new byte[bImage.remaining()];
            bImage.get(tp);
            tp = redPic(tp, type);    
            bImage = ByteBuffer.wrap(tp);
        }
        p.setPic(bImage, length, type);

        return p;

    }

}
