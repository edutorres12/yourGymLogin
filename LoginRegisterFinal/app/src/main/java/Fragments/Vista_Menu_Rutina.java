package Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import yourgym.loginregisterfinal.JasonParser;
import yourgym.loginregisterfinal.R;

/**
 * Created by Eduardo on 01/10/2015.
 */
public class Vista_Menu_Rutina extends Fragment {

    ListView listViewEjercicio;
    TextView idEjercicio;
    TextView nombreEjercicio;
    TextView descripcionEjercicio;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    private static String url = "http://yourgym.site88.net/loadEjercicios.php/";

    private static final String TAG_ID = "ver";
    private static final String TAG_NOMBRE = "name";
    private static final String TAG_DESCRIPCION = "api";

    public Vista_Menu_Rutina() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.vista_menu_rutina, container, false);
        oslist = new ArrayList<HashMap<String, String>>();

        return rootView;


    }
}