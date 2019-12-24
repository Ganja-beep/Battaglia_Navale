/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_battaglia_navale;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    private String nomeGiocatore = "";
    
    /**
     * Il campo di battaglia
     */
    private int [][] griglia = new int[21][21];
    
    private ArrayList <Nave> ArrayNavi = new ArrayList(7);
    
    /**
     * Creazione dell'istanza di giocatore
     * @param socket Il socket di connessione
     */
    public Giocatore(Socket socket)
    {
        this.socket = socket;
        Riempimentogriglia();
        SetupNavi();
    }
    
    /**
     * Costruttore vuoto
     */
    public Giocatore() 
    {
        Riempimentogriglia();
        SetupNavi();
    }

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
     * Creazioni navi del giocatore
     */
    private void SetupNavi() {
        ArrayNavi.add(new Nave(2));
        ArrayNavi.add(new Nave(2));
        ArrayNavi.add(new Nave(3));
        ArrayNavi.add(new Nave(3));
        ArrayNavi.add(new Nave(4));
        ArrayNavi.add(new Nave(5));
        }
    
    /**
     * Riempie la griglia ad 1 corrispondente all'acqua
     */
    private void Riempimentogriglia()
    {
        for (int i = 0; i < griglia.length; i++) {
            for (int j = 0; j < griglia.length; j++) {
                griglia[i][j] = 1;                
            }
        }
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
    
    /**
     * Si impostano il nome e il posizionamento delle barche
     * @throws IOException 
     */
    private void Setup () throws IOException
    {
        input = new Scanner (socket.getInputStream());
        output = new PrintWriter (socket.getOutputStream(), true);
        
        output.println("Inserisci il tuo nome: ");
        while(true)
        {
            if(input.hasNextLine())
            {
                nomeGiocatore = input.nextLine();
                break;
            }      
        }
        System.out.println("Nome giocatore: " + nomeGiocatore);
        output.println("Ciao " + nomeGiocatore + "!!");
        PiazzamentoNavi();

    }
    /**
     * Si occupa di gestire il piazzamento delle barche.
     * Il giocatore inserisce la coordinate x e y ed in seguito la direzione.
     * La direzione viene espressa come 'O' (orizzontale) e 'V'(verticale).
     */
    private void PiazzamentoNavi()
    {
        int l_Posizionamento_Navi = 0;
        int x = 0;
        int y = 0;
        String Orientamento_nave = "";
        //7 sono le barche che il giocatore deve piazzare
        while(l_Posizionamento_Navi <= 7)
        {

            output.println("Inserire la coordinata x della nave lunga "
                    + ArrayNavi.get(l_Posizionamento_Navi).getLunghezza()
                    + ": ");
            while (true) {
                if (input.hasNextInt()) {
                    x = input.nextInt();
                    break;
                }
            }

            if (x < 21 && x > 0) {
                output.println("Inserire la coordinata y: ");
                while (true) {
                    if (input.hasNextInt()) {
                        y = input.nextInt();
                        break;
                    }
                    
                }

                if (y < 21 && y > 0) {
                    output.println("Inserire orientamento della nave (O/V)");
                    while (Orientamento_nave.isEmpty()) {
                        if (input.hasNextLine()) {
                            Orientamento_nave = input.nextLine();
                        }
                    }
                    Orientamento_nave = Orientamento_nave.toUpperCase();
                    if (Orientamento_nave.equals("O") || Orientamento_nave.equals("V")) {
                        //Salvataggio delle posizioni delle navi
                        ArrayNavi.get(l_Posizionamento_Navi).setX(x);
                        ArrayNavi.get(l_Posizionamento_Navi).setY(y);
                        ArrayNavi.get(l_Posizionamento_Navi).setOrientamento(Orientamento_nave);
                        l_Posizionamento_Navi++;
                        System.out.println("e' stata piazzata la nave di lunghezza "
                                + ArrayNavi.get(l_Posizionamento_Navi).getLunghezza()
                                + " nelle posizioni (" + x + ", " + y + ").");
                    }
                }
            }
            
        }
        output.println("fine");
    }


    }
}