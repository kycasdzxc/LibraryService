package service.rent;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.getKorCnt;
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
import util.LibUtil;
import util.StringUtil;

public class RentServiceImpl implements RentService {
	
	private UserDao userDao = UserDao.getInstance();
	private BookDao bookDao = BookDao.getInstance();
	private RentDao rentDao = RentDao.getInstance();
	
	List<LibBook> libBooks = bookDao.listLibBook();
	List<Book> books = bookDao.listBook();
	List<Rent> rents = rentDao.listRent();
	List<User> users = userDao.listUser();
	
	/**
	 * 대여 가능한 소장도서번호를 찾는 메서드. 대여 가능한 도서가 없을 경우, null을 반환한다.
	 * @param rBook : 대여하려는 도서의 도서정보(Book 타입)
	 * @return 대여 가능한 소장도서번호(LibBook 클래스의 id) 반환
	 */
	private int matchLibBook(Book rBook) {
		LibBook libBook = null;
		for (LibBook l : libBooks) {
			if (l.getBookID() == rBook.getId() && l.isRent() == false) {
				libBook = l;
				libBook.setRent(true);
				break;
			}
		}
		return libBook.getId();
	}

	/**
	 * 해당 도서를 대여상태로 바꿔주는 메서드.
	 * matchLibBook()에서 대여가능 도서를 조회하여
	 * 대여 가능한 도서가 있으면 해당 소장도서의 대여여부(LibBook 클래스의 rent)를 true로 변경한다.
	 * 해당 도서의 재고(Book 클래스의 amount)도 1 감소시킨다.
	 * @param rBook : 대여하려는 도서의 도서정보(Book 타입)
	 * @param userID : 도서를 대여하는 계정의 ID(User 클래스의 id)
	 */
	private void rent(Book rBook, int userID) {
		rents.add(new Rent(rents.get(rents.size() - 1).getRentNum(), userID, matchLibBook(rBook)));
		rBook.setAmount(rBook.getAmount() - 1);
		System.out.println("대여완료");
	}


	/**
	 * 대여 중인 도서를 반납하면 대여상태를 '보유'로 바꿔주는 메서드.
	 * 반납이 완료되면 해당 도서의 재고(Book 클래스의 amount)도 1 증가시킨다.
	 * @param returnBook : 반납할 도서의 대여정보(Rent 타입)
	 */
	private void changeReturnState(Rent returnBook) {
		for (LibBook l : libBooks) {
			if (l.getId() == returnBook.getLibBookID()) {
				l.setRent(false);
				findBookByLibBookID(l.getId()).setAmount(findBookByLibBookID(l.getId()).getAmount() + 1);
			}
		}
	}

	/**
	 * 1/1000초(ms) 단위를 날짜형태로 변환해주는 메서드. 'yyyy-MM-dd'로 출력한다.
	 * @param time : 날짜형태로 변환할 시간(ms : 1/1000초 단위)
	 * @return 'yyyy-MM-dd' 양식의 문자열로 반환
	 */
	private String dateFormat(long time) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(time);
	}
	
	/**
	 * 반납일을 문자열로 반환하는 메서드. 반납되지 않은 도서는 공백으로 반환한다.
	 * @param rent : 반납일을 문자열로 반환할 대여정보(Rent 타입)
	 * @return 반납일이 기록된 도서는 반납일을, 반납되지 않은 도서는 공백으로 반환
	 */
	private String checkDateReturn(Rent rent) {
		if (rent.getDateReturn() == 0) {
			return "";
		} else {
			return dateFormat(rent.getDateReturn());
		}
	}
	
	/**
	 * 연체일을 계산하는 메서드. 대여일로부터 7일이 지나면 문자열 "연체"를 반환한다.
	 * @param rent : 연체일을 계산할 대여정보(Rent 타입)
	 * @return 대여일로부터 7일이 지나면 문자열 "연체" 반환
	 * @throws ParseException 문자열을 ms 단위로 변환할 때 발생하는 예외상황
	 */
	private String findOverdueByRent(Rent rent) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long returnTime = dateFormat.parse(dateFormat.format(rent.getDateRent())).getTime() + 1000 * 60 * 60 * 24 * 7;

		if (System.currentTimeMillis() > returnTime) {
			return "연체";
		} else {
			return "";
		}
	}

	/**
	 * 대여하고자 하는 도서를 검색하고, 검색한 도서를 대여하는 메서드.
	 * @param userID : 도서를 대여하려는 계정의 ID(User 클래스의 id)
	 */
	public void rentBook(int userID) { // 대여 기능
		if (findUserByID(userID) != null) {
			System.out.println("대여할 도서를 검색해주세요.");
//			List<Book> sBooks = bookService.searchBook();
			List<Book> sBooks = null;
			
			if (sBooks.size() > 0) {
				LibUtil.rentIndex();
				int cnt = 1;
				for (Book b : sBooks) {
					System.out.printf("│ %2d │ ", cnt++);
					System.out.print(convertLeft(bookTitleLength(b), 24) + " │ ");
					System.out.print(convertLeft(bookAuthorLength(b), 20) + " │ ");
					System.out.print(convertLeft(bookPublisherLength(b), 20) + " │  ");
					System.out.println(convert(b.getAmount() + "", 2) + "권  │");
					System.out.printf("────────────────────────────────────────────────────────────────────────────────────────%n");
				}
				int rentNum = nextInt("대여할 도서의 번호를 입력해주세요. > ") - 1;

				if (!(rentNum < 0 || rentNum > sBooks.size())) {

					Book rBook = sBooks.get(rentNum);
					LibUtil.bookIndex();
					System.out.print("│     " + convert(rBook.getId() + "", 3) + "    │ ");
					System.out.print(convertLeft(bookTitleLength(rBook), 24) + " │ ");
					System.out.print(convertLeft(bookAuthorLength(rBook), 20) + " │ ");
					System.out.print(convertLeft(bookPublisherLength(rBook), 20) + " │ ");
					System.out.print(convertLeft(rBook.getIsbn(), 10) + " │  ");
					System.out.println(convert(rBook.getAmount() + "", 2) + "권  │");
					System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

					if (nextInt("대여하시겠습니까?[1.네 2.아니오] > ") == 1) {
						// 대여할 도서가 있는 경우
						if (rBook.getAmount() > 0) {
							rent(rBook, userID);
						// 모든 도서가 대여 중일 경우
						} else {
							System.out.println("현재 모든 책이 대여 중입니다.");
						}
					}
				} else {
					System.out.println("잘못 입력하셨습니다.");
				}
			}
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}

	/**
	 * 대여한 도서목록을 확인하고 대여한 도서를 반납하는 메서드.
	 * @param userID : 도서를 반납하려는 계정의 ID(User 클래스의 id)
	 */
	public void returnBook(int userID) throws ParseException { // 반납 기능 >> userID 입력 필요
		System.out.println("도서반납입니다.");
		List<Rent> rBooks = new ArrayList<Rent>();
		int cnt = 1;
		LibUtil.rentListIndex();
		for (Rent r : rents) {
			if (r.getUserID() == userID /* logIn 한 userID */ && findLibBookByID(r.getLibBookID()).isRent() == true) {
				System.out.printf("│ %2d │   ", cnt++);
				System.out.print(convert(r.getRentNum() + "", 4) + "   │ ");
				System.out.print(convertLeft(bookTitleLength(findBookByLibBookID(r.getLibBookID())), 24) + " │ ");
				System.out.print(convertLeft(bookAuthorLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
				System.out.print(convertLeft(bookPublisherLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
				System.out.print(convertLeft(dateFormat(r.getDateRent()), 10) + " │    ");
				System.out.println(convertLeft(findOverdueByRent(r), 4) + "    │");
				System.out.printf("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
				rBooks.add(r);
			}
		}
		if (rBooks.size() > 0) {
			int returnNum = nextInt("반납할 도서의 번호를 입력해주세요. > ") - 1;
			if (!(returnNum < 0 || returnNum > rBooks.size())) {
				changeReturnState(rBooks.get(returnNum));
				rBooks.get(returnNum).setDateReturn(System.currentTimeMillis());
				System.out.println("반납완료");
			} else {
				System.out.println("잘못 입력하셨습니다.");
			}
		} else {
			System.out.println();
			System.out.println("반납할 도서가 없습니다.");
		}
	}

	/**
	 * 도서대여이력을 출력하는 메서드. 대여여부 및 대여일, 반납일을 확인할 수 있다.
	 * 일반 계정은 자신의 도서대여이력만 볼 수 있고, 관리자 계정은 전체 도서대여이력을 볼 수 있다.
	 * @param userID : 대여이력을 확인하고자 하는 계정의 ID(User 클래스의 id)
	 */
	public void rentList(int userID) {
		System.out.println("도서대여이력입니다.");
		LibUtil.listIndex();
		if (findUserByID(userID).isAdmin() == true) {
			for (Rent r : rents) {
				System.out.print("│    " + convert(r.getRentNum() + "", 4) + "    │  ");
				System.out.print(convertLeft(userNameLength(findUserByID(r.getUserID())), 13) + "   │ ");
				System.out.print(convertLeft(bookTitleLength(findBookByLibBookID(r.getLibBookID())), 24) + " │ ");
				System.out.print(convertLeft(bookAuthorLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
				System.out.print(convertLeft(bookPublisherLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
				System.out.print(convertLeft(dateFormat(r.getDateRent()), 10) + " │ ");
				System.out.print(convertLeft(checkDateReturn(r), 10) + " │   ");
				System.out.println(convert(StringUtil.checkRentState(findLibBookByID(r.getLibBookID())), 4) + "   │");
				System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
			}
		} else {
			for (Rent r : rents) {
				if (r.getUserID() == userID /* logIn 한 userID */) {
					System.out.print("│    " + convert(r.getRentNum() + "", 4) + "    │  ");
					System.out.print(convertLeft(userNameLength(findUserByID(userID)), 13) + "   │ ");
					System.out.print(convertLeft(bookTitleLength(findBookByLibBookID(r.getLibBookID())), 24) + " │ ");
					System.out.print(convertLeft(bookAuthorLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
					System.out
							.print(convertLeft(bookPublisherLength(findBookByLibBookID(r.getLibBookID())), 20) + " │ ");
					System.out.print(convertLeft(dateFormat(r.getDateRent()), 10) + " │ ");
					System.out.print(convertLeft(checkDateReturn(r), 10) + " │   ");
					System.out.println(convert(StringUtil.checkRentState(findLibBookByID(r.getLibBookID())), 4) + "   │");
					System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
				}
			}
		}
	}

	/**
	 * 소장도서 번호가 일치하는 소장도서 정보를 찾는 메서드. 일치하는 값이 없을 경우, null을 반환한다.
	 * @param libBookID : 소장도서 번호(LibBook 클래스의 id)
	 * @return libBook : 소장도서 번호가 일치하는 소장도서 정보를 LibBook 타입으로 반환
	 */
	private LibBook findLibBookByID(int libBookID) {
		LibBook libBook = null;
		for (LibBook lB : libBooks) {
			if (lB.getId() == libBookID) {
				libBook = lB;
				break;
			}
		}
		return libBook;
	}
	
	/**
	 * 소장도서 번호가 일치하는 도서정보를 찾는 메서드. 일치하는 값이 없을 경우, null을 반환한다.
	 * @param libBookID : 소장도서 번호(LibBook 클래스의 id)
	 * @return book : 소장도서 번호가 일치하는 도서정보를 Book 타입으로 반환
	 */
	private Book findBookByLibBookID(int libBookID) { // LibBookID로 Book 탐색
		Book book = null;
		for (Book b : books) {
			if (b.getId() == findLibBookByID(libBookID).getBookID()) {
				book = b;
			}
		}
		return book;
	}
	
	/** @문자열_관련_메서드 */
	
	/**
	 * 도서정보의 책제목을 축약하는 메서드. 책제목이 20글자가 넘어가면, 19번째 글자 이후는 ... 으로 출력된다.(반각문자 기준)
	 * @param book : 책제목을 축약할 도서의 정보(Book 타입)
	 * @return 반각문자(영문자, 숫자)는 최대 19글자, 전각문자(한글)는 최대 9글자까지 출력이 가능하다.
	 * 		   20글자가 넘어가면 ... 으로 출력된다.
	 */
	private String bookTitleLength(Book book) {
		int ko = getKorCnt(book.getTitle());
		if (book.getTitle().length() > 19 - ko) {
			return book.getTitle().substring(0, 19 - ko) + "...";
		} else {
			return book.getTitle();
		}
	}

	/**
	 * 도서정보의 저자명을 축약하는 메서드. 저자명이 16글자가 넘어가면, 15번째 글자 이후는 ... 으로 출력된다.(반각문자 기준)
	 * @param book : 저자명을 축약할 도서의 정보(Book 타입)
	 * @return 반각문자(영문자, 숫자)는 최대 15글자, 전각문자(한글)는 최대 7글자까지 출력이 가능하다.
	 * 		   16글자가 넘어가면 ... 으로 출력된다.
	 */
	private String bookAuthorLength(Book book) {
		int ko = getKorCnt(book.getAuthor());
		if (book.getAuthor().length() > 15 - ko) {
			return book.getAuthor().substring(0, 15 - ko) + "...";
		} else {
			return book.getAuthor();
		}
	}

	/**
	 * 도서정보의 출판사명을 축약하는 메서드. 출판사명이 16글자가 넘어가면, 15번째 글자 이후는 ... 으로 출력된다.(반각문자 기준)
	 * @param book : 출판사명을 축약할 도서의 정보(Book 타입)
	 * @return 반각문자(영문자, 숫자)는 최대 15글자, 전각문자(한글)는 최대 7글자까지 출력이 가능하다.
	 * 		   16글자가 넘어가면 ... 으로 출력된다.
	 */
	private String bookPublisherLength(Book book) {
		int ko = getKorCnt(book.getPublisher());
		if (book.getPublisher().length() > 15 - ko) {
			return book.getPublisher().substring(0, 15 - ko) + "...";
		} else {
			return book.getPublisher();
		}
	}
	
	/**
	 * 계정정보의 이름을 축약하는 메서드. 이름이 6글자가 넘어가면, 5번째 글자 이후는 ... 으로 출력된다.
	 * @param user : 이름을 축약할 계정의 정보(User 타입)
	 * @return 반각문자, 전각문자 상관없이 최대 5글자까지 출력이 가능하다.
	 * 6글자가 넘어가면 ... 으로 출력된다.
	 */
	private String userNameLength(User user) {
		if (user.getName().length() > 5) {
			return user.getName().substring(0, 5) + "...";
		} else {
			return user.getName();
		}
	}

	/**
	 * 계정 ID가 일치하는 계정정보를 찾는 메서드. 일치하는 값이 없을 경우, null을 반환한다.
	 * @param userID : 계정 ID(User 클래스의 id)
	 * @return user : 계정 ID가 일치하는 계정정보를 User 타입으로 반환
	 */
	private User findUserByID(int userID) { // UserID로 User 탐색
		User user = null;

		for (User u : users) {
			if (u.getId() == userID) {
				user = u;
			}
		}
		return user;
	}
}
