package sample;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.EventHandler;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class LevelD extends Application {
    private static int columnSize;
    private static int rowSize;
    private static int gridSize = 60;
    private static int menuSize = 37;
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
    private static Image image7 = new Image("sample/error.png");
    private static Image image8 = new Image("sample/draw.png");
    private ImageView mine;
    private static Text text1;
    private ImageView menuIcon = new ImageView("sample/menuIcon.png");
    private static Text userOneScore;
    private static Text userTwoScore;
    private static Text turn;
    private static Pane rt;
    private static boolean gameOver = false;
    private static String user1, user2;
    boolean user = false;
    int score1 = 0, score2 = 0;

    private void errorAlert(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                "\nEntered input is wrong.", ButtonType.CLOSE);
        errorAlert.setTitle("Error");
        ImageView err = new ImageView(image7);
        err.setFitHeight(80);
        err.setFitWidth(80);
        errorAlert.setGraphic(err);
        errorAlert.setHeaderText(null);
        errorAlert.showAndWait();
    }

    private void defWidth(int row, int column){
        if(row <= 5 && column <= 5){
            gridSize = 400/Math.max(row,column);;
        }else if(row <= 10 && column <= 10){
            gridSize = 500/Math.max(row,column);;
        }else if(row <= 15 && column <= 15){
            gridSize = 800/Math.max(row,column);;
        }else {
            gridSize = 1000/Math.max(row,column);;
        }
    }

    private void inputMis(String m, Stage stage, String[] dimen){
        int x = 0;
        for(int i = 0;i < m.length();i++){
            if(!(m.charAt(i) >= 48 && m.charAt(i) <= 57) && m.charAt(i) != 32 || dimen.length != 2) {
                if(x == 0) {
                    errorAlert();
                    start(stage);
                }
                x++;
            }

        }
    }

    @Override
    public void start(Stage stage) throws ArrayIndexOutOfBoundsException{
        try {
            // first scene
            userOneScore = new Text();
            userTwoScore = new Text();
            TextInputDialog dialog = new TextInputDialog("e.g., 6 5");


            dialog.setTitle("Minesweeper");
            dialog.setHeaderText("Welcome to Minesweeper!");
            dialog.setContentText("Grid:");

            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent())
                System.exit(1);


            String[] dimen = result.get().split("\\s");
            inputMis(result.get(), stage, dimen);
            columnSize = Integer.parseInt(dimen[0]);
            rowSize = Integer.parseInt(dimen[1]);
            defWidth(rowSize, columnSize);

            // second scene
            GridPane grid1 = new GridPane();
            grid1.setAlignment(Pos.CENTER);
            grid1.setHgap(10);
            grid1.setVgap(10);
            grid1.setPadding(new Insets(50, 50, 50, 50));

            Scene scene1 = new Scene(grid1, 500, 350);
            Text sceneTitle = new Text();
            sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid1.add(sceneTitle, 0, 0, 2, 1);

            Label userName1 = new Label("Player 1 name:");
            grid1.add(userName1, 0, 1);

            TextField nickname1 = new TextField();
            if (nickname1.getText().length() == 0) {
                user1 = "Player 1";
                userOneScore.setText(user1);
            }
            grid1.add(nickname1, 1, 1);
            ;

            Label userName2 = new Label("Player 2 name:");
            grid1.add(userName2, 0, 2);

            TextField nickname2 = new TextField();
            if (nickname2.getText().length() == 0) {
                user2 = "Player 2";
                userTwoScore.setText(user2);
            }
            grid1.add(nickname2, 1, 2);

            Button button1 = new Button("Enter");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(button1);
            grid1.add(hbBtn, 1, 4);
            stage.setScene(scene1);
            button1.setOnAction(e -> {
                if (nickname1.getText().length() == 0 && nickname2.getText().length() == 0) {
                } else if (nickname1.getText().length() == 0) {
                    user2 = nickname2.getText();
                    userTwoScore.setText(user2);
                } else if (nickname2.getText().length() == 0) {
                    user1 = nickname1.getText();
                    userOneScore.setText(user1);
                } else {
                    user1 = nickname1.getText();
                    userOneScore.setText(user1);
                    user2 = nickname2.getText();
                    userTwoScore.setText(user2);
                }
                stage.setScene(scene);
                scene.setRoot(restart());
            });

            // third scene
            root = new Group(restart());
            scene = new Scene(root, gridSize * rowSize, gridSize * columnSize + 1.6 * menuSize);
            scene.setFill(Color.LAVENDER);
            stage.setTitle("Minesweeper");

            stage.setResizable(false);
            stage.getIcons().add(image);
            stage.show();
        }catch (RuntimeException e){}
    }

    Parent restart(){
        user = false;
        score1 = 0;
        score2 = 0;
        userOneScore.setText(user1 + ": " + score1);
        userTwoScore.setText(user2 + ": " + score2);
        numberOfBombs = 0;
        rt = new Pane();
        gameOver =false;
        grid = new Position[rowSize][columnSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                grid[i][j] = new Position(i, j,Math.random()>0.85);
                rt.getChildren().add(grid[i][j]);
            }
        }

        countBomb(grid);

        Pane upperSection = new Pane();

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

        Rectangle flagImage = new Rectangle(20,20);
        flagImage.setFill(new ImagePattern(image4));
        flagImage.setTranslateX(80);
        flagImage.setTranslateY(5);

        Button retry = new Button("Retry");
        retry.prefWidth(60);
        retry.prefHeight(40);
        retry.setTranslateX(gridSize * rowSize - 60);
        retry.setTranslateY(5);
        retry.setOnAction(e-> scene.setRoot(restart()));

        HBox upperSection2 = new HBox();
        text1 = new Text(": " + numberOfBombs );
        text1.setTranslateY(10);
        upperSection2.setTranslateX(100);
        turn = new Text( user1 + "'s turn");
        turn.setTranslateY(10);
        turn.setTranslateX(40);
        upperSection2.getChildren().addAll(text1,turn);

        upperSection.getChildren().addAll(menuBar,flagImage,upperSection2,retry);

        HBox lowerSection = new HBox(30);
        lowerSection.setTranslateY(gridSize*columnSize+menuSize + 3);
        lowerSection.getChildren().addAll(userOneScore, userTwoScore);

        rt.getChildren().addAll(upperSection,lowerSection);
        return rt;
    }

    private void winAlert(){
        String x = null;
        int num = 0;
        if(score1 > score2) {
            x = user1;
            num = score1;
        }
        else if(score1 < score2) {
            x = user2;
            num = score2;
        }
        if(x == null){
            Alert drawAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "\nCongratulations! You won this game together.", ButtonType.CLOSE);
            drawAlert.setTitle("Draw");
            ImageView flag = new ImageView(image8);
            flag.setFitHeight(80);
            flag.setFitWidth(80);
            drawAlert.setGraphic(flag);
            drawAlert.setHeaderText(null);
            drawAlert.showAndWait();
        }
        else {
            Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "\nCongratulations! " + x + " won with score " + num, ButtonType.CLOSE);
            winAlert.setTitle("Win");
            ImageView flag = new ImageView(image6);
            flag.setFitHeight(80);
            flag.setFitWidth(80);
            winAlert.setGraphic(flag);
            winAlert.setHeaderText(null);
            winAlert.showAndWait();
        }
    }

    void loseAlert(){
        String x;
        if(user)
            x = user1;
        else
            x = user2;
        Alert loseAlert = new Alert(Alert.AlertType.CONFIRMATION,"\n\nOops!" + x + " lose", ButtonType.CLOSE);
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
            setTranslateY(menuSize + y * gridSize);
            getChildren().addAll(rectangle, text);


            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (!gameOver) {
                        if (rectangle.getFill() != Color.TRANSPARENT) {
                            if (e.isSecondaryButtonDown()) {
                                if (isFlagged) {
                                    rectangle.setFill(Color.LIGHTSLATEGRAY);
                                    isFlagged = false;
                                    numberOfBombs++;
                                } else {
                                    if(numberOfBombs > 0) {
                                        rectangle.setFill(new ImagePattern(image2));
                                        isFlagged = true;
                                        numberOfBombs--;

                                    }
                                }
                                text1.setText(": " +numberOfBombs);
                                if (checkForWin(grid, rowSize, columnSize)) {
                                    if (isFlagged)
                                        isFlagged = false;

                                    mine.setVisible(true);
                                    open();
                                    gameOver=true;
                                    return;
                                }

                            } else if (e.isPrimaryButtonDown()) {
                                if (!isFlagged) {
                                    user = !user;
                                    open();
                                }
                                turn.setText( (user?user2:user1)  + "'s turn");
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
            if (checkForWin(grid, rowSize, columnSize)) {
                if (this.isFlagged)
                    this.isFlagged = false;
                winAlert();
                return;
            }
            if(user) {
                score1++;
                userOneScore.setText(user1 + ": " + score1);
            }
            else {
                score2++;
                userTwoScore.setText(user2 + ": " + score2);
            }
            if (this.text.getText().matches("")) {
                getNeigh(this).forEach(Position::open);

            }
        }

        public boolean checkForWin(Position[][] tile, int row, int col){
            boolean win = true;
            for(int i = 0;i < row;i++){
                for(int j = 0;j < col;j++){
                    if(!(tile[i][j].isOpened)){
                        if(tile[i][j].hasBomb){}
                        else
                            win = false;
                    }
                }
            }
            return win;
        }


        private List<Position> getNeigh(Position tile){
            List<Position> neighbors = new ArrayList<>();
            for (int i = tile.x - 1; i < tile.x + 2; i++) {
                for (int j = tile.y - 1; j < tile.y + 2; j++) {
                    try {
                        if (i != tile.x || j != tile.y) {
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

