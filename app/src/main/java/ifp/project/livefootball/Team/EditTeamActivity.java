package ifp.project.livefootball.Team;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class EditTeamActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Button boton1;
    private Button boton2;
    private Spinner spinner;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_editTeam);
        boton1 = findViewById(R.id.button1_editTeam);
        boton2 = findViewById(R.id.boton1_inicioEditTeam);
        spinner = findViewById(R.id.spinner_teams);

        ArrayList<Teams> teams = db.getTeams(); // obtener los equipos de la base de datos
        ArrayAdapter<Teams> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teams selectedTeam = (Teams) parent.getItemAtPosition(position);
                int teamId = selectedTeam.getId();
                // Guardar el id del equipo seleccionado
                getIntent().putExtra("teamId", teamId);
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
}