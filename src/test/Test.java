package test;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.Buffer;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.Data;



public class Test {
    public static void main(String[] args) throws Exception {
    	JFrame jFrame=new JFrame();
    	jFrame.setSize(500,500);
    	BufferedImage bImage= ImageIO.read(new File("111.jpg"));
    	//BufferedImage bi=new BufferedImage(bImage.getWidth()/2, bImage.getHeight()/2, bImage.getType());
    	//Graphics g=bi.getGraphics();
    	//g.drawImage(bImage, 0, 0, bi.getWidth(),bi.getHeight(), 0, 0, bImage.getWidth(), bImage.getHeight(), null);
    	long time1=System.currentTimeMillis();
        Image bi= bImage.getScaledInstance(bImage.getWidth()/5*4, bImage.getHeight()/5*4, Image.SCALE_SMOOTH  );
        System.out.println(System.currentTimeMillis()-time1);
        BufferedImage bi2=new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_INT_BGR);
        time1=System.currentTimeMillis();
        Image bi1= bi.getScaledInstance(bImage.getWidth(),bImage.getHeight(), Image.SCALE_SMOOTH  );
        bi2.getGraphics().drawImage(bi1, bImage.getWidth(), bImage.getHeight(), null);
        System.out.println(System.currentTimeMillis()-time1);
    	jFrame.add(new JLabel(new ImageIcon(bi2)));
    	jFrame.pack();
    	jFrame.setVisible(true);
    }
    
}