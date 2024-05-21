package com.example.suivigrossesse.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suivigrossesse.R;
import com.example.suivigrossesse.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
    ImageView ivBaby;
    Button btSuiviGrossesse;
    User currentUser;
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
        // tells the system that your fragment wants to receive menu-related callbacks
        setHasOptionsMenu(true);

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
        ivBaby = binding.findViewById(R.id.imageView1);
        btSuiviGrossesse = binding.findViewById(R.id.btSuiviGrossesse);

        // --- Recuperation User
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        String name = task.getResult().getString("name");
                        String email = task.getResult().getString("email");
                        String address = task.getResult().getString("address");
                        String phone = task.getResult().getString("phone");
                        LocalDate dateConception;

//                        DocumentSnapshot docSnapshot = task.getResult();
//                        currentUser = docSnapshot.toObject(User.class);
                        Log.d(TAG, "From HomeFragment - " + currentUser);

//                        if (currentUser.getDateConception() == null) {
                        if (task.getResult().getString("dateConception") == null) {
                            currentUser = new User(name, email, address, phone);
                            Toast.makeText(this.getContext(), currentUser.getEmail() +
                                            " - No Date Conception", Toast.LENGTH_SHORT).show();
                            showDatePicker();
                        } else {
                            dateConception = LocalDate.parse(task.getResult().getString("dateConception"),
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            currentUser = new User(name, email, address, phone, dateConception);
                            upDateView();
                        }

                        //other stuff
                    }else{
                        //deal with error
                    }
                });
        // --- End Recuperation User
        btSuiviGrossesse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        // --- My Code End ---

        return binding;
    }

    //Function to display the custom dialog.
    @RequiresApi(api = Build.VERSION_CODES.O)
    void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.suivi_grossesse_dialog);

        //Initializing the views of the dialog.
        TextView tv_detail_grossesse = dialog.findViewById(R.id.tv_detail_grossesse);
        RoundedImageView iv_grossesse_detail = dialog.findViewById(R.id.iv_grossesse_detail);

        // Set the current baby image
        int ImgRes = getBabyImageRes(currentUser.semaineGrossesse());
        iv_grossesse_detail.setImageResource(ImgRes);


        // --- Recuperation Détail Semaine Grossesse
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("grossesse").document("S" + currentUser.semaineGrossesse())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        String semaineDetail = task.getResult().getString("detail");

                        if (semaineDetail == null) {
                            semaineDetail = "Semaine détails..\n non encore saisis !";
                        }
                        Log.d(TAG, "From showCustomDialog() - " + semaineDetail);

                        tv_detail_grossesse.setText(semaineDetail.replace("\\n", "\n"));

                    }else{
                        //deal with error
                    }
                });
        // --- End Détail Semaine Grossesse


        Button btOk = dialog.findViewById(R.id.btOk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentUser.setDateConception(LocalDate.of(year, month+1, dayOfMonth));
                        Log.d(TAG, "2-From HomeFragment - " + currentUser);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        updateDbUserDateConception(currentUser.getDateConception().format(formatter),
                                FirebaseFirestore.getInstance());
                        upDateView();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        // DatePicker: set min and max date
        final int maxDay = c.get(Calendar.DAY_OF_MONTH);
        final int maxMonth = c.get(Calendar.MONTH);
        final int maxYear = c.get(Calendar.YEAR);

        c.set(maxYear, maxMonth-10, maxDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        c.set(maxYear, maxMonth, maxDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        // DatePickerDialog set title
        datePickerDialog.setTitle("Date de Conception");
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void upDateView() {
        // Calcul et affichage de la semaine de grossesse en cours
        tvSemaineGrossesse.setText("Semaine " + currentUser.semaineGrossesse());

        // Calcul et affichage de la date d'accouchement estimée
        tvDateConception.setText("Date de Conception : " + currentUser.getDateConception().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Calcul et affichage de la date d'accouchement estimée
        tvDateAccouchement.setText("Date d'accouchement : " + currentUser.dateAccouchement());

        // Set the current baby image
        int ImgRes = getBabyImageRes(currentUser.semaineGrossesse());
        ivBaby.setImageResource(ImgRes);
    }

    private int getBabyImageRes(int week) {
        int res;

        switch (week) {
            case 1:
                res = R.drawable.baby; break;
            case 2:
                res = R.drawable.baby; break;
            case 3:
                res = R.drawable.baby; break;
            case 4:
                res = R.drawable.baby; break;
            case 5:
                res = R.drawable.s5; break;
            case 6:
                res = R.drawable.baby; break;
            case 7:
                res = R.drawable.baby; break;
            case 8:
                res = R.drawable.s8; break;
            case 9:
                res = R.drawable.baby; break;
            case 10:
                res = R.drawable.s10; break;
            case 11:
                res = R.drawable.baby; break;
            case 12:
                res = R.drawable.baby; break;
            case 13:
                res = R.drawable.baby; break;
            case 14:
                res = R.drawable.s14; break;
            case 15:
                res = R.drawable.baby; break;
            case 16:
                res = R.drawable.baby; break;
            case 17:
                res = R.drawable.baby; break;
            case 18:
                res = R.drawable.baby; break;
            case 19:
                res = R.drawable.baby; break;
            case 20:
                res = R.drawable.s20; break;
            case 21:
                res = R.drawable.baby; break;
            case 22:
                res = R.drawable.baby; break;
            case 23:
                res = R.drawable.baby; break;
            case 24:
                res = R.drawable.s24; break;
            case 25:
                res = R.drawable.baby; break;
            case 26:
                res = R.drawable.baby; break;
            case 27:
                res = R.drawable.baby; break;
            case 28:
                res = R.drawable.s28; break;
            case 29:
                res = R.drawable.baby; break;
            case 30:
                res = R.drawable.baby; break;
            case 31:
                res = R.drawable.baby; break;
            case 32:
                res = R.drawable.baby; break;
            case 33:
                res = R.drawable.baby; break;
            case 34:
                res = R.drawable.baby; break;
            case 35:
                res = R.drawable.baby; break;
            case 36:
                res = R.drawable.s36; break;
            case 37:
                res = R.drawable.baby; break;
            case 38:
                res = R.drawable.baby; break;
            case 39:
                res = R.drawable.s39; break;
            default:
                res = R.drawable.baby;
        }

        return res;
    }

    public void updateDbUserDateConception(String dateConception, FirebaseFirestore db) {
        //-- test
        // Update an existing document
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String current = user.getUid();//getting unique user id

        DocumentReference docRef = db.collection("User").document(current);
        docRef.update("dateConception", dateConception).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Conception Date updated Successfully!" );
                } else {
                    Log.d(TAG, " dateConception Failure !!!!" );
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//---
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        // Handle presses on the action bar items
        int id = item.getItemId();
        if (id == R.id.miCompose) {
            Toast.makeText(this.getContext(), "Icon Compose",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miSetting) {
            Toast.makeText(this.getContext(), "Icon Profile",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miDateConception) {
            showDatePicker();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

}