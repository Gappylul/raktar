package hu.gappy.raktar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WarehouseLogic {
    private final ObservableList<String> list1 = FXCollections.observableArrayList();
    private final ObservableList<String> list2 = FXCollections.observableArrayList();
    private Path dataFile = Paths.get("raktar.dat");

    public ObservableList<String> getList1() { return list1; }
    public ObservableList<String> getList2() { return list2; }

    public void setDataFile(Path dataFile) { this.dataFile = dataFile; }

    public void addToList(int listId, String item) {
        if (item == null || item.trim().isEmpty()) return;
        if (listId == 1) list1.add(item.trim());
        else if (listId == 2) list2.add(item.trim());
    }

    public String moveItem(int fromListId, int index) {
        if (fromListId == 1 && index >= 0 && index < list1.size()) {
            String item = list1.remove(index);
            list2.add(item);
            return item;
        } else if (fromListId == 2 && index >= 0 && index < list2.size()) {
            String item = list2.remove(index);
            list1.add(item);
            return item;
        }
        return null;
    }

    public void deleteItem(int listId, int index) {
        if (listId == 1 && index >= 0 && index < list1.size()) {
            list1.remove(index);
        } else if (listId == 2 && index >= 0 && index < list2.size()) {
            list2.remove(index);
        }
    }

    public void saveToFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(dataFile)) {
            for (String item : list1) {
                writer.write("1;" + item);
                writer.newLine();
            }
            for (String item : list2) {
                writer.write("2;" + item);
                writer.newLine();
            }
        }
    }

    public void loadFromFile() {
        list1.clear();
        list2.clear();
        if (Files.exists(dataFile)) {
            try {
                List<String> lines = Files.readAllLines(dataFile);
                for (String line : lines) {
                    String[] parts = line.split(";", 2);
                    if (parts.length == 2) {
                        if (parts[0].equals("1")) list1.add(parts[1]);
                        else if (parts[0].equals("2")) list2.add(parts[1]);
                    }
                }
            } catch (IOException e) {
                System.err.println("Hiba a betöltéskor: " + e.getMessage());
            }
        }
    }
}