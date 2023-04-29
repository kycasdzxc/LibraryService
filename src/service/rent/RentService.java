package service.rent;

import java.text.ParseException;

import domain.User;

public interface RentService {
	
	// 도서 대여·반납 기록 조회
	void listRent(User user);

	// 소장도서 대여
	void rentBook(User user);
	
	// 소장도서 반납
	void returnBook(User user) throws ParseException;
	
}
