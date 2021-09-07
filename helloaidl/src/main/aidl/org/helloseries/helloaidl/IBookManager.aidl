// BookManager.aidl
package org.helloseries.helloaidl;

// Declare any non-default types here with import statements
import org.helloseries.helloaidl.model.Book;

interface IBookManager {

List<Book> getBooks();

void addBook(in Book book);

}