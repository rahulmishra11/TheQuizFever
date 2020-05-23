package com.example.thequizfever;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thequizfever.User_Info.User_Info;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;


public class homepage extends AppCompatActivity {


    @BindView(R.id.profile)
    TextView profile;

    @BindView(R.id.score)
    TextView score;

    @BindView(R.id.start_quiz)
    TextView startQuiz;

    @BindView(R.id.constraint_score)
    ConstraintLayout bottomscore;

    @BindView(R.id.constraint_profile)
    ConstraintLayout bottom_profile;

    @BindView(R.id.highscore)
    TextView textViewHighscore;

    @BindView(R.id.cross_image)
    ImageView image_cross;

    @BindView(R.id.cross_image2)
    ImageView image_cross2;

    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.user_phone)
    TextView user_phone;

    @BindView(R.id.log_out)
    TextView log_out;

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private int highscore;
    BottomSheetBehavior sheetBehavior1,sheetBehavior2;
    boolean isSheetClosed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        Paper.init(this);
       startQuiz.setOnClickListener(v -> {
                    Intent intent = new Intent(this, QuizActivity.class);
                      startActivityForResult(intent, REQUEST_CODE_QUIZ);
                });

       log_out.setOnClickListener(v-> {
           Paper.book().destroy();
           startActivity(new Intent(this,MainActivity.class));
       });

        loadHighscore();

        sheetBehavior1 = BottomSheetBehavior.from(bottomscore);
        sheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        if (!isSheetClosed)
                            sheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        if (!isSheetClosed)
                            sheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior2 = BottomSheetBehavior.from(bottom_profile);
        sheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        if (!isSheetClosed)
                            sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        if (!isSheetClosed)
                            sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        score.setOnClickListener(v -> homepage.this.showSheet());
        profile.setOnClickListener(v -> homepage.this.showSheet2());
        image_cross.setOnClickListener(v -> homepage.this.hideSheet());
        image_cross2.setOnClickListener(v -> homepage.this.hideSheet2());

        String Userphone = Paper.book().read(User_Info.UserPhoneKey);
       // String Username = Paper.book().read(Prevalent.UserPhoneKey);
        user_phone.setText("PHONE: "+Userphone);
        //String st=Paper.book().read(Prevalent.high_score);
        //textViewHighscore.setText(st);


    }

    private void showSheet2() {
        isSheetClosed = false;
        sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void hideSheet() {
        isSheetClosed = true;
        sheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private void hideSheet2() {
        isSheetClosed = true;
        sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void showSheet() {
        isSheetClosed = false;
        sheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
        //Paper.book().write(Prevalent.high_score, highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);
       // Paper.book().write(Prevalent.high_score, highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

}

