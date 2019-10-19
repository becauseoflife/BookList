package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewBookActivity extends AppCompatActivity {

    private Button buttonOk, buttonCancle;
    private EditText editTextBookTitle, editTextBookPrice;
    private int insertPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        buttonOk = findViewById(R.id.button_ok);
        buttonCancle = findViewById(R.id.button_cancle);
        editTextBookTitle = findViewById(R.id.edit_text_book_title);
        editTextBookPrice = findViewById(R.id.edit_text_book_price);

        editTextBookTitle.setText(getIntent().getStringExtra("book_title"));
        editTextBookPrice.setText(getIntent().getDoubleExtra("book_price",-1)+"");
        insertPosition = getIntent().getIntExtra("book_insert_position", 0);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("book_insert_position", insertPosition);
                intent.putExtra("book_title", editTextBookTitle.getText().toString().trim());
                intent.putExtra("book_price", Double.parseDouble(editTextBookPrice.getText().toString()));
                setResult(RESULT_OK, intent);
                NewBookActivity.this.finish();
            }
        });
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBookActivity.this.finish();
            }
        });

    }
}
