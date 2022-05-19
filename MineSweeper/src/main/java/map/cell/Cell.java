package map.cell;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;
import map.CellListener;

/**
 *
 * @author Lampirg
 */


public class Cell extends javax.swing.JButton {
    
    private Coordinate coordinate;
    private UpperFlag upperFlag;
    private LowerFlag lowerFlag;
    private CellListener map;
    private boolean locked = false;
    
    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        setBackground(Color.GRAY);
        upperFlag = UpperFlag.CLOSED;
        lowerFlag = LowerFlag.NONE;
        //System.out.println(Arrays.toString(LowerFlag.values()));
        setListener();
    }    
    
    public void addCellListener(CellListener cellListener) {
        map = cellListener;
    }
    
    public void reveal() {
        if (isBombed() && isFlagged())
            return;
        setOpened();
    }
    
    public Coordinate getCoordinate() {
        return coordinate;
    }
    
    public boolean isBombed() {
        return lowerFlag == LowerFlag.BOMB;
    }
    
    public boolean isZero() {
        return lowerFlag == LowerFlag.ZERO;
    }
    
    public boolean isNone() {
        return lowerFlag == LowerFlag.NONE;
    }
    
    public boolean isFlagged() {
        return upperFlag == UpperFlag.FLAGGED;
    }
    
    public void setBombAmount(int amount) {
        if (amount > 8 || amount < 0)
            throw new java.lang.IllegalArgumentException("can not be such amount of bombs");
        lowerFlag = LowerFlag.getFlags()[amount];
    }
    
    public void setBombed() {
        lowerFlag = LowerFlag.BOMB;
    }
    
    public void setLocked() {
        locked = true;
    }
    
    public void setFlagged() {
        setBackground(Color.red);
        upperFlag = UpperFlag.FLAGGED;
        map.flag(this);
    }
    
    public void setQuestioned() {
        setBackground(Color.ORANGE);
        if (isFlagged())
            map.unflag(this);
        upperFlag = UpperFlag.QUESTIONED;
    }
    
    public void setClosed() {
        setBackground(Color.GRAY);
        if (isFlagged())
            map.unflag(this);
        upperFlag = UpperFlag.CLOSED;
    }
    
    public void setOpened() {
        if (isFlagged())
            map.unflag(this);
        upperFlag = UpperFlag.OPENED;
        if (lowerFlag == LowerFlag.NONE) {
            map.firstOpen(this);
        }
        if (isBombed()) {
            setExploded();
            return;
        }
        setBackground(Color.WHITE);
        //setFont(new Font("Arial", Font.BOLD, 15));
        setText(Integer.toString(lowerFlag.ordinal()));
        if (lowerFlag == LowerFlag.ZERO)
            map.openNextZeroes(this);
    }
    
    private void setExploded() {
        setBackground(Color.BLACK);
        if (!locked)
            map.explode();
    }
    
    private void leftClick() {
        if (upperFlag == UpperFlag.FLAGGED)
            return;
        setOpened();
    }
    
    private void rightClick() {
        switch (upperFlag) {
            case OPENED:
                return;
            case CLOSED:
                setFlagged();
                break;
            case FLAGGED:
                setQuestioned();
                break;
            case QUESTIONED:
                setClosed();
                break;
        }
        //System.out.println(upperFlag);   
    }
    
    private void setListener() {
        addMouseListener(new MouseAdapter() {
            boolean isPressed;

            @Override
            public void mousePressed(MouseEvent e) {
                if (locked)
                    return;
                getModel().setArmed(true);
                getModel().setPressed(true);
                isPressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (locked)
                    return;
                if (isPressed) {
                    if (SwingUtilities.isLeftMouseButton(e))
                        leftClick();
                    if (SwingUtilities.isRightMouseButton(e))
                        rightClick();
                }
                getModel().setArmed(false);
                getModel().setPressed(false);
                isPressed = false;
            }      
    });
    }
    
}
