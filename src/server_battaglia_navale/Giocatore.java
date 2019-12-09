/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_battaglia_navale;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author informatica
 */
public class Giocatore implements Runnable{
    
    /**
     * Scrive al server
     */
    PrintWriter output;
    
    /**
     * 
     */
    Scanner input;
    private Socket socket;
    private Giocatore opponente;

    @Override
    public void run() {
        Setup();
    }
    
    private void Setup () throws IOException
    {
        input = new Scanner (socket.getInputStream());
    }
}
