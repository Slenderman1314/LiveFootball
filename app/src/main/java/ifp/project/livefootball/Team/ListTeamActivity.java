package ifp.project.livefootball.Team;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.Player.EditPlayerActivity;
import ifp.project.livefootball.Player.ListPlayersActivity;
import ifp.project.livefootball.R;

public class ListTeamActivity extends AppCompatActivity {

    private Database db;
    private ArrayList<Teams> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView1;
    private Button boton1;
    private String textContent;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_team);

        listView1 = (ListView) findViewById(R.id.listView1_listTeam);
        boton1 = (Button) findViewById(R.id.boton1_inicioListTeam);
        db = new Database(this);
        arrayList = db.getTeams();

        ArrayAdapter<Teams> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            textContent = savedInstanceState.getString("textContent");
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textContent = adapterView.getItemAtPosition(i).toString();
                changeActivity = new Intent(ListTeamActivity.this, ListPlayersActivity.class);
                changeActivity.putExtra("TEAM", textContent);
                startActivity(changeActivity);
                finish();
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Teams selectedTeam = (Teams) parent.getItemAtPosition(position);
                String teamName = selectedTeam.getName();

                new AlertDialog.Builder(ListTeamActivity.this)
                        .setTitle("Eliminar Equipo")
                        .setMessage("¿Estás seguro de que quieres eliminar el equipo " + teamName + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Intentar eliminar el equipo
                                boolean teamDeleted = db.deleteTeam(teamName);
                                if (teamDeleted) {
                                    // El equipo se eliminó, actualizar la lista de equipos
                                    arrayList = db.getTeams();
                                    ArrayAdapter<Teams> arrayAdapter = new ArrayAdapter<>(ListTeamActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                    listView1.setAdapter(arrayAdapter);
                                } else {
                                    // El equipo no se pudo eliminar, mostrar un mensaje
                                    Toast.makeText(ListTeamActivity.this, "No se puede eliminar el equipo porque tiene jugadores asignados", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true; // Indica que el evento de clic largo ha sido manejado
            }
        });



        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(ListTeamActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putString("textContent", textContent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        textContent = savedInstanceState.getString("textContent");
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
            finish();  // Cierra la actividad y por lo tanto la aplicación
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}