package wordspuzzle;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*; 


class WordsBank {
    private final static String[] words = { "Adventure", "Hungary", "Pizza", "Madrid", "Flower",
            "Chicken", "Romania", "Denmark", "Australia" };

    private final Random rand = new Random();

    public String getRandomWord() {
        return words[rand.nextInt(words.length)];
    }
}

class ChosenWord {

    private String word;
    private boolean[] chars_guessed; 

    public ChosenWord(String word){
        this.word = word.toLowerCase();
        
        chars_guessed = new boolean[word.length()];
    }

    public boolean isEntireWordGuessed() {

        for (boolean b : chars_guessed) {
            if (!b)
                return false;
        }

        return true;
    }

    public void charGuess(char guess) {
        int index = word.indexOf(guess);
        while (index >= 0) {
            chars_guessed[index] = true; 
            index = word.indexOf(guess, index + 1);
        }
    }

    @Override
    public String toString(){
        StringBuilder formatted_word = new StringBuilder();

        for(int index = 0; index < word.length(); index++) {
            if (chars_guessed[index]) {
                formatted_word.append(word.charAt(index));
            } else {
                formatted_word.append('_');
            }

            formatted_word.append(' ');
        }

        return formatted_word.toString();
    }
}

class Game {

    private final static String letters = "abcdefghijklmnopqrstuvwxyz";

    private final WordsBank words_bank = new WordsBank();
    private final JFrame frame = new JFrame("Guess the Word");
    private final JLabel puzzle_word;
    private final ArrayList<JButton> letter_buttons = new ArrayList<>();

    private int number_guesses;
    private ChosenWord chosen_word;

    Game() {
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        puzzle_word = new JLabel("Puzzle: ");
        panel.add(puzzle_word, BorderLayout.PAGE_START);

        JPanel grid = new JPanel(new GridLayout(0, 7));
        for (int i=0; i<letters.length(); i++) {
            String letter = letters.substring(i, i+1);
            JButton btn = new JButton(letter);
            btn.setActionCommand(letter);
            btn.addActionListener(this::guessLetter);
            letter_buttons.add(btn);
            grid.add(btn);
        }
        panel.add(grid, BorderLayout.CENTER);

        JButton btn = new JButton("Start a new Game");
        panel.add(btn, BorderLayout.PAGE_END);
        btn.addActionListener(ActionEvent -> this.reset());

        frame.setContentPane(panel);

        reset();
        frame.setVisible(true);
    }

    private void reset() {
        chosen_word = new ChosenWord(words_bank.getRandomWord());
        number_guesses = 0;

        for(JButton btn : letter_buttons) {
            btn.setEnabled(true);
        }

        update_game_state();
     }

    private void guessLetter(ActionEvent evt) {
        char guessed_letter = evt.getActionCommand().charAt(0);
        handleUserLetterGuess(guessed_letter);

        JButton button = (JButton) evt.getSource();
        button.setEnabled(false);

        if (chosen_word.isEntireWordGuessed()) {
            for (JButton btn : letter_buttons) {
                btn.setEnabled(false);
            }
        }
    }

    private void handleUserLetterGuess(char guessed_char){

        number_guesses++;
        chosen_word.charGuess(guessed_char);
        update_game_state();
    }

    private void update_game_state() {
        puzzle_word.setText("Puzzle: " + chosen_word + ", guesses: "+ number_guesses);      
    }
}

public class Main {

    public static void main(String[] args) {
    	new Game();
      
     
    }
}