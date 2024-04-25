package ifp.project.livefootball.Player;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class EditPlayerActivity extends AppCompatActivity {
    private Database db;
    private EditText caja1;
    private Spinner teamSpinner, playerSpinner;
    private Button boton1;
    private Button boton2;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_playerEdit);
        playerSpinner = findViewById(R.id.spinner_playerPlayerEdit);
        teamSpinner = findViewById(R.id.spinner_teamPlayerEdit);
        boton1 = findViewById(R.id.button1_playerEdit);
        boton2 = findViewById(R.id.boton1_inicioplayerEdit);

        ArrayList<String> players = db.getPlayers();
        players.add(0, "Seleccione jugador");
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, players);
        playerSpinner.setAdapter(playerAdapter);

        ArrayList<String> teams = db.getTeams();
        teams.add(0, "Seleccione equipo");
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
        teamSpinner.setAdapter(teamAdapter);

        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String playerName = playerSpinner.getSelectedItem().toString();
                    caja1.setText(playerName);

                    // Aquí debes obtener el equipo del jugador seleccionado
                    String playerTeam = db.getPlayerTeam(playerName);
                    int spinnerPosition = teamAdapter.getPosition(playerTeam);
                    teamSpinner.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = caja1.getText().toString();
                String teamName = teamSpinner.getSelectedItem() != null ? teamSpinner.getSelectedItem().toString() : "";

                if (playerName.isEmpty()) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
                } else if (teamName.equals("Seleccione equipo")) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, selecciona un equipo", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("playerName", playerName);
                    contentValues.put("team", teamName);
                    dbWrite.update("players", contentValues, "playerName = ?", new String[]{playerName});
                    Toast.makeText(EditPlayerActivity.this, "Jugador actualizado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(EditPlayerActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }
}

