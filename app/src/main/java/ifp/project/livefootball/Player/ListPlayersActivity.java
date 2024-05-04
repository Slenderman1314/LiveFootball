package ifp.project.livefootball.Player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class ListPlayersActivity extends AppCompatActivity {

    private Database db;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView1;
    private Button boton2;
    private String teamName;
    private String playerName;
    private Intent pasarPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        /**listView1 = new ListView(this);
        listView1.setId(R.id.listView1_listPlayers);**/
        listView1 = (ListView) findViewById(R.id.listView1_listPlayers);
        boton2 = (Button)findViewById(R.id.boton1_inicioListPLayer);
        db = new Database(this);

        // Obtén el nombre del equipo del Intent
        teamName = getIntent().getStringExtra("TEAM");
        arrayList = db.getPlayersByTeam(teamName);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            teamName = savedInstanceState.getString("teamName");
            playerName = savedInstanceState.getString("playerName");
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playerName = adapterView.getItemAtPosition(i).toString();
                pasarPantalla = new Intent(ListPlayersActivity.this, EditPlayerActivity.class);
                pasarPantalla.putExtra("PLAYER_NAME", playerName);
                pasarPantalla.putExtra("FROM_LIST_PLAYERS_ACTIVITY", true);
                startActivity(pasarPantalla);

                // Actualiza la lista de jugadores
                arrayList = db.getPlayersByTeam(teamName);
                arrayAdapter.notifyDataSetChanged();
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
}
