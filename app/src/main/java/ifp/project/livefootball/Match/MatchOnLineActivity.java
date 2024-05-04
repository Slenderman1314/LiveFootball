package ifp.project.livefootball.Match;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
    private boolean isChronometerRunning = false;
    private long timeWhenStopped = 0; // Añade esta variable al principio de tu clase

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
                if (position == 0) { // Si no se seleccionó un equipo (posición 0 es "Seleccione partido")
                    localScoreButton.setEnabled(false);
                    guestScoreButton.setEnabled(false);
                    localYellowCardsButton.setEnabled(false);
                    guestYellowCardsButton.setEnabled(false);
                    localRedCardsButton.setEnabled(false);
                    guestRedCardsButton.setEnabled(false);
                    botonCrono.setEnabled(false);
                } else {
                    localScoreButton.setEnabled(true);
                    guestScoreButton.setEnabled(true);
                    localYellowCardsButton.setEnabled(true);
                    guestYellowCardsButton.setEnabled(true);
                    localRedCardsButton.setEnabled(true);
                    guestRedCardsButton.setEnabled(true);
                    botonCrono.setEnabled(true);
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
                TextView partidoEnCurso = findViewById(R.id.textView7);
                if (!isChronometerRunning) {
                    // Si el cronómetro no está en ejecución, iniciar el cronómetro
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    isChronometerRunning = true; // El cronómetro está en ejecución
                    botonCrono.setText("Parar"); // Cambiar el texto del botón a "Parar"
                    // Hacer que el texto "Partido en curso" parpadee
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(700); // Puedes gestionar la velocidad del parpadeo con este parámetro
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    partidoEnCurso.startAnimation(anim);
                    Toast.makeText(MatchOnLineActivity.this, "¡Cronómetro iniciado!", Toast.LENGTH_SHORT).show();
                } else {
                    // Si el cronómetro está en ejecución, detener el cronómetro
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    isChronometerRunning = false; // El cronómetro no está en ejecución
                    botonCrono.setText("Inicio"); // Cambiar el texto del botón a "Inicio"
                    // Detener el parpadeo del texto "Partido en curso"
                    partidoEnCurso.clearAnimation();
                    Toast.makeText(MatchOnLineActivity.this, "Cronómetro detenido.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        localScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localScore++;
                localScoreView.setText(String.valueOf(localScore));
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET localScore = ? WHERE idMatch = ?");
                stmt.bindLong(1, localScore);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close();
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    localScore = stats.getLocalScore();
                }
            }
        });

        guestScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestScore++;
                guestScoreView.setText(String.valueOf(guestScore));
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestScore = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestScore);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close();
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    guestScore = stats.getGuestScore();
                }
            }
        });

        localYellowCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Incrementar el número de tarjetas amarillas locales
                localYellowCards++;
                // Actualizar la vista
                localYellowCardsView.setText(String.valueOf(localYellowCards));
                // Actualizar la base de datos
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET localYellowCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, localYellowCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close(); // No olvides cerrar el objeto SQLiteStatement
                // Actualizar la variable localYellowCards con el nuevo valor de la base de datos
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    localYellowCards = stats.getLocalYellowCards();
                }
            }
        });

        guestYellowCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestYellowCards++;
                guestYellowCardsView.setText(String.valueOf(guestYellowCards));
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestYellowCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestYellowCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close();
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    guestYellowCards = stats.getGuestYellowCards();
                }
            }
        });

        localRedCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRedCards++;
                localRedCardsView.setText(String.valueOf(localRedCards));
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET localRedCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, localRedCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close();
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    localRedCards = stats.getLocalRedCards();
                }
            }
        });

        guestRedCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestRedCards++;
                guestRedCardsView.setText(String.valueOf(guestRedCards));
                SQLiteStatement stmt = db.getReadableDatabase().compileStatement("UPDATE footballMatch SET guestRedCards = ? WHERE idMatch = ?");
                stmt.bindLong(1, guestRedCards);
                stmt.bindLong(2, matchId);
                stmt.execute();
                stmt.close();
                MatchStatistics stats = db.getMatchStatistics(matchId);
                if (stats != null) {
                    guestRedCards = stats.getGuestRedCards();
                }
            }
        });

        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChronometerRunning || isHalfTimeBreak) {
                    // Permitir ir a la pantalla principal si el cronómetro no se ha iniciado o está en pausa
                    cambiarPantalla = new Intent(MatchOnLineActivity.this, MainMenuActivity.class);
                    startActivity(cambiarPantalla);
                    finish();
                } else {
                    Toast.makeText(MatchOnLineActivity.this, "No puedes ir a la pantalla principal mientras el partido está en curso.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState != null) {
            // Restaurar el estado guardado
            localScore = savedInstanceState.getInt("localScore");
            guestScore = savedInstanceState.getInt("guestScore");
            localYellowCards = savedInstanceState.getInt("localYellowCards");
            guestYellowCards = savedInstanceState.getInt("guestYellowCards");
            localRedCards = savedInstanceState.getInt("localRedCards");
            guestRedCards = savedInstanceState.getInt("guestRedCards");
            isChronometerRunning = savedInstanceState.getBoolean("isChronometerRunning");
            timeWhenStopped = savedInstanceState.getLong("timeWhenStopped");
            if (isChronometerRunning) {
                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                chronometer.start();
            } else {
                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado
        outState.putInt("localScore", localScore);
        outState.putInt("guestScore", guestScore);
        outState.putInt("localYellowCards", localYellowCards);
        outState.putInt("guestYellowCards", guestYellowCards);
        outState.putInt("localRedCards", localRedCards);
        outState.putInt("guestRedCards", guestRedCards);
        outState.putBoolean("isChronometerRunning", isChronometerRunning);
        outState.putLong("timeWhenStopped", timeWhenStopped);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado guardado
        localScore = savedInstanceState.getInt("localScore");
        guestScore = savedInstanceState.getInt("guestScore");
        localYellowCards = savedInstanceState.getInt("localYellowCards");
        guestYellowCards = savedInstanceState.getInt("guestYellowCards");
        localRedCards = savedInstanceState.getInt("localRedCards");
        guestRedCards = savedInstanceState.getInt("guestRedCards");
        isChronometerRunning = savedInstanceState.getBoolean("isChronometerRunning");
        timeWhenStopped = savedInstanceState.getLong("timeWhenStopped");
        if (isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        }
    }

    // Método para actualizar estadísticas en la tabla
    private void updateStatistics(int matchId) {
        //Database db = new Database(this);
        MatchStatistics stats = db.getMatchStatistics(matchId);
        if (stats != null) {
            localScore = stats.getLocalScore();
            guestScore = stats.getGuestScore();
            localYellowCards = stats.getLocalYellowCards();
            guestYellowCards = stats.getGuestYellowCards();
            localRedCards = stats.getLocalRedCards();
            guestRedCards = stats.getGuestRedCards();
            updateTabla(stats);
        } else {
            Toast.makeText(MatchOnLineActivity.this, "Error: No se encontraron estadísticas para el partido.", Toast.LENGTH_SHORT).show();
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