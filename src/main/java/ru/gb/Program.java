package ru.gb;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static final int WIN_COUNT = 4;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {
        do {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Хотите сыграть еще раз? (Y - да): ");
        } while (scanner.next().equalsIgnoreCase("Y"));
    }

    /**
     * Инициализация игрового поля
     */
    static void initialize(){
        fieldSizeY = 5;
        fieldSizeX = 5;

        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    private static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++){
            System.out.print("-" + (i+1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++){
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++){
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(){
        int x;
        int y;

        do {
            System.out.print("Введите координаты хода X и Y (от 1 до 5)\nчерез пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[y][x] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn(){
        int x;
        int y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));

        field[y][x] = DOT_AI;
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     */
    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * Проверка доступности ячейки игрового поля
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Метод проверки состояния игры
     * @param dot - фишка игрока
     * @param s - победный слоган
     * @return - результат проверки состояния игры
     */
    static boolean checkGameState(char dot, String s) {
        if (checkWin(dot)){
            System.out.println(s);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    /**
     * Проверка на ничью
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка победы по каждому из полей: X и Y
     * @param dot - фишка игрока
     * @return - признак победы
     */
    private static boolean checkWin(char dot) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == dot && check(x, y, dot)) return true;
            }
        }
        return false;
    }

    /**
     * Проверка победы, объединяющая направления: горизонталь, вертикаль,
     * диагональ вверх вправо, диагональ вниз вправо
     *
     * @param dot - фишка игрока
     * @return - признак победы
     */
    static boolean check(int x, int y, char dot) {
        if ((y + Program.WIN_COUNT) <= fieldSizeY) {
            if (checkHorizontal(x, y, dot)) return true;
        }
        if ((x + Program.WIN_COUNT) <= fieldSizeX) {
            if (checkVertical(x, y, dot)) return true;
        }
        if ((y + Program.WIN_COUNT) <= fieldSizeY && (x - Program.WIN_COUNT + 1) >= 0) {
            if (checkDiagonalUpRight(x, y, dot)) return true;
        }
        if ((x + Program.WIN_COUNT) <= fieldSizeX && (y + Program.WIN_COUNT) <= fieldSizeY) {
            return checkDiagonalDownRight(x, y, dot);
        }
        return false;
    }

    /**
     * Метод проверки победы по направлению: горизонталь
     *
     * @param dot - фишка игрока
     * @return - признак победы
     */
    static boolean checkHorizontal(int x, int y, char dot){
        int dotCount = 0;
        for (int i = 0; i < Program.WIN_COUNT; i++){
            if (field[x][y + i] == dot)
                dotCount++;
            }
        return (dotCount == Program.WIN_COUNT);
    }

    /**
     * Метод проверки победы по направлению: вертикаль
     *
     * @param dot - фишка игрока
     * @return - признак победы
     */
    static boolean checkVertical(int x, int y, char dot){
        int dotCount = 0;
            for (int i = 0; i < Program.WIN_COUNT; i++){
                if (field[x + i][y] == dot)
                    dotCount++;
            }
        return (dotCount == Program.WIN_COUNT);
    }

    /**
     * Метод проверки победы по направлению: диагональ вверх вправо
     *
     * @param dot - фишка игрока
     * @return - признак победы
     */
    static boolean checkDiagonalUpRight(int x, int y, char dot){
        int dotCount = 0;
        for (int i = 0; i < Program.WIN_COUNT; i++) {
            if (field[x - i][y + i] == dot) dotCount++;
        }
            return (dotCount == Program.WIN_COUNT);
    }

    /**
     * Метод проверки победы по направлению: диагональ вниз вправо
     *
     * @param dot - фишка игрока
     * @return - признак победы
     */
    static boolean checkDiagonalDownRight(int x, int y, char dot){
        int dotCount = 0;
        for (int i = 0; i < Program.WIN_COUNT; i++){
            if (field[x + i][y + i] == dot)
                dotCount++;
        }
        return (dotCount == Program.WIN_COUNT);
    }
}