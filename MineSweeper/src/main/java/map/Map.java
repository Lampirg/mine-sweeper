package map;

import game.Main;
import game.MapListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import map.cell.*;

/**
 *
 * @author Lampirg
 */

public class Map extends javax.swing.JPanel implements CellListener {
    
    private final int height;
    private final int width;
    private final int bombAmount;
    private int flags = 0;
    private int flaggedBombs = 0;
    private Cell[][] matrix;
    private MapListener game;
    private Set<Cell> zeroClaster;
    boolean isStarted = false;
    
    public Map(int height, int width, int bombAmount) {
        if (bombAmount >= height * width - 7)
            throw new java.lang.IllegalArgumentException("too many bombs");
        this.height = height;
        this.width = width;
        this.bombAmount = bombAmount;
        setLayout(new java.awt.GridLayout(height, width));
        zeroClaster = new HashSet<>();
        initializeMatrix();
        addToPanel();
    }
    
    private void addToPanel() {
        for (Cell[] line : matrix)
            for (Cell cell : line) {
                add(cell);
                cell.addCellListener(this);
            }
    }
    
    public void addMapListener(Main game) {
        this.game = game;
    }
    
    private void initializeMatrix() {
        matrix = new Cell[height][width];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = new Cell(new Coordinate(j, i));
    }
    
    private void checkIfWin() {
        if (flaggedBombs == bombAmount) {
                game.setGameState("You Won!");
                lockAndRevealCells();
        }
    }
    
    @Override
    public void explode() {
        game.setGameState("You lost!");
        lockAndRevealCells();
    }
      
    
    
    @Override
    public void openNextZeroes(Cell zero) {
        if (zero.isBombed())
            throw new java.lang.IllegalStateException("bomb is not zero"); 
        for (Cell neighbor : getNextCells(zero)) {
            if (!neighbor.isBombed() && !zeroClaster.contains(neighbor)) {
                zeroClaster.add(neighbor);
                neighbor.setOpened();
            }
        }
    }
     
    private void lockAndRevealCells() {
        for (Cell[] line : matrix)
            for (Cell cell : line) {
                cell.setLocked();
                cell.reveal();
            }
    }
       
    @Override
    public void firstOpen(Cell cell) {
        if (!cell.isNone())
            throw new java.lang.IllegalStateException("already generated");
        GenerateBombs(cell);
    }
    
    private void GenerateBombs(Cell except) {
        Random random = new Random();
        int randomX, randomY, createdBombs = 0;
        while (createdBombs < bombAmount) {
            randomX = random.nextInt(width);
            randomY = random.nextInt(height);
            if (toSkip(randomX, randomY, except))
                continue;
            matrix[randomY][randomX].setBombed();
            createdBombs++;
            if (matrix[randomY][randomX].isFlagged()) {
                flaggedBombs++;
            }
        }
        startCountBombs();
        checkIfWin();
    }
    
    private void startCountBombs() {
        for (Cell[] line : matrix)
            for (Cell cell : line)
                if (!cell.isBombed())
                    countBombs(cell);
    }
      
    private void countBombs(Cell cell) {
        int amount = 0;
        for (Cell neighbor : getNextCells(cell)) {
            if (neighbor.isBombed() && !cell.isBombed())
                   amount++;
        }
        cell.setBombAmount(amount);
    }
    
    private Cell[] getNextCells(Cell cell) {
        final int centreCell = 1;
        Coordinate previous = cell.getCoordinate().getLeftUpCoordinate();
        Coordinate next = cell.getCoordinate().getRightDownCoordinate(height, width);
        final int cellsAmount = Coordinate.getDistance(next, previous);
        Cell[] cells = new Cell[cellsAmount - centreCell];
        int count = 0;
        for (int i = previous.getY(); i <= next.getY(); i++)
            for (int j = previous.getX(); j <= next.getX(); j++) {
                if (matrix[i][j] == cell) {
                    continue;
                }
                cells[count] = matrix[i][j];
                count++;
            }
        return cells;
    }
    
    private boolean toSkip(int randomX, int randomY, Cell except) {
        if (matrix[randomY][randomX].isBombed() || matrix[randomY][randomX] == except)
                return true;
        for (Cell neighbor : getNextCells(except))
            if (matrix[randomY][randomX] == neighbor)
                return true;
        return false;
    }
    
    @Override
    public void flag(Cell cell) {
        flags++;
        game.setFlagAmount(flags);
        if (flags > bombAmount)
            cell.setClosed();
        if (cell.isBombed()) {
            flaggedBombs++;
            checkIfWin();
        }
    }
    
    @Override
    public void unflag(Cell cell) {
        flags--;
        game.setFlagAmount(flags);
        if (cell.isBombed())
            flaggedBombs--;
    }
    
}
