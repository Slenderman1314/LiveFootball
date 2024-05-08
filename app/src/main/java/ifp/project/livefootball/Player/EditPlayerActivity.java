package ifp.project.livefootball.Player;

import androidx.appcompat.app.AppCompatActivity;
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
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Team.Teams;

public class EditPlayerActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Spinner teamSpinner, playerSpinner, oldTeamSpinner;
    private Button boton1;
    private Button boton2;
    private String teamName;
    protected Intent changeActivity;
    private ArrayList<Teams> teams;
    private ArrayList<String> players;
    private boolean fromListPlayersActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_playerEdit);
        playerSpinner = findViewById(R.id.spinner_playerPlayerEdit);
        teamSpinner = findViewById(R.id.spinner_teamPlayerEdit);
        oldTeamSpinner = findViewById(R.id.spinnerEditPlayer);
        boton1 = findViewById(R.id.button1_playerEdit);
        boton2 = findViewById(R.id.boton1_inicioplayerEdit);

        players = db.getPlayers();
        players.add(0, "Seleccione jugador");
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, players);
        playerSpinner.setAdapter(playerAdapter);

        teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Agregar un objeto Teams con id 0 y nombre "Seleccione equipo"
        ArrayAdapter<Teams> teamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        teamSpinner.setAdapter(teamAdapter);

        // Obtén si la actividad se inició desde ListPlayersActivity del Intent
        fromListPlayersActivity = getIntent().getBooleanExtra("FROM_LIST_PLAYERS_ACTIVITY", false);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            caja1.setText(savedInstanceState.getString("caja1Text"));
            playerSpinner.setSelection(savedInstanceState.getInt("playerSelection"));
            teamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
            oldTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
        }

        ArrayList<Teams> teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Agregar un objeto Teams con id 0 y nombre "Seleccione equipo"
        //ArrayAdapter<Teams> teamAdapter = new ArrayAdapter<>(EditPlayerActivity.this, android.R.layout.simple_spinner_item, teams);
        oldTeamSpinner.setAdapter(teamAdapter);

        // Comprueba si el Intent contiene el nombre del equipo
        if (getIntent().hasExtra("TEAM")) {
            teamName = getIntent().getStringExtra("TEAM");

            // Selecciona el equipo en el Spinner
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i).getName().equals(teamName)) {
                    oldTeamSpinner.setSelection(i);
                    break;
                }
            }
        }

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedTeamName = teams.get(position).getName();
                    if (selectedTeamName != null && !selectedTeamName.equals("Seleccione equipo")) {
                        ArrayList<String> teamPlayers = db.getPlayersByTeam(selectedTeamName);
                        teamPlayers.add(0, "Seleccione jugador");
                        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(EditPlayerActivity.this, android.R.layout.simple_spinner_item, teamPlayers);
                        playerSpinner.setAdapter(playerAdapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        oldTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedTeamName = teams.get(position).getName();
                    ArrayList<String> teamPlayers = db.getPlayersByTeam(selectedTeamName);
                    teamPlayers.add(0, "Seleccione jugador");
                    ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(EditPlayerActivity.this, android.R.layout.simple_spinner_item, teamPlayers);
                    playerSpinner.setAdapter(playerAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

// Llama manualmente al método onItemSelected
        teamSpinner.getOnItemSelectedListener().onItemSelected(teamSpinner, null, teamSpinner.getSelectedItemPosition(), 0);


        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedPlayerName = players.get(position);
                    caja1.setText(selectedPlayerName);

                    // Obtener el equipo del jugador seleccionado
                    String playerTeamName = db.getPlayerTeam(selectedPlayerName);
                    Teams playerTeam = null;
                    for (Teams team : teams) {
                        if (team.getName().equals(playerTeamName)) {
                            playerTeam = team;
                            break;
                        }
                    }
                    int spinnerPosition = teamAdapter.getPosition(playerTeam);
                    teamSpinner.setSelection(spinnerPosition);
                } else if (fromListPlayersActivity) {
                    // Si la actividad se inició desde ListPlayersActivity, establece el jugador seleccionado
                    String playerName = getIntent().getStringExtra("PLAYER_NAME");
                    if (playerName != null) {
                        int spinnerPosition = playerAdapter.getPosition(playerName);
                        playerSpinner.setSelection(spinnerPosition);
                    }
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
                String oldPlayerName = playerSpinner.getSelectedItem().toString();
                String newPlayerName = caja1.getText().toString();
                String teamName = ((Teams) teamSpinner.getSelectedItem()).getName();

                if (newPlayerName.isEmpty()) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
                } else if (teamName.equals("Seleccione equipo")) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, selecciona un equipo", Toast.LENGTH_SHORT).show();
                } else {
                    // Actualizar jugador en la base de datos
                    db.updatePlayerTeam(oldPlayerName, newPlayerName, getTeamId(teamName), teamName);
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

    // Obtener el ID del equipo seleccionado
    private int getTeamId(String teamName) {
        for (Teams team : teams) {
            if (team.getName().equals(teamName)) {
                return team.getId();
            }
        }
        return -1; // Equipo no encontrado
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putString("caja1Text", caja1.getText().toString());
        outState.putInt("playerSelection", playerSpinner.getSelectedItemPosition());
        outState.putInt("teamSelection", teamSpinner.getSelectedItemPosition());
        outState.putInt("teamSelection", oldTeamSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        caja1.setText(savedInstanceState.getString("caja1Text"));
        playerSpinner.setSelection(savedInstanceState.getInt("playerSelection"));
        teamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
        oldTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
    }
}