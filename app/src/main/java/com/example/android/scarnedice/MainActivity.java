package com.example.android.scarnedice;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollBtn = findViewById(R.id.roll_button);
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTurn();
            }
        });

        Button resetBtn = findViewById(R.id.reset_button);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        Button holdBtn = findViewById(R.id.hold_button);
        holdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });
    }

    private int userGlobalScore = 0;
    private int userRoundScore = 0;
    private int computerGlobalScore = 0;
    private int computerRoundScore = 0;
    Handler handler = new Handler();
    private Random rand = new Random();

    public void userTurn() {
        int roll = rollDice();
        TextView gameStatus = (TextView)findViewById(R.id.game_status_view);
        if (roll > 1) {
            userRoundScore += roll;
            gameStatus.setText("Your score: " + userGlobalScore + " computer score: " + computerGlobalScore + " your turn score: " + userRoundScore);
        } else {
            userRoundScore = 0;
            gameStatus.setText("Your score: " + userGlobalScore + " computer score: " + computerGlobalScore + " your turn score: " + userRoundScore);
            computerTurn();
        }
    }

    public int rollDice() {
        int diceRoll = rand.nextInt(6) + 1;
        ImageView diceRollView = (ImageView)findViewById(R.id.dice_roll_image);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(diceRollView, "rotation", 0f, 50f, 0f, -20f, 0f);
        rotate.setRepeatCount(20); // repeat the loop 20 times
        rotate.setDuration(100); // animation play time 100 ms
        rotate.start();
        switch (diceRoll) {
            case 1:
                diceRollView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceRollView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceRollView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceRollView.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceRollView.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceRollView.setImageResource(R.drawable.dice6);
                break;
        }
        return diceRoll;
    }

    public void reset() {
        TextView gameStatus = (TextView)findViewById(R.id.game_status_view);
        userGlobalScore = 0;
        userRoundScore = 0;
        computerGlobalScore = 0;
        computerRoundScore = 0;
        gameStatus.setText("Your score: 0 computer score: 0 your turn score: 0");
    }

    public void hold() {
        TextView gameStatus = (TextView)findViewById(R.id.game_status_view);
        userGlobalScore += userRoundScore;
        userRoundScore = 0;
        gameStatus.setText("Your score: " + userGlobalScore + " computer score: " + computerGlobalScore + " your turn score: " + 0);
    }

    public void computerHold() {
        computerGlobalScore += computerRoundScore;
    }

    public void computerTurn() {
        // Disable both roll and hold buttons during computers turn
        Button rollBtn = findViewById(R.id.roll_button);
        rollBtn.setEnabled(false);

        Button holdBtn = findViewById(R.id.hold_button);
        holdBtn.setEnabled(false);

        TextView gameStatus = (TextView)findViewById(R.id.game_status_view);

        int roll = rollDice();
        if (roll > 1) {
            computerRoundScore += roll;
            if (computerRoundScore > 20) {
                computerHold();
                rollBtn.setEnabled(true);
                holdBtn.setEnabled(true);
                gameStatus.setText("Your score: " + userGlobalScore + " computer score: " + computerGlobalScore + " your turn score: " + 0 + "\n Computer holds " + computerRoundScore);
            } else {
                gameStatus.setText("The computer rolled a " + roll + " it will roll again.");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                }, 3000);
            }
        } else {
            computerRoundScore = 0;
            gameStatus.setText("Your score: " + userGlobalScore + " computer score: " + computerGlobalScore + " your turn score: " + 0 + "\n Computer rolled a 1");
            rollBtn.setEnabled(true);
            holdBtn.setEnabled(true);
        }
    }
}
