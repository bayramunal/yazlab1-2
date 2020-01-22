
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class ServerLocation {
    int x, y, row, column;
    boolean isUsed = false;
    boolean forbidden = false;
   
    
    public ServerLocation (int x, int y, int row, int column) {
        this.x = x; 
        this.y = y;
        this.row = row;
        this.column = column;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

    
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    
    
    
}


