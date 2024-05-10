package ifp.project.livefootball.Team;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class CreateTeamActivity extends AppCompatActivity {

    private Database db;
    private EditText caja1;
    private Button boton1;
    private Button boton2;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        db = new Database(this);

        caja1 = findViewById(R.id.editText1_createTeam);
        boton1 = findViewById(R.id.button1_createTeam);
        boton2 = findViewById(R.id.boton1_inicioCreateTeam);

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            caja1.setText(savedInstanceState.getString("caja1Text"));
        }

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teamName = caja1.getText().toString();
                if (!teamName.isEmpty()) {
                    db.insertTeam(teamName);
                    Toast.makeText(CreateTeamActivity.this, "Equipo registrado con éxito", Toast.LENGTH_SHORT).show();
                    caja1.setText("");
                } else {
                    Toast.makeText(CreateTeamActivity.this, "Por favor, ingresa el nombre del equipo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(CreateTeamActivity.this, MainMenuActivity.class);
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        caja1.setText(savedInstanceState.getString("caja1Text"));
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
