package com.casper.testdrivendevelopment;

import android.content.Context;
import android.media.Image;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    private ArrayList<Book> listBooks;
    private ListView listViewBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        InitData();

        listViewBooks =findViewById(R.id.list_view_books);
        BooksArrayAdapter theAdapter =new BooksArrayAdapter(this, R.layout.list_item_books, listBooks);
        listViewBooks.setAdapter(theAdapter);
    }

    public List<Book> getListBooks()
    {
        return listBooks;
    }

    private void InitData() {
        listBooks = new ArrayList<>();
        listBooks.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        listBooks.add(new Book("创新工程实践", R.drawable.book_no_name));
        listBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }

    private class BooksArrayAdapter extends ArrayAdapter<Book> {
        private int resourceId;
        public BooksArrayAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView bookImage = item.findViewById(R.id.image_view_book_cover);
            TextView bookTitle = item.findViewById(R.id.text_view_book_title);

            Book book_item = this.getItem(position);
            bookImage.setImageResource(book_item.getCoverResourceId());
            bookTitle.setText(book_item.getTitle());

            return item;
        }


    }
}
