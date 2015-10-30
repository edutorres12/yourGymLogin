package yourgym.loginregisterfinal;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import Fragments.Vista_Load_Perfil_Info;
import Fragments.Vista_Menu_Inicio;
import Fragments.Vista_Menu_Rutina;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SERVER_ADDRESS ="http://yourgym.site88.net/";
    Button bLogout;
    ImageView downloadedImage;
    EditText etName,etAge,etUsername;
UserLocalStore userLocalStore;
    ListView drawerListView;
    String[] sectionsTitle;
    DrawerLayout drawerLayout;
    LinearLayout linearLayout;
    Button loadDataButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  changeFrameContent(new Vista_Menu_Inicio(), "fgAdd");
        setContentView(R.layout.activity_main);
       // changeFrameContent(new Vista_Load_Perfil_Info(), "fgInicioPerfil");
        sectionsTitle = getResources().getStringArray(R.array.str_array_sectionsTitle);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerListView = (ListView) findViewById(R.id.drawer_listview);
        linearLayout= (LinearLayout) findViewById(R.id.asdasd);
        loadDataButton= (Button) findViewById(R.id.btn_loaddata);

        Adaptador adapter = new Adaptador(Arrays.asList(sectionsTitle));
        drawerListView.setAdapter(adapter);


        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        downloadedImage=(ImageView)findViewById(R.id.imageToDownload);
        bLogout = (Button) findViewById(R.id.bLogout);



        bLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        loadDataButton.setOnClickListener(this);





        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    changeFrameContent(new Vista_Menu_Inicio(), "fgInicio");
                    linearLayout.setVisibility(View.INVISIBLE);


                } else if (position == 1) {
                    changeFrameContent(new Vista_Menu_Rutina(), "fgRutina");
                    linearLayout.setVisibility(View.INVISIBLE);
                } else if (position == 2) {

                } else {


                }
                drawerLayout.closeDrawers();


            }
        });





        }



    private void changeFrameContent(Fragment newFg, String tag){

        FragmentManager fragmentManager = getFragmentManager();

        Fragment currentFragment= fragmentManager.findFragmentByTag(tag);

        if (currentFragment !=null && currentFragment.isVisible())
        {
            return;
        }


        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, newFg, tag);

        //Ya se hace toda la transaccion, dar la orden de que ya lo haga.
        ft.commit();
    }


protected void onStart() {
    super.onStart();
if (authenticate()==true){
displayUserDetail();
}else {

    startActivity(new Intent(MainActivity.this,Login.class));
}
    new DownloadImage(etUsername.getText().toString()).execute();

}

    private boolean authenticate (){
return userLocalStore.getUserLoggedIn();

    }

    private void displayUserDetail () {
     User user= userLocalStore.getLoggedInUser();

        etUsername.setText(user.username);
        etName.setText(user.name);
       //etAge.setText(user.age);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:

          //  new DownloadImage(etUsername.getText().toString()).execute();

                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);



                startActivity(new Intent(this, Login.class));
                break;

            case R.id.btn_loaddata:

                Ejercicio ejercicio= new Ejercicio("","",1);
                authenticate(ejercicio);






          //     AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
          //    dialogBuilder.setMessage("hola");
         //      dialogBuilder.setPositiveButton("Ok", null);
         //       dialogBuilder.show();


                break;




        }
    }



    private void authenticate (Ejercicio ejercicio){
        ServerRequests serverRequests= new ServerRequests(this);
        serverRequests.fetchEjercicioDataInBackground(ejercicio, new GetEjercicioCallback() {
            @Override
            public void done(Ejercicio returnEjercicio) {
                if ( returnEjercicio == null){
                    showErrorMessage();
                }else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    dialogBuilder.setMessage(returnEjercicio.toString());
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                }

            }
        });


    }


    private void showErrorMessage () {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }
    private void showCorrectMessage () {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage("Correct User Details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }

private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{
    String name;

    public DownloadImage(String name){
this.name=name;
    }



    protected void onPostExecute(Bitmap bitmap) {


        super.onPostExecute(bitmap);
if(bitmap!=null) {
    downloadedImage.setImageBitmap(bitmap);

}

    }

    @Override
    protected Bitmap doInBackground(Void... params) {
String url = SERVER_ADDRESS +"pictures/"+name+".JPG";


        try{
            URLConnection connection= new URL(url).openConnection();
            connection.setConnectTimeout(1000 * 30);
            connection.setReadTimeout(100 * 30);
            return BitmapFactory.decodeStream((InputStream)connection.getContent(), null,null);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }











    }
}





}

