package ifp.project.livefootball.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ifp.project.livefootball.Account.LogInActivity;
import ifp.project.livefootball.Match.MatchOnLineActivity;
import ifp.project.livefootball.Player.EditPlayerActivity;
import ifp.project.livefootball.Player.CreatePlayerActivity;
import ifp.project.livefootball.Player.ListPlayersActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.Match.EditMatchActivity;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.Team.CreateTeamActivity;
import ifp.project.livefootball.Team.EditTeamActivity;
import ifp.project.livefootball.Team.ListTeamActivity;

public class MainMenuActivity extends AppCompatActivity {
    private Button botonCrearPartido;
    private Button botonCrearEquipo;
    private Button botonCrearJugador;
    private Button botonEditarPartido;
    private Button botonEditarEquipo;
    private Button botonEditarJugador;
    private Button botonMostrarJugadores;
    private Button botonMostrarEquipo;
    private Button botonPartidoEnCurso;
    private Button botonLogOut;
    private TextView caja1;
    private Intent pasarPantalla;
    private String userType;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initViews();
        setupButtons();
    }

    private void initViews() {
        botonCrearPartido = findViewById(R.id.botonMainCrearPartido);
        botonCrearEquipo = findViewById(R.id.botonMainCrearEquipo);
        botonCrearJugador = findViewById(R.id.botonMainCrearJugador);
        botonEditarPartido = findViewById(R.id.botonMainEditarPartido);
        botonEditarEquipo = findViewById(R.id.botonMainEditarEquipo);
        botonEditarJugador = findViewById(R.id.botonMainEditarJugador);
        botonMostrarEquipo = findViewById(R.id.botonMainMostrarEquipo);
        botonPartidoEnCurso = findViewById(R.id.botonMainMatchOnline);
        botonMostrarJugadores = findViewById(R.id.botonMainMostrarJugadores);
        botonLogOut = findViewById(R.id.boton2_menu);
        caja1 = findViewById(R.id.label3_menu);
        db = new Database(this);

        // Obtén el nombre de usuario de las preferencias compartidas
        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userName = prefs.getString("userName", "");

        // Configura el nombre de usuario en tu TextView
        caja1.setText(userName);

        botonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(pasarPantalla);
            }
        });
    }

    private void setupButtons() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userName = prefs.getString("userName", "");
        String userType = db.getUserType(userName);
        if (userType == null) {
            userType = "Asistente"; // Valor predeterminado
        }
        if (userType.equals("Entrenador")) {
            enableButtonsForEntrenador();
        } else if (userType.equals("Asistente")) {
            enableButtonsForAsistente();
        }
    }

    private void enableButtonsForEntrenador() {
        botonCrearPartido.setEnabled(true);
        botonCrearEquipo.setEnabled(true);
        botonCrearJugador.setEnabled(true);

        botonEditarPartido.setEnabled(false);
        botonEditarEquipo.setEnabled(false);
        botonEditarJugador.setEnabled(false);
        botonMostrarEquipo.setEnabled(false);
        botonPartidoEnCurso.setEnabled(false);
        botonMostrarJugadores.setEnabled(false);

        botonEditarPartido.setBackgroundColor(Color.GRAY);
        botonEditarEquipo.setBackgroundColor(Color.GRAY);
        botonEditarJugador.setBackgroundColor(Color.GRAY);
        botonMostrarEquipo.setBackgroundColor(Color.GRAY);
        botonPartidoEnCurso.setBackgroundColor(Color.GRAY);
        botonMostrarJugadores.setBackgroundColor(Color.GRAY);
        botonCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, CreateMatchActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, CreateTeamActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonCrearJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, CreatePlayerActivity.class);
                startActivity(pasarPantalla);
            }
        });
    }

    private void enableButtonsForAsistente() {
        botonCrearPartido.setEnabled(false);
        botonCrearEquipo.setEnabled(false);
        botonCrearJugador.setEnabled(false);

        botonEditarPartido.setEnabled(true);
        botonEditarEquipo.setEnabled(true);
        botonEditarJugador.setEnabled(true);
        botonMostrarEquipo.setEnabled(true);
        botonPartidoEnCurso.setEnabled(true);
        botonMostrarJugadores.setEnabled(true);

        botonCrearPartido.setBackgroundColor(Color.GRAY);
        botonCrearEquipo.setBackgroundColor(Color.GRAY);
        botonCrearJugador.setBackgroundColor(Color.GRAY);
        botonEditarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, EditMatchActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonEditarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, EditTeamActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonEditarJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, EditPlayerActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonMostrarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, ListTeamActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonPartidoEnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, MatchOnLineActivity.class);
                startActivity(pasarPantalla);
            }
        });

        botonMostrarJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainMenuActivity.this, ListPlayersActivity.class);
                startActivity(pasarPantalla);
            }
        });
    }
}
