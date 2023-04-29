package service.rent;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.nextInt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.BookDao;
import dao.RentDao;
import dao.UserDao;
import domain.Book;
import domain.LibBook;
import domain.Rent;
import domain.User;
import service.book.BookService;
import service.book.BookServiceImpl;
import util.LibUtil;
import util.StringUtil;

public class RentServiceImpl implements RentService {
	
	private BookService bookService = new BookServiceImpl();
	
	private UserDao userDao = UserDao.getInstance();
	private BookDao bookDao = BookDao.getInstance();
	private RentDao rentDao = RentDao.getInstance();
	
	@Override
	public void listRent(User user) {
		System.out.println("도서대여이력입니다.");
		
		List<Rent> listRent = user.isAdmin() ? rentDao.listRent() : rentDao.listRent(user);
		
		listRentHistFormat(listRent);
	}
	
	@Override
	public void rentBook(User user) { // 대여 기능
		int userID = user.getId();
		
		// 관리자 계정일 경우
		if( user.isAdmin() ) {
			userID = nextInt("ID를 입력해주세요. > ");
		}
		
		if (userDao.getUser(userID) != null) {
			System.out.println("대여할 도서를 검색해주세요.");
			
			List<Book> listBook = bookService.searchBook(false);
			
			if ( listBook.size() > 0 ) {
				// 대여정보가 담긴 도서목록 출력
				listRentFormat(listBook);
				
				try {
					int rentNum = nextInt("대여할 도서의 번호를 입력해주세요. > ") - 1;
					Book book = listBook.get(rentNum);
					
					bookFormat(book); // 도서정보 출력
					
					int index = nextInt("대여하시겠습니까?[1.네 2.아니오] > ");
					
					if ( index == 1 ) {
						// 대여할 도서가 있는 경우
						if ( book.getAmount() > 0 ) {
							rent(book, userID);
						}
						// 모든 도서가 대여 중일 경우
						else {
							System.out.println("현재 모든 책이 대여 중입니다.");
						}
					}
				} catch (Exception e) {
					System.out.println("잘못 입력하셨습니다.");
				}
			}
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}

	private void rent(Book book, int userID) {
		List<Rent> listRent = rentDao.listRent();
		int rentNum = listRent.get(listRent.size() - 1).getRentNum() + 1;
		
		// 대여정보 생성
		Rent rent = new Rent();
		rent.setRentNum(rentNum);
		rent.setUserID(userID);
		rent.setLibBookID(findLibBookIdRentAble(book));
		
		rentDao.regRent(rent);

		// 재고 변경
		book.setAmount(book.getAmount() - 1);
		bookDao.modifyBook(book);
		
		System.out.println("대여완료");
	}

	private int findLibBookIdRentAble(Book book) {
		List<LibBook> libBooks = bookDao.listLibBook(book.getId());
		
		for (LibBook libBook : libBooks) {
			if ( !libBook.isRent() ) {
				libBook.setRent(true);
				bookDao.updateLibBook(libBook);
				return libBook.getId();
			}
		}
		return 0;
	}
	
	@Override
	public void returnBook(User user) throws ParseException { // 반납 기능
		int userID = user.getId();
		
		// 관리자 계정일 경우
		if( user.isAdmin() ) {
			userID = nextInt("ID를 입력해주세요. > ");
		}
		
		System.out.println("도서반납입니다.");
		
		List<Rent> listRent = rentDao.listRent();
		int cnt = 1;
		
		List<Rent> listReturnAble = new ArrayList<Rent>();
		
		LibUtil.returnIndex();
		for (Rent rent : listRent) {
			LibBook libBook = bookDao.getLibBook(rent.getLibBookID());
			
			if ( rent.getUserID() == userID && libBook.isRent() ) {
				returnBookFormat(rent, cnt);
				listReturnAble.add(rent);
				cnt++;
				
			}
		}
		
		if (listReturnAble.size() > 0) {
			int returnNum = nextInt("반납할 도서의 번호를 입력해주세요. > ") - 1;
			
			try {
				// 반납일자 저장
				Rent rent = listReturnAble.get(returnNum);
				rentDao.modifyRent(rent);
				
				// 대여상태 변경
				LibBook libBook = bookDao.getLibBook(rent.getLibBookID());
				libBook.setRent(false);
				bookDao.updateLibBook(libBook);
				
				// 도서재고 변경
				Book book = bookDao.getBook(libBook.getBookID());
				book.setAmount(book.getAmount() + 1);
				bookDao.modifyBook(book);
				
				System.out.println("반납완료");
				
			} catch (Exception e) {
				System.out.println("잘못 입력하셨습니다.");
			}
			
		} else {
			System.out.printf("\n반납할 도서가 없습니다.\n");
		}
	}
	
	private void listRentHistFormat(List<Rent> listRent) {
		LibUtil.rentHistIndex();
		for (Rent rent : listRent) {
			User user = userDao.getUser(rent.getUserID());
			LibBook libBook = bookDao.getLibBook(rent.getLibBookID());
			Book book = bookDao.getBook(libBook.getBookID());
			
			System.out.print("│    " + convert(rent.getRentNum() + "", 4) + "    │  ");
			System.out.print(convertLeft(StringUtil.userNameLength(user), 13) + "   │ ");
			System.out.print(convertLeft(StringUtil.bookTitleLength(book), 24) + " │ ");
			System.out.print(convertLeft(StringUtil.bookAuthorLength(book), 20) + " │ ");
			System.out.print(convertLeft(StringUtil.bookPublisherLength(book), 20) + " │ ");
			System.out.print(convertLeft(LibUtil.dateFormat(rent.getDateRent()), 10) + " │ ");
			System.out.print(convertLeft(checkDateReturn(rent), 10) + " │   ");
			System.out.println(convert(libBook.isRent() ? "대여" : "보유", 4) + "   │");
			System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		}
	}
	
	private void listRentFormat(List<Book> listBook) {
		int cnt = 1;
		
		LibUtil.rentIndex();
		for (Book book : listBook) {
			System.out.printf("│ %2d │ ", cnt++);
			System.out.print(convertLeft(StringUtil.bookTitleLength(book), 24) + " │ ");
			System.out.print(convertLeft(StringUtil.bookAuthorLength(book), 20) + " │ ");
			System.out.print(convertLeft(StringUtil.bookPublisherLength(book), 20) + " │  ");
			System.out.println(convert(book.getAmount() + "", 2) + "권  │");
			System.out.printf("────────────────────────────────────────────────────────────────────────────────────────%n");
		}
	}
	
	private void bookFormat(Book book) {
		LibUtil.bookIndex();
		System.out.print("│     " + convert(book.getId() + "", 3) + "    │ ");
		System.out.print(convertLeft(StringUtil.bookTitleLength(book), 24) + " │ ");
		System.out.print(convertLeft(StringUtil.bookAuthorLength(book), 20) + " │ ");
		System.out.print(convertLeft(StringUtil.bookPublisherLength(book), 20) + " │ ");
		System.out.print(convertLeft(book.getIsbn(), 10) + " │  ");
		System.out.print(convert(book.getAmount() + "", 2) + "권  │");
		System.out.printf("%n────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
	}
	
	private void returnBookFormat(Rent rent, int cnt) throws ParseException {
		LibBook libBook = bookDao.getLibBook(rent.getLibBookID());
		Book book = bookDao.getBook(libBook.getBookID());
		
		System.out.printf("│ %2d │   ", cnt);
		System.out.print(convert(rent.getRentNum() + "", 4) + "   │ ");
		System.out.print(convertLeft(StringUtil.bookTitleLength(book), 24) + " │ ");
		System.out.print(convertLeft(StringUtil.bookAuthorLength(book), 20) + " │ ");
		System.out.print(convertLeft(StringUtil.bookPublisherLength(book), 20) + " │ ");
		System.out.print(convertLeft(LibUtil.dateFormat(rent.getDateRent()), 10) + " │    ");
		System.out.println(convertLeft(calcOverdue(rent), 4) + "    │");
		System.out.printf("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
	}
	
	private String checkDateReturn(Rent rent) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return rent.getDateReturn() > 0 ? dateFormat.format(rent.getDateReturn()) : "";
	}

	private String calcOverdue(Rent rent) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long returnTime = dateFormat.parse(dateFormat.format(rent.getDateRent())).getTime() + 1000 * 60 * 60 * 24 * 7;

		if (System.currentTimeMillis() > returnTime) {
			return "연체";
		} else {
			return "";
		}
	}
	
}
