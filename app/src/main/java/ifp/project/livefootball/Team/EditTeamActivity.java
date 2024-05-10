package ifp.project.livefootball.Team;

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

public class EditTeamActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Button boton1;
    private Button boton2;
    private Spinner spinner;
    protected Intent changeActivity;
    private ArrayList<Teams> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_editTeam);
        boton1 = findViewById(R.id.button1_editTeam);
        boton2 = findViewById(R.id.boton1_inicioEditTeam);
        spinner = findViewById(R.id.spinner_teams);
        teams = db.getTeams();

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            caja1.setText(savedInstanceState.getString("caja1Text"));
            spinner.setSelection(savedInstanceState.getInt("teamSelection"));
        }

        ArrayList<Teams> teams = db.getTeams(); // obtener los equipos de la base de datos

        // Crear una lista de nombres de equipos
        ArrayList<String> teamNames = new ArrayList<>();
        teamNames.add("Seleccione equipo"); // Agregar "Seleccione equipo" al inicio de la lista

        for (Teams team : teams) {
            teamNames.add(team.getName()); // Agregar el nombre de cada equipo a la lista
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignora la opción "Seleccione equipo"
                    Teams selectedTeam = teams.get(position - 1); // Ajusta la posición
                    int teamId = selectedTeam.getId();
                    // Guardar el id del equipo seleccionado
                    getIntent().putExtra("teamId", teamId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ...
            }
        });

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTeamName = caja1.getText().toString();
                int teamId = getIntent().getIntExtra("teamId", 0);
                if (!newTeamName.isEmpty()) {
                    db.updateTeamName(teamId, newTeamName);
                    changeActivity = new Intent(EditTeamActivity.this, MainMenuActivity.class);
                    startActivity(changeActivity);
                    finish();
                    Toast.makeText(EditTeamActivity.this, "Nombre del equipo actualizado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditTeamActivity.this, "Por favor, ingresa el nuevo nombre del equipo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(EditTeamActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putString("caja1Text", caja1.getText().toString());
        outState.putInt("teamSelection", spinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        caja1.setText(savedInstanceState.getString("caja1Text"));
        spinner.setSelection(savedInstanceState.getInt("teamSelection"));
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