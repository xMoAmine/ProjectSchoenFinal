package com.example.projectschoen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class ViewModel extends AndroidViewModel {

    private final MutableLiveData<String[]> mInsoleColor = new MutableLiveData<>();

    LiveData<String[]> getInsoleColor() {return mInsoleColor;}

    public ViewModel(@NonNull Application application) {
        super(application);

        Integer[] value = new Integer[]{0,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,0};
        // onInsoleValueReceived(value);
    }


//    private void onInsoleValueReceived(Integer[] value) {
//
//        HashMap<Integer, String> colourMap = new HashMap<>();
//        colourMap.put(0, "#FAEA5C"); 	// Light yellow
//        colourMap.put(1, "#F7D055");
//        colourMap.put(2, "#F5B64E");
//        colourMap.put(3, "#F29C48");
//        colourMap.put(4, "#F08241");
//        colourMap.put(5, "#ED683A");
//        colourMap.put(6, "#EB4E34");
//        colourMap.put(7, "#E8342D");
//        colourMap.put(8, "#E61A26");
//        colourMap.put(9, "#E40120");    // Red
//
//        String[] colors = new String[value.length];
//
//        for (int i=0; i<value.length; i++){
//            colors[i] = colourMap.get(value[i]);
//        }

        // mInsoleColor.postValue(colors);
    }


