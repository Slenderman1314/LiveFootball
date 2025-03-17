package ifp.project.livefootball.Player;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.Match.EditMatchActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Team.Teams;

public class ListPlayersActivity extends AppCompatActivity {

    private Database db;
    private ListView listView1;
    private Button boton2;
    private String teamName;
    private String playerName;
    private Spinner spinner;
    private Intent pasarPantalla;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        listView1 = (ListView) findViewById(R.id.listView1_listPlayers);
        boton2 = (Button) findViewById(R.id.boton1_inicioListPLayer);
        spinner = (Spinner) findViewById(R.id.spinnerListPlayers);
        db = new Database(this);

        // Inicializa arrayList como una lista vacía
        arrayList = new ArrayList<>();

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            teamName = savedInstanceState.getString("teamName");
            playerName = savedInstanceState.getString("playerName");
        }

        ArrayList<Teams> teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Agregar un objeto Teams con id 0 y nombre "Seleccione equipo"
        ArrayAdapter<Teams> teamAdapter = new ArrayAdapter<>(ListPlayersActivity.this, android.R.layout.simple_spinner_item, teams);
        spinner.setAdapter(teamAdapter);

        // Comprueba si el Intent contiene el nombre del equipo
        if (getIntent().hasExtra("TEAM")) {
            teamName = getIntent().getStringExtra("TEAM");

            // Selecciona el equipo en el Spinner
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i).getName().equals(teamName)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teams selectedTeam = (Teams) parent.getItemAtPosition(position);
                teamName = selectedTeam.getName(); // Asume que Teams tiene un método getTeamName

                // Obtiene los jugadores del equipo seleccionado y actualiza el ListView
                arrayList = db.getPlayersByTeam(teamName);
                adapter = new ArrayAdapter<>(ListPlayersActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView1.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playerName = adapterView.getItemAtPosition(i).toString();
                teamName = adapterView.getItemAtPosition(i).toString();
                pasarPantalla = new Intent(ListPlayersActivity.this, EditPlayerActivity.class);
                pasarPantalla.putExtra("PLAYER_NAME", playerName);
                pasarPantalla.putExtra("PLAYER_TEAM", teamName);
                pasarPantalla.putExtra("FROM_LIST_PLAYERS_ACTIVITY", true);
                startActivity(pasarPantalla);
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                playerName = parent.getItemAtPosition(position).toString();

                new AlertDialog.Builder(ListPlayersActivity.this)
                        .setTitle("Eliminar Jugador")
                        .setMessage("¿Estás seguro de que quieres eliminar a " + playerName + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continuar con la eliminación
                                db.deletePlayer(playerName);
                                // Actualizar la lista de jugadores
                                arrayList = db.getPlayersByTeam(teamName);
                                adapter = new ArrayAdapter<>(ListPlayersActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                listView1.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true; // Indica que el evento de clic largo ha sido manejado
            }
        });


        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                pasarPantalla = new Intent(ListPlayersActivity.this, MainMenuActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Actualiza la lista de jugadores cuando vuelves a esta actividad
        if (teamName != null) {
            arrayList = db.getPlayersByTeam(teamName);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putString("teamName", teamName);
        outState.putString("playerName", playerName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        teamName = savedInstanceState.getString("teamName");
        playerName = savedInstanceState.getString("playerName");
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
}

