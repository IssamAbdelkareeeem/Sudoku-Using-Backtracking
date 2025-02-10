package com.example.demo29;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver extends Application {
    private Stage primaryStage;
    private File file;

    private int grid[][] = new int[9][9]; //all the numbers are stored here

    int[][] solutionGrid = new int[9][9];


    private Button loadFileBtn = new Button("Load File");
    private Button solveBtn = new Button("Show Solution");
    private Button backBtn = new Button("");
    private Button checkResultBtn = new Button("Check Result");
    Button eraseBtn = new Button("Erase All Attempts");

    Image image = new Image("C:\\Users\\issam\\Downloads\\questionn.png");
    ImageView question = new ImageView(image);
    Image checkImage = new Image("C:\\Users\\issam\\Downloads\\checked.png");
    ImageView check = new ImageView(checkImage);
    public CheckBox cellHighlightBox = new CheckBox("");


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openFileScreen();
    }

    public void openFileScreen() {
        primaryStage.getIcons().add(new Image("C:\\Users\\issam\\Downloads\\file.png"));
        Label titleLabel = new Label("Sudoku");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Comic Sans MS';");
        titleLabel.setAlignment(Pos.CENTER);
        loadFileBtn.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #121111; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 15px; " +
                        "-fx-background-radius: 15px;"
        );

        VBox centerBox = new VBox(20, loadFileBtn);
        centerBox.setAlignment(Pos.CENTER);
        BorderPane layout = new BorderPane();
        layout.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.TOP_CENTER);
        layout.setCenter(centerBox);
        layout.setStyle("-fx-background-color: #aaaa8e;");

        Scene fileScene = new Scene(layout, 1500.4, 838.4);
        primaryStage.setScene(fileScene);
        primaryStage.setTitle("File");
        primaryStage.setMaximized(true);
        primaryStage.show();

        loadFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Sudoku File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            file = fileChooser.showOpenDialog(primaryStage);

            if (file != null && isValidFile(file)) {
                fillGrid(file);
                printGrid();
                openSudokuStage();
            } else {
                System.out.println("Invalid file");
            }
        });
    }

    public void openSudokuStage() {
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(new Image("C:\\Users\\issam\\Downloads\\sudokuu.png"));
        primaryStage.setTitle("Sudoku BackTracking");
        glow(checkResultBtn);
        glow(solveBtn);
        glow(backBtn);
        glowred(eraseBtn);

        backBtn.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-padding: 0px; " +
                        "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: transparent; "
        );

        Label highlightLabel = new Label("Cell Highlight");
        highlightLabel.setStyle("-fx-text-fill: #3b403b; -fx-font-weight: bold; -fx-font-family: 'Comic Sans MS';");

        HBox hintBox = new HBox(5);
        hintBox.setAlignment(Pos.CENTER);
        hintBox.getChildren().addAll(highlightLabel, cellHighlightBox);

        backBtn.setOnAction(e -> openFileScreen());
        String imagePath = "file:C:/Users/issam/Downloads/backk.png";
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        backBtn.setGraphic(imageView);
        check.setFitWidth(32);
        check.setFitHeight(32);

        HBox solveButtonBox = new HBox(-20);
        solveButtonBox.setAlignment(Pos.CENTER);
        solveButtonBox.getChildren().addAll(solveBtn, check);

        HBox checkButtonBox = new HBox(-20);
        checkButtonBox.setAlignment(Pos.CENTER);
        checkButtonBox.getChildren().addAll(checkResultBtn, question);

        question.setFitWidth(28);
        question.setFitHeight(28);

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setGridLinesVisible(false);

        TextField[][] gridCells = new TextField[9][9];

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                GridPane subGrid = new GridPane();
                subGrid.setGridLinesVisible(true);
                subGrid.setStyle("-fx-border-color: #000000; -fx-border-width: 1.8px;");

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int fullGridRow = boxRow * 3 + row;
                        int fullGridCol = boxCol * 3 + col;

                        TextField cell = new TextField();

                        cell.setPrefSize(65, 120);
                        cell.setAlignment(Pos.CENTER);
                        cell.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px; -fx-caret-color: transparent;");
                        //this is the color of the small lines


                        if (grid[fullGridRow][fullGridCol] != 0) {
                            cell.setText(String.valueOf(grid[fullGridRow][fullGridCol]));//put the numbers
                            //from the file in the cell
                            cell.setEditable(false);
                            cell.setStyle(cell.getStyle() + " -fx-background-color: #d3d3d3;");
                        }
                        subGrid.add(cell, col, row);
                        gridCells[fullGridRow][fullGridCol] = cell;


                        //the 2 cell actions : if we either press or type in a tf
                        //it gets highlighted
                        cell.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue.matches("[1-9]?")) {
                                int number = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                                if (cellHighlightBox.isSelected()) {
                                    highlight(gridCells, fullGridRow, fullGridCol, number);
                                }
                            } else {
                                cell.setText(oldValue);
                            }
                        });

                        cell.setOnMouseClicked(event -> {
                            if (cellHighlightBox.isSelected()) {
                                int number = cell.getText().isEmpty() ? 0 : Integer.parseInt(cell.getText());
                                highlight(gridCells, fullGridRow, fullGridCol, number);
                            }
                        });

                        cellHighlightBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue) {
                                for (int r = 0; r < 9; r++) {
                                    for (int c = 0; c < 9; c++) {
                                        TextField cellValue = gridCells[r][c];
                                        if (cellValue.isEditable()) {
                                            cellValue.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px;");
                                        } else {
                                            cellValue.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px; -fx-background-color: #d3d3d3;");
                                        }
                                    }
                                }
                            }
                        });


                    }
                }

                mainGrid.add(subGrid, boxCol, boxRow);
            }
        }

        solveBtn.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-color: transparent; " +
                        "-fx-text-fill: #272727; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: transparent; "
        );

        eraseBtn.setStyle(
                "-fx-font-size: 10px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #575a5a; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 15px; " +
                        "-fx-background-radius: 15px;"
        );


        checkResultBtn.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-color: transparent; " +
                        "-fx-text-fill: #272727; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: transparent; "
        );

        eraseBtn.setOnAction(e -> {
            boolean isEmptyCell = false;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (gridCells[row][col].isEditable() && !gridCells[row][col].getText().isEmpty()) {
                        gridCells[row][col].clear(); //erase
                        isEmptyCell = true;
                        cellHighlightBox.setSelected(false);
                        cellHighlightBox.setSelected(true);
                    }
                }
            }
            if (!isEmptyCell) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("No cells left to erase");
                alert.show();
            }
        });

        checkResultBtn.setOnAction(e -> {
            boolean allCellsFilled = true;

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = gridCells[row][col].getText();
                    if (text.isEmpty()) { //if there's empty cells
                        allCellsFilled = false; //not all of them are filled
                    } else {
                        grid[row][col] = Integer.parseInt(text);
                    }
                }
            }

            if (!allCellsFilled) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Incomplete Grid, please fill all the cells");
                alert.show();
                return;
            }

            if (isWin(grid)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Correct Sudoku Solution, You Won");
                Image image2 = new Image("C:\\Users\\issam\\Downloads\\check.png");
                ImageView imgView = new ImageView(image2);
                imgView.setFitWidth(50);
                imgView.setFitHeight(50);
                alert.setGraphic(imgView);
                alert.setTitle("Win");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Solution, Try Again");
                alert.setTitle("Loss");
                alert.show();
            }
        });
        solveBtn.setOnAction(e -> {
            if (Sudoku.getSudokuSolution(solutionGrid)) {
                System.out.println("solution:");
                printGrid();
                openSudokuSolutionStage();
            } else {
                System.out.println("No solution");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("This Sudoku grid can't be solved");
                alert.show();
            }
        });


        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(eraseBtn, checkButtonBox, solveButtonBox, hintBox);
        buttonBox.setAlignment(Pos.CENTER);
        Text normalText1 = new Text("Press ");
        Text boldL = new Text("L");
        Text normalText2 = new Text(" if you want to\nload another Grid file");
        normalText1.setFont(Font.font("Comic Sans MS", 12));
        boldL.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 14));
        normalText2.setFont(Font.font("Comic Sans MS", 12));
        TextFlow fileLabelFlow = new TextFlow(normalText1, boldL, normalText2);
        VBox sudokuBox = new VBox(10, mainGrid);
        sudokuBox.setAlignment(Pos.CENTER);
        BorderPane mainLayout = new BorderPane();
        VBox backBox = new VBox(10);
        backBox.getChildren().addAll(backBtn, fileLabelFlow);
        mainLayout.setTop(backBox);
        BorderPane.setAlignment(backBtn, Pos.TOP_LEFT);
        mainLayout.setCenter(sudokuBox);
        mainLayout.setBottom(buttonBox);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        BorderPane.setMargin(buttonBox, new Insets(50, 0, 150, 0));
        BorderPane.setMargin(backBox, new Insets(10, 0, 0, 10));
        BorderPane.setMargin(fileLabelFlow, new Insets(40, 50, 0, 40));
        Scene sudokuScene = new Scene(mainLayout, 1550.4, 838.4);
        mainLayout.setStyle("-fx-background-color: #aaaa8e;");
        sudokuScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.L) {
                loadFileBtn.fire();
            }
            if (event.getCode() == KeyCode.H) {
                cellHighlightBox.setSelected(!cellHighlightBox.isSelected());
            }
        });

        primaryStage.setScene(sudokuScene);
    }


    public void fillGrid(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int row = 0;
            while (scanner.hasNextLine() && row < 9) {
                String line = scanner.nextLine().trim();
                line = line.replace("grid = ", "").replace("{", "").replace("}", "").replace(";", "").trim();

                String[] numbers = line.split(",");

                for (int col = 0; col < numbers.length && col < 9; col++) {
                    grid[row][col] = Integer.parseInt(numbers[col].trim());
                    solutionGrid[row][col] = Integer.parseInt(numbers[col].trim());
                }
                row++;
            }
            System.out.println("grid :");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getPath());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void printGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(solutionGrid[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


    private void highlight(TextField[][] gridCells, int row, int col, int number) {
        // reset to default when we press or type in a tf
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextField cell = gridCells[r][c];
                if (cell.isEditable()) {
                    cell.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px;");
                } else {
                    cell.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px; -fx-background-color: #d3d3d3;");
                }
            }
        }

        if (number == 0) {
            gridCells[row][col].setStyle(gridCells[row][col].getStyle() + " -fx-background-color: white;");
        }

        // highlight rows/columns for this cell
        for (int i = 0; i < 9; i++) {
            if (i != col) {
                gridCells[row][i].setStyle(gridCells[row][i].getStyle() + " -fx-background-color: #9aa3a3;");
            }
            if (i != row) {
                gridCells[i][col].setStyle(gridCells[i][col].getStyle() + " -fx-background-color: #9aa3a3;");
            }
        }

        // highlight box for this cell
        int rowOfBox = row - row % 3;
        int colOfBox = col - col % 3;
        for (int r = rowOfBox; r < rowOfBox + 3; r++) {
            for (int c = colOfBox; c < colOfBox + 3; c++) {
                if (r != row || c != col) {
                    gridCells[r][c].setStyle(gridCells[r][c].getStyle() + " -fx-background-color: #9aa3a3;");
                }
            }
        }

        String numberStr = String.valueOf(number);
        boolean notSafe = false;

        //check if there's a duplicate in rows/columns for the number in this cell
        //same as IsSafe logic
        for (int i = 0; i < 9; i++) {
            if (i != col && gridCells[row][i].getText().equals(numberStr)) {
                gridCells[row][i].setStyle(gridCells[row][i].getStyle() + " -fx-background-color: #ff6666;");
                notSafe = true;
            }
            if (i != row && gridCells[i][col].getText().equals(numberStr)) {
                gridCells[i][col].setStyle(gridCells[i][col].getStyle() + " -fx-background-color: #ff6666;");
                notSafe = true;
            }
        }

        //check if there's a duplicate in the box for this cell's number
        for (int r = rowOfBox; r < rowOfBox + 3; r++) {
            for (int c = colOfBox; c < colOfBox + 3; c++) {
                if ((r != row || c != col) && gridCells[r][c].getText().equals(numberStr)) {
                    gridCells[r][c].setStyle(gridCells[r][c].getStyle() + " -fx-background-color: #ff6666;");
                    notSafe = true;
                }
            }
        }
        //if we don't find any duplicates the cell is safe
        if (!notSafe && number != 0) {
            gridCells[row][col].setStyle(gridCells[row][col].getStyle() + " -fx-background-color: #8fadd8;");
        }
    }

    private void glow(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(0.233);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        button.setOnMouseEntered(event -> button.setEffect(glow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }

    private void glowred(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(3);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        button.setOnMouseEntered(event -> button.setEffect(glow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }


    public static boolean isWin(int[][] grid) {
        boolean[][] rowSearch = new boolean[9][10];
        boolean[][] colSearch = new boolean[9][10];
        boolean[][] boxSearch = new boolean[9][10];

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid.length; c++) {

                int x = grid[r][c];

                if (x == 0)
                    return false; //our grid should be full

                int boxIndex = (r / 3) * 3 + (c / 3);

                //here we check if x is already in its row/col/box
                if (rowSearch[r][x] || colSearch[c][x] || boxSearch[boxIndex][x]) {
                    return false;
                }

                rowSearch[r][x] = colSearch[c][x] = boxSearch[boxIndex][x] = true;

            }
        }
        return true; //no duplicates found, it's a WIN
    }


    private void openSudokuSolutionStage() {
        //the grid is built by using a grid pane of subGridPanes
        Stage solvedStage = new Stage();
        solvedStage.setTitle("Solution");
        solvedStage.getIcons().add(new Image("C:\\Users\\issam\\Downloads\\check.png"));

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setGridLinesVisible(false);


        TextField[][] solvedGridCells = new TextField[9][9];


        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                GridPane subGrid = new GridPane();
                subGrid.setGridLinesVisible(true);
                subGrid.setStyle("-fx-border-color: #040404; -fx-border-width: 1.5px;");

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int fullGridRow = boxRow * 3 + row;
                        int fullGridCol = boxCol * 3 + col;

                        TextField cell = new TextField();
                        cell.setPrefSize(50, 50);
                        cell.setAlignment(Pos.CENTER);
                        cell.setStyle("-fx-font-size: 19px; -fx-font-family: 'Comic Sans MS'; -fx-border-color: #505353; -fx-border-width: 1px;");

                        cell.setText(String.valueOf(solutionGrid[fullGridRow][fullGridCol]));
                        cell.setEditable(false);
                        cell.setStyle(cell.getStyle() + " -fx-background-color: #d3d3d3;");

                        subGrid.add(cell, col, row);
                        solvedGridCells[fullGridRow][fullGridCol] = cell;
                    }
                }
                mainGrid.add(subGrid, boxCol, boxRow);
            }
        }

        Button closeBtn = new Button("Close");
        closeBtn.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-color: #454545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold;"
        );
        closeBtn.setOnAction(e -> solvedStage.close());
        glow(closeBtn);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(closeBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox sudokuBox = new VBox(10, mainGrid, buttonBox);
        sudokuBox.setAlignment(Pos.CENTER);
        sudokuBox.setStyle("-fx-background-color: #aaaa8e;");


        Scene solvedScene = new Scene(sudokuBox, 500, 550);
        solvedStage.setScene(solvedScene);
        solvedStage.getIcons().add(new Image("C:\\Users\\issam\\Downloads\\check.png"));
        solvedStage.show();
    }


    public boolean isValidFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.matches("[0-9{},\\s]*")) {
                    return false;
                }
                line = line.replace("{", "").replace("}", "").trim();
                String[] numbers = line.split(",");
                for (String num : numbers) {
                    if (!num.trim().isEmpty()) {
                        int value = Integer.parseInt(num.trim());

                        if (value < 0 || value > 9) {
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
