package com.casper.testdrivendevelopment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.casper.testdrivendevelopment.data.BookServer;
import com.casper.testdrivendevelopment.data.model.Book;
import com.casper.testdrivendevelopment.data.model.BookFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_NEW = 1;
    public static final int CONTEXT_MENU_UPDATE = CONTEXT_MENU_NEW + 1;
    public static final int CONTEXT_MENU_REMOVE = CONTEXT_MENU_UPDATE + 1;
    public static final int CONTEXT_MENU_ABOUT = CONTEXT_MENU_REMOVE + 1;
    public static final int REQUEST_CODE_NEW_BOOK = 901;
    private static final int REQUEST_CODE_UPDATE_BOOK = 902;
    private ArrayList<Book> listBooks;
    BooksArrayAdapter bookAdapter;
    BookServer bookServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookServer = new BookServer(this);
        listBooks = bookServer.load();
        if (listBooks.size() == 0){
            InitData();
        }

        bookAdapter =new BooksArrayAdapter(this, R.layout.list_item_books, listBooks);

        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<>();
        datas.add(new BookListFragment(bookAdapter));
        datas.add(new WebViewFragment());
        datas.add(new WebViewFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStop() {
        super.onStop();
        bookServer.save();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == findViewById(R.id.list_view_books)) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(listBooks.get(info.position).getTitle());
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTEXT_MENU_NEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_UPDATE, 0, "修改");
            menu.add(0, CONTEXT_MENU_REMOVE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_NEW_BOOK:
                if (resultCode == RESULT_OK){
                    String book_title = data.getStringExtra("book_title");
                    double book_price = data.getDoubleExtra("book_price", 0);
                    int insertPosition = data.getIntExtra("book_insert_position", 0);
                    getListBooks().add(insertPosition, new Book(book_title , book_price,R.drawable.book_no_name));
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(BookListMainActivity.this,"新建成功",Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:
                if (resultCode == RESULT_OK){
                    int bookUpdatePosition = data.getIntExtra("book_insert_position",0);
                    Book bookAtPosition = getListBooks().get(bookUpdatePosition);

                    bookAtPosition.setTitle(data.getStringExtra("book_title"));
                    bookAtPosition.setPrice(data.getDoubleExtra("book_price",0));
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(BookListMainActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_NEW:
                Intent intent1 = new Intent(BookListMainActivity.this, NewBookActivity.class);
                intent1.putExtra("book_title", "无名书籍");
                intent1.putExtra("book_price", 0.0);
                intent1.putExtra("book_insert_position", ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);
                startActivityForResult(intent1, REQUEST_CODE_NEW_BOOK);

/*                final int insertPosition = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                listBooks.add(insertPosition, new Book("无名书籍", R.drawable.book_no_name));
                bookAdapter.notifyDataSetChanged();*/
                break;
            case CONTEXT_MENU_UPDATE:
                int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Intent intent2 = new Intent(BookListMainActivity.this, NewBookActivity.class);
                intent2.putExtra("book_title", listBooks.get(position).getTitle());
                intent2.putExtra("book_price", listBooks.get(position).getPrice());
                intent2.putExtra("book_insert_position", position);
                startActivityForResult(intent2, REQUEST_CODE_UPDATE_BOOK);
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
        listBooks.add(new Book("软件项目管理案例教程（第4版）", 38.00, R.drawable.book_2 ));
        listBooks.add(new Book("创新工程实践", 35.00,R.drawable.book_no_name));
        listBooks.add(new Book("信息安全数学基础（第2版）", 40.00,R.drawable.book_1));
    }

    public class BooksArrayAdapter extends ArrayAdapter<Book> {
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
            //book_item.setCoverResourceId(R.drawable.book_no_name);
            bookImage.setImageResource(book_item.getCoverResourceId());
            bookTitle.setText(book_item.getTitle()+ "," + book_item.getPrice() + "元");

            return item;
        }
    }
}
