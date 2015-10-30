package yourgym.loginregisterfinal;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.provider.Settings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 21/09/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIME= 1000 * 15;
    public static final String SERVER_ADDRESS= "http://yourgym.site88.net/";
    public ServerRequests(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");



    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user,userCallback).execute();

    }

public void fetchUserDataInBackground(User user, GetUserCallback callback){
progressDialog.show();
    progressDialog.show();
    new fetchUserDataAsyncTask(user,callback).execute();

}

    public void fetchEjercicioDataInBackground(Ejercicio ejercicio, GetEjercicioCallback callbackEjercicio){
        progressDialog.show();
        progressDialog.show();
        new fetchEjercicioDataAsyncTask(ejercicio,callbackEjercicio).execute();

    }






    

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
GetUserCallback userCallback;
public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
    this.user=user;
    this.userCallback=userCallback;
}
        protected Void doInBackground(Void... params) {
ArrayList<NameValuePair> dataToSend= new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("age", user.age + ""));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(post);
            }catch (Exception e){

                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }


public class fetchUserDataAsyncTask extends  AsyncTask <Void,Void,User> {
    User user;
    GetUserCallback userCallback;
    public fetchUserDataAsyncTask (User user, GetUserCallback userCallback){
    this.user=user;
        this.userCallback=userCallback;

    }

    @Override
    protected User doInBackground(Void... params) {

        ArrayList<NameValuePair> dataToSend= new ArrayList<>();

        dataToSend.add(new BasicNameValuePair("username", user.username));
        dataToSend.add(new BasicNameValuePair("password", user.password));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
        HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");



        User returnedUser= null;
        try {

            post.setEntity(new UrlEncodedFormEntity(dataToSend));

           HttpResponse httpResponse= client.execute(post);
            HttpEntity entity= httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jObject= new JSONObject(result);

            if (jObject.length()== 0){
user= null;


            }else {

                String name= jObject.getString("name");
                int age = jObject.getInt("age");
                returnedUser = new User(name,age,user.username,user.password);

            }

        }catch (Exception e){
e.printStackTrace();
        }

        return returnedUser;
    }


    protected void onPostExecute(User returnedUser) {
        progressDialog.dismiss();
        userCallback.done(returnedUser);
        super.onPostExecute(returnedUser);
    }





}


    public class fetchEjercicioDataAsyncTask extends  AsyncTask <Void,Void,Ejercicio> {
        Ejercicio ejercicio;
        GetEjercicioCallback ejercicioCallback;
        public fetchEjercicioDataAsyncTask (Ejercicio ejercicio, GetEjercicioCallback ejercicioCallback){
            this.ejercicio=ejercicio;
            this.ejercicioCallback=ejercicioCallback;

        }

        @Override
        protected Ejercicio doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend= new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("ejercicio_id", "1"));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "loadTodosEjercicios.php");



            Ejercicio returnedEjercicio= null;
            try {

                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                HttpResponse httpResponse= client.execute(post);
                HttpEntity entity= httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject= new JSONObject(result);

                if (jObject.length()== 0){
                    ejercicio= null;


                }else {

                    String nombre_ejercicio= jObject.getString("nombre_ejercicio");
                    int ejercicio_id = jObject.getInt("ejercicio_id");

                    returnedEjercicio = new Ejercicio(nombre_ejercicio,"hola",ejercicio_id);


                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return returnedEjercicio;
        }


        protected void onPostExecute(Ejercicio returnedEjercicio) {
            progressDialog.dismiss();
            ejercicioCallback.done(returnedEjercicio);
            super.onPostExecute(returnedEjercicio);
        }





    }


}
