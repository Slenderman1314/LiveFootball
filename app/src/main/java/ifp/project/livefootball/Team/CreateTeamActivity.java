package ifp.project.livefootball.Team;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.Player.PlayerCreateActivity;
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

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teamName = caja1.getText().toString();
                if (!teamName.isEmpty()) {
                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", teamName);
                    dbWrite.insert("teams", null, contentValues);
                    Toast.makeText(CreateTeamActivity.this, "Equipo registrado con éxito", Toast.LENGTH_SHORT).show();
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
}
