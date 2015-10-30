package yourgym.loginregisterfinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {
Button bLogin;
    EditText etUserName, etPassword;
    TextView tvregisterLink;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

etUserName=(EditText) findViewById(R.id.etUsername);
        etPassword=(EditText) findViewById(R.id.etPassword);
        bLogin=(Button) findViewById( R.id.bLogin2);

        bLogin.setOnClickListener(this);
        tvregisterLink=  (TextView)findViewById(R.id.tvRegisterLink);

       tvregisterLink.setOnClickListener(this);
        userLocalStore= new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin2:
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                User user=new User(username,password);
                authenticate(user);



                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate (User user){

        ServerRequests serverRequests= new ServerRequests(this);
serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
        @Override
        public void done(User returnedUser) {
            if ( returnedUser == null){
                showErrorMessage();
            }else {
                logUserIn(returnedUser);
            }

        }
    });

}

    private void showErrorMessage () {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }

    private void logUserIn(User returnedUser){

        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this,MainActivity.class));


    }
}
