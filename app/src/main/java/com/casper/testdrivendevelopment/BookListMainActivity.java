package com.casper.testdrivendevelopment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_NEW = 1;
    public static final int CONTEXT_MENU_REVISE = CONTEXT_MENU_NEW + 1;
    public static final int CONTEXT_MENU_REMOVE = CONTEXT_MENU_REVISE + 1;
    public static final int CONTEXT_MENU_ABOUT = CONTEXT_MENU_REMOVE + 1;
    private ArrayList<Book> listBooks;
    private ListView listViewBooks;
    BooksArrayAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        InitData();

        listViewBooks =findViewById(R.id.list_view_books);
        bookAdapter =new BooksArrayAdapter(this, R.layout.list_item_books, listBooks);
        listViewBooks.setAdapter(bookAdapter);

        this.registerForContextMenu(listViewBooks);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == listViewBooks) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(listBooks.get(info.position).getTitle());
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTEXT_MENU_NEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_REVISE, 0, "修改");
            menu.add(0, CONTEXT_MENU_REMOVE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_NEW:
                final int insertPosition = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                listBooks.add(insertPosition, new Book("无名书籍", R.drawable.book_no_name));
                bookAdapter.notifyDataSetChanged();
                Toast.makeText(BookListMainActivity.this,"新建成功",Toast.LENGTH_LONG).show();
                break;
            case CONTEXT_MENU_REVISE:
                break;
            case CONTEXT_MENU_REMOVE:
                final int removePosition = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("删除图书？")
                        .setMessage("确定删除这条图书吗？")
                        .setIcon(R.drawable.alter)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listBooks.remove(removePosition);

                                bookAdapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
                break;
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(BookListMainActivity.this,"版权所有 by xxx...",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
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
