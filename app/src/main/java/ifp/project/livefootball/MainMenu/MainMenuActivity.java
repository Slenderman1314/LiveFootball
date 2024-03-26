package ifp.project.livefootball.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import ifp.project.livefootball.R;
import java.util.ArrayList;
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.Match.EditMatchActivity;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.Team.ListTeamActivity;

public class MainMenuActivity extends AppCompatActivity {
    private Database db;
    private ArrayList <String> arrayList= new ArrayList<String>();
    private ArrayAdapter <String> arrayAdapter;
    private ImageView ima1;
    private String textContent;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ima1 = (ImageView) findViewById(R.id.ima1_menu);

        db= new Database(this);
        arrayList= db.getMatches();

        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Teams_options_main_menu) {
            changeActivity = new Intent(MainMenuActivity.this, ListTeamActivity.class);
            finish();
            startActivity(changeActivity);
            return true;
        } else if (item.getItemId() == R.id.Exit_options_main_menu) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}