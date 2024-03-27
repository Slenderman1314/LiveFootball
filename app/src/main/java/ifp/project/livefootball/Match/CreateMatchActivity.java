package ifp.project.livefootball.Match;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import ifp.project.livefootball.R;

public class CreateMatchActivity extends AppCompatActivity {
    private ImageView ima1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        ima1 = (ImageView) findViewById(R.id.ima1_create_match);
    }


}