package com.quizapp.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HomePage extends AppCompatActivity {
    private TextView countLabel;
  //  ProgressBar mProgressBar;
    private TextView questionLabel;
    private TextView quizTimer;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    private CountDownTimer mCountDownTimer;
    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;

    static final private int QUIZ_COUNT = 5;
    static final long START_TIME_IN_MILIS = 60000;
    private long mTimeLeftinMillis = START_TIME_IN_MILIS;
    int PROGRESS_BAR_INCREMENT = 100/QUIZ_COUNT;


    private void startTimer()
    {
        mCountDownTimer = new CountDownTimer(mTimeLeftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftinMillis = millisUntilFinished;
                quizTimer.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                quizTimer.setText("DONE!");
            }
        }.start();

    }
    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    String quizData[][] = {

            //{"Question", "right answer", "choicea", "choiceb", "choicec", "choiced"}
            {"World Tourism Day is celebrated on ?", "September 27", "September 12", " September 25", "September 29"},
            {"When is the International Yoga Day celebrated ?", "June 21", "March 21", "April 22", "May 31"},
            {"'Line of Blood' is a book written by whom?", "Bairaj Khanna", "Ursula Vernon", "Amal EI-Mohtar", "Diksha Basu"},
            {"The first Indian State to go wholley organic is", "Sikkim", " Meghalaya", "Manipur", "Kerala"},
            {"Which among the following cities in India, is not located in Golden Quadrilateral Road Network?", "Chandigarh", "New Delhi", "Mumbai", "Kolkata"},
            {"Canada's capital city?", "Ottawa", "Toronto", "St.Johns", "Montreal"},
            {"Who introduced 'Green Army' for environment conservation?", "Australia", "China", "apan", "Egypt"},
            {"Number of planets in solar system?", "9", "8", "7", "11"},
            {"Biggest planet in solar system?", "Jupiter", "Neptune", "Saturn", "Uranus"},
            {"Ozone Layer Preservation Day' is celebrated on -", "16th September", "5th June", "23rd March", "21st April"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();

        countLabel = (TextView)findViewById(R.id.countLabel);
        questionLabel = (TextView)findViewById(R.id.question_text_view);
        quizTimer = (TextView)findViewById(R.id.timer);
        answerButton1 = (Button)findViewById(R.id.answer1);
        answerButton2 = (Button)findViewById(R.id.answer2);
        answerButton3 = (Button)findViewById(R.id.answer3);
        answerButton4 = (Button)findViewById(R.id.answer4);
        startTimer();


//create quizArray from quizData
        for (int i = 0; i< quizData.length; i++)
        {

            //Prepare array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]);
            tmpArray.add(quizData[i][1]);
            tmpArray.add(quizData[i][2]);
            tmpArray.add(quizData[i][3]);
            tmpArray.add(quizData[i][4]);

            //Add tmpArray to quizArray
            quizArray.add(tmpArray);
        }
        showNextQuiz();


        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomePage.this);
        builder1.setMessage("Click on OK to start the quiz.");
        builder1.setTitle("Start Quiz!!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void showNextQuiz() {

        //Update quizCountLabel
        countLabel.setText("Q" + quizCount);
        //Generate random number between 0 and 14(quizArray's size -1).
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        //Pick one quiz set.
        ArrayList<String> quiz = quizArray.get(randomNum);

        //set quetion and right answer.
        //Array format as above;
        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        //remove "Question" from quiz and Shuffle choices.
        quiz.remove(0);
        Collections.shuffle(quiz);

        //set choices
        answerButton1.setText(quiz.get(0));
        answerButton2.setText(quiz.get(1));
        answerButton3.setText(quiz.get(2));
        answerButton4.setText(quiz.get(3));

        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view) {

        //Get pushed button
        Button answerBtn = (Button) findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;

        if (btnText.equals(rightAnswer)) {

            //correct!
            alertTitle = "Correct";
            rightAnswerCount++;
        }
        else {
            //wrong
            alertTitle = "Wrong...";
        }
//alert handler


        final ProgressDialog dialog = new ProgressDialog(HomePage.this);
        dialog.setMessage("Please wait..");
        dialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
                if(quizCount == QUIZ_COUNT) {
                    //show Result
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    // mProgressBar.setProgress(0);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    startActivity(intent);
                }
                else {
                    quizCount++;
                    //  mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
                    showNextQuiz();
                }


            }
        }, 1500);

    }
}


