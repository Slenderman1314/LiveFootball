package ifp.project.livefootball.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.R;

public class LogInActivity extends AppCompatActivity {
    private TextView label1;
    private TextView label2;
    private EditText caja1;
    private TextView label3;
    private EditText caja2;
    private Button boton1;
    private Button boton2;
    private ImageView ima1;
    private Database db;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        label1 = (TextView) findViewById(R.id.label1_logIn);
        label2 = (TextView) findViewById(R.id.label2_logIn);
        label3 = (TextView) findViewById(R.id.label3_logIn);
        caja1 = (EditText) findViewById(R.id.caja1_logIn);
        caja2 = (EditText) findViewById(R.id.caja2_logIn);
        boton1 = (Button) findViewById(R.id.boton1_logIn);
        boton2 = (Button) findViewById(R.id.boton2_logIn);
        ima1 = (ImageView) findViewById(R.id.ima1_login);
        db = new Database(this);

        if (savedInstanceState != null) {
            caja1.setText(savedInstanceState.getString("caja1"));
            caja2.setText(savedInstanceState.getString("caja2"));
        }

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = caja1.getText().toString();
                String pass = caja2.getText().toString();

                if (name.equals("") || pass.equals("")) {
                    Toast.makeText(LogInActivity.this, "Debe indicar usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    // Comprueba si el usuario existe y si la contraseña es correcta
                    if (db.getUser(name) != null && db.getPass(name) != null) {
                        if (db.getUser(name).equals(name) && db.getPass(name).equals(pass)) {
                            Toast.makeText(LogInActivity.this, "Tienes acceso", Toast.LENGTH_SHORT).show();

                            // Guarda el nombre de usuario en las preferencias compartidas
                            SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userName", name);
                            editor.apply();

                            changeActivity = new Intent(LogInActivity.this, MainMenuActivity.class);
                            finish();
                            startActivity(changeActivity);
                        } else {
                            Toast.makeText(LogInActivity.this, "Usuario y/o contraseña erróneos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity = new Intent(LogInActivity.this, RegisterActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("caja1", caja1.getText().toString());
        outState.putString("caja2", caja2.getText().toString());
    }
}
