package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmin.charging.AdsLib;
import com.shashank.sony.fancytoastlib.FancyToast;

import static com.sharmin.charging.SP.getSubCode;
import static com.sharmin.charging.SP.setSubCode;

public class MainActivity extends AppCompatActivity {
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
    AdsLib adsLib;
    int qc = 0;
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

              if(qc <=1000) {
                  evaluateUserAnswer(true);
                  changeQuestionOnButtonClick();
                  qc++;
              }else{
               //   showDialog(MainActivity.this);
              }
            }
        });

        btnF=findViewById(R.id.btnFalse);
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qc <=1000 ) {
                    evaluateUserAnswer(false);
                    changeQuestionOnButtonClick();
                    qc++;
                }else {
                 //   showDialog(MainActivity.this);
                }
            }
        });


        adsLib= ChargingInstance.getAdsLib();
        adsLib.checkSubStatus(getSubCode());

       // showDialog(MainActivity.this);
    }



    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.sub);




        final TextView textView_sub = dialog.findViewById(R.id.textView_sub);
        final TextView textView_sub1 = dialog.findViewById(R.id.textView_sub1);

        Button button_s_daily = dialog.findViewById(R.id.button_s_daily);
        Button button_s_daily_api = dialog.findViewById(R.id.button_s_daily_api);
        final Button bt_send_sms = dialog.findViewById(R.id.bt_send_sms);
        final Button submit_code = dialog.findViewById(R.id.submit_code);

        final LinearLayout ll_sub = dialog.findViewById(R.id.ll_sub);
        final LinearLayout ll_sub_1 = dialog.findViewById(R.id.ll_sub_1);
        final EditText otp_code = dialog.findViewById(R.id.otp_code);


        button_s_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView_sub.setText("সাবস্ক্রাইব করতে আপনার মোবাইল নাম্বার দিন");
                textView_sub1.setText("শুধুমাত্র রবি এবং এয়ারটেল গ্রাহকদের জন্য");
                // ll_sub.setVisibility(View.VISIBLE);
                ll_sub_1.setVisibility(View.GONE);
                bt_send_sms.setVisibility(View.VISIBLE);
                adsLib.subscribe();

            }
        });

        button_s_daily_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call sub api
            }
        });

        bt_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:21213");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "start "+ ChargingInstance.MSG_TEXT);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });


        submit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).show();
                setSubCode(otp_code.getText().toString().isEmpty() ? "111111" : otp_code.getText().toString());
                adsLib.checkSubStatus(otp_code.getText().toString().isEmpty() ? "111111" : otp_code.getText().toString());
                qc = 100;
                dialog.dismiss();
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.video_ad);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        dialog.show();

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