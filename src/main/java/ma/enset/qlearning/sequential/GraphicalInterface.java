package ma.enset.qlearning.sequential;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class GraphicalInterface extends Application {
    private static final int ROW_COUNT = 9;
    private static final int COLUMN_COUNT = 9;
    Qlearning qlearning =new Qlearning();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TableView<String[]> table = createTable();
        Button button = createButton(table);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(button);
        BorderPane.setMargin(button, new Insets(10));

        Scene scene = new Scene(root, 280, 320);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Q-learning");
        primaryStage.show();
    }

    private TableView<String[]> createTable() {
        TableView<String[]> table = new TableView<>();

        // ...

        for (int i = 0; i < COLUMN_COUNT; i++) {
            TableColumn<String[], String> column = new TableColumn<>(""+(i + 1));
            final int columnIndex = i;
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> cellData) {
                    String[] rowData = cellData.getValue();
                    if (rowData != null && columnIndex < rowData.length) {
                        return new SimpleStringProperty(rowData[columnIndex]);
                    } else {
                        return new SimpleStringProperty("");
                    }
                }
            });
            column.setCellFactory(cellData -> new TableCell<String[], String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);

                    // Set background color based on cell value
                    if (!empty && item != null) {
                        if (item.equals("0")) {
                            setStyle("-fx-background-color: yellow;");
                        }
                        if (item.equals("-1"))
                        {
                            setStyle("-fx-background-color: red;");
                        }
                        if (item.equals("1"))
                        {
                            setStyle("-fx-background-color: green;");
                        }
                        if (getTableRow() != null && getTableRow().getIndex() == 0 && getIndex() == 0 && columnIndex==0) {
                            setStyle("-fx-background-color: blue;");
                        }


                    } else {
                        setStyle("");
                    }
                }
            });
            table.getColumns().add(column);
        }

        table.setItems(FXCollections.observableArrayList(createEmptyRows()));

        return table;
    }

    private String[][] createEmptyRows() {
        int[][] rows = qlearning.grid;
        String [][]tab = new String[ROW_COUNT][COLUMN_COUNT];
        for (int i=0;i<ROW_COUNT;i++)
        {
            for(int j=0;j<COLUMN_COUNT;j++)
            {
                tab[i][j]=""+rows[i][j];
            }
        }
        return tab;
    }

    private Button createButton(TableView<String[]> table) {
        Button button = new Button("Chercher par Q-learning");
        button.setOnAction(event -> {
            qlearning.run();
            List<int[]> tab = qlearning.tab0fActions();
            tab.forEach(ints -> {
                if (!table.getItems().isEmpty()) {
                    String[] firstRow = table.getItems().get(ints[0]);
                    if (firstRow.length > 0) {
                        firstRow[ints[1]] = "";
                        table.refresh();
                    }
                }
            });

        });
        return button;
    }
}


