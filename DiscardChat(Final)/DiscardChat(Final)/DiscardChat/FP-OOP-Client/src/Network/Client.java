/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Util.Log;
import Model.ClientFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Georgius, Vincent, Jerdy
 */
public class Client implements Runnable{
    
    /**
     * Connect Client to Server
     */
    private Socket socket;
    
    /**
     * Socket Input
     */
    private InputStream IS;
    
    /**
     *  Way For Receiving Message From Each Client
     */
    private DataInputStream input;
    
    /**
     *  A Way Made Specifically For Receiving Online Request
     */
    private ObjectInputStream objectin;
    
    /**
     * Socket Output
     */
    private OutputStream OS;
    
    /**
     * Every New Message Written Will be Sent Through Here
     */
    private DataOutputStream output;
    
    /**
     * Way to save file
     */
    private FileOutputStream fos;
    
    /**
     * Type of Messages
     */
    private String message;
    
    /**
     * Scope of the Messages
     */
    private String messagescope = "GLOBAL";
    
    /**
     * Initialize Client Socket and I/O
     */
    public Client(){
        try{
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress("10.25.150.58", 8000), 10000);
            
        }catch(Exception e){
            Log.error("Client failed to initialize");
        }
        
        try{
            this.input = new DataInputStream(this.IS = this.socket.getInputStream());
            this.output = new DataOutputStream(this.OS = this.socket.getOutputStream());
            this.objectin = new ObjectInputStream(this.IS);
        }catch(Exception ex){
            Log.error("I/O failed");
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public DataInputStream getInput() {
        return this.input;
    }

    public DataOutputStream getOutput() {
        return this.output;
    }
    
    public OutputStream getOS() {
        return this.OS;
    }

    public InputStream getIS() {
        return this.IS;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessagescope() {
        return messagescope;
    }

    public void setMessagescope(String messagescope) {
        this.messagescope = messagescope;
    }
    
    /**
     * Start Client
     */
    @Override
    public void run(){
        try{
            while(true){
                this.message = input.readUTF();
                
                switch(message){
                    case "SEND_MESSAGE":
                        //show message to GUI
                        ClientFrame.msgarea.append(input.readUTF() + "\n");
                        break;
                        
                    case "SEND_FILE":
                        Log.print("PACKET 1");
                        
                        String file_name = input.readUTF();
                        Log.print("PACKET 2");
                        
                        int size = Integer.parseInt(input.readUTF());
                        Log.print("PACKET 3");
                        
                        Log.print("size: " + (Integer.toString(size)));
                        byte[] file = new byte[size];
                        
                        for(int x = 0; x < size; x++){
                            file[x] = input.readByte();
                        }
                        this.fos = new FileOutputStream(file_name);
                        this.fos.write(file);
                        this.fos.close();
                        Log.print("FILE SUCCESSFULLY RECEIVED");
                        break;
                    
                    case "ONLINE":
//                        Log.print("ONLINE");
                        int length = Integer.parseInt(this.input.readUTF());
//                        Log.print("Get length");
                        ClientFrame.onarea.setText("");
                        
                        for(int i = 0; i < length; i++){
                            //add element from onlist to ClientUserFrame
                            String name = this.input.readUTF();
                            ClientFrame.onarea.append(name + "\n");
                        }
//                        Log.print("Successfully printed all values");
                }
            }
        }catch(Exception e){
            Log.error(e.getMessage());
        }
    }
}
