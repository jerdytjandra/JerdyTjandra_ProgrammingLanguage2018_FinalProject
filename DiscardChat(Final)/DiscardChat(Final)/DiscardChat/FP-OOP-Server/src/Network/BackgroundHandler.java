/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Model.User;
import Util.Log;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Georgius
 */
public class BackgroundHandler implements Runnable{

    /**
     * Maintain Connected User
     */
    private User user;
    
    /**
     * Accept Client Input
     */
    
    /**
     * Receive User Input
     */
    private String message;
    
    /**
     * Constructor
     * @param user
     */
    public BackgroundHandler(User user){
        this.user = user;
    }
    
    /**
     * Thread Runner
     */
    @Override
    public void run() {
        try{

            while(true){
                this.message = this.user.getInput().readUTF();
                String scope = null;
                switch(message){
                    case "SEND_MESSAGE":
                        scope = this.user.getInput().readUTF();
                        if(scope.equalsIgnoreCase("GLOBAL")){
                            user.broadcastmsg(this.user.getName() + ": " + this.user.getInput().readUTF());
                        }
                        else if(scope.equalsIgnoreCase("PRIVATE")){
                            user.privatemsg(this.user.getInput().readUTF(), this.user.getName(), this.user.getName() + ": " + this.user.getInput().readUTF());
                        }
                        break;
                    
                    case "SEND_FILE":
                        String receiver = null;
                        Log.print("PACKET 1");
                        
                        scope = this.user.getInput().readUTF();
                        if(scope.equals("PRIVATE"))
                        {
                            receiver = this.user.getInput().readUTF();
                        }
                        Log.print("PACKET 2");
                        
                        String file_name = this.user.getInput().readUTF();
                        Log.print("PACKET 3");
                        
                        int size = Integer.parseInt(this.user.getInput().readUTF());
                        Log.print("PACKET 4");
                        
                        Log.print((Integer.toString(size)));
                        byte[] file = new byte[size];
                        
                        for(int x = 0; x < size; x++){
                            
                            file[x] = this.user.getInput().readByte();
                        }
                        Log.print("PACKET 5");
                        
                        
                        if(scope.equalsIgnoreCase("GLOBAL")){
                            user.broadcastfile(size, file_name, file);
                            user.broadcastmsg(this.user.getName() + "has sent a file named: " + file_name);
                        }
                        else if(scope.equalsIgnoreCase("PRIVATE")){
                            user.privatefile(receiver, this.user.getName(), size, file_name, file);
                            Log.print("FILE SENT TO " + receiver);
                            user.privatemsg(receiver, this.user.getName(), this.user.getName() + " has sent a file named: " + file_name);
                        }

                        break;
                    
                    case "ONLINE":
                        user.onlinerequest();
                }
                
            }
        }catch(Exception e){
            Server.getUsers().remove(this.user);
            Log.error(e.getMessage());
        }
    } 
}
