package ifp.project.livefootball.Player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class CreatePlayerActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Spinner spinner1;
    private Button boton1;
    private Button boton2;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_create);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_playerCreate);
        spinner1 = findViewById(R.id.spinner_team);
        boton1 = findViewById(R.id.button1_playerCreate);
        boton2 = findViewById(R.id.boton1_inicioCreatePlayer);

        ArrayList<Teams> teams = db.getTeams();
        teams.add(0, new Teams(0, "Seleccione equipo")); // Agregar un objeto Teams con id 0 y nombre "Seleccione equipo"
        ArrayAdapter<Teams> adapter = new ArrayAdapter<Teams>(this, android.R.layout.simple_spinner_item, teams);
        spinner1.setAdapter(adapter);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            caja1.setText(savedInstanceState.getString("caja1Text"));
            spinner1.setSelection(savedInstanceState.getInt("spinner1Selection"));
        }

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = caja1.getText().toString();
                String teamName = spinner1.getSelectedItem() != null ? spinner1.getSelectedItem().toString() : "";

                if (playerName.isEmpty()) {
                    Toast.makeText(CreatePlayerActivity.this, "Por favor, ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
                } else if (teamName.equals("Seleccione equipo")) {
                    Toast.makeText(CreatePlayerActivity.this, "Por favor, selecciona un equipo", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertar jugador en la base de datos
                    db.insertPlayer(playerName, getTeamId(teamName), teamName);
                    Toast.makeText(CreatePlayerActivity.this, "Jugador registrado con éxito", Toast.LENGTH_SHORT).show();
                    caja1.setText("");
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(CreatePlayerActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }

    // Obtener el ID del equipo seleccionado
    private int getTeamId(String teamName) {
        ArrayList<Teams> teamList = db.getTeams();
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).equals(teamName)) {
                // El índice + 1 se considera como el ID del equipo
                return i + 1;
            }
        }
        return -1; // Equipo no encontrado
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putString("caja1Text", caja1.getText().toString());
        outState.putInt("spinner1Selection", spinner1.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        caja1.setText(savedInstanceState.getString("caja1Text"));
        spinner1.setSelection(savedInstanceState.getInt("spinner1Selection"));
    }
}

