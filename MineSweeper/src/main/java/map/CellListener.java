package map;
import map.cell.Cell;
/**
 *
 * @author Lampirg
 */
public interface CellListener {
    void explode();
    void flag(Cell cell);
    void unflag(Cell cell);
    void firstOpen(Cell cell);
    void openNextZeroes(Cell zero);
}
