/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.Log;
import Network.Server;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Georgius, Vincent, Jerdy
 */

public class User {
    
    /**
     * Unique Socket For Every User Connected
     */
    private Socket socket;
    
    /**
     * Every New Message sent to Client Side Will be Sent Through Here
     */
    private DataOutputStream output;
    
    /**
     * A Way Made Specifically For Sending Online Request
     */
    private ObjectOutputStream objectout;
    
    /**
     * Socket Output
     */
    private OutputStream OS;

    /**
     *  Way For Receiving Message From Each Client
     */
    private DataInputStream input;
    
    /**
     * Socket Input
     */
    private InputStream IS;
    
    /**
     * User ID
     */
    private int id;
    
    /**
     * User Name
     */
    private String name;
    
    /**
     * Constructor
     * @param socket 
     * @param id 
     * @param name 
     */
    
    public User(Socket socket,int id, String name){
        this.socket = socket;
        this.id = id;
        this.name = name;
        
        try {
            this.output = new DataOutputStream(this.OS = socket.getOutputStream());
            this.input = new DataInputStream(this.IS = socket.getInputStream());
            this.objectout = new ObjectOutputStream(this.OS);
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
        
    }
    
    public void sendmsg(String msg){
        try {
            this.output.writeUTF("SEND_MESSAGE");
            this.output.writeUTF(msg);
            this.output.flush();
        } catch (IOException ex) {
            Log.error("Can't send message to " + this.name);
        }
    }
    
    /**
     * Function For Broadcasting Message to All Online Users
     * @param msg 
     */
    public void broadcastmsg(String msg){
        Server.getUsers().forEach((user) -> {
            user.sendmsg(msg);
        });
    }
    
    public void privatemsg(String recipient, String sender, String message) {
        Server.getUsers().forEach((user) -> {
            if(user.getName().equalsIgnoreCase(recipient) || user.getName().equalsIgnoreCase(sender)){
                user.sendmsg("PRIVATE " + message);
            }
        });
    }
    
    public void sendfile(int size, String file_name, byte[] file){
        try {
            this.output.writeUTF("SEND_FILE");
            Log.print("PACKAGE SENT 1/4");
            this.output.flush();

            this.output.writeUTF(file_name);
            Log.print("PACKAGE SENT 2/4");
            this.output.flush();

            this.output.writeUTF(Integer.toString(size));
            Log.print("PACKAGE SENT 3/4");
            this.output.flush();

            this.output.write(file);
            Log.print("PACKAGE SENT 4/4");
            this.output.flush();
            
        } catch (Exception ex) {
            Log.error("FILE failed to send to " +this.name);
        }
    }
    
    /**
     * Function For Broadcasting a File to All Online Users
     * @param size
     * @param file_name
     * @param file 
     */
    public void broadcastfile(int size, String file_name, byte[] file){
        Server.getUsers().forEach((user) -> {
            if(!user.getName().equals(this.name)){
                user.sendfile(size, file_name, file);
            }
            
        });
        
    }
    
    public void privatefile(String recipient, String sender, int size, String file_name, byte[] file) {
        
        Server.getUsers().forEach((user) -> {
            if(user.getName().equalsIgnoreCase(recipient)){
                user.sendfile(size, file_name, file);
            }
        });
        
    }
    
    /**
     * Function For Requesting Who's Online 
     */
    public void onlinerequest() {
        int size = Server.getUsers().size();
        
        try {
            this.output.flush();
            this.output.writeUTF("ONLINE");
            this.output.flush();
            this.output.writeUTF(Integer.toString(size));
            this.output.flush();
//            Log.print("Sending the data");
            for(int x = 0; x < Server.getUsers().size(); x++){
                if(Server.getUsers().get(x).getName().equals(this.name)){
                    this.output.writeUTF(Server.getUsers().get(x).getName() + " (You)");
                }
                else{
                    this.output.writeUTF(Server.getUsers().get(x).getName());
                    this.output.flush();
                }
                
                
            }
//            Log.print("Successfully sent");
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
    }

    /**
     * Get User Socket
     * @return Socket 
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set User Socket
     * @param socket 
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Get User ID
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set User ID
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get User Name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Set User Name
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set Output
     * @param output 
     */
    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    /**
     * Get Output
     * @return 
     */
    public DataOutputStream getOutput() {
        return output;
    }

    /**
     * Set Input
     * @param input 
     */
    public void setInput(DataInputStream input) {
        this.input = input;
    }

    /**
     * Get Input
     * @return 
     */
    public DataInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getObjectout() {
        return objectout;
    }
    
    public OutputStream getOS() {
        return OS;
    }

    public void setOS(OutputStream OS) {
        this.OS = OS;
    }

    public InputStream getIS() {
        return IS;
    }

    public void setIS(InputStream IS) {
        this.IS = IS;
    }
}
