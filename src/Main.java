import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

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

        String[] words = {"apple", "car", "house", "book", "ball", "sun", "tree", "school", "shop", "phone"};
        String[] questions = {"fruit", "people drive that vehicle", "people live there", "people read this", "item for playing football",
                "the star in Solar System", "gives us oxygen", "pupil study there", "we can buy goods there", "we use it for chatting"};

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

            System.out.println(" ----------------------------------");
            System.out.println(blue + players.get(i) + yellow + " is answering now" + colorStop);
            System.out.println(green + "Question: " + blue + questions[randomIndex] + "?" + colorStop);
            System.out.println(cyan + "Already found letters in the word: ");
            for (char letter : outputArray) {
                System.out.print(letter + " ");
            }
            System.out.print(colorStop);
            System.out.println();
            System.out.print(yellow + "Enter letter or word: " + colorStop);
            String userAnswer = sc.nextLine();

            if (userAnswer.length() > 1 && userAnswer.equals(words[randomIndex])) {
                System.out.println(purple + "Game finished! " + players.get(i) + " has won by entering full word !!!" + colorStop);
                break;
            }
            else if (userAnswer.length() > 1 && !userAnswer.equals(words[randomIndex])) {
                System.out.println(red + "Wrong answer, " + blue + players.get(i) + red + " leaves the game" + colorStop);
                players.remove(i);
                scores.remove(i);
            }
            else {
                char userAnswerLetter = userAnswer.charAt(0);

                boolean flag = false;

                for (int j = 0; j < wordArray.length; j++) {
                    flag = false;
                    if (userAnswerLetter == outputArray[j]) {
                        System.out.println(red + "This letter was already entered" + colorStop);
                        i++;
                        break;
                    }
                    else if (userAnswerLetter == wordArray[j]) {
                        outputArray[j] = userAnswerLetter;
                        int score = scores.get(i) + 100;
                        scores.set(i, score);
                        break;
                    }
                    else if(userAnswerLetter != wordArray[j]) {
                        flag = true;
                    }
                }

                if (flag) {
                    System.out.println(red + "There is no  " + blue + userAnswerLetter + red + "  letter" + colorStop);
                    i++;
                }

                i = startSequence(i);
            }
        }
        if (checkScoreWin(wordArray.length)) {
            System.out.println(purple + players.get(i) + " won, because of getting possible max points -> "
                    + red + scores.get(i) + " out of " + wordArray.length * 100 + purple + " !!!" + colorStop);
        }
    }
    static void saveUserNames() {
        int minTwoPlayersCount = 0;
        while(true) {
            System.out.print(cyan + "Enter your name(when finish, enter f): " + colorStop);
            String name = sc.nextLine();
            if (name.equals("f")) {
                if (minTwoPlayersCount < 2) {
                    System.out.println(red + "At least 2 players must join, add another players" + colorStop);
                }
                else {
                    break;
                }
            }
            else {
                players.add(name);
                scores.add(0);
                minTwoPlayersCount++;
            }
        }
    }
    static void showSequence() {
        Collections.shuffle(players);
        System.out.println("Sequence:");
        for (int j = 0; j < players.size(); j++) {
            System.out.println(j + 1 + " -> " + players.get(j));
        }
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