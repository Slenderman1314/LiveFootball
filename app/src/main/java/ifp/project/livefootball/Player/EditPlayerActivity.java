package ifp.project.livefootball.Player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import ifp.project.livefootball.Team.Teams;

public class EditPlayerActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Spinner newTeamSpinner, playerSpinner, oldTeamSpinner;
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
        newTeamSpinner = findViewById(R.id.spinner_newTeamPlayerEdit);
        oldTeamSpinner = findViewById(R.id.spinner_oldTeamEditPlayer);
        boton1 = findViewById(R.id.button1_playerEdit);
        boton2 = findViewById(R.id.boton1_inicioplayerEdit);

        teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Add "Seleccione equipo" option
        ArrayAdapter<Teams> newTeamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        newTeamSpinner.setAdapter(newTeamAdapter);

        ArrayAdapter<Teams> oldTeamAdapter = new ArrayAdapter<>(EditPlayerActivity.this, android.R.layout.simple_spinner_item, teams);
        oldTeamSpinner.setAdapter(oldTeamAdapter);

        players = db.getPlayers();
        players.add(0, "Seleccione jugador");
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, players);
        playerSpinner.setAdapter(playerAdapter);

        // Obtén si la actividad se inició desde ListPlayersActivity del Intent
        //fromListPlayersActivity = getIntent().getBooleanExtra("FROM_LIST_PLAYERS_ACTIVITY", false);

        Intent intent = getIntent();
        String playerName = intent.getStringExtra("PLAYER_NAME");
        String playerTeam = intent.getStringExtra("PLAYER_TEAM");

        // Comprueba si playerName y playerTeam no son null antes de usarlos
        if (playerName != null && playerTeam != null) {
            // Rellenar los campos con los datos del jugador seleccionado
            caja1.setText(playerName);
            playerSpinner.setSelection(getPositionInSpinner(playerSpinner, playerName));
            oldTeamSpinner.setSelection(getTeamPosition(oldTeamSpinner, getPlayerTeam(playerName)));
            newTeamSpinner.setSelection(getPositionInSpinner(newTeamSpinner, playerTeam));
        }

        // Rellenar los campos con los datos del jugador seleccionado
        caja1.setText(playerName);
        playerSpinner.setSelection(getPositionInSpinner(playerSpinner, playerName));
        oldTeamSpinner.setSelection(getTeamPosition(oldTeamSpinner, getPlayerTeam(playerName)));
        newTeamSpinner.setSelection(getPositionInSpinner(newTeamSpinner, playerTeam));

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            caja1.setText(savedInstanceState.getString("caja1Text"));
            playerSpinner.setSelection(savedInstanceState.getInt("playerSelection"));
            newTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
            oldTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
        }

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

        newTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do nothing
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
                    playerAdapter.clear();
                    playerAdapter.addAll(teamPlayers);
                    playerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedPlayerName = players.get(position);
                    caja1.setText(selectedPlayerName);

                    // Get the team of the selected player
                    String playerTeamName = db.getPlayerTeam(selectedPlayerName);
                    Teams playerTeam = null;
                    for (Teams team : teams) {
                        if (team.getName().equals(playerTeamName)) {
                            playerTeam = team;
                            break;
                        }
                    }
                    int spinnerPosition = newTeamAdapter.getPosition(playerTeam);
                    newTeamSpinner.setSelection(spinnerPosition);
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
                String teamName = ((Teams) newTeamSpinner.getSelectedItem()).getName();

                if (newPlayerName.isEmpty()) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
                } else if (teamName.equals("Seleccione equipo")) {
                    Toast.makeText(EditPlayerActivity.this, "Por favor, selecciona un equipo", Toast.LENGTH_SHORT).show();
                } else {
                    // Actualizar jugador en la base de datos
                    db.updatePlayerTeam(oldPlayerName, newPlayerName, getTeamId(teamName), teamName);
                    Toast.makeText(EditPlayerActivity.this, "Jugador actualizado con éxito", Toast.LENGTH_SHORT).show();
                    // Redirige al usuario al menú
                    changeActivity = new Intent(EditPlayerActivity.this, MainMenuActivity.class);
                    startActivity(changeActivity);
                    finish();
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
        outState.putInt("teamSelection", newTeamSpinner.getSelectedItemPosition());
        outState.putInt("teamSelection", oldTeamSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        caja1.setText(savedInstanceState.getString("caja1Text"));
        playerSpinner.setSelection(savedInstanceState.getInt("playerSelection"));
        newTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
        oldTeamSpinner.setSelection(savedInstanceState.getInt("teamSelection"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.options_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics del elemento de la barra de acción
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Teams_options_main_menu) {
            // Aquí va tu código para manejar el clic en "Equipos"
            return true;
        } else if (id == R.id.Exit_options_main_menu) {
            // Aquí va tu código para manejar el clic en "Salir"
            System.exit(0);  // Cierra la actividad y por lo tanto la aplicación
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getPositionInSpinner(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                return i;
            }
        }
        return 0; // Devuelve 0 si no se encuentra el valor
    }

    private int getTeamPosition(Spinner spinner, String teamName) {
        for (int i = 0; i < spinner.getCount(); i++) {
            Teams team = (Teams) spinner.getItemAtPosition(i);
            if (team.getName().equals(teamName)) {
                return i;
            }
        }
        return 0; // Devuelve 0 si no se encuentra el equipo
    }

    private String getPlayerTeam(String playerName) {
        // Obtener el equipo del jugador seleccionado
        // Puedes utilizar una base de datos o una lista de jugadores para obtener el equipo
        // Por ejemplo:
        return db.getPlayerTeam(playerName);
    }
}