package com.casper.testdrivendevelopment.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.baidu.mapapi.map.Marker;
import com.casper.testdrivendevelopment.data.model.Book;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookServerTest {
    private BookServer bookKeeper;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        bookKeeper = new BookServer(context);
        bookKeeper.load();
    }

    @After
    public void tearDown() throws Exception {
        bookKeeper.save();
    }

    @Test
    public void getBooks() {
        Assert.assertNotNull(bookKeeper.getBooks());
        BookServer bookServer = new BookServer(context);
        Assert.assertNotNull(bookKeeper.getBooks());
        Assert.assertEquals(0, bookServer.getBooks().size());

    }

    @Test
    public void saveThenLoad() {
        BookServer bookServerTest = new BookServer(context);
        Assert.assertEquals(0, bookServerTest.getBooks().size());
        Book book = new Book("test title", 1.23, 123);
        bookServerTest.getBooks().add(book);
        book = new Book("test book2", 1.24,124);
        bookServerTest.getBooks().add(book);
        bookServerTest.save();

        BookServer bookServerLoader = new BookServer(context);
        bookServerLoader.load();
        Assert.assertEquals(bookServerTest.getBooks().size(), bookServerLoader.getBooks().size());
        for (int i=0; i<bookServerTest.getBooks().size(); i++)
        {
            Book bookThis = bookServerTest.getBooks().get(i);
            Book bookThat = bookServerLoader.getBooks().get(i);
            Assert.assertEquals(bookThat.getCoverResourceId(), bookThis.getCoverResourceId());
            Assert.assertEquals(bookThat.getTitle(), bookThis.getTitle());
            Assert.assertEquals(bookThat.getPrice(),bookThis.getPrice(), 1e-4);
        }

    }

    @Test
    public void saveEmptyThenLoad() {
        BookServer bookServerTest = new BookServer(context);
        Assert.assertEquals(0, bookServerTest.getBooks().size());
        bookServerTest.save();

        BookServer bookServerLoader = new BookServer(context);
        bookServerLoader.load();
        Assert.assertEquals(bookServerTest.getBooks().size(), bookServerLoader.getBooks().size());
    }
}