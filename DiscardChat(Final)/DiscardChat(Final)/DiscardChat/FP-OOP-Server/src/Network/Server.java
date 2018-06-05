/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Model.User;
import Util.Log;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *  Final Project Programming Languages
 * @author Georgius, Vincent, Jerdy
 */
public class Server {
    
    /**
     * Integer Port For Server
     */
    private static int PORT;
    
    /**
     * Server Object
     */
    private static ServerSocket server;
    
    /**
     * Maintains All Users
     */
    private static ArrayList<User> users;
    
    /**
     * Temporary ID Maker
     */
    private int id;
    
    /**
     * Constructor
     * @param PORT 
     */
    public Server(int PORT)
    {
        this.PORT = PORT;
        this.users = new ArrayList<User>();
        this.id = 1;
    }
    
    /**
     * Initialize Server
     */
    public void initialize()
    {
        Log.print("Server Initializing...");

        try
        {
            server = new ServerSocket(this.PORT);
            Log.print("Server Initialized...");
            
        }catch(Exception e){
            Log.error(e.getMessage());
        }
    }
    
    /**
     * Start Server
     */
    public void start()
    {
        Log.print("Server Starting...");
        
        while(true){
            
            try
            {
                Socket temp = server.accept();
                
                Log.print("User Connecting....");
                Log.print(temp.getInetAddress().getHostAddress() + " has connected...");
                
//                User user = new User(temp, this.id, "USER " + Integer.toString(id));
                User user = new User(temp, this.id, temp.getInetAddress().getHostName());
                users.add(user);
                
                Thread background = new Thread(new BackgroundHandler(user));
                Log.print("User and Background Thread have been initialized");
                
                background.start();
                Log.print("Background Thread started");
                
                this.id++;
                
            }catch(Exception e){
                Log.error(e.getMessage());
            }
        }
    }

    /**
     * User List Getter
     * @return 
     */
    public static ArrayList<User> getUsers() {
        return users;
    }
}
