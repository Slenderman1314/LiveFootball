package ifp.project.livefootball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import ifp.project.livefootball.Account.LogInActivity;
import ifp.project.livefootball.MainMenu.MainMenuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask myTimertask = new TimerTask() {
            @Override
            public void run() {
                Intent changeActivity= new Intent(MainActivity.this, LogInActivity.class);
                finish();
                startActivity(changeActivity);
            }
        };

        Timer timer = new Timer();
        timer.schedule(myTimertask, 3000);
    }
}