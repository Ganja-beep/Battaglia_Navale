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
     * @param gio il giocatore che si unisce
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
     * Viene impostato il primo giocatore che deve eseguire la mossa
     * @param c 
     */
    public void setGiocatoreCorrente(Giocatore c)
    {
        GiocatoreCorrente = c;
    }
    
    /**
     * Ritorna il giocatore che deve eseguire la mossa
     * @return GiocatoreCorrente
     */
    public Giocatore getGiocatoreCorrente()
    {
        return GiocatoreCorrente;
    }
    
    /**
     * IL giocatore inserisce la posizione da bombardare
     */
    public void InserimentoBombardamento()
    {
        int x = 0, y = 0;
        System.out.println("Turno di " + GiocatoreCorrente.nomeGiocatore);

        
        while(true)
            {
                GiocatoreCorrente.output.println("Inserire la coordinata x del bombardamento: ");
                if(GiocatoreCorrente.input.hasNextInt())
                {
                    x = GiocatoreCorrente.input.nextInt();
                    if (x < 21 && x > -1) {
                    GiocatoreCorrente.output.println("Inserire la coordinata y: ");
                    while (true) {
                        if (GiocatoreCorrente.input.hasNextInt()) 
                        {
                            y = GiocatoreCorrente.input.nextInt();
                            if(y < 21 && y > -1)
                            {
                                mossa(x, y);
                                break;
                            }
                        }
                    }
                }
            }

        }
    }
    
    
    /**
    * Viene chiamata ad ogni mossa di ciascun giocatore
    * @param x
    * @param y
    * @param gio
    */        
    public synchronized void mossa(int x, int y)
    {
        if (GiocatoreCorrente.getOpponente() == null)
        {
            throw new IllegalStateException("Non è presente nessuno avversario");
        }
        else if(GiocatoreCorrente.opponente.getGriglia() [x][y] == 0)
        {
            GiocatoreCorrente.output.println("Acqua...ritenta al prossimo turno");
        }
        else if (GiocatoreCorrente.opponente.getGriglia() [x][y] == 1)
            throw new IllegalStateException("La posizione è già stata bombardata");
        else if (GiocatoreCorrente.opponente.getGriglia() [x][y] == 2)
        {
            //Corrisponde ad una posizione gia bombardata
            GiocatoreCorrente.opponente.getGriglia() [x][y] = 1;
            GiocatoreCorrente.output.println("Hai colpito una nave");
            GiocatoreCorrente.opponente.tot_barche--;
            if(GiocatoreCorrente.opponente.tot_barche == 0)
               FinePartita();
        }
        GiocatoreCorrente = GiocatoreCorrente.opponente;
    }
    
     private void FinePartita()
     {
         GiocatoreCorrente.opponente.output.println("perso");
         GiocatoreCorrente.output.println("vinto");
     }
    
    
    public class Giocatore implements Runnable {
    
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
     * Indica se il setup e' stato completato
     */
    private boolean setup = false;
    
    /**
     * Il campo di battaglia
     */
    private int [][] griglia = new int[21][21];
    
    /**
     * La lista delle navi
     */
    private ArrayList <Nave> ArrayNavi = new ArrayList(7);
    
    /**
     * 
     */
    private int tot_barche = 2;
    
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
     * Viene impostato l'avversario
     * @param opponente il giocatore avversario
     */
    public void setOpponente(Giocatore opponente)
    {
        this.opponente = opponente;
    }
    
    
    /**
     * Viene impostato il socket di comunicazione
     * @param so il socket di comunicazione
     * @return il giocatore da eseguire
     */
    public Giocatore setSocket(Socket so)
    {
         socket = so;
         return this;
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
     * Ritorna il socket del giocatore
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
                griglia[i][j] = 0;                
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
            System.out.println("Errore nell'impostazione della partita");
        } /**finally
        {
            if(opponente != null && opponente.output != null)
                output.println("L'altro giocatore è uscito");
        }        **/
        while(true)
            if(this.isSetup() && opponente.isSetup())
            {
                System.out.println("Inizio partita");
                Game.this.InserimentoBombardamento();
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
        //Il giocatore ha terminato il proprio setup
        setup = true;
        if(ConnessioneGiocatoreAvversario() && opponente.isSetup())
        {

            //Nel caso il secondo giocatore fosse stato bloccato in attesa del secondo giocatore
            synchronized(this)
            {
                output.println("Avvio della partita...");
                this.notifyAll();
            }
        }
        else
        {
            
            try {
                    synchronized(this)
                    {    
                        output.println("Rimani in attesa del secondo giocatore...");
                        this.wait();
                    }
            } catch (InterruptedException e)
            {System.out.println(e.getCause());}
        }
    }
    
    /**
     * Ritorna true se il setup e' finito, altrimenti false
     * @return 
     */
    public boolean isSetup()
    {
        return setup;
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
        ////////////////////////////////////////////////////////////////////////////
        while(l_Posizionamento_Navi < 1)
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

            if (x < 21 && x > -1) {
                output.println("Inserire la coordinata y: ");
                while (true) {
                    if (input.hasNextInt()) {
                        y = input.nextInt();
                        break;
                    }
                    
                }

                if (y < 21 && y > -1) {
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
                        //Chiamata metodo di posizionamento delle navi
                        //Per piazzare la nave successiva
                        //true se piazza correttamente altrimenti false
                        if(posizionamentoNave(ArrayNavi.get(l_Posizionamento_Navi).getLunghezza(),
                        x, y, Orientamento_nave))
                        {
                            l_Posizionamento_Navi++;
                        }
                        Orientamento_nave = "";                        
                        System.out.println(nomeGiocatore + " ha piazzato la nave di lunghezza "
                                + ArrayNavi.get(l_Posizionamento_Navi).getLunghezza()
                                + " nelle posizioni (" + x + ", " + y + ").");


                    }
                }
            }
            
        }
        //Avviso il giocatore che ha finito l'inserimento
        output.println("fine");
        VisualizzazioneFormazione(nomeGiocatore);

    }
    
    /**
     * Visualizza la griglia delle navi del giocatore
     * @param nome il nome del giocatore
     */
    private void VisualizzazioneFormazione(String nome)
    {
        System.out.println("Griglia giocatore: " + nome);
        for (int i = 0; i < 21; i++) {
            System.out.println("");
            for (int j = 0; j < 21; j++) {
                System.out.print(" " + griglia[i][j]);
            }
        }
        System.out.println("");
    }
    
    /**
     * Posiziona per intero la nave
     * @param lunghezzaNave lunghezza della nave
     * @param posX la posizione x scelta dal giocatore 
     * @param posY la posizione y scelta dal giocatore
     * @param Orientamento orientamento scelto dal giocatore
     * @return true se posizionata correttamente, altrimenti false
     */
    private boolean posizionamentoNave(int lunghezzaNave, int posX, int posY, String Orientamento)
    {
        griglia[posX][posY] = 2;
        if(Orientamento.equals("O"))
        {
            //Se la differenza e' maggiore di 0 e' possibile piazzare la nave, perche' altrimenti uscirebbe dalla griglia
            if((posY + lunghezzaNave) < 21)
            {   
                for (int i = 1; i < lunghezzaNave; i++) {
                        posY++;
                        griglia[posX][posY] = 2;
                }
                System.out.println(nomeGiocatore + " nave posizionata correttamente");
                return true;
            }
        }
            else if((posX + lunghezzaNave) < 21)
            {
                for (int i = 1; i < lunghezzaNave; i++) {
                    posX++;
                    griglia[posX][posY] = 2;
                }
                System.out.println(nomeGiocatore + " nave posizionata correttamente");
                return true;
            }
        return false;
    }
    
    /**
     * Controlla se e' presente il secondo giocatore
     */
    private boolean ConnessioneGiocatoreAvversario() 
    {
        try { 
            System.out.println("Connessione giocatore: " + opponente.nomeGiocatore+ " - " + opponente.socket.isConnected());
        }
        catch(NullPointerException e) {
            System.out.println("Il giocatore avversario non si e' connesso");
            return false;
        }
        return opponente.socket.isConnected();
    }


    }
}