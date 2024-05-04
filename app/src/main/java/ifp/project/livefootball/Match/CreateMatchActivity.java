package ifp.project.livefootball.Match;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Team.Teams;

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

        ArrayList<Teams> teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo"));
        ArrayAdapter<Teams> teamAdapter = new ArrayAdapter<>(CreateMatchActivity.this, android.R.layout.simple_spinner_item, teams);
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
                // Redirige al usuario al menú
                changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teams localTeam = (Teams) localTeamSpinner.getSelectedItem();
                Teams guestTeam = (Teams) guestTeamSpinner.getSelectedItem();

                if (localTeam.getId() == 0 || guestTeam.getId() == 0) {
                    Toast.makeText(CreateMatchActivity.this, "Por favor, selecciona los equipos", Toast.LENGTH_SHORT).show();
                } else {
                    db.insertMatch(localTeam.getId(), guestTeam.getId(), localTeam.getName(), guestTeam.getName());
                    changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                    startActivity(changeActivity);
                    finish();
                    Toast.makeText(CreateMatchActivity.this, "Partido registrado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(CreateMatchActivity.this)
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
                                Toast.makeText(CreateMatchActivity.this, "Partido eliminado", Toast.LENGTH_SHORT).show();
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
}



