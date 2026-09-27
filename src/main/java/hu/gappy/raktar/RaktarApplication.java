package hu.gappy.raktar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class RaktarApplication extends Application {

    private WarehouseLogic logic;

    @Override
    public void start(Stage primaryStage) {
        logic = new WarehouseLogic();
        logic.loadFromFile();

        Button btnRaktar1 = new Button("Raktár1", getIcon("add.png"));
        TextField inputField = new TextField();
        HBox.setHgrow(inputField, Priority.ALWAYS);
        Button btnRaktar2 = new Button("Raktár2", getIcon("add.png"));

        btnRaktar1.setId("btnRaktar1");
        inputField.setId("inputField");
        btnRaktar2.setId("btnRaktar2");

        btnRaktar1.setContentDisplay(ContentDisplay.RIGHT);
        btnRaktar2.setContentDisplay(ContentDisplay.LEFT);

        HBox topBox = new HBox(10, btnRaktar1, inputField, btnRaktar2);
        topBox.setPadding(new Insets(10));

        ListView<String> lv1 = new ListView<>(logic.getList1());
        ListView<String> lv2 = new ListView<>(logic.getList2());

        lv1.setId("list1");
        lv2.setId("list2");

        HBox.setHgrow(lv1, Priority.ALWAYS);
        HBox.setHgrow(lv2, Priority.ALWAYS);

        Button btnRight = new Button("", getIcon("right.png"));
        Button btnLeft = new Button("", getIcon("left.png"));

        btnRight.setId("btnRight");
        btnLeft.setId("btnLeft");

        VBox moveBox = new VBox(10, btnRight, btnLeft);
        moveBox.setAlignment(Pos.CENTER);

        Button btnDel1 = new Button("", getIcon("del.png"));
        Button btnDel2 = new Button("", getIcon("del.png"));

        btnDel1.setId("btnDel1");
        btnDel2.setId("btnDel2");

        HBox delBox = new HBox(10, btnDel1, btnDel2);
        delBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox centerButtons = new VBox(10, moveBox, spacer, delBox);
        centerButtons.setPadding(new Insets(0, 5, 0, 5));

        HBox middleBox = new HBox(10, lv1, centerButtons, lv2);
        middleBox.setPadding(new Insets(0, 10, 10, 10));
        VBox.setVgrow(middleBox, Priority.ALWAYS);

        Button btnSave = new Button("Mentés", getIcon("save.png"));
        btnSave.setId("btnSave");
        HBox bottomBox = new HBox(btnSave);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 10, 10, 10));

        VBox root = new VBox(topBox, middleBox, bottomBox);
        Scene scene = new Scene(root, 550, 400);

        Image appIcon = loadImage("raktar.png");
        if (appIcon != null) primaryStage.getIcons().add(appIcon);

        primaryStage.setTitle("Raktár");
        primaryStage.setScene(scene);
        primaryStage.show();

        btnRaktar1.setOnAction(_ -> handleAdd(1, inputField));
        btnRaktar2.setOnAction(_ -> handleAdd(2, inputField));

        btnRight.setOnAction(_ -> handleMove(1, lv1, lv2));
        btnLeft.setOnAction(_ -> handleMove(2, lv2, lv1));

        btnDel1.setOnAction(_ -> handleDelete(1, lv1));
        btnDel2.setOnAction(_ -> handleDelete(2, lv2));

        btnSave.setOnAction(_ -> {
            try {
                logic.saveToFile();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sikeres mentés!");
                alert.setHeaderText(null);
                alert.show();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Hiba mentéskor: " + ex.getMessage()).show();
            }
        });
    }

    private void handleAdd(int listId, TextField inputField) {
        logic.addToList(listId, inputField.getText());
        inputField.setText("");
        inputField.requestFocus();
    }

    private void handleMove(int fromListId, ListView<String> fromLv, ListView<String> toLv) {
        int idx = fromLv.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            String movedItem = logic.moveItem(fromListId, idx);
            if (movedItem != null) {
                toLv.getSelectionModel().select(movedItem);
                toLv.scrollTo(movedItem);
            }
        }
    }

    private void handleDelete(int listId, ListView<String> lv) {
        int idx = lv.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            logic.deleteItem(listId, idx);
            int newSize = lv.getItems().size();
            if (newSize > 0) {
                int nextIdx = Math.min(idx, newSize - 1);
                lv.getSelectionModel().select(nextIdx);
                lv.scrollTo(nextIdx);
            }
        }
    }

    private ImageView getIcon(String filename) {
        Image img = loadImage(filename);
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(16);
            iv.setFitHeight(16);
            return iv;
        }
        return new ImageView();
    }

    private Image loadImage(String filename) {
        try {
            InputStream is = getClass().getResourceAsStream("/hu/gappy/raktar/" + filename);
            if (is != null) {
                return new Image(is);
            }
        } catch (Exception e) {
            System.err.println("Nem található a kép: " + filename);
        }
        return null;
    }

}