package com.casper.testdrivendevelopment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {

    private BookListMainActivity.BooksArrayAdapter bookAdapter;
    public BookListFragment(BookListMainActivity.BooksArrayAdapter bookAdapter) {
        // Required empty public constructor
        this.bookAdapter = bookAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        ListView listViewBooks = view.findViewById(R.id.list_view_books);
        listViewBooks.setAdapter(bookAdapter);
        this.registerForContextMenu(listViewBooks);

        return view;
    }

}
