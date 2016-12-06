/*
Harshil Parikh
Mr. Wilhelm
ICS 4UI
March 16th, 2016
*/
package gameoflife;

import java.io.*;
import java.util.Scanner;
import java.awt.*; //needed for graphics
import javax.swing.*; //needed for graphics
import static javax.swing.JFrame.EXIT_ON_CLOSE; //needed for graphics

public class GameOfLife_RunFile extends JFrame{
    
    //FIELDS
    static int numGenerations = 200;
    static int currGeneration = 0;
    
    Color aliveColor = Color.YELLOW;
    Color deadColor = Color.BLUE;
    
    String fileName = "Initial cells.txt";

    int width = 800; //width of the window in pixels
    int height = 800;
    int borderWidth = 50;

    int numCellsX = 30; //width of the grid (in cells)
    int numCellsY = 30;

    //All values of boolean array are set to false when initialized
    boolean alive[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION 
    boolean aliveNext[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION
    
    int cellWidth = (width-2*borderWidth)/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
     
    int labelX = width / 2 - 40;
    int labelY = borderWidth;

    //reads the first generations' alive cells from a file
    public void declareFirstGenerationFromFile() throws IOException {
        FileReader f = new FileReader(fileName);
        Scanner s = new Scanner(f);

        int x, y;

        while ( s.hasNext() ) {
            y = s.nextInt();
            x = s.nextInt();
            alive[x][y] = true;
        }
    }
    
    //Applies the rules of The Game of Life to set the true-false values of the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {
        for (int i = 0; i < alive.length; i++){
            for (int j = 0; j < alive[i].length; j++){
                int aroundBlocks = countLivingNeighbors(i, j);
                if (alive[i][j]){
                    switch(aroundBlocks){
                        case 2:
                        case 3:    
                            aliveNext[i][j] = true;
                            break;
                        default:
                            aliveNext[i][j] = false;
                            break;
                    }
                }
                else if(aroundBlocks == 3){
                    aliveNext[i][j] = true;
                }
            }
        }
    }
    
    //Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        for (int i = 0; i < alive.length; i++){
            System.arraycopy(aliveNext[i], 0, alive[i], 0, alive[i].length);
        }
    }
    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int i, int j) {
        int a, b;
        int aroundBlocks = 0;
        int[][] aIJ = new int[][]{
           // i  j 
            {-1,-1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
        };
        
        for (int[] aIJ1 : aIJ) {
            a = aIJ1[0];
            b = aIJ1[1];
            if (a+i > -1 && a+i < alive.length &&
                    b+j > -1 && b+j < alive[i].length){
                if (alive[a+i][b+j]){
                    aroundBlocks++;
                }
            }
        }
        return aroundBlocks;
    }
    
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }
    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        g.setColor(Color.black);
        g.fillRect(0, 0, width, borderWidth);
        g.setColor(aliveColor);
        g.drawString("Generation: " + state, labelX, labelY);
    }
    
    //Draws the current generation of living cells on the screen
    @Override
    public void paint( Graphics g ) {                
        drawLabel(g, currGeneration);
        
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                if (alive[i][j]){
                    g.setColor(aliveColor);
                }
                else{
                    g.setColor(deadColor);
                }
                g.fillRect(borderWidth+j*cellWidth, borderWidth+i*cellWidth, cellWidth, cellWidth);
                g.setColor(Color.white);
                g.drawRect(borderWidth+j*cellWidth, borderWidth+i*cellWidth, cellWidth, cellWidth);
            }
        }
        computeNextGeneration();
        plantNextGeneration();
    }
    
    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(height, width);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.black);
        setVisible(true); //calls paint() for the first time
    }

    public static void main(String[] args) throws IOException{
        GameOfLife_RunFile currGame = new GameOfLife_RunFile();
        
        currGame.declareFirstGenerationFromFile(); //Sets the initial generation of living cells, either by reading from a file or creating them algorithmically
        currGame.initializeWindow();
        
        for (int i = 1; i <= numGenerations; i++) {
            currGeneration++;
            GameOfLife_RunFile.sleep( 150 );
            currGame.repaint();
        }
    }
}
