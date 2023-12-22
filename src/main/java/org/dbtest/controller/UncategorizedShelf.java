package org.dbtest.controller;

import org.dbtest.BookView2;

import java.net.MalformedURLException;

public class UncategorizedShelf extends Shelf{
    @Override
    public void updateView(){
        int i = 0;
        int j = 0;
        for (var book: bs.getUncategorizedBooks()){
            BookView2 newBook = null;
            try {
                newBook = new BookView2(book);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            //newBook.setText(book.getName());

            booksPane.add(newBook, j, i);
            if(++j == booksPane.getColumnCount()){
                j = 0;
                booksPane.addRow(i++);
            }
        }
    }
}
