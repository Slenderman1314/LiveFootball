package ifp.project.livefootball.Match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class EditMatchActivity extends AppCompatActivity {

    private EditText caja0, caja1, caja2;
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
        caja1 = findViewById(R.id.caja1_editEquipoLocal);
        caja2 = findViewById(R.id.caja2_editEquipoVisitante);
        boton1 = findViewById(R.id.boton1_updateMatch);
        boton2 = findViewById(R.id.boton2_inicioEditMatch);
        listView = findViewById(R.id.lista1_editMatch);

        ArrayList<String> matches = db.getMatches();
        adapter = new ArrayAdapter<>(EditMatchActivity.this, android.R.layout.simple_list_item_1, matches);
        listView.setAdapter(adapter);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int matchId =Integer.parseInt(caja0.getText().toString());
                String localTeam = caja1.getText().toString();
                String guestTeam = caja2.getText().toString();

                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("idLocalTeam", localTeam);
                contentValues.put("idGuestTeam", guestTeam);

                int result = database.update("footballMatch", contentValues, "idMatch = ?", new String[]{String.valueOf(matchId)});

                if (result > 0) {
                    Toast.makeText(EditMatchActivity.this, "Partido actualizado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditMatchActivity.this, "Error al actualizar el partido", Toast.LENGTH_SHORT).show();
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
    }
}