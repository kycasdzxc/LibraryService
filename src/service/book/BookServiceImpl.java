package service.book;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.util.ArrayList;
import java.util.List;

import dao.BookDao;
import dao.RentDao;
import domain.Book;
import domain.LibBook;
import domain.Rent;
import util.LibUtil;
import util.StringUtil;

public class BookServiceImpl implements BookService {
	
	private BookDao bookDao = BookDao.getInstance();
	private RentDao rentDao = RentDao.getInstance();
	
	@Override
	public void regBook() { // 관리자 기능
		int index = nextInt("1.새로운 책 추가 2.기존의 책 추가 > ", 1, 2);

		Book book = null;
		
		List<LibBook> libBooks = bookDao.listLibBook();
		int libBookNum = libBooks.get(libBooks.size() - 1).getId() + 1;
		
		switch (index) {
		
		case 1: // 새로운 책 추가
			int id = nextInt("추가할 책의 도서번호 > ");
			book = bookDao.getBook(id);
			
			if( book == null ) {
				// 도서정보 생성
				book = new Book();
				book.setId(id);
				book.setTitle(nextLine("제목 > ", true, false));
				book.setAuthor(nextLine("저자 > ", true, false));
				book.setPublisher(nextLine("출판사 > ", true, false));
				book.setIsbn(nextLine("ISBN > ", false, true));
				
				// 소장도서 생성
				LibBook libBook = new LibBook();
				libBook.setId(libBookNum);
				libBook.setBookID(id);
				
				bookDao.regBook(book);
				bookDao.regLibBook(libBook);
				
			} else {
				System.out.println("존재하는 도서번호입니다.");
			}
			break;
			
		case 2: // 기존의 책 추가
			System.out.println("기존의 책을 추가합니다.");
			book = bookDao.getBook(nextInt("추가할 도서의 도서번호 > "));
			
			if ( book != null ) {
				// 재고 증가
				book.setAmount(book.getAmount() + 1);
				
				// 소장도서 생성
				LibBook libBook = new LibBook();
				libBook.setId(libBookNum);
				libBook.setBookID(book.getId());
				
				bookDao.modifyBook(book);
				bookDao.regLibBook(libBook);
				
			} else {
				System.out.println("존재하지 않는 도서번호입니다.");
			}
		}
	}
	
	@Override
	public List<Book> searchBook(boolean flag) {
		int index = nextInt("1. 제목 검색 2. 저자 검색 3. 출판사 검색 > ", 1, 3);
		String word = "";
		
		switch (index) {
		// 제목 검색
		case 1: word = nextLine("제목을 입력해주세요. > "); break;
		// 저자 검색
		case 2: word = nextLine("저자를 입력해주세요. > "); break;
		// 출판사 검색
		case 3: word = nextLine("출판사를 입력해주세요. > ");
		}
		
		List<Book> bookShelf = searchBookIf(word, index);
		
		if( !flag ) {
			return bookShelf;
		}
		
		if ( bookShelf.size() > 0 ) {
			listBookFormat(bookShelf);
		} else {
			System.out.println("소장되지 않은 도서입니다.");
		}
		return bookShelf;
	}

	private List<Book> searchBookIf(String keyword, int index) {
		List<Book> books = bookDao.listBook();
		keyword = transWord(keyword);

		List<Book> searchBooks = new ArrayList<Book>();
		
		for (Book book : books) {
			// 제목으로 검색
			if( index == 1 && transWord(book.getTitle()).contains(keyword) ) {
				searchBooks.add(book);
			}
			// 저자로 검색
			else if ( index == 2 && transWord(book.getAuthor()).contains(keyword) ) {
				searchBooks.add(book);
			}
			// 출판사로 검색
			else if ( index == 3 && transWord(book.getPublisher()).contains(keyword) ) {
				searchBooks.add(book);
			}
		}
		return searchBooks;
	}
	
	private String transWord(String str) {
		str = str.toLowerCase().replaceAll(" ", "");
		return str;
	}
	
	@Override
	public void findBook() { // 관리자 기능
		int index = nextInt("1.전체재고조회 2.도서번호검색 > ", 1, 2);
		
		switch (index) {
		case 1:
			listBookFormat(bookDao.listBook());
		
		case 2:
			int libBookNum = nextInt("조회할 도서의 도서번호를 입력해주세요. > ");
			List<LibBook> libBooks = bookDao.listLibBook(libBookNum);
			
			if ( libBooks.size() > 0 ) {
				listLibBookFormat(libBooks);
			} else {
				System.out.println("존재하지 않는 도서번호입니다.");
			}
		}
	}
	
	@Override
	public void modifyBook() { // 관리자 기능
		String isbn = nextLine("변경할 책의 ISBN > ");
		
		Book book = bookDao.getBook(isbn);
		
		if ( book != null ) {
			// 도서정보 수정
			book.setTitle(nextLine("이름 > ", true, false));
			book.setAuthor(nextLine("저자 > ", true, false));
			book.setPublisher(nextLine("출판사 > ", true, false));
			book.setIsbn(nextLine("ISBN > ", false, true));
			
			bookDao.modifyBook(book);
			
			System.out.println("책이 수정되었습니다.");
			
		} else {
			System.out.println("존재하지 않는 ISBN입니다.");
		}
	}
	
	@Override
	public void removeBook() { // 관리자 기능
		int id = nextInt("삭제할 책의 도서번호 > ");
		
		List<LibBook> libBooks = bookDao.listLibBook(id);
		
		if (libBooks.size() > 0) {
			// 소장도서 목록 출력
			listLibBookFormat(libBooks);
			
			int libBookNum = nextInt("삭제할 책의 소장번호 > ");
			
			LibBook libBook = bookDao.getLibBook(libBookNum);
			
			if ( libBook == null ) {
				System.out.println("소장번호를 확인해주세요.");
			}
			else if ( libBook.isRent() ) {
				System.out.println("대여 중인 도서입니다.");
			}
			else {
				// 재고 감소
				Book book = bookDao.getBook(libBook.getBookID());
				book.setAmount(book.getAmount() - 1);
				
				bookDao.removeLibBook(libBook);
				
				// 대여기록 삭제
				List<Rent> rents = rentDao.listRent(libBook.getId());
				
				for(int i = 0 ; i < rents.size() ; i++) {
					rentDao.removeRent(rents.get(i));
				}

				System.out.println("책이 삭제되었습니다.");
			}
		} else {
			System.out.println("존재하지 않는 도서번호입니다.");
		}
	}
	
	private void listBookFormat(List<Book> books) {
		LibUtil.bookIndex();
		for (Book book : books) {
			System.out.print("│     " + convert(book.getId() + "", 3) + "    │ ");
			System.out.print(convertLeft(StringUtil.bookTitleLength(book), 24) + " │ ");
			System.out.print(convertLeft(StringUtil.bookAuthorLength(book), 20) + " │ ");
			System.out.print(convertLeft(StringUtil.bookPublisherLength(book), 20) + " │ ");
			System.out.print(convertLeft(book.getIsbn(), 10) + " │  ");
			System.out.print(convert(book.getAmount() + "", 2) + "권  │");
			System.out.printf("%n────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		}
	}
	
	private void listLibBookFormat(List<LibBook> libBooks) {
		LibUtil.libBookIndex();
		for (LibBook libBook : libBooks) {
			System.out.print("│     " + convert(libBook.getBookID() + "", 3) + "    │ ");
			System.out.print("  " + convert(libBook.getId() + "", 3) + "    │ ");
			System.out.print(convertLeft(StringUtil.bookTitleLength(bookDao.getBook(libBook.getBookID())), 24) + " │ ");
			System.out.print(convertLeft(StringUtil.bookAuthorLength(bookDao.getBook(libBook.getBookID())), 20) + " │ ");
			System.out.print(convertLeft(StringUtil.bookPublisherLength(bookDao.getBook(libBook.getBookID())), 20) + " │   ");
			System.out.print(convert(libBook.isRent() ? "대여" : "보유", 4) + "   │");
			System.out.printf("%n───────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		}
	}
	
}
