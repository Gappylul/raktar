package hu.gappy.raktar;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

// VM option --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED

class RaktarAppTest extends ApplicationTest {

    private final Path dataFile = Paths.get("raktar.dat");

    @Override
    public void start(Stage stage) throws Exception {
        new RaktarApplication().start(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        Files.deleteIfExists(dataFile);
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        Files.deleteIfExists(dataFile);
    }

    @Test
    void testGuiAddItems() {
        clickOn("#inputField").write("Kalapács");
        clickOn("#btnRaktar1");

        clickOn("#inputField").write("Szög");
        clickOn("#btnRaktar2");

        ListView<String> lv1 = lookup("#list1").queryListView();
        ListView<String> lv2 = lookup("#list2").queryListView();

        assertTrue(lv1.getItems().contains("Kalapács"), "A Raktár1 listában benne kell lennie a Kalapácsnak");
        assertTrue(lv2.getItems().contains("Szög"), "A Raktár2 listában benne kell lennie a Szögnek");
    }

    @Test
    void testGuiMoveItem() {
        clickOn("#inputField").write("Fúrógép");
        clickOn("#btnRaktar1");

        clickOn("Fúrógép");

        clickOn("#btnRight");

        ListView<String> lv1 = lookup("#list1").queryListView();
        ListView<String> lv2 = lookup("#list2").queryListView();

        assertFalse(lv1.getItems().contains("Fúrógép"), "A Raktár1 listából el kellett tűnnie");
        assertTrue(lv2.getItems().contains("Fúrógép"), "A Raktár2 listában meg kellett jelennie");
    }

    @Test
    void testGuiDeleteItem() {
        clickOn("#inputField").write("Létra");
        clickOn("#btnRaktar2");

        clickOn("Létra");
        clickOn("#btnDel2");

        ListView<String> lv2 = lookup("#list2").queryListView();
        assertFalse(lv2.getItems().contains("Létra"), "A törlés gombnak el kell távolítania a kiválasztott elemet");
    }

    @Test
    void testGuiSave() {
        clickOn("#inputField").write("Méterrúd");
        clickOn("#btnRaktar1");

        clickOn("#btnSave");

        clickOn("OK");

        assertTrue(Files.exists(dataFile), "A mentés gombnak létre kell hoznia a raktar.dat fájlt");
    }
}