package com.example.suivigrossesse.fragments;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;

import com.example.suivigrossesse.R;
import com.example.suivigrossesse.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppoitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppoitmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Suivi Grossesse";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View binding;

    Calendar calendar;
    CalendarView calendarView;
    TextView tvDateRendezVous;
    Button btConfirm;
    Appointment currentAppoitment;

    public AppoitmentFragment() {
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
    public static AppoitmentFragment newInstance(String param1, String param2) {
        AppoitmentFragment fragment = new AppoitmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // --

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.fragment_appoitment, container, false);

        // --- My Code ---
        // Récupération des vues de l'interface
        tvDateRendezVous = binding.findViewById(R.id.tvDateRendezVous);
        btConfirm = binding.findViewById(R.id.btConfirmer);
        calendarView = binding.findViewById(R.id.calendarView);

        dbGetAppoitment();
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dbConfirmAppoitment();
                showAlertDialogConfirm();
            }
        });
        // --- My Code End ---

        return binding;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showAlertDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        builder.setMessage("Confirmez le rendez-vous ?");

        // Set Alert Title
        builder.setTitle("Rendez-vous");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            dbConfirmAppoitment();
            Toast.makeText(getContext(), "Rendez-vous confirmé !",
                    Toast.LENGTH_SHORT).show();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void dbGetAppoitment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("appoitments")
                .whereEqualTo("visitDone", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Document found, you can access its data
                                DocumentSnapshot docSnapshot = task.getResult().getDocuments().get(0);
                                currentAppoitment = docSnapshot.toObject(Appointment.class);
                                Log.d(TAG, "AppoitmentActivityObject - " + currentAppoitment);

                                Map<String, Object> appoitmentData = document.getData();

                                Log.d(TAG, "AppoitmentActivity - " + appoitmentData);
                                Log.d(TAG, "AppoitmentActivity - " + (String) appoitmentData.get("date"));

                                LocalDate dateApt = LocalDate.parse((String) appoitmentData.get("date"),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                tvDateRendezVous.setText(dateApt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.DAY_OF_MONTH, dateApt.getDayOfMonth());
                                calendar.set(Calendar.YEAR, dateApt.getYear());
                                calendar.set(Calendar.MONTH, dateApt.getMonthValue() - 1);

                                calendarView.setDate(calendar.getTimeInMillis(), false, false);

                                if (currentAppoitment.isBooked()) {
                                    btConfirm.setText("C'est confirmé !");
                                    btConfirm.setEnabled(false);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dbAddAppoitment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("appoitments")
                .document(LocalDate.now().toString())
                .set(new Appointment(LocalDate.now().toString(), false, false,
                        LocalDate.of(2024, 06, 01).toString()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Appoitment Set !" );
                        } else {
                            Log.d(TAG, " Appoitment Failure !!!!" );
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dbConfirmAppoitment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("appoitments")
                .document(currentAppoitment.getId())
                .update("booked", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Appoitment Booked !" );
                            btConfirm.setText("C'est confirmé !");
                            btConfirm.setEnabled(false);
                        } else {
                            Log.d(TAG, " Appoitment Failure !!!!" );
                        }
                    }
                });
    }

}
