package yourgym.loginregisterfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

public class Register extends AppCompatActivity implements View.OnClickListener {
private static final int RESULT_LOAD_IMAGE=1;
    private static final String SERVER_ADDRESS ="http://yourgym.site88.net/";
    Button bRegister;
    ImageView imageToUpload;
    EditText etName, etUsername, etPassword, etAge;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageToUpload= (ImageView) findViewById(R.id.imageToUpload);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister= (Button) findViewById( R.id.bRegister);

        bRegister.setOnClickListener(this);
        imageToUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:

                String name=etName.getText().toString();
                String userName=etUsername.getText().toString();
                String password= etPassword.getText().toString();
                int age= Integer.parseInt(etAge.getText().toString());

                Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                new UploadImage(image,userName).execute();

                User user = new User(name,age,userName,password);

                registerUser(user);

                break;

            case R.id.imageToUpload:
              Intent galleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                break;





        }
    }

    private void registerUser (User user){

        ServerRequests serverRequests= new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnUser) {
                startActivity(new Intent(Register.this, Login.class));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
  if (requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data !=null){
      Uri selectedImage= data.getData();
      imageToUpload.setImageURI(selectedImage);
  }
    }

private class UploadImage extends AsyncTask<Void,Void,Void> {
    Bitmap image;
    String name;
   public UploadImage(Bitmap image, String name){
     this.image=image;
       this.name= name;
   }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
        Toast.makeText(getApplicationContext(),"Registration Complete",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        ArrayList<NameValuePair> dataToSend= new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image",encodedImage));
        dataToSend.add(new BasicNameValuePair("name",name));

        HttpParams httpRequestParams= getHttpRequestParams();
        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post= new HttpPost(SERVER_ADDRESS + "SavePicture.php");
        try{
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(post);
        }catch (Exception e){
            e.printStackTrace();

        }



        return null;


    }
}
private HttpParams getHttpRequestParams(){

    HttpParams httpRequestParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
    HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
    return httpRequestParams;

}
}
