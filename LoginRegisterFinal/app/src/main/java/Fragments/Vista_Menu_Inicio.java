package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yourgym.loginregisterfinal.R;

/**
 * Created by 210 Equipo 01 on 29/09/2015.
 */
public class Vista_Menu_Inicio extends Fragment {

    public Vista_Menu_Inicio(){

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.vista_menu_inicio, container, false);

        return  rootView;
    }

}
