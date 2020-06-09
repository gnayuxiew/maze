import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Stack;

public class Main extends Application {
    private final int width = 80;
    private final int height = 80;
    private int size = 10;
    private Cell[] cells = new Cell[width * height];
    private Pane pane = new Pane();
    private Scene scene = new Scene(pane, width * size, height * size);

    @Override
    public void start(Stage stage) {
        Rectangle background = new Rectangle(0, 0, width * size, height * size);
        pane.getChildren().add(background);
        for (int i = 0; i < width * height; i++) {
            cells[i] = new Cell(i);
        }
        stage.setScene(scene);
        stage.show();
        Thread maze = new Thread(this::mazeGo);
        maze.start();
    }

    private void mazeGo() {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        cells[0].visited = true;
        while (!stack.empty()) {
            int current = stack.pop();
            ArrayList<Integer> list = getNeighbors(current);
            if (list.size() != 0) {
                stack.push(current);
                int next = list.get((int) (Math.random() * list.size()));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (current - next) {
                    case 1:
                        cells[current].setLeftStroke();
                        cells[next].setRightStroke();
                        break;
                    case -1:
                        cells[next].setLeftStroke();
                        cells[current].setRightStroke();
                        break;
                    case width:
                        cells[current].setTopStroke();
                        cells[next].setBottomStroke();
                        break;
                    case -width:
                        cells[next].setTopStroke();
                        cells[current].setBottomStroke();
                        break;
                }
                cells[next].visited = true;
                stack.push(next);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Integer> getNeighbors(int x) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        if (x / width != 0 && !cells[x - width].visited) {
            neighbors.add(x - width);
        }
        if (x / width != height - 1 && !cells[x + width].visited) {
            neighbors.add(x + width);
        }
        if (x % width != 0 && !cells[x - 1].visited) {
            neighbors.add(x - 1);
        }
        if (x % width != width - 1 && !cells[x + 1].visited) {
            neighbors.add(x + 1);
        }
        return neighbors;
    }

    class Cell {
        int x;
        int y;
        boolean visited;
        Line top;
        Line bottom;
        Line left;
        Line right;

        Cell(int i) {
            x = i % width;
            y = i / width;
            top = new Line(x * size, y * size, x * size + size, y * size);
            bottom = new Line(x * size, y * size + size, x * size + size, y * size + size);
            left = new Line(x * size, y * size, x * size, y * size + size);
            right = new Line(x * size + size, y * size, x * size + size, y * size + size);
            top.setStroke(Color.WHITE);
            bottom.setStroke(Color.WHITE);
            left.setStroke(Color.WHITE);
            right.setStroke(Color.WHITE);

            pane.getChildren().addAll(top, bottom, left, right);

        }

        void setTopStroke() {
            Platform.runLater(() -> top.setStroke(Color.BLACK));
        }

        void setBottomStroke() {
            Platform.runLater(() -> bottom.setStroke(Color.BLACK));
        }

        void setLeftStroke() {
            Platform.runLater(() -> left.setStroke(Color.BLACK));
        }

        void setRightStroke() {
            Platform.runLater(() -> right.setStroke(Color.BLACK));
        }
    }
}
