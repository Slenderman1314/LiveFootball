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
import android.widget.Spinner;

import ifp.project.livefootball.Account.LogInActivity;
import ifp.project.livefootball.Account.RegisterActivity;
import ifp.project.livefootball.Player.EditPlayerActivity;
import ifp.project.livefootball.Player.PlayerCreateActivity;
import ifp.project.livefootball.R;
import java.util.ArrayList;
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.Match.EditMatchActivity;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.Team.CreateTeamActivity;
import ifp.project.livefootball.Team.EditTeamActivity;
import ifp.project.livefootball.Team.ListTeamActivity;

public class MainMenuActivity extends AppCompatActivity {
    private Database db;
    private ArrayList <String> arrayList= new ArrayList<String>();
    private ArrayAdapter <String> arrayAdapter;
    private ImageView ima1;
    private String selectedOption;
    private Spinner spin1;
    private Button boton1;
    private ArrayAdapter<CharSequence> adapter;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ima1 = (ImageView) findViewById(R.id.ima1_menu);
        spin1 = findViewById(R.id.spin1_menu);
        boton1 = findViewById(R.id.boton1_menu);
        db= new Database(this);
        arrayList= db.getMatches();

        adapter = ArrayAdapter.createFromResource(this,
                R.array.main_menu_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        boton1 = findViewById(R.id.boton1_menu);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (selectedOption) {
                    case "Crear partido":
                        // Start activity to create match
                        changeActivity = new Intent(MainMenuActivity.this, CreateMatchActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Crear equipo":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, CreateTeamActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Editar partido":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, EditMatchActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Editar equipo":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, EditTeamActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Crear jugador":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, PlayerCreateActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Editar jugador":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, EditPlayerActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    case "Mostrar equipo":
                        // Start activity to create team
                        changeActivity = new Intent(MainMenuActivity.this, ListTeamActivity.class);
                        finish();
                        startActivity(changeActivity);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}