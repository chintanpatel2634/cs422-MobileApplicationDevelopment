package com.example.converter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static double MI_TO_KM = 1.60934;
    private static double KM_TO_MI = 0.621371;
    private String historySave = "";
    private double inputNumber;
    private double outputNumber;
    private double conversionConstant = MI_TO_KM;
    private String inputWord = "Mi";
    private String outputWord = "Km";
    private static final String TAG = "MainActivity";
    private EditText input;
    private TextView output;
    private TextView history;
    private TextView inputTextName;
    private TextView outputTextName;
    private SharedPreferences myPrefs;
    private RadioButton optionSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.inputValue);
        output = findViewById(R.id.outputText);
        history = findViewById(R.id.history);
        inputTextName = findViewById(R.id.inputTextName);
        outputTextName = findViewById(R.id.outputTextName);
        myPrefs = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        int optionSelectedId = myPrefs.getInt("SAVE_OPTION", R.id.option1);
        long conversionConstantLong = myPrefs.getLong("SAVE_CONVERSION_CONSTANT", Double.doubleToRawLongBits(MI_TO_KM));
        conversionConstant = Double.longBitsToDouble(conversionConstantLong);
        inputWord = myPrefs.getString("SAVE_INPUT_WORD", "Mi");
        outputWord = myPrefs.getString("SAVE_OUTPUT_WORD", "Km");
        String inputSavedName = myPrefs.getString("SAVE_INPUT_TEXT_NAME", "Miles Value:");
        String outputSavedName = myPrefs.getString("SAVE_OUTPUT_TEXT_NAME", "Kilometers Value:");
        inputTextName.setText(inputSavedName);
        outputTextName.setText(outputSavedName);

        optionSelected = findViewById(optionSelectedId);
        optionSelected.setChecked(true);

        //show scrollbar
        history.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        //save output TextView
        outState.putString("outputSaved", output.getText().toString());

        //save history TextView
        outState.putString("historySaved", history.getText().toString());

        //Run later
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        output.setText(savedInstanceState.getString("outputSaved"));
        history.setText(savedInstanceState.getString("historySaved"));
        historySave = history.getText().toString();
    }

    public void convertValue(View v) {
        //get input text
        String inputString = input.getText().toString();
        inputNumber = Double.parseDouble(inputString);

        //convert into KM's
        outputNumber = inputNumber * conversionConstant;


        //printing the output
        output.setText(String.format("%.1f", outputNumber));
        input.setText(String.format(""));

        //printing History and save the history for next history
        history.setText(String.format("%.1f %s ==> %.1f %s", inputNumber, inputWord, outputNumber, outputWord) + "\n" + historySave);
        historySave = history.getText().toString();

    }


    //select the option from mi_to_km or km_to_mi
    public void chooseConvert(View v) {

        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        Log.d(TAG, "chooseConvert: " + v);
        switch(v.getId()) {
            //mi_to_km
            case R.id.option1:
                conversionConstant = MI_TO_KM;  //multiply this constant if option 1
                inputWord = "Mi";
                outputWord = "Km";
                inputTextName.setText(String.format("Miles Value:"));
                outputTextName.setText(String.format("Kilometers Value:"));
                prefsEditor.putInt("SAVE_OPTION", R.id.option1);
                prefsEditor.putLong("SAVE_CONVERSION_CONSTANT", Double.doubleToRawLongBits(conversionConstant));
                prefsEditor.putString("SAVE_INPUT_WORD", inputWord);
                prefsEditor.putString("SAVE_OUTPUT_WORD", outputWord);
                prefsEditor.putString("SAVE_INPUT_TEXT_NAME", "Miles Value:");
                prefsEditor.putString("SAVE_OUTPUT_TEXT_NAME", "Kilometers Value:");
                prefsEditor.apply();
                break;
            //km_to_mi
            case R.id.option2:
                conversionConstant = KM_TO_MI; //multiply this constant if option 2
                inputWord = "Km";
                outputWord = "Mi";
                inputTextName.setText(String.format("Kilometers Value:"));
                outputTextName.setText(String.format("Miles Value:"));
                prefsEditor.putInt("SAVE_OPTION", R.id.option2);
                prefsEditor.putLong("SAVE_CONVERSION_CONSTANT", Double.doubleToRawLongBits(conversionConstant));
                prefsEditor.putString("SAVE_INPUT_WORD", inputWord);
                prefsEditor.putString("SAVE_OUTPUT_WORD", outputWord);
                prefsEditor.putString("SAVE_INPUT_TEXT_NAME", "Kilometers Value:");
                prefsEditor.putString("SAVE_OUTPUT_TEXT_NAME", "Miles Value:");
                prefsEditor.apply();
                break;
        }
    }

    //clearing the history by clicking "CLEAR"
    public void clearHistory(View v) {
        history.setText(String.format(""));
        historySave = "";
    }
}

