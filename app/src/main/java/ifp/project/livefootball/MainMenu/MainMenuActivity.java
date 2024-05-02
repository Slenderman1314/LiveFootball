package ifp.project.livefootball.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ifp.project.livefootball.Account.LogInActivity;
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
    private Database db;
    private ImageView ima1;
    private Button boton2;
    private Button botonCrearPartido;
    private Button botonCrearEquipo;
    private Button botonEditarPartido;
    private Button botonEditarEquipo;
    private Button botonCrearJugador;
    private Button botonEditarJugador;
    private Button botonMostrarEquipo;
    protected Intent changeActivity;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ima1 = (ImageView) findViewById(R.id.ima1_main_menu);
        boton2 = findViewById(R.id.boton2_menu);
        botonCrearPartido = findViewById(R.id.botonMainCrearPartido);
        botonCrearEquipo = findViewById(R.id.botonMainCrearEquipo);
        botonEditarPartido = findViewById(R.id.botonMainEditarPartido);
        botonEditarEquipo = findViewById(R.id.botonMainEditarEquipo);
        botonCrearJugador = findViewById(R.id.botonMainCrearJugador);
        botonEditarJugador = findViewById(R.id.botonMainEditarJugador);
        botonMostrarEquipo = findViewById(R.id.botonMainMostrarEquipo);
        db = new Database(this);

        botonCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, CreateMatchActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, CreateTeamActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonEditarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, EditMatchActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonEditarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, EditTeamActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonCrearJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, PlayerCreateActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonEditarJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, EditPlayerActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        botonMostrarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity = new Intent(MainMenuActivity.this, ListTeamActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes borrar la información del usuario actualmente registrado si es necesario

                // Redirige al usuario a LoginActivity
                changeActivity = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(changeActivity);
                finish();  // Esto cierra MainMenuActivity
            }
        });

    }
}
