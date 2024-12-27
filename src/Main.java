import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<Integer> scores = new ArrayList<>();

    static String red = "\u001B[31m";
    static String green = "\u001B[32m";
    static String yellow =  "\u001B[33m";
    static String blue =  "\u001B[34m";
    static String purple = "\u001B[35m";
    static String colorStop = "\u001B[0m";
    static String cyan = "\u001B[36m";

    public static void main(String[] args) {

        String[] words = {"bread", "car", "house", "heart", "chair", "sun", "oxygen", "night", "shop", "phone"};
        String[] questions = {"A basic food made of flour, water and sold",
                "People drive this", "People live in this place", "The organ in the body that pumps blood",
                "A piece of furniture designed for sitting",
                "The star in Solar System", "A gas we need to breathe and stay alive", "The time of day when it is dark, between sunset and sunrise",
                "We can buy goods there", "we use it for chatting"};

        int randomIndex = random.nextInt(words.length);
        char[] wordArray = words[randomIndex].toCharArray();
        char[] outputArray = new char[wordArray.length];
        for (int i = 0; i < wordArray.length; i++) {
            outputArray[i] = '?';
        }

        saveUserNames();
        showSequence();
        checkUsersAnswer(wordArray, outputArray, randomIndex, words, questions);
    }
    static void checkUsersAnswer(char[] wordArray, char[] outputArray, int randomIndex, String[] words, String[] questions) {

        int i = 0;

        while(!checkScoreWin(wordArray.length) && !checkGameFinished(wordArray, outputArray)) {

            System.out.println(colorStop + "               ═════════════════════════════════════════");
            System.out.println("               " + blue + players.get(i) + yellow + " is answering now");
            System.out.println(green + "               Question: " + blue + questions[randomIndex] + "?");
            System.out.println(cyan + "               Already found letters in the word: ");
            System.out.print("               ");
            for (char letter : outputArray) {
                System.out.print(letter + "        ");
            }
            System.out.print(colorStop);
            System.out.println();
            System.out.print(yellow + "               Enter letter or word: ");
            String userAnswer = sc.nextLine().toLowerCase().trim();

            if (userAnswer.length() > 1 && userAnswer.equals(words[randomIndex])) {
                System.out.println("               Answer: " + words[randomIndex].toUpperCase());
                System.out.println(purple + "               Game finished! " + red + players.get(i) +
                        purple + " has won by entering full word !!!");
                break;
            }
            else if (userAnswer.length() > 1 && !userAnswer.equals(words[randomIndex])) {
                System.out.println(red + "               Wrong answer, " + blue + players.get(i) +
                        red + " leaves the game");
                players.remove(i);
                scores.remove(i);
            }
            else {
                char userAnswerLetter = userAnswer.charAt(0);

                boolean flag = false;

                for (int j = 0; j < wordArray.length; j++) {
                    flag = false;
                    if (userAnswerLetter == outputArray[j]) {
                        System.out.println(red + "               This letter was already entered");
                        i++;
                        break;
                    }
                    else if (userAnswerLetter == wordArray[j]) {
                        outputArray[j] = userAnswerLetter;
                        int score = scores.get(i) + 100;
                        scores.set(i, score);
                        System.out.println(yellow + "               Right letter, " + blue + players.get(i) +
                                yellow + " answers again");
                        break;
                    }
                    else if(userAnswerLetter != wordArray[j]) {
                        flag = true;
                    }
                }

                if (flag) {
                    System.out.println(red + "               There is no  " + blue + userAnswerLetter +
                            red + "  letter");
                    i++;
                }

                i = startSequence(i);
            }
            if (players.size() < 2) {
                System.out.println("               Answer: " + words[randomIndex].toUpperCase());
                System.out.println(yellow + "               Only one player left, so " + blue +
                        players.getFirst() + yellow + " won !!!");
                break;
            }
        }
        if (checkScoreWin(wordArray.length) && checkLettersFound(outputArray)) {
            System.out.println("               " + purple + players.get(i) + " GOT possible max points -> "
                    + red + scores.get(i) + " out of " + wordArray.length * 100 + purple +
                    " !!!" + colorStop);
            System.out.println(cyan + "               BUT" + red + " other" + cyan + " players have chance ↓");

            boolean flag = true;
            for (int j = 0; j < players.size(); j++) {
                if (j == i) continue;
                else {
                    System.out.print("               " + blue + players.get(j) + yellow + " has LAST opportunity to win by entering full word: ");
                    String lastAnswer = sc.nextLine().toLowerCase().trim();
                    if (lastAnswer.equals(words[randomIndex])) {
                        System.out.println("               " + red + players.get(j) + green + " used own last chance and WON !!!");
                        flag = false;
                        break;
                    }
                    else {
                        System.out.println("               " + red + players.get(j) + green + " answered wrong and left the game");
                        players.remove(j);
                        scores.remove(j);
                        j--;
                    }
                }
            }
            if (flag) {
                System.out.println(red + "               Nobody used own chance to win, so");
                System.out.println("               " + purple + players.get(i) + " won, because of getting possible max points -> "
                        + red + scores.get(i) + " out of " + wordArray.length * 100 + purple + " !!!");
                System.out.println("               Answer: " + words[randomIndex].toUpperCase());
            }
        }
        else if (checkScoreWin(wordArray.length)) {
            System.out.println("               Answer: " + words[randomIndex].toUpperCase());
            System.out.println("               " + purple + players.get(i) + " win because FIND word and GOT possible max points -> "
                    + red + scores.get(i) + " out of " + wordArray.length * 100 + purple +
                    " !!!" + colorStop);
        }
    }
    static void saveUserNames() {
        int minTwoPlayersCount = 0;
        while(true) {
            System.out.print(cyan + "               Enter your name(when finish, enter f): " + colorStop);
            String name = sc.nextLine();
            if (name.equals("f")) {
                if (minTwoPlayersCount < 2) {
                    System.out.println(red + "               At least 2 players must join, add another players" + colorStop);
                }
                else {
                    break;
                }
            }
            else {
                boolean flag = false;
                for (String player : players) {
                    if (name.equals(player)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    System.out.println("               " + name + " name was already entered, try again");
                    continue;
                }
                players.add(name);
                scores.add(0);
                minTwoPlayersCount++;
            }
        }
    }
    static void showSequence() {
        Collections.shuffle(players);
        System.out.println("               Sequence:");
        for (int j = 0; j < players.size(); j++) {
            System.out.println("               " + (j + 1) + " -> " + players.get(j));
        }
    }
    static boolean checkLettersFound(char[] outputArray) {
        for (char letter : outputArray) {
            if (letter == '?') {
                return true;
            }
        }
        return false;
    }
    static boolean checkScoreWin(int possibleScore) {
        int max = scores.getFirst();
        for (Integer score : scores) {
            if (score > max) {
                max = score;
            }
        }
        return max > ((possibleScore * 100) / 2);
    }
    static boolean checkGameFinished(char[] wordArray, char[] outputArray) {
        for (int j = 0; j < wordArray.length; j++) {
            if (wordArray[j] != outputArray[j]) {
                return false;
            }
        }
        return true;
    }
    static int startSequence(int sequenceIndex) {
        if (sequenceIndex == players.size()) return 0;
        return sequenceIndex;
    }
}