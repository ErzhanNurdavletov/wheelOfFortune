import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<Integer> scores = new ArrayList<>();

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

            System.out.println(players.get(i) + " is answering now");
            System.out.println(" ----------------------------------");
            System.out.println("Question: " + questions[randomIndex] + "?");
            System.out.println("Already found letters in the word: ");
            for (char letter : outputArray) {
                System.out.print(letter + " ");
            }
            System.out.println();
            System.out.print("Enter letter or word: ");
            String userAnswer = sc.nextLine();

            if (userAnswer.length() > 1 && userAnswer.equals(words[randomIndex])) {
                System.out.println("Game finished! " + players.get(i) + " has won.");
                break;
            }
            else if (userAnswer.length() > 1 && !userAnswer.equals(words[randomIndex])) {
                System.out.println("Wrong answer, " + players.get(i) + " leaves the game");
                players.remove(i);
                scores.remove(i);
            }
            else {
                char userAnswerLetter = userAnswer.charAt(0);

                boolean flag = false;

                for (int j = 0; j < wordArray.length; j++) {
                    flag = false;
                    if (userAnswerLetter == outputArray[j]) {
                        System.out.println("This letter was already entered");
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
                    System.out.println("There is no  " + userAnswerLetter + "  letter");
                    i++;
                }

                i = startSequence(i);
            }
        }
        if (checkScoreWin(wordArray.length)) {
            System.out.println(players.get(i) + " won, because of getting possible max points -> " + scores.get(i) + " out of " + wordArray.length * 100 + " !!!");
        }
    }
    static void saveUserNames() {
        while(true) {
            System.out.print("Enter your name(when finish, enter f): ");
            String name = sc.nextLine();
            if (name.equals("f")) {
                break;
            }
            players.add(name);
            scores.add(0);
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