package com.example.itlavision;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentTranslated extends Fragment implements View.OnClickListener {
    FloatingActionButton enter;
    TranslateLanguage translateLanguage;

    String languageOrigin;
    String languageOriginCode;

    String languageDestiny;
    String languageDestinyCode;

    TextView editText;

    EditText textToTranslate;
    Button Idioma1;
    Button Idioma2;
    TextView traductorT;
    ImageButton clear, camara;
    ImageButton copy;
    ImageButton invert;
    Context context;
    Bitmap imageBitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static FragmentTranslated instancia;

    public static FragmentTranslated getInstance() {
        if (instancia == null){
            instancia = new FragmentTranslated();
        }
        return instancia;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getString("boton") == "Idioma1"){
                languageOrigin = getArguments().getString("name");
                languageOriginCode = getArguments().getString("language");
            }
            if (getArguments().getString("boton") == "Idioma2"){
                languageDestiny= getArguments().getString("name");
                languageDestinyCode= getArguments().getString("language");
            }
        } else {
            languageOrigin = "Ingles";
            languageOriginCode = "en";

            languageDestiny= "Español";
            languageDestinyCode= "es";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.translated_layout, container, false);

        translateLanguage = TranslateLanguage.getInstance();
        enter = view.findViewById(R.id.enter);

        editText = getActivity().findViewById(R.id.translatedText);

        textToTranslate = view.findViewById(R.id.edittext);
        Idioma1= view.findViewById(R.id.Idioma1);
        Idioma2= view.findViewById(R.id.Idioma2);
        traductorT = getActivity().findViewById(R.id.traduccionT);
        clear = view.findViewById(R.id.clear);
        camara = view.findViewById(R.id.camara);
        copy = getActivity().findViewById(R.id.copy);
        invert = view.findViewById(R.id.invert);

        if(languageOrigin != null){
            Idioma1.setText(languageOrigin);
        }

        if (languageDestiny != null) {
            Idioma2.setText(languageDestiny);
            traductorT.setText(languageDestiny);
        }

        enter.setOnClickListener(this);
        Idioma1.setOnClickListener(this);
        Idioma2.setOnClickListener(this);
        clear.setOnClickListener(this);
        camara.setOnClickListener(this);
        copy.setOnClickListener(this);
        invert.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.imageBitmap = imageBitmap;
            Toast.makeText(getContext(), "bien", Toast.LENGTH_LONG).show();
            //imageView.setImageBitmap(imageBitmap);
            detectTextFromImage();
        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if(blockList.size()==0){
            Toast.makeText(getContext(), "Texto no detectado", Toast.LENGTH_LONG).show();
        } else {
            for(FirebaseVisionText.Block block:  firebaseVisionText.getBlocks()){
                String text = block.getText();
                textToTranslate.setText(text);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter:
                translateLanguage.TranslateThisText(languageOriginCode, languageDestinyCode, textToTranslate.getText().toString(), getActivity(), new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result, ArrayList listLenguage) {
                        editText.setText(result);
                    }
                });
                break;

            case R.id.Idioma1:
                FragmentList fragmentList = new FragmentList();

                Bundle bundle = new Bundle();
                bundle.putString("boton", "Idioma1");
                fragmentList.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment, fragmentList).commit();
                break;

            case R.id.Idioma2:
                fragmentList = new FragmentList();

                bundle = new Bundle();
                bundle.putString("boton", "Idioma2");
                fragmentList.setArguments(bundle);

                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment, fragmentList).commit();
                break;

            case R.id.clear:
                editText.setText("");
                textToTranslate.setText("");
                break;

            case R.id.camara:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
                textToTranslate.setText("");
                break;

            case R.id.copy:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, editText.getText());
                intent.setType("text/plain");

                Intent chooser = Intent.createChooser(intent, "Selecciona");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(chooser);
                }
                break;

            case R.id.invert:

                String languageOriginTemp= languageOrigin;
                String languageOriginCodeTemp= languageOriginCode;

                String languageDestinyTemp= languageDestiny;
                String languageDestinyCodeTemp= languageDestinyCode;

                languageOrigin= languageDestinyTemp;
                languageOriginCode= languageDestinyCodeTemp;

                languageDestiny= languageOriginTemp;
                languageDestinyCode= languageOriginCodeTemp;

                if(languageOrigin != null){
                    Idioma1.setText(languageOrigin);
                }

                if (languageDestiny != null) {
                    Idioma2.setText(languageDestiny);
                    traductorT.setText(languageDestiny);
                }
                break;
        }
    }

}
