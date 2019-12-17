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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe di gestione della partita
 * @author gange
 */
public class Game {
    /**
     * Il giocatore che deve eseguire il turno
     */
    private Giocatore GiocatoreCorrente;
    
    /**
     * Costruttore della classe game 
     * @param gio il giocatore che si unisce per primo
     */
    public Game (Giocatore gio) 
    {
        GiocatoreCorrente = gio;
    }
    
    /**
     * Costruttore vuoto
     */
    public Game (){}
    
    /**
     *
     * @param x
     * @param y
     * @param gio
     */
    public synchronized void mossa(int x, int y, Giocatore gio)
    {
        if(gio != GiocatoreCorrente)
        {
            throw new IllegalStateException("Non è il tuo turno");
        } 
        else if (GiocatoreCorrente.getOpponente() == null)
        {
            throw new IllegalStateException("Non è presente nessuno avversario");
        }
        else if (GiocatoreCorrente.getGriglia() [x][y] == 1)
            throw new IllegalStateException("La posizione è già stata bombardata");
        //TODO controllo del colpo se colpito o no e cambio dei turni
    }
    
    
    public class Giocatore implements Runnable{
    
    /**
     * Scrive al server
     */
    PrintWriter output;
    
    /**
     * Input dal giocatore
     */
    Scanner input;
    /**
     * Socket di connessione
     */
    private Socket socket;
    /**
     * Il giocatore avversario
     */
    private Giocatore opponente;
    /**
     * Il nome del giocatore
     */
    private String nomeGiocatore;
    
    /**
     * Il campo di battaglia
     */
    private int [][] griglia = new int[21][21];
    /**
     * Creazione dell'istanza di giocatore
     * @param socket Il socket di connessione
     */
    public Giocatore(Socket socket)
    {
        this.socket = socket;
    }
    
    /**
     * Costruttore vuoto
     */
    public Giocatore() {}

    /**
     * Ritorna il PrintWriter
     * @return 
     */
    public PrintWriter getOutput() {
        return output;
    }

    /**
     * Ritorna lo Scanner
     * @return 
     */
    public Scanner getInput() {
        return input;
    }

    /**
     * Ritorna il socket del
     * @return 
     */
    public Socket getSocket() {
        return socket;
    }

    public Giocatore getOpponente() {
        return opponente;
    }

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }

    public int[][] getGriglia() {
        return griglia;
    }
    
    
    
    /**
     *
     */
    @Override
    public void run() {
        try {
            Setup();
        } catch (IOException ex) {
            Logger.getLogger(Giocatore.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            if(opponente != null && opponente.output != null)
                output.println("L'altro giocatore è uscito");
        }
    }
    
    private void Setup () throws IOException
    {
        
                                                                                                                                                                      input = new Scanner (socket.getInputStream());
        output = new PrintWriter (socket.getOutputStream(), true);
        output.println("Inserisci il tuo nome: ");
        while(true)
        if(input.hasNextLine())
        {
            nomeGiocatore = input.nextLine();
            break;
        }        
        output.println("Ciao " + nomeGiocatore + "!!");
    }
}
}