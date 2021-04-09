import org.junit.Test;
import java.io.FileNotFoundException;
import static org.junit.Assert.assertEquals;

public class TestNapoleon {

    @Test
    public void testGetWinChance() throws FileNotFoundException {
        BoardAPI board = new Board();
        PlayerAPI player = new Player(0);
        Napoleon napoleon = new Napoleon(board, player);
        assertEquals(41.18, napoleon.winChance(1, 1), 0);
        assertEquals(75.23, napoleon.winChance(2, 1), 0);
        assertEquals(71.28, napoleon.winChance(30, 29), 0);
    }
}
