package ifp.project.livefootball.Match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class CreateMatchActivity extends AppCompatActivity {
    private ImageView ima1;
    private Button boton1;
    private Button boton2;
    private EditText caja1, caja2;
    private Database db;
    protected Intent changeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        db = new Database(this);

        ima1 = (ImageView) findViewById(R.id.ima1_create_match);
        boton1 = (Button) findViewById(R.id.boton1_inicioCreateMatch);
        caja1 = findViewById(R.id.caja1_createEquipoLocal);
        caja2 = findViewById(R.id.caja2_createEquipoVisitante);
        boton2 = findViewById(R.id.registerMatchButton);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();  // Esto cierra MainMenuActivity
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localTeam = caja1.getText().toString();
                String guestTeam = caja2.getText().toString();


                // Aquí puedes agregar más validaciones si es necesario

                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("idLocalTeam", localTeam);
                contentValues.put("idGuestTeam", guestTeam);
                changeActivity = new Intent(CreateMatchActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();  // Esto cierra MainMenuActivity


                long result = database.insert("footballMatch", null, contentValues);

                if (result != -1) {
                    Toast.makeText(CreateMatchActivity.this, "Partido registrado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateMatchActivity.this, "Error al registrar el partido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}