package ifp.project.livefootball.Match;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.widget.Toast;
import java.util.ArrayList;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class MatchOnLineActivity extends AppCompatActivity {
    private int localScore = 0;
    private int guestScore = 0;
    private int localYellowCards = 0;
    private int guestYellowCards = 0;
    private int localRedCards = 0;
    private int guestRedCards = 0;
    private static final int HALF_TIME = 45 * 60 * 1000; // 45 minutes in milliseconds
    private static final int BREAK_TIME = 15 * 60 * 1000; // 15 minutes in milliseconds
    private boolean isHalfTimeBreak = false;
    private TextView localScoreView;
    private TextView guestScoreView;
    private TextView localYellowCardsView;
    private TextView guestYellowCardsView;
    private TextView localRedCardsView;
    private TextView guestRedCardsView;
    private Chronometer chronometer;
    private Database db;
    private Spinner matchSpinner;
    private int matchId;
    private Button botonInicio;
    private Intent cambiarPantalla;
    private Button localScoreButton;
    private Button guestScoreButton;
    private Button localYellowCardsButton;
    private Button guestYellowCardsButton;
    private Button localRedCardsButton;
    private Button guestRedCardsButton;
    private Button botonCrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_online);

        db = new Database(this);

        localScoreView = findViewById(R.id.MatchOnLineLocal_score);
        guestScoreView = findViewById(R.id.MatchOnLineGuest_score);
        localYellowCardsView = findViewById(R.id.MatchOnLine_LocalYellowCards);
        guestYellowCardsView = findViewById(R.id.MatchOnLine_GuestYellowCards);
        localRedCardsView = findViewById(R.id.MatchOnLine_LocalRedCards);
        guestRedCardsView = findViewById(R.id.MatchOnLine_GuestRedCards);
        matchSpinner = findViewById(R.id.spinnerMatchOnLine);
        botonInicio = (Button) findViewById(R.id.boton1_inicioMatchOnline);
        botonCrono = (Button) findViewById(R.id.startCronoButtonMatchOnLine);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        localScoreButton = findViewById(R.id.local_score_button);
        guestScoreButton = findViewById(R.id.guest_score_button);
        localYellowCardsButton = findViewById(R.id.local_yellow_cards_button);
        guestYellowCardsButton = findViewById(R.id.guest_yellow_cards_button);
        localRedCardsButton = findViewById(R.id.local_red_cards_button);
        guestRedCardsButton = findViewById(R.id.guest_red_cards_button);

        // Llenar el Spinner con los partidos disponibles
        ArrayList<String> matches = db.getMatches();
        // Agregar el elemento inicial a la lista
        matches.add(0, "Seleccione partido");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matchSpinner.setAdapter(adapter);
        // Configurar el elemento inicial como seleccionado por defecto
        matchSpinner.setSelection(0);

        matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String matchInfo = parent.getItemAtPosition(position).toString();
                String[] parts = matchInfo.split(" - ");
                if (parts.length > 1) {
                    String[] subParts = parts[0].split(": ");
                    if (subParts.length > 1) {
                        matchId = Integer.parseInt(subParts[1]); // Extraer el ID del partido del String
                        // Actualizar estadísticas en la tabla
                        updateStatistics(matchId); // Llamar al método updateStatistics aquí
                    } else {
                        // Mostrar un mensaje de error al usuario
                        Toast.makeText(getApplicationContext(), "Error: El formato del partido no es correcto. Se esperaba ': ' en " + parts[0], Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Mostrar un mensaje de error al usuario
                    Toast.makeText(getApplicationContext(), "Error: El formato del partido no es correcto. Se esperaba ' - ' en " + matchInfo, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        botonCrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar el cronómetro cuando se presiona el botón "Iniciar"
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                Toast.makeText(MatchOnLineActivity.this, "¡Cronómetro iniciado!", Toast.LENGTH_SHORT).show();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MatchOnLineActivity.this, "!Inicio del partido!", Toast.LENGTH_SHORT).show();
                if (!isHalfTimeBreak) {
                    chronometer.stop();
                    Toast.makeText(MatchOnLineActivity.this, "!Medio Tiempo! Tómate 15 minutos de descanso.", Toast.LENGTH_SHORT).show();
                    isHalfTimeBreak = true;
                    handler.postDelayed(this, BREAK_TIME);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    Toast.makeText(MatchOnLineActivity.this, "!Inicio de la segunda parte!", Toast.LENGTH_SHORT).show();
                    isHalfTimeBreak = false;
                    matchSpinner.setEnabled(false); // Bloquear el Spinner una vez que el cronómetro se inicie
                }
            }
        }, HALF_TIME);

        localScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperar el valor actual del score local
                int currentLocalScore = localScore;
                // Incrementar el score local
                currentLocalScore++;
                // Actualizar el score local
                localScore = currentLocalScore;
                // Mostrar el nuevo valor del score local
                localScoreView.setText(String.valueOf(localScore));
            }
        });

        guestScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestScore++;
                guestScoreView.setText(String.valueOf(guestScore));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestScore = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestScore);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
            }
        });

        localYellowCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localYellowCards++;
                localYellowCardsView.setText(String.valueOf(localYellowCards));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET localYellowCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, localYellowCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
            }
        });

        guestYellowCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestYellowCards++;
                guestYellowCardsView.setText(String.valueOf(guestYellowCards));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestYellowCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestYellowCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
            }
        });

        localRedCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRedCards++;
                localRedCardsView.setText(String.valueOf(localRedCards));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET localRedCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, localRedCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
            }
        });

        guestRedCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestRedCards++;
                guestRedCardsView.setText(String.valueOf(guestRedCards));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestRedCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestRedCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
            }
        });

        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHalfTimeBreak) {
                    // Permitir ir a la pantalla principal si el cronómetro está parado
                    cambiarPantalla = new Intent(MatchOnLineActivity.this, MainMenuActivity.class);
                    startActivity(cambiarPantalla);
                    finish();
                } else {
                    Toast.makeText(MatchOnLineActivity.this, "No puedes ir a la pantalla principal mientras el partido está en curso.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para actualizar estadísticas en la tabla
    private void updateStatistics(int matchId) {
        Database db = new Database(this);
        MatchStatistics stats = db.getMatchStatistics(matchId);
        if (stats != null) {
            updateTabla(stats); // Pasar la variable stats como un parámetro
        } else {
            Toast.makeText(MatchOnLineActivity.this, "Error: No se encontraron estadísticas para el partido.", Toast.LENGTH_SHORT).show();;
        }
    }

    private void updateTabla(MatchStatistics stats) {
        localScoreView.setText(String.valueOf(stats.getLocalScore()));
        guestScoreView.setText(String.valueOf(stats.getGuestScore()));
        localYellowCardsView.setText(String.valueOf(stats.getLocalYellowCards()));
        guestYellowCardsView.setText(String.valueOf(stats.getGuestYellowCards()));
        localRedCardsView.setText(String.valueOf(stats.getLocalRedCards()));
        guestRedCardsView.setText(String.valueOf(stats.getGuestRedCards()));
    }
}

