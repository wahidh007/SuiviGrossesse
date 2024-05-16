package com.example.suivigrossesse.fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suivigrossesse.R;
import com.example.suivigrossesse.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Suivi Grossesse";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View binding;

    TextView tvSemaineGrossesse;
    TextView tvDateAccouchement;
    TextView tvDateConception;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.fragment_home, container, false);

        // --- My Code ---
        // Récupération des vues de l'interface
        tvSemaineGrossesse = binding.findViewById(R.id.tvSemaineGrossesse);
        tvDateAccouchement = binding.findViewById(R.id.tvDateAccouchement);
        tvDateConception = binding.findViewById(R.id.tvDateConception);

        // --- Recuperation User
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
//                        String firstName = task.getResult().getString("First Name");
//                        String email = task.getResult().getString("Email");
//                        String phone = task.getResult().getString("Phone");
                        DocumentSnapshot docSnapshot = task.getResult();
                        User user1 = docSnapshot.toObject(User.class);
                        Log.d(TAG, "From HomeFragment - " + user1);

                        if (user1.getDateConception() == null) {
                            Log.d(TAG, "From HomeFragment - Date Null");

                            Toast.makeText(this.getContext(), user1.getEmail() +
                                            " - No Date Conception", Toast.LENGTH_SHORT).show();
//                            showDatePicker();
                        }

                        // Calcul et affichage de la semaine de grossesse en cours
                        tvSemaineGrossesse.setText("Semaine " + user1.semaineGrossesse());

                        // Calcul et affichage de la date d'accouchement estimée
                        tvDateConception.setText("Date de Conception : " + user1.getDateConception());

                        // Calcul et affichage de la date d'accouchement estimée
                        tvDateAccouchement.setText("Date d'accouchement : " + user1.dateAccouchement());

                        //other stuff
                    }else{
                        //deal with error
                    }
                });
        // --- End Recuperation User
        // --- My Code End ---

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        return binding;
    }

    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TextView tvDateConception = binding.findViewById(R.id.tvDateConception);
                        tvDateConception.setText("Date de Conception : " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("Date de Conception");
        datePickerDialog.show();
    }

}