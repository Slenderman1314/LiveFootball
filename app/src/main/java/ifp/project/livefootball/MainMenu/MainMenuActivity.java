package ifp.project.livefootball.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ifp.project.livefootball.Account.LogInActivity;
import ifp.project.livefootball.Match.MatchOnLineActivity;
import ifp.project.livefootball.Player.EditPlayerActivity;
import ifp.project.livefootball.Player.PlayerCreateActivity;
import ifp.project.livefootball.R;
import ifp.project.livefootball.Match.CreateMatchActivity;
import ifp.project.livefootball.Match.EditMatchActivity;
import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.Team.CreateTeamActivity;
import ifp.project.livefootball.Team.EditTeamActivity;
import ifp.project.livefootball.Team.ListTeamActivity;
import ifp.project.livefootball.Account.User;

public class MainMenuActivity extends AppCompatActivity {
    private Button botonCrearPartido;
    private Button botonCrearEquipo;
    private Button botonCrearJugador;
    private Button botonEditarPartido;
    private Button botonEditarEquipo;
    private Button botonEditarJugador;
    private Button botonMostrarEquipo;
    private Button botonPartidoEnCurso;
    private Button botonLogOut;
    private TextView caja1;
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
                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(intent);
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
        botonCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreateMatchActivity.class);
                startActivity(intent);
            }
        });

        botonCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        botonCrearJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, PlayerCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableButtonsForAsistente() {
        botonEditarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, EditMatchActivity.class);
                startActivity(intent);
            }
        });

        botonEditarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, EditTeamActivity.class);
                startActivity(intent);
            }
        });

        botonEditarJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, EditPlayerActivity.class);
                startActivity(intent);
            }
        });

        botonMostrarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ListTeamActivity.class);
                startActivity(intent);
            }
        });

        botonPartidoEnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MatchOnLineActivity.class);
                startActivity(intent);
            }
        });
    }
}
