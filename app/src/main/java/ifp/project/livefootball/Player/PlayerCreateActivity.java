package ifp.project.livefootball.Player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.R;

public class PlayerCreateActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Spinner spinner1;
    private Button boton1;
    private Button boton2;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_create);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_playerCreate);
        spinner1 = findViewById(R.id.spinner_team);
        boton1 = findViewById(R.id.button1_playerCreate);
        boton2 = findViewById(R.id.boton1_inicioCreatePlayer);

        ArrayList<String> teams = db.getTeams();
        teams.add(0, "Seleccione equipo");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
        spinner1.setAdapter(adapter);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = caja1.getText().toString();
                String teamName = spinner1.getSelectedItem() != null ? spinner1.getSelectedItem().toString() : "";

                if (playerName.isEmpty()) {
                    Toast.makeText(PlayerCreateActivity.this, "Por favor, ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
                } else if (teamName.equals("Seleccione equipo")) {
                    Toast.makeText(PlayerCreateActivity.this, "Por favor, selecciona un equipo", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("playerName", playerName);
                    contentValues.put("team", teamName);
                    dbWrite.insert("players", null, contentValues);
                    Toast.makeText(PlayerCreateActivity.this, "Jugador registrado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(PlayerCreateActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }
}
