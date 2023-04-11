package service.rent;

import java.text.ParseException;

public interface RentService {
	
	// 소장도서 대여
	void rentBook(int userID);
	
	// 소장도서 반납
	void returnBook(int userID)	throws ParseException;
	
	// 도서 대여·반납 기록 조회
	void rentList(int userID);
	
}
