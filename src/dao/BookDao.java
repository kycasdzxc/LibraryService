package dao;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import domain.Book;
import domain.LibBook;
import util.DataUtil;

public class BookDao {
	
	// Singleton 패턴 적용
	private static BookDao bookDao = new BookDao();
	
	private BookDao() {}
	
	public static BookDao getInstance() {
		return bookDao;
	}
	
	private static List<Book> books;
	private static List<LibBook> libBooks;
	
	// Book, LibBook 데이터 초기화
	{
		books = DataUtil.initBooks();
		libBooks = DataUtil.initLibBooks();
	}
	
	// 도서정보 목록조회
	public List<Book> listBook() {
		return books;
	}
	
	// 도서정보 조회
	public Book getBook(int id) { 
		for (Book b : books) {
			if (b.getId() == id) {
				return b;
			}
		}
		return null;
	}
	
	// 도서정보 조회(ISBN)
	public Book getBook(String isbn) { 
		for (Book b : books) {
			if (b.getIsbn().equals(isbn)) {
				return b;
			}
		}
		return null;
	}
	
	// 소장도서 목록조회
	public List<LibBook> listLibBook() {
		return libBooks;
	}
	
	// 소장도서 목록조회(도서정보 ID)
	public List<LibBook> listLibBook(int id) {
		List<LibBook> lBooks = new ArrayList<LibBook>();
		
		for (LibBook lB : libBooks) {
			if (lB.getBookID() == id) {
				lBooks.add(lB);
			}
		}
		return lBooks;
	}
	
	// 소장도서 조회
	public LibBook getLibBook(int id) { 
		for (LibBook lB : libBooks) {
			if (lB.getId() == id) {
				return lB;
			}
		}
		return null;
	}
	
	// Book 데이터 저장
	private void saveBook() {
		ObjectOutputStream oos;
		try {
		oos = new ObjectOutputStream(new FileOutputStream("books.ser"));
		oos.writeObject(books);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// LibBook 데이터 저장
	private void saveLibBook() {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("libBooks.ser"));
			oos.writeObject(libBooks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
