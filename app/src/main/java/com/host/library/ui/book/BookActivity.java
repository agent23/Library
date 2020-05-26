package com.host.library.ui.book;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.host.library.R;

import java.util.ArrayList;
import java.util.Calendar;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner_year;
    private Button btn_add_book;
    private EditText et_isbn, et_title, et_author, et_publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initViews();

    }

    private void initViews(){
        et_isbn = findViewById(R.id.et_isbn);
        et_title = findViewById(R.id.et_title);
        et_author = findViewById(R.id.et_author);
        et_publisher = findViewById(R.id.et_publisher);
        spinner_year = findViewById(R.id.spinner_year);
        btn_add_book = findViewById(R.id.btn_add_book);
        btn_add_book.setOnClickListener(this);

        setSpinnerList();
    }

    private void setSpinnerList(){
        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);

        spinner_year = findViewById(R.id.spinner_year);
        spinner_year.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_book){

        }
    }
}
