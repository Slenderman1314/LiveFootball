package ifp.project.livefootball.Match;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Team.Teams;

public class EditMatchActivity extends AppCompatActivity {

    private EditText caja0;
    private Spinner localTeamSpinner, guestTeamSpinner;
    private Button boton1;
    private Button boton2;
    private Database db;
    private int matchId;
    protected Intent changeActivity;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        db = new Database(this);

        caja0 = (EditText) findViewById(R.id.caja0_editMatchId);
        localTeamSpinner = findViewById(R.id.spinner_editMatchLocal);
        guestTeamSpinner = findViewById(R.id.spinner_editMatchGuest);
        boton1 = findViewById(R.id.boton1_updateMatch);
        boton2 = findViewById(R.id.boton2_inicioEditMatch);
        listView = findViewById(R.id.lista1_editMatch);

        ArrayList<String> matches = db.getMatches();
        adapter = new ArrayAdapter<>(EditMatchActivity.this, android.R.layout.simple_list_item_1, matches);
        listView.setAdapter(adapter);
        System.out.println("Partidos recuperados: " + matches); // Agrega esta línea

        ArrayList<Teams> teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Agregar un objeto Teams con id 0 y nombre "Seleccione equipo"
        ArrayAdapter<Teams> teamAdapter = new ArrayAdapter<>(EditMatchActivity.this, android.R.layout.simple_spinner_item, teams);
        localTeamSpinner.setAdapter(teamAdapter);
        guestTeamSpinner.setAdapter(teamAdapter);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            localTeamSpinner.setSelection(savedInstanceState.getInt("localTeamSelection"));
            guestTeamSpinner.setSelection(savedInstanceState.getInt("guestTeamSelection"));
        }

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caja0.getText().toString().isEmpty()) {
                    Toast.makeText(EditMatchActivity.this, "Por favor, introduce un ID de partido", Toast.LENGTH_SHORT).show();
                    return;
                }

                int matchId = Integer.parseInt(caja0.getText().toString());
                Teams localTeam = (Teams) localTeamSpinner.getSelectedItem();
                Teams guestTeam = (Teams) guestTeamSpinner.getSelectedItem();

                if (db.getMatch(matchId) == null) {
                    Toast.makeText(EditMatchActivity.this, "El ID de partido no existe", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (localTeam.getId() == 0 || guestTeam.getId() == 0) {
                    Toast.makeText(EditMatchActivity.this, "Por favor, selecciona los equipos", Toast.LENGTH_SHORT).show();
                } else {
                    db.updateMatch(matchId, localTeam.getId(), guestTeam.getId(), localTeam.getName(), guestTeam.getName());
                    Toast.makeText(EditMatchActivity.this, "Partido actualizado con éxito", Toast.LENGTH_SHORT).show();
                    changeActivity = new Intent(EditMatchActivity.this, MainMenuActivity.class);
                    startActivity(changeActivity);
                    finish();  // Esto cierra MainMenuActivity
                }
            }
        });


        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(EditMatchActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();  // Esto cierra MainMenuActivity
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(EditMatchActivity.this)
                        .setTitle("Eliminar partido")
                        .setMessage("¿Estás seguro de que quieres eliminar este partido?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Obtener la cadena del partido
                                String matchInfo = adapter.getItem(position);
                                // Extraer el id del partido de la cadena
                                String[] parts = matchInfo.split(" - ");
                                String idPart = parts[0];
                                String[] idParts = idPart.split(": ");
                                int idMatch = Integer.parseInt(idParts[1]);
                                // Eliminar el partido de la base de datos
                                db.deleteMatch(idMatch);
                                // Actualizar la lista
                                matches.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(EditMatchActivity.this, "Partido eliminado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putInt("localTeamSelection", localTeamSpinner.getSelectedItemPosition());
        outState.putInt("guestTeamSelection", guestTeamSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        localTeamSpinner.setSelection(savedInstanceState.getInt("localTeamSelection"));
        guestTeamSpinner.setSelection(savedInstanceState.getInt("guestTeamSelection"));
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
            System.exit(0); // Cierra la actividad y por lo tanto la aplicación
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

