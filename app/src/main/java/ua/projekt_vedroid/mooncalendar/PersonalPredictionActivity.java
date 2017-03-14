package ua.projekt_vedroid.mooncalendar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class PersonalPredictionActivity extends AppCompatActivity {

    private TextView textView;

    private static String result = "Null";
    private static int d;
    private static int m;
    private static int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_prediction);

        initThread();

        textView = (TextView) findViewById(R.id.textViewPP);
        textView.setText(Html.fromHtml(result));
    }

    private void initThread() {                                 //Инициализация
        if (MainActivity.mSettings.contains(MainActivity.APP_PREFERENCES_BDAY)) {
            d = MainActivity.mSettings.getInt(MainActivity.APP_PREFERENCES_BDAY, d);
        }
        if (MainActivity.mSettings.contains(MainActivity.APP_PREFERENCES_BMONTH)) {
            m = MainActivity.mSettings.getInt(MainActivity.APP_PREFERENCES_BMONTH, m);
        }
        if (MainActivity.mSettings.contains(MainActivity.APP_PREFERENCES_BYEAR)) {
            y = MainActivity.mSettings.getInt(MainActivity.APP_PREFERENCES_BYEAR, y);
        }
        if (isOnline()) {
            URLCon uc = new URLCon(MainActivity.URL, d, m, y);
            uc.start();                                         //Запуск потока
            waitThread(uc);                                     //Ожидание завершения потока
        } else {
            result = getResources().getString(R.string.text_no_internet);
        }
    }

    private void waitThread(Thread thread) {
        if (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onClickBack(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        textView.setText(R.string.text_wait);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public static void setResult(String inputStr) {
        result = inputStr;
    }
}
