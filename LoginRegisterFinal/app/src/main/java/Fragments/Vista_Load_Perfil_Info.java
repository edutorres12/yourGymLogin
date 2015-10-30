package Fragments;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import yourgym.loginregisterfinal.Login;
import yourgym.loginregisterfinal.MainActivity;
import yourgym.loginregisterfinal.R;

/**
 * Created by 210 Equipo 01 on 29/09/2015.
 */
public class Vista_Load_Perfil_Info extends Fragment {

    EditText etName,etUsername, etAge;


    public Vista_Load_Perfil_Info(){

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.vista_menu_rutina, container, false);



        return  rootView;




    }



}
