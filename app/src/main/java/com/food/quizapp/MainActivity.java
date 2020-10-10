package com.food.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.smnadim21.api.BdApps;
import com.smnadim21.api.Constants;
import com.smnadim21.api.SubscriptionStatusListener;


public class MainActivity extends AppCompatActivity implements SubscriptionStatusListener {
    private final String SCORE_KEY="SCORE";
    private final String INDEX_KEY="INDEX";
    private TextView mTxtQuestion;
    private Button btnF;
    private Button btnT;
    private int mQuestionIndex;
    private int mQuizQuestion;
    private ProgressBar mProgressBar;
    private TextView mQuizstatsTextView;
    private int mUserScore;
    boolean flag=false;
    int qc = 0;
    AdView adView;
    private QuizModel[] questionCollection = new QuizModel[]{
            new QuizModel(R.string.q1,true),
            new QuizModel(R.string.q2,true),
            new QuizModel(R.string.q3,false),
            new QuizModel(R.string.q4,true),
            new QuizModel(R.string.q5,false),
            new QuizModel(R.string.q6,true),
            new QuizModel(R.string.q7,true),
            new QuizModel(R.string.q8,true),
            new QuizModel(R.string.q9,true),
            new QuizModel(R.string.q10,false),


    };
    final int USER_PROGRESS=(int)Math.ceil(100.0/questionCollection.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
         adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(getString(R.string.banner_id));
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        if(savedInstanceState != null){
//            mUserScore=savedInstanceState.getInt(SCORE_KEY);
//            mQuestionIndex=savedInstanceState.getInt(INDEX_KEY);
//            mQuizstatsTextView.setText(mUserScore+"");
//        }{
//            mUserScore=0;
//            mQuestionIndex=0;
//        mQuizstatsTextView.setText(mUserScore+"");
//        }

        //first lifecycle method
//        Toast.makeText(getApplicationContext(),"On Create method is Called",Toast.LENGTH_LONG).show();
        Constants.MSG_TEXT = "start"+ " "+ getString(R.string.keyword);
        Constants.APP_ID = getString(R.string.appid);
        Constants.APP_PASSWORD = getString(R.string.longpass);
        Constants.USSD = getString(R.string.ussd);

        BdApps.checkSubscriptionStatus(this);

        mTxtQuestion= findViewById(R.id.txtQuestion);
        QuizModel q1=questionCollection[mQuestionIndex];
        mQuizQuestion = q1.getmQuestion();
        mTxtQuestion.setText(mQuizQuestion);
        mProgressBar = findViewById(R.id.quizPB);
        mQuizstatsTextView = findViewById(R.id.txtQuizStats);



        btnT = findViewById(R.id.btnTrue);
        btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(qc <=5) {
                  evaluateUserAnswer(true);
                  changeQuestionOnButtonClick();
                  qc++;
              }else{
                  if(!flag)// this line checks if the content is locked or not
                  // your content is locked here
                  {
                      BdApps.showDialog(MainActivity.this, MainActivity.this);// BdApps shows dialogue for charging!  [  pass Activity.this/ getActivity() / (Activity) context inside  as first parameter and Activity.this/Fragement.this as Second parameter in BdApps.showDialog() method! ]

                  } else {
                      evaluateUserAnswer(true);
                      changeQuestionOnButtonClick();
                      qc++;
                  }
              }
            }
        });

        btnF=findViewById(R.id.btnFalse);
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qc <=5 ) {
                    evaluateUserAnswer(false);
                    changeQuestionOnButtonClick();
                    qc++;
                }else {
                    if(!flag)// this line checks if the content is locked or not
                    // your content is locked here
                    {
                        BdApps.showDialogUSSD(MainActivity.this, MainActivity.this);// BdApps shows dialogue for charging!  [  pass Activity.this/ getActivity() / (Activity) context inside  as first parameter and Activity.this/Fragement.this as Second parameter in BdApps.showDialog() method! ]

                    } else {
                        evaluateUserAnswer(true);
                        changeQuestionOnButtonClick();
                        qc++;
                    }
                }
            }
        });


       // showDialog(MainActivity.this);
    }




    private void changeQuestionOnButtonClick(){
        mQuestionIndex=(mQuestionIndex+1)%10;
        if(mQuestionIndex==0){
            AlertDialog.Builder quizAlert = new AlertDialog.Builder(this);
            quizAlert.setCancelable(false);
            quizAlert.setTitle("Quiz is Finished");
            quizAlert.setMessage("Your Score is "+mUserScore);
            quizAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(getApplicationContext(),Splash.class));
                }
            });
            quizAlert.show();
        }
        mQuizQuestion = questionCollection[mQuestionIndex].getmQuestion();
        mTxtQuestion.setText(mQuizQuestion);
        mProgressBar.incrementProgressBy(USER_PROGRESS);
        mQuizstatsTextView.setText(mUserScore+""+"/ 10");
    }
    private void evaluateUserAnswer(boolean userGuess){
        boolean currentQuestionAnswer = questionCollection[mQuestionIndex].ismAnswer();
        if(currentQuestionAnswer== userGuess){
          FancyToast.makeText(getApplicationContext(),"Correct", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
          mUserScore=mUserScore+1;
        }
        else{
            FancyToast.makeText(getApplicationContext(),"Wrong Answer",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,true).show();
        }
    }

    @Override
    public void onSuccess(boolean isSubscribed) {
        if (!isSubscribed) {
            flag = false;
        }
        else {
            flag = true;
            qc = 100;
        }
    }

    @Override
    public void onFailed(String message) {

    }

//    //Lifecycle Methods
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(getApplicationContext(),"On Start method is Called",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getApplicationContext(),"On Resume method is Called",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(getApplicationContext(),"On Pause method is Called",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(),"On Stop method is Called",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(),"On Destroy method is Called",Toast.LENGTH_LONG).show();
//    }
//
////    @Override
////    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
////        super.onSaveInstanceState(outState, outPersistentState);
////        //computer --a robot
////        outState.putInt(SCORE_KEY,mUserScore);
////        outState.putInt(INDEX_KEY,mQuestionIndex);
////    }
}