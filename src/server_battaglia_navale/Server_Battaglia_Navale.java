/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_battaglia_navale;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server_battaglia_navale.Game.Giocatore;
/**
 *
 * @author informatica
 */
public class Server_Battaglia_Navale {
    
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(58901)) {
            System.out.println("Il server è in esecuzione...");
            ExecutorService pool = Executors.newFixedThreadPool(2);
            Game game = new Game();
            Giocatore g1 = game.new Giocatore();
            System.out.println("Creato primo giocatore");
            Giocatore g2 = game.new Giocatore();
            System.out.println("Creato secondo giocatore");
            g1.setOpponente(g2);
            g2.setOpponente(g1);
            game.setGiocatoreCorrente(g1);
            while (true) {
                pool.execute(g1.setSocket(listener.accept()));
                pool.execute(g2.setSocket(listener.accept()));
            }            
        }
    }
}