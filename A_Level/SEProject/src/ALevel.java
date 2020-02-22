import java.util.InputMismatchException;
import java.util.Scanner;
public class ALevel {
    private static void countBomb(String[][] isBomb) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (isBomb[i][j] == "\uD83D\uDCA3") {
                } else {
                    int num = 0;
                    for (int m = i - 1; m < i + 2; m++) {
                        for (int n = j - 1; n < j + 2; n++) {
                            try {
                                if (isBomb[m][n] == "\uD83D\uDCA3") {
                                    num++;
                                }
                            } catch (ArrayIndexOutOfBoundsException e1) {
                            }
                        }
                    }
                    if(num == 0)
                        isBomb[i][j] = " ";
                    else
                        isBomb[i][j] = Integer.toString(num);
                }
            }
        }
    }

    private static void bomber(String[][] array) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                double d = Math.random();
                if (d > 0.85) {
                    array[i][j] = "\uD83D\uDCA3";
                }
            }
        }
    }

    private static void display(String[][] array) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void search(String[][] isBomb, String[][] arrDisp, int m, int n){
        if(isBomb[m][n].contains(" ")){
            arrDisp[m][n] = isBomb[m][n];
            for (int i = m - 1; i < m + 2; i++) {
                for (int j = n - 1; j < n + 2; j++) {
                    try {
                        if(i == m && j == n){}
                        else if(arrDisp[i][j].contains(" ") && isBomb[i][j].contains(" ")){}
                        else {

                            arrDisp[i][j] = isBomb[i][j];
                            if (isBomb[i][j].contains(" ")) {
                                search(isBomb, arrDisp, i, j);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e1) {}
                }
            }
        }
        else if(isBomb[m][n] == "\uD83D\uDCA3"){
            arrDisp[m][n] = "\uD83D\uDCA3";
        }
        else{
            arrDisp[m][n] = isBomb[m][n];
        }
    }

    public static boolean checkForWin(String[][] isBomb, String[][] arrDisp){
        boolean m = true;
        for(int i = 0;i < 9;i++){
            for(int j = 0;j < 9;j++){
                if(arrDisp[i][j] != isBomb[i][j]){
                    if(isBomb[i][j] != "\uD83D\uDCA3"){
                        m = false;
                    }
                }
            }
        }
        return m;
    }

    public static void main(String[] args) {
        boolean b = true;
        do{
            Scanner input = new Scanner(System.in);
            String array[][] = new String[9][9];
            String output[][] = new String[9][9];
            bomber(array);
            countBomb(array);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    char sign = (char) 164;
                    output[i][j] = String.valueOf(sign);
                }
            }
            display(output);
            int r = 0,c = 0;
            boolean e = true, bbc = true;
            do{
                boolean wrin = true, ccb = true;
                while(wrin) {
                    try {
                        if(bbc == true) {
                            System.out.print("Enter your move (row[1-9] column[1-9]):");
                        } else
                            System.out.print("Enter your next move (row[1-9] column[1-9]):");
                        r = input.nextInt() - 1;
                        ccb = false;
                        c = input.nextInt() - 1;
                        ccb = true;
                        if(r>=0 && r<9 && c>=0 && c<9) {
                            wrin = false;
                        }
                        else {
                            System.out.println("The input you entered is out of bounds. Try again.\n\n");
                        }
                    } catch (InputMismatchException in) {
                        System.out.println("Input should be integer type. Please try again.\n\n");
                        if(ccb == true) {
                            input.next();
                            input.next();
                        }else{
                            input.next();
                        }
                    }
                }
                bbc = false;
                if(output[r][c] == array[r][c]){
                    System.out.println("The element that you entered is already revealed. Try again.\n\n");
                }else {
                    search(array, output, r, c);
                    display(output);

                    if (checkForWin(array, output) == true) {
                        System.out.print("You win! Congrats.");
                        e = false;
                    }
                    if (output[r][c] == "\uD83D\uDCA3")
                        System.out.print("Oops! You lose.");
                }
            } while(output[r][c] != "\uD83D\uDCA3" && e);

            System.out.print("Would you like to play again? (1/0):");
            if(input.nextInt()==0)
                b=false;
        }while(b);
    }
}