package com.example.studyjapanese;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.studyjapanese.data.WordContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity {

    private static String[] questions, answers;
    private Random rnd;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final Button choice1 = (Button) findViewById(R.id.button1);
        final Button choice2 = (Button) findViewById(R.id.button2);
        final Button choice3 = (Button) findViewById(R.id.button3);
        final Button choice4 = (Button) findViewById(R.id.button4);
        final Button nextButton = (Button) findViewById(R.id.button_next);
        final Button finishButton = (Button) findViewById(R.id.button_finish);
        TextView questionText = (TextView) findViewById(R.id.text_view_word);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        questions = intent.getStringArrayExtra("questions");
        answers = intent.getStringArrayExtra("answers");
        totalQuestions = intent.getIntExtra("totalQuestions", 1);

        shuffleArrays(questions, answers);

        questionText.setText(questions[0]);
        fillButtonsWithAnswer(answers, choice1, choice2, choice3, choice4);

        nextButton.setVisibility(View.INVISIBLE);

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attempt = (String) choice1.getText();
                if(attempt.equals(answers[0])){
                    choice1.setBackgroundColor(Color.GREEN);
                    choice2.setClickable(false);
                    choice3.setClickable(false);
                    choice4.setClickable(false);
                    //currentStatus = QuizStatus.WIN;
                }
                else{
                    choice1.setBackgroundColor(Color.RED);
                    choice2.setClickable(false);
                    choice3.setClickable(false);
                    choice4.setClickable(false);
                    if(choice2.getText().equals(answers[0])) choice2.setBackgroundColor(Color.GREEN);
                    if(choice3.getText().equals(answers[0])) choice3.setBackgroundColor(Color.GREEN);
                    if(choice4.getText().equals(answers[0])) choice4.setBackgroundColor(Color.GREEN);
                    //currentStatus = QuizStatus.LOSE;
                }
                if(totalQuestions == 0){
                    nextButton.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                } else{
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attempt = (String) choice2.getText();
                if(attempt.equals(answers[0])){
                    choice2.setBackgroundColor(Color.GREEN);
                    choice1.setClickable(false);
                    choice3.setClickable(false);
                    choice4.setClickable(false);
                    //currentStatus = QuizStatus.WIN;
                }
                else{
                    choice2.setBackgroundColor(Color.RED);
                    choice1.setClickable(false);
                    choice3.setClickable(false);
                    choice4.setClickable(false);
                    if(choice1.getText().equals(answers[0])) choice1.setBackgroundColor(Color.GREEN);
                    if(choice3.getText().equals(answers[0])) choice3.setBackgroundColor(Color.GREEN);
                    if(choice4.getText().equals(answers[0])) choice4.setBackgroundColor(Color.GREEN);
                    //currentStatus = QuizStatus.LOSE;
                }
                if(totalQuestions == 0){
                    nextButton.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                } else{
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attempt = (String) choice3.getText();
                if(attempt.equals(answers[0])){
                    choice3.setBackgroundColor(Color.GREEN);
                    choice1.setClickable(false);
                    choice2.setClickable(false);
                    choice4.setClickable(false);
                    //currentStatus = QuizStatus.WIN;
                }
                else{
                    choice3.setBackgroundColor(Color.RED);
                    choice1.setClickable(false);
                    choice2.setClickable(false);
                    choice4.setClickable(false);
                    if(choice1.getText().equals(answers[0])) choice1.setBackgroundColor(Color.GREEN);
                    if(choice2.getText().equals(answers[0])) choice2.setBackgroundColor(Color.GREEN);
                    if(choice4.getText().equals(answers[0])) choice4.setBackgroundColor(Color.GREEN);
                    //currentStatus = QuizStatus.LOSE;
                }
                if(totalQuestions == 0){
                    nextButton.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                } else{
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attempt = (String) choice4.getText();
                if(attempt.equals(answers[0])){
                    choice4.setBackgroundColor(Color.GREEN);
                    choice1.setClickable(false);
                    choice2.setClickable(false);
                    choice3.setClickable(false);
                    //currentStatus = QuizStatus.WIN;
                }
                else{
                    choice4.setBackgroundColor(Color.RED);
                    choice1.setClickable(false);
                    choice2.setClickable(false);
                    choice3.setClickable(false);
                    if(choice1.getText().equals(answers[0])) choice1.setBackgroundColor(Color.GREEN);
                    if(choice2.getText().equals(answers[0])) choice2.setBackgroundColor(Color.GREEN);
                    if(choice3.getText().equals(answers[0])) choice3.setBackgroundColor(Color.GREEN);
                    //currentStatus = QuizStatus.LOSE;
                }
                if(totalQuestions == 0){
                    nextButton.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                } else{
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                intent.putExtra("questions", questions);
                intent.putExtra("answers", answers);
                intent.putExtra("totalQuestions", totalQuestions-1);
                startActivity(intent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, WordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the MainActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void shuffleArrays(String[] question, String[] answer){
        rnd = new Random();
        if(question.length == answer.length) {
            for(int i = question.length - 1; i >= 0; i--){
                int index = rnd.nextInt(i + 1);
                String tmpQuestion = question[i];
                question[i] = question[index];
                question[index] = tmpQuestion;
                String tmpAnswer = answer[i];
                answer[i] = answer[index];
                answer[index] = tmpAnswer;
            }
        }
        else return;
    }

    private void fillButtonsWithAnswer(String[] answerSet, Button ... buttons){
        HashSet<Integer> usedButton = new HashSet<Integer>();
        HashSet<Integer> usedAnswer = new HashSet<Integer>();
        rnd = new Random();
        int randomValue = rnd.nextInt(buttons.length);
        while(usedButton.contains(randomValue)) randomValue = rnd.nextInt(buttons.length);
        usedButton.add(randomValue);

        buttons[randomValue].setText(answerSet[0]);

        for(int index = buttons.length - 1; index >= 0; index--){
            if(index == randomValue) continue;
            else{
                int randomAnswer = rnd.nextInt(answerSet.length);
                while(usedAnswer.contains(randomAnswer) || randomAnswer == 0) randomAnswer = rnd.nextInt(answerSet.length);
                usedAnswer.add(randomAnswer);
                buttons[index].setText(answerSet[randomAnswer]);
            }
        }
    }

    private String[] removeFirstElement(String[] array){
        List<String> arr = new ArrayList<String>();

        if(array.length == 1) return array;

        for(int i = 1; i < array.length; i++){
            arr.add(array[i]);
        }
        return arr.toArray(new String[0]);
    }

}
