package com.example.suivigrossesse.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.suivigrossesse.R;

import java.util.Calendar;
import java.util.Date;

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
        calendarView = binding.findViewById(R.id.calendarView);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        calendar.set(Calendar.YEAR, 2024);
        calendar.set(Calendar.MONTH, Calendar.JUNE);

        calendarView.setDate(calendar.getTimeInMillis(), false, false);
        // --- My Code End ---

        return binding;
    }

}