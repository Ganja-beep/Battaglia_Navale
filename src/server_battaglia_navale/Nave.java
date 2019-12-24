/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_battaglia_navale;

/**
 *
 * @author gange
 */
public class Nave {
    /**
     * La lunghezza della barca
     */
    private int lunghezza;
    /**
     * La posizione x della barca
     */
    private int x;
    /**
     * La posizione y della barca
     */
    private int y;
    /**
     * Costruttore della nave
     * @param l la lunghezza della barca
     */
    public Nave (int l)
    {
        this.lunghezza = l;
    }
    
    /**
     * Setta la posizione x della barca
     * @param x 
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Setta la posizione y della barca
     * @param y 
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get della lunghezza
     * @return lunghezza della barca
     */
    public int getLunghezza() {
        return lunghezza;
    }
    /**
     * Get della posizione x
     * @return posizione x
     */
    public int getX() {
        return x;
    }
    /**
     * Get della posizione y
     * @return posizione y
     */
    public int getY() {
        return y;
    }
    
    
}
