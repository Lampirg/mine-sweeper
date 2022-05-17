import game.Main;
import map.Map;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author Lampirg
 */
public class TestCell {
    @Test(expected = IllegalArgumentException.class)
    public void testMapExceptions() {
        Map map;
        map = new Map(4, 4, 20);
    }
    @Test
    public void testMap() {
        Map map;
        map = new Map(4, 4, 2);
    }
}
