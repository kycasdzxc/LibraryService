package service.book;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.util.ArrayList;
import java.util.List;

import dao.BookDao;
import dao.RentDao;
import dao.UserDao;
import domain.Book;
import domain.LibBook;
import domain.Rent;
import util.LibUtil;
import util.StringUtil;

public class BookServiceImpl implements BookService {
	
	private BookDao bookDao = BookDao.getInstance();
	private RentDao rentDao = RentDao.getInstance();
	
	List<LibBook> libBooks = bookDao.listLibBook();
	List<Rent> rents = rentDao.listRent();
	
	/**
	 * 도서관에 등록되어 있는 도서의 목록을 보여주는 메서드. 재고 확인 가능.
	 */
	private void bookList() {
		List<Book> books = bookDao.listBook();
		
		LibUtil.bookIndex();
		for (Book b : books) {
			System.out.print("│     " + convert(b.getId() + "", 3) + "    │ ");
			System.out.print(convertLeft(StringUtil.bookTitleLength(b), 24) + " │ ");
			System.out.print(convertLeft(StringUtil.bookAuthorLength(b), 20) + " │ ");
			System.out.print(convertLeft(StringUtil.bookPublisherLength(b), 20) + " │ ");
			System.out.print(convertLeft(b.getIsbn(), 10) + " │  ");
			System.out.println(convert(b.getAmount() + "", 2) + "권  │");
			System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		}
	}

	/**
	 * 신규 도서 등록 시, 중복된 도서번호가 있는지 확인하는 메서드. 해당 도서번호가 존재할 경우, 0을 반환한다.
	 * 
	 * @param bookID : 확인할 도서번호(Book 클래스의 id)
	 * @return bookID : 중복이 없을 경우, 도서번호 반환. 중복이 있을 경우, 0 반환
	 */
	private int duplBookID(int bookID) {
		List<Book> books = bookDao.listBook();
		
		for (Book b : books) {
			if (bookID == b.getId()) {
				bookID = 0;
				break;
			}
		}
		return bookID;
	}

	/**
	 * 소장도서 삭제 시, 해당 도서의 대여기록을 삭제해주는 메서드.
	 * 
	 * @param scrapKey : 삭제하려는 소장도서 번호(LibBook 클래스의 id)
	 */
	private void removeRents(int scrapKey) {
		for (int i = 0, cnt = 0; i < rents.size() - cnt; i++) {
			if (rents.get(i).getLibBookID() == scrapKey) {
				rents.remove(i);
				cnt++;
			}
		}
	}

	/**
	 * 문자의 공백을 제거하고, 영문자를 소문자로 변경하는 메서드.
	 * 
	 * @param s : 검색할 내용의 문자열
	 * @return s : 공백 제거, 영문자는 소문자로 변경
	 */
	private String transWord(String s) {
		s = s.toLowerCase().replaceAll(" ", "");
		return s;
	}

	/**
	 * 기존 도서목록 중 검색내용이 도서정보(제목, 저자, 출판사) 안에 포함된 도서를 찾아주는 메서드.
	 * 
	 * @param keyword : 검색할 내용의 문자열
	 * @param kind : 검색방식(1.제목, 2.저자, 3.출판사)
	 * @return books : 검색내용과 일치한 도서들을 Book 타입의 List 형식으로 반환
	 */
	private List<Book> findKindBy(String keyword, int kind) {
		List<Book> books = bookDao.listBook();
		List<Book> searchBooks = new ArrayList<Book>(); // 검색관리를 위한 List생성
		keyword = transWord(keyword); // 검색내용

		for (Book b : books) {
			
			switch (kind) { // 기존 도서목록의 제목과 일치하는 것이 있는지 확인
			
			case 1: // 제목으로 탐색
				if (transWord(b.getTitle()).contains(keyword)) {
					searchBooks.add(b);
				}
				break;
				
			case 2: // 저자로 탐색
				if (transWord(b.getAuthor()).contains(keyword)) {
					searchBooks.add(b);
				}
				break;
				
			case 3: // 출판사로 탐색
				if (transWord(b.getPublisher()).contains(keyword)) {
					searchBooks.add(b);
				}
				break;
				
			default:
				break;
			}
		}
		return searchBooks;
	}

	/**
	 * 검색한 도서의 소장 여부를 확인하는 메서드.
	 * 
	 * @param word  : 검색할 내용의 문자열
	 * @param index : 검색방식(1.제목, 2.저자, 3.출판사)
	 * @return 해당 도서가 존재할 경우, List 형식의 도서목록 반환
	 */
	private List<Book> searchBookIf(String word, int index) { // 소장 여부 확인
		List<Book> bookShelf = findKindBy(word, index);
		
		if (bookShelf.isEmpty()) {
			System.out.println("소장되지 않은 도서입니다.");
		}
		return bookShelf;
	}

	/**
	 * 도서 데이터를 목록화하여 전체재고조회(1)와 도서번호검색(2)으로 출력하는 메서드.
	 */
	public void findBook() { // 사서(관리자)용 기능
		int storageIndex = nextInt("1.전체재고조회 2.도서번호검색 > ", 1, 2);
		
		switch (storageIndex) {
		
		case 1:
			bookList();
			break;

		case 2:
			List<LibBook> booksInven = bookDao.listLibBook(nextInt("조회할 도서의 도서번호를 입력해주세요. > "));
			if (booksInven.size() > 0) { // 북ID(책번호)가 없을 경우
				LibUtil.libBookIndex();
				for (LibBook lB : booksInven) {
					System.out.print("│     " + convert(lB.getBookID() + "", 3) + "    │ ");
					System.out.print("  " + convert(lB.getId() + "", 3) + "    │ ");
					System.out.print(convertLeft(StringUtil.bookTitleLength(bookDao.getBook(lB.getBookID())), 24) + " │ ");
					System.out.print(convertLeft(StringUtil.bookAuthorLength(bookDao.getBook(lB.getBookID())), 20) + " │ ");
					System.out.print(convertLeft(StringUtil.bookPublisherLength(bookDao.getBook(lB.getBookID())), 20) + " │   ");
					System.out.println(convert(StringUtil.checkRentState(bookDao.getLibBook(lB.getId())), 4) + "   │");
					System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
				}
			} else {
				System.out.println("존재하지 않는 도서번호입니다.");
				return;
			}
			break;

		default:
			System.out.println("올바른 번호를 입력하세요.");
			break;
		} // switch
	}

	/**
	 * 도서를 제목(1), 저자(2), 출판사(3)로 검색하는 메서드.
	 * @return bookShelf : 검색한 도서정보를 Book 타입의 List로 반환
	 */
	public List<Book> searchBook() {
		int index = nextInt("1. 제목 검색 2. 저자 검색 3. 출판사 검색 > ", 1, 3);

		List<Book> bookShelf = null;
		
		switch (index) {
		
		case 1: // 제목으로 검색
			String word = nextLine("제목을 입력해주세요. > ");
			bookShelf = searchBookIf(word, index);
			break;
			
		case 2: // 저자로 검색
			String word2 = nextLine("저자를 입력해주세요. > ");
			bookShelf = searchBookIf(word2, index);
			break;
			
		case 3: // 출판사로 검색
			String word3 = nextLine("출판사를 입력해주세요. > ");
			bookShelf = searchBookIf(word3, index);
			break;
			
		default:
			System.out.println("올바른 번호를 입력하세요.");
			break;
		}
		return bookShelf;
	}

	/**
	 * 소장도서 데이터를 등록하는 메서드. 새로운 책 추가(1) 시, Book 클래스에도 데이터가 생성된다.
	 */
	public void regBook() { // 사서(관리자)용 기능
		List<Book> books = bookDao.listBook();
		int regIndex = nextInt("1.새로운 책 추가 2.기존의 책 추가 > ", 1, 2);

		switch (regIndex) {
		
		case 1:
			int regKey = duplBookID(nextInt("추가할 책의 도서번호 > ")); // 도서번호(bookID) 입력

			switch (regKey) {
			
			case 0: // 추가할 책의 번호가 이미 존재하는지 확인
				System.out.println("존재하는 도서번호입니다.");
				break;

			default:
				books.add(new Book(regKey, nextLine("제목 > ", true, false), nextLine("저자 > ", true, false),
									nextLine("출판사 > ", true, false), nextLine("ISBN > ", false, true)));
				libBooks.add(new LibBook(libBooks.get(libBooks.size() - 1).getId() + 1, regKey));

				Book enrollBook = bookDao.getBook(regKey); // 재고 증가
				enrollBook.setAmount(enrollBook.getAmount() + 1);
				break;
			}
			break;

		case 2:
			System.out.println("기존의 책을 추가합니다.");
			Book addBook = bookDao.getBook(nextInt("추가할 도서의 도서번호 > "));
			
			if (addBook != null) {
				libBooks.add(new LibBook(libBooks.get(libBooks.size() - 1).getId() + 1, addBook.getId()));
				addBook.setAmount(addBook.getAmount() + 1);
				break;
			} else {
				System.out.println("존재하지 않는 도서번호입니다.");
				break;
			}

		default:
			System.out.println("올바른 번호를 입력하세요.");
			break;
		}
	}

	/**
	 * 소장도서 데이터를 수정하는 메서드. ISBN으로 수정할 도서의 정보를 찾는다.
	 */
	public void modifyBook() { // 사서(관리자)용 기능

		Book alterbook = bookDao.getBook(nextLine("변경할 책의 ISBN > "));
		
		if (alterbook == null) {
			System.out.println("존재하지 않는 ISBN입니다.");
			return;
		}
		// 수정하는 내용 등록
		alterbook.setTitle(nextLine("이름 > ", true, false));
		alterbook.setAuthor(nextLine("저자 > ", true, false));
		alterbook.setPublisher(nextLine("출판사 > ", true, false));
		alterbook.setIsbn(nextLine("ISBN > ", false, true));

		System.out.println("책이 수정되었습니다.");
	}
	
	/**
	 * 소장도서 데이터를 삭제하는 메서드. 대여 중일 경우, 데이터 삭제는 불가하다.
	 */
	public void removeBook() { // 사서(관리자)용 기능

		List<LibBook> scrapBooks = bookDao.listLibBook(nextInt("삭제할 책의 도서번호 > ")); // BookID로 접근하여 libBookID 검색
		
		if (scrapBooks.size() > 0) { // 북ID(책번호)가 없을 경우
			LibUtil.libBookIndex();
			for (LibBook lB : scrapBooks) {
				System.out.print("│     " + convert(lB.getBookID() + "", 3) + "    │ ");
				System.out.print("  " + convert(lB.getId() + "", 3) + "    │ ");
				System.out.print(convertLeft(StringUtil.bookTitleLength(bookDao.getBook(lB.getBookID())), 24) + " │ ");
				System.out.print(convertLeft(StringUtil.bookAuthorLength(bookDao.getBook(lB.getBookID())), 20) + " │ ");
				System.out.print(convertLeft(StringUtil.bookPublisherLength(bookDao.getBook(lB.getBookID())), 20) + " │   ");
				System.out.println(convert(StringUtil.checkRentState(bookDao.getLibBook(lB.getId())), 4) + "   │");
				System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
			}

			int scrapKey = nextInt("삭제할 책의 소장번호 > ");
			
			if (bookDao.getLibBook(scrapKey) == null) {
				System.out.println("소장번호를 확인해주세요.");
			}
			else if (bookDao.getLibBook(scrapKey) != null && bookDao.getLibBook(scrapKey).isRent() == false) {
				
				LibBook libBook = bookDao.getLibBook(scrapKey);
				Book disuseBook = bookDao.getBook(libBook.getBookID()); // 재고 감소
				disuseBook.setAmount(disuseBook.getAmount() - 1);

				removeRents(scrapKey);

				libBooks.remove(bookDao.getLibBook(scrapKey)); // 상단에 출력된 libBookID 중 선택하여 삭제
				System.out.println("책이 삭제되었습니다.");
				
			} else if (bookDao.getLibBook(scrapKey).isRent() == true) {
				System.out.println("대여 중인 도서입니다.");
			}
			
		} else {
			System.out.println("존재하지 않는 도서번호입니다.");
		}
	}

	/**
	 * searchBook()에서 반환 받은 Book 타입의 List를 도서목록으로 출력하는 메서드.
	 * @param bookShelf : searchBook()에서 반환 받은 List 형식의 도서목록
	 */
	public void searchList(List<Book> bookShelf) { // 이용자 기능
		if (bookShelf.size() > 0) {
			LibUtil.bookIndex();
			for (Book b : bookShelf) {
				System.out.print("│     " + convert(b.getId() + "", 3) + "    │ ");
				System.out.print(convertLeft(StringUtil.bookTitleLength(b), 24) + " │ ");
				System.out.print(convertLeft(StringUtil.bookAuthorLength(b), 20) + " │ ");
				System.out.print(convertLeft(StringUtil.bookPublisherLength(b), 20) + " │ ");
				System.out.print(convertLeft(b.getIsbn(), 10) + " │  ");
				System.out.println(convert(b.getAmount() + "", 2) + "권  │");
				System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			}
		}
	}

}
