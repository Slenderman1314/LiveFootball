package ifp.project.livefootball.Match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class CreateMatchActivity extends AppCompatActivity {
    private ImageView ima1;
    private Button boton1;
    private Button boton2;
    private Spinner localTeamSpinner, guestTeamSpinner;
    private Database db;
    protected Intent changeActivity;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        db = new Database(this);

        ima1 = (ImageView) findViewById(R.id.ima1_create_match);
        boton1 = (Button) findViewById(R.id.boton1_inicioCreateMatch);
        localTeamSpinner = findViewById(R.id.spinner_localTeam);
        guestTeamSpinner = findViewById(R.id.spinner_guestTeam);
        boton2 = findViewById(R.id.registerMatchButton);
        listView = findViewById(R.id.lista1_createMatch);

        ArrayList<String> matches = db.getMatches();
        adapter = new ArrayAdapter<>(CreateMatchActivity.this, android.R.layout.simple_list_item_1, matches);
        listView.setAdapter(adapter);

        ArrayList<String> teams = db.getTeams();
        teams.add(0, "Seleccione equipo");
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(CreateMatchActivity.this, android.R.layout.simple_spinner_item, teams);
        localTeamSpinner.setAdapter(teamAdapter);
        guestTeamSpinner.setAdapter(teamAdapter);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localTeam = localTeamSpinner.getSelectedItem().toString();
                String guestTeam = guestTeamSpinner.getSelectedItem().toString();

                if (localTeam.equals("Seleccione equipo") || guestTeam.equals("Seleccione equipo")) {
                    Toast.makeText(CreateMatchActivity.this, "Por favor, selecciona los equipos", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase database = db.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("idLocalTeam", localTeam);
                    contentValues.put("idGuestTeam", guestTeam);
                    changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                    startActivity(changeActivity);
                    finish();

                    long result = database.insert("footballMatch", null, contentValues);

                    if (result != -1) {
                        Toast.makeText(CreateMatchActivity.this, "Partido registrado con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMatchActivity.this, "Error al registrar el partido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

