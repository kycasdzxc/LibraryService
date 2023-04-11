package util;

import static util.LibUtil.getKorCnt;

import domain.Book;
import domain.LibBook;
import domain.User;

public class StringUtil {
	
	/**
	 * 도서정보의 책제목을 축약하는 메서드. 책제목이 20글자가 넘어가면, 19번째 글자 이후는 ... 으로 출력된다.(반각문자 기준)
	 * @param book : 책제목을 축약할 도서의 정보(Book 타입)
	 * @return 반각문자(영문자, 숫자)는 최대 19글자, 전각문자(한글)는 최대 9글자까지 출력이 가능하다.
	 * 		   20글자가 넘어가면 ... 으로 출력된다.
	 */
	public static String bookTitleLength(Book book) {
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
	public static String bookAuthorLength(Book book) {
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
	public static String bookPublisherLength(Book book) {
		int ko = getKorCnt(book.getPublisher());
		if (book.getPublisher().length() > 15 - ko) {
			return book.getPublisher().substring(0, 15 - ko) + "...";
		} else {
			return book.getPublisher();
		}
	}
	
	/**
	 * 소장도서의 대여여부를 문자열로 반환하는 메서드. 대여여부가 true면 "대여", false면 "보유"를 반환한다.
	 * @param libBook : 대여여부를 문자열로 반환하려는 소장도서정보(LibBook 타입)
	 * @return libBook.isRent()가 true면 "대여", false면 "보유"를 문자열로 반환
	 */
	public static String checkRentState(LibBook libBook) {
		return libBook.isRent() ? "대여" : "보유";
	}
	
	/**
	 * 계정정보의 이름을 축약하는 메서드. 이름이 6글자가 넘어가면, 5번째 글자 이후는 ... 으로 출력된다.
	 * @param user : 이름을 축약할 계정의 정보(User 타입)
	 * @return 반각문자, 전각문자 상관없이 최대 5글자까지 출력이 가능하다.
	 * 6글자가 넘어가면 ... 으로 출력된다.
	 */
	public static String userNameLength(User user) {
		if (user.getName().length() > 5) {
			return user.getName().substring(0, 5) + "...";
		} else {
			return user.getName();
		}
	}

	/**
	 * 생년월일의 길이를 확인하는 메서드. 생년월일은 6자리로 제한한다.
	 * @param birth : 길이를 확인할 생년월일
	 * @return 조건이 true면 생년월일 반환, false면 문구 출력
	 */
	public static String birthLength(String birth) {
		if (birth.length() == 6) {
			return birth;
		} else {
			throw new RuntimeException("6자리로 입력해주세요.[ex)921024]");
		}
	}

	/**
	 * 전화번호의 길이를 확인하는 메서드. 전화번호 입력은 11자리로 제한한다.
	 * @param birth : 길이를 확인할 생년월일
	 * @return 조건이 true면 생년월일 반환, false면 문구 출력
	 */
	public static String pnLength(String pn) {
		if (pn.length() == 11) {
			return pn.substring(0, 3) + "-" + pn.substring(3, 7) + "-" + pn.substring(7, 11);
		} else {
			throw new RuntimeException("11자리로 입력해주세요.[ex)01086940273]");
		}
	}
}
