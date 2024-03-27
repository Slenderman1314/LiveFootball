package ifp.project.livefootball.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import ifp.project.livefootball.User;

public class LogInActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private EditText editText1;
    private TextView textView3;
    private EditText editText2;
    private Button button1;
    private Button button2;
    private ImageView ima1;
    private Database db;
    private String textContent1;
    private String textContent2;
    protected Intent changeActivity;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        textView1 = (TextView) findViewById(R.id.textView1_logIn);
        textView2 = (TextView) findViewById(R.id.textView2_logIn);
        textView3 = (TextView) findViewById(R.id.textView3_logIn);
        editText1 = (EditText) findViewById(R.id.editText1_logIn);
        editText2 = (EditText) findViewById(R.id.editText2_logIn);
        button1 = (Button) findViewById(R.id.button1_logIn);
        button2 = (Button) findViewById(R.id.button2_logIn);
        ima1 = (ImageView) findViewById(R.id.ima1_menu);
        db = new Database(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText1.getText().toString();
                String pass = editText2.getText().toString();
                String role = "rolUsuario"; // Define el rol aquí

                if (name.equals("") || pass.equals("")) {
                    Toast toast = Toast.makeText(LogInActivity.this, "Debe indicar usuario y contraseña", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    User user = new User(name, pass, role);
                    db.insertUser(user);

                    if (db.getUser(name).equals(name) && db.getPass(name).equals(pass)) {
                        Toast.makeText(LogInActivity.this, "Tienes acceso", Toast.LENGTH_SHORT).show();
                        Intent changeActivity = new Intent(LogInActivity.this, MainMenuActivity.class);
                        finish();
                        startActivity(changeActivity);
                    } else {
                        Toast toast = Toast.makeText(LogInActivity.this, "Usuario y/o contraseña erróneos", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(LogInActivity.this, RegisterActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });
    }

}