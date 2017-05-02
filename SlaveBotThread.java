/**
 *
 * @author pankaj
 */
import java.io.*;
//import java.util.*;
import java.lang.*;
import java.net.*;
//import java.text.*;

public class SlaveBotThread extends Thread {
    
    private Socket socket;
    private String argument;
    private String path;
    private String hostName;
    private int port;
    URL url;
    URLConnection urlConn;
    HttpURLConnection conn;
    BufferedReader in;
      
    public SlaveBotThread(String host,int port){
        try{
        this.hostName=host;
        this.port=port;
        this.socket=new Socket(host,port);
        System.out.println("created...thread");
        System.out.println("Connected target socket");
        }catch(Exception e){}
    }
    public SlaveBotThread(String host,int port,String keepalive){
        try{
            this.hostName=host;
            this.port=port;
            this.socket=new Socket(host,port);
            socket.setKeepAlive(true);
            System.out.println("created...thread");
            System.out.println("Connected target socket keepalive");
        }catch(Exception e){}
    }
    
    public SlaveBotThread(String urlString, String host, int port){
        try{
            this.url=new URL(urlString);
            urlConn=url.openConnection();
            this.hostName=url.getHost();
            this.port=url.getDefaultPort();
            //System.out.println(this.hostName+" "+this.port);
            conn=(HttpURLConnection) urlConn;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            System.out.println("created...thread");
            System.out.println("Connected target url");
        }catch(Exception e){}
    }
    
    @Override
            
    public void run(){
        sendRequest();
    }
    
    public void setPath(String path){
        this.path=path;
    }
    
    public Socket getSock(){
       return socket;
    }
    
    public HttpURLConnection getconn(){
       return conn;
    }
    
    public String gethost(){
       return hostName;
    }
    
    public int getport(){
       return port;
    }
    
    public void sendRequest(){
        try{
            String t;
            while((t = in.readLine()) != null) System.out.println(t);
            in.close();
        }catch(Exception e){System.out.println(e);}
    }
    
    public void disconnect(){
        try{
            if(socket.isBound()){
                socket.close();
                System.out.println("Disconnected target socket");
            }
            else{
                conn.disconnect();
                System.out.println("Disconnected target url");
            }
        }catch(Exception e){conn.disconnect(); System.out.println("Disconnected target url");}
    }   
 
}
