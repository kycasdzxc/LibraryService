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
	
	private List<Book> books = DataUtil.initBooks();
	private List<LibBook> libBooks = DataUtil.initLibBooks();
	
	// 도서정보 목록조회
	public List<Book> listBook() {
		return books;
	}
	
	// 도서정보 조회
	public Book getBook(int bookID) { 
		for (Book book : books) {
			if (book.getId() == bookID) {
				return book;
			}
		}
		return null;
	}
	
	// 도서정보 조회(ISBN)
	public Book getBook(String isbn) { 
		for (Book book : books) {
			if (book.getIsbn().equals(isbn)) {
				return book;
			}
		}
		return null;
	}
	
	// 도서정보 생성
	public void regBook(Book book) {
		books.add(book);
		saveBook();
	}
	
	// 도서정보 수정
	public void modifyBook(Book b) {
		Book book = getBook(b.getId());
		
		book.setTitle(b.getTitle());
		book.setAuthor(b.getAuthor());
		book.setPublisher(b.getPublisher());
		book.setIsbn(b.getIsbn());
		book.setAmount(b.getAmount());
		
		saveBook();
	}
	
	// 도서정보 삭제
	public void removeBook(Book book) {
		books.remove(book);
		saveBook();
	}
	
	// 소장도서 목록조회
	public List<LibBook> listLibBook() {
		return libBooks;
	}
	
	// 소장도서 목록조회(도서정보 ID)
	public List<LibBook> listLibBook(int bookID) {
		List<LibBook> libBooks = new ArrayList<LibBook>();
		
		for (LibBook libBook : this.libBooks) {
			if (libBook.getBookID() == bookID) {
				libBooks.add(libBook);
			}
		}
		return libBooks;
	}
	
	// 소장도서 조회
	public LibBook getLibBook(int libBookID) { 
		for (LibBook libBook : libBooks) {
			if (libBook.getId() == libBookID) {
				return libBook;
			}
		}
		return null;
	}
	
	// 소장도서 생성
	public void regLibBook(LibBook libBook) {
		libBooks.add(libBook);
		saveLibBook();
	}
	
	// 소장도서 대여, 반납
	public void updateLibBook(LibBook lb) {
		LibBook libBook = getLibBook(lb.getId());
		
		libBook.setRent(lb.isRent());
		
		saveLibBook();
	}
	
	// 소장도서 삭제
	public void removeLibBook(LibBook lb) {
		libBooks.remove(lb);
		saveLibBook();
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
