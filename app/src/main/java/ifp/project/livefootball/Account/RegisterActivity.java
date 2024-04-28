package ifp.project.livefootball.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.R;

public class RegisterActivity extends AppCompatActivity {
    private Database db;
    protected Intent changeActivity;
    private TextView label1;
    private TextView label2;
    private EditText caja1;
    private TextView label3;
    private EditText caja2;
    private TextView label4;
    private RadioButton radioBoton1;
    private RadioButton radioBoton2;
    private Button boton1;
    private Button boton2;
    private ImageView ima1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        label1 = (TextView) findViewById(R.id.label1_register);
        label2 = (TextView) findViewById(R.id.label2_register);
        caja1 = (EditText) findViewById(R.id.caja1_register);
        label3 = (TextView) findViewById(R.id.label3_register);
        caja2 = (EditText) findViewById(R.id.caja2_register);
        label4 = (TextView) findViewById(R.id.label4_register);
        radioBoton1 = (RadioButton) findViewById(R.id.radioBoton1_register);
        radioBoton2 = (RadioButton) findViewById(R.id.radioBoton2_register);
        boton1 = (Button) findViewById(R.id.boton1_register);
        boton2 = (Button) findViewById(R.id.boton2_register);
        ima1 = (ImageView) findViewById(R.id.ima1_logo);

        db = new Database(this);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = caja1.getText().toString();
                String pass = caja2.getText().toString();
                String role = radioBoton1.isChecked() ? "Asistente" : radioBoton2.isChecked() ? "Entrenador" : "";

                if (name.equals("") || pass.equals("") || role.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Debes rellenar ambos apartados y seleccionar un rol", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(name, pass, role);
                    db.insertUser(user);
                    Toast.makeText(RegisterActivity.this, "La cuenta se creo con exito", Toast.LENGTH_SHORT).show();
                    Intent changeActivity = new Intent(RegisterActivity.this, LogInActivity.class);
                    finish();
                    startActivity(changeActivity);
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity = new Intent(RegisterActivity.this, LogInActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });

    }
}