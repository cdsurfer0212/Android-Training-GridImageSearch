package com.example.gridimagesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.models.SearchCondition;

public class AdvancedSearchDialog extends DialogFragment {

    private SearchCondition searchCondition;
    
    public interface AdvancedSearchDialogListener {
        void onFinishEditDialog(SearchCondition searchCondition);
    }
    
    public AdvancedSearchDialog() {
        // Empty constructor required for DialogFragment
        searchCondition = new SearchCondition();
    }

    public static AdvancedSearchDialog newInstance() {
        AdvancedSearchDialog frag = new AdvancedSearchDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advanced_search, container);
        getDialog().setTitle("Advanced search options");
      
        Spinner spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colorFilterAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.color_filter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colorFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spColorFilter.setAdapter(colorFilterAdapter);
        spColorFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                searchCondition.setImageColor(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        
        Spinner spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spImageSize.setAdapter(imageSizeAdapter);
        spImageSize.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                searchCondition.setImageSize(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        
        final Spinner spImageType = (Spinner) view.findViewById(R.id.spImageType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spImageType.setAdapter(imageTypeAdapter);
        
        ((Button) view.findViewById(R.id.btnSave)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdvancedSearchDialogListener listener = (AdvancedSearchDialogListener) getActivity();
                
                searchCondition.setImageType(spImageType.getSelectedItem().toString());
                
                listener.onFinishEditDialog(searchCondition);
                dismiss();
            }
        });
        
        return view;
    }

}