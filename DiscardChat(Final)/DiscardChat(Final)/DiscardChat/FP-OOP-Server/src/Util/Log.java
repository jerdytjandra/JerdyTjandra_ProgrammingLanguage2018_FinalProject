/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author Georgius
 */
public class Log {
    
    /**
     * 
     * @param msg 
     */
    public static void print(String msg){
        System.out.println("LOG: " + msg);
    }
    
    /**
     * 
     * @param msg 
     */
    public static void error(String msg){
        System.out.println("ERROR: " + msg);
    }
}
