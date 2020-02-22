package sample;

import com.sun.scenario.effect.Effect;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.List;
import java.util.ArrayList;

public class Main extends Application  {
    private static int columnSize = 9;
    private static int rowSize = 9;
    private static int gridSize = 50;
    private static int numberOfBombs = 0;
    private static Position grid[][];
    private static Scene scene;
    private static Group root;
    private static Image image =  new Image("sample/bomb.png");
    private static Image image2 =  new Image("sample/flag4.png");
    private static Image image3 =  new Image("sample/noBomb.png");
    private static Image image4 = new Image("sample/flag3.png");
    private static Image image5 = new Image("sample/explosion.png");
    private static Image image6 = new Image("sample/winPic.png");
    private ImageView mine;
    private ImageView menuIcon = new ImageView("sample/menuIcon.png");

    private static Text text1;
    private static Pane rt;
    private static boolean gameOver=false;

    @Override
    public void start(Stage stage){
        root = new Group(restart());
        scene = new Scene(root, gridSize*rowSize, gridSize*columnSize + 40);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");

        stage.setResizable(false);
        stage.getIcons().add(image);
        stage.show();
    }


    Parent restart(){
        rt = new Pane();
        numberOfBombs = 0;
        gameOver =false;
        grid = new Position[rowSize][columnSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                grid[i][j] = new Position(i, j,Math.random()>0.85);
                rt.getChildren().add(grid[i][j]);
            }
        }

        countBomb(grid);
        StackPane upperSection = new StackPane();
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: lightslategray;-fx-opacity: 0.5;");
        Menu menuFile = new Menu();
        menuIcon.setFitHeight(27);
        menuIcon.setFitWidth(27);
        menuFile.setGraphic(menuIcon);
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> {
            Alert aboutAlert = new Alert(Alert.AlertType.ERROR,
                    "Created by Runtime Terror", ButtonType.CLOSE);
            aboutAlert.setTitle("About");
            aboutAlert.setHeaderText("Minesweeper");
            aboutAlert.showAndWait();
        });

        MenuItem reload = new MenuItem("New game");
        reload.setOnAction(e ->scene.setRoot(restart()));
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(e -> Platform.exit());
        menuFile.getItems().addAll(reload, about, quit);
        menuBar.getMenus().addAll(menuFile);

        text1 = new Text(": " + numberOfBombs);
        Rectangle bombSection = new Rectangle(40,40,Color.TRANSPARENT);
        Rectangle flagImage = new Rectangle(20,20);
        flagImage.setFill(new ImagePattern(image4));
        Button retry = new Button("Retry");
        retry.prefWidth(60);
        retry.prefHeight(40);
        retry.setTranslateX(gridSize * rowSize - 60);
        text1.setTranslateX(70);
        flagImage.setTranslateX(50);
        retry.setOnAction(e-> scene.setRoot(restart()));
        upperSection.getChildren().addAll(bombSection,text1,retry, flagImage,menuBar);
        rt.getChildren().add(upperSection);
        return rt;
    }

    void winAlert(){
        Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "\nCongratulations! You won.", ButtonType.CLOSE);
        winAlert.setTitle("Win");
        ImageView flag = new ImageView(image6);
        flag.setFitHeight(80);
        flag.setFitWidth(80);
        winAlert.setGraphic(flag);
        winAlert.setHeaderText(null);
        winAlert.showAndWait();
    }

    void loseAlert(){
        Alert loseAlert = new Alert(Alert.AlertType.CONFIRMATION,"\n\nOops! You lose", ButtonType.CLOSE);
        loseAlert.setTitle("Game Over");
        mine = new ImageView(image5);
        mine.setFitHeight(80);
        mine.setFitWidth(80);
        loseAlert.setGraphic(mine);
        loseAlert.setHeaderText(null);
        loseAlert.showAndWait();
    }

    void openBombs(Position grid[][]) {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (grid[i][j].hasBomb) {
                    if(!grid[i][j].isFlagged)
                        grid[i][j].rectangle.setFill(Color.TRANSPARENT);
                }
                else
                if (grid[i][j].isFlagged)
                    grid[i][j].rectangle.setFill(new ImagePattern(image3));

            }
        }
    }

    public void countBomb(Position grid[][]) {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (grid[i][j].hasBomb) {
                    numberOfBombs++;
                } else {
                    int num = 0;
                    for (int m = i - 1; m < i + 2; m++) {
                        for (int n = j - 1; n < j + 2; n++) {
                            try {
                                if (grid[m][n].hasBomb)
                                    num++;
                            } catch (ArrayIndexOutOfBoundsException e1) {
                            }
                        }
                    }
                    if(num!=0)
                        grid[i][j].text.setText(Integer.toString(num));
                }
            }
        }
    }



    class Position extends Pane{
        int x, y;
        boolean isOpened = false;
        boolean hasBomb;
        boolean isFlagged;
        Rectangle rectangle = new Rectangle(gridSize, gridSize);
        Text text = new Text();
        public Position(int x, int y, boolean hasBomb) {
            this.x = x; this.y = y;
            this.hasBomb = hasBomb;
            isFlagged = false;
            mine = new ImageView(image);
            rectangle.setStroke(Color.LIGHTBLUE);
            rectangle.setStrokeWidth(2);
            rectangle.setFill(Color.LIGHTSLATEGRAY);
            text.setFont(Font.font(13));
            text.setX(3*gridSize/7);
            text.setY(4*gridSize/7);
            if (this.hasBomb) {
                mine.setVisible(true);
                mine.setFitHeight(gridSize*0.7);
                mine.setFitWidth(gridSize*0.7);
                mine.setX(gridSize/8);
                mine.setY(gridSize/8);
                getChildren().add(mine);
            }
            text.setVisible(false);
            setTranslateX(x * gridSize);
            setTranslateY(40+ y * gridSize);
            getChildren().addAll(rectangle, text);

            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (!gameOver) {
                        if (rectangle.getFill() != Color.TRANSPARENT) {
                            if (e.isSecondaryButtonDown()) {
                                if (isFlagged) {
                                    rectangle.setFill(Color.LIGHTSLATEGRAY);
                                    numberOfBombs++;
                                    isFlagged = false;
                                } else {
                                    if(numberOfBombs > 0) {
                                        rectangle.setFill(new ImagePattern(image2));
                                        numberOfBombs--;
                                        isFlagged = true;
                                    }
                                }
                                text1.setText(": " + numberOfBombs);
                                if (checkForWin(grid, rowSize, columnSize)) {
                                    if (isFlagged)
                                        isFlagged = false;

                                    mine.setVisible(true);
                                    open();
                                    gameOver = true;
                                    return;
                                }

                            } else if (e.isPrimaryButtonDown()) {
                                if (!isFlagged)
                                    open();
                            }
                        }
                    }
                }
            };
            rectangle.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        }
        public void open() {
            if (this.isOpened)
                return;
            if (this.hasBomb && !this.isFlagged) {
                gameOver = true;
                mine.setVisible(true);
                openBombs(grid);
                loseAlert();
                return;
            }

            isOpened = true;
            rectangle.setFill(Color.TRANSPARENT);
            this.text.setVisible(true);
            this.text.setVisible(true);
            if (checkForWin(grid, rowSize,columnSize)) {
                if (this.isFlagged)
                    this.isFlagged = false;
                winAlert();
                return;
            }
            if (this.text.getText().matches("")) {
                getNeigh(this).forEach(Position::open);
            }
        }

        public boolean checkForWin(Position[][] element, int row, int col){
            boolean win = true;
            gameOver = true;
            for(int i = 0;i < col;i++){
                for(int j = 0;j < row;j++){
                    if(!(element[i][j].isOpened)){
                        if(element[i][j].hasBomb){}
                        else {
                            win = false;
                            gameOver = false;
                        }
                    }
                }
            }
            return win;
        }

        private List<Position> getNeigh(Position element){
            List<Position> neighbors = new ArrayList<>();
            for (int i = element.x - 1; i < element.x + 2; i++) {
                for (int j = element.y - 1; j < element.y + 2; j++) {
                    try {
                        if (i != element.x || j != element.y) {
                            neighbors.add(grid[i][j]);
                        }
                    }catch (ArrayIndexOutOfBoundsException e1){}
                }
            }
            return neighbors;
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}

