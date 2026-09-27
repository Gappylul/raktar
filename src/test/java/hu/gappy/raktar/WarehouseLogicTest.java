package hu.gappy.raktar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseLogicTest {

    private WarehouseLogic logic;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        logic = new WarehouseLogic();
    }

    @Test
    void testAddToList() {
        logic.addToList(1, "csavar");
        logic.addToList(2, "zokni");

        assertEquals(1, logic.getList1().size());
        assertEquals("csavar", logic.getList1().getFirst());
        assertEquals(1, logic.getList2().size());
        assertEquals("zokni", logic.getList2().getFirst());
    }

    @Test
    void testMoveItem() {
        logic.addToList(1, "csavar");
        logic.addToList(1, "kalapács");

        String moved = logic.moveItem(1, 0);

        assertEquals("csavar", moved);
        assertEquals(1, logic.getList1().size());
        assertEquals("kalapács", logic.getList1().getFirst());

        assertEquals(1, logic.getList2().size());
        assertEquals("csavar", logic.getList2().getFirst());
    }

    @Test
    void testDeleteItem() {
        logic.addToList(2, "cipő");
        logic.addToList(2, "sapka");

        logic.deleteItem(2, 0);

        assertEquals(1, logic.getList2().size());
        assertEquals("sapka", logic.getList2().getFirst());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        Path tempFile = tempDir.resolve("test_raktar.dat");
        logic.setDataFile(tempFile);

        logic.addToList(1, "csavar");
        logic.addToList(2, "zokni");
        logic.addToList(2, "sapka");

        logic.saveToFile();
        assertTrue(Files.exists(tempFile));

        WarehouseLogic logic2 = new WarehouseLogic();
        logic2.setDataFile(tempFile);
        logic2.loadFromFile();

        assertEquals(1, logic2.getList1().size());
        assertEquals("csavar", logic2.getList1().getFirst());

        assertEquals(2, logic2.getList2().size());
        assertEquals("zokni", logic2.getList2().get(0));
        assertEquals("sapka", logic2.getList2().get(1));
    }
}