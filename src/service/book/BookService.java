package service.book;

import java.util.List;

import domain.Book;

public interface BookService {
	
	// 소장도서 데이터 생성
	void regBook();

	// 도서 검색 : 제목, 저자, 출판사
	List<Book> searchBook(boolean flag);

	// 재고 조회 : 보유 도서 조회
	void findBook();
		
	// 소장도서 데이터 수정
	void modifyBook();
	
	// 소장도서 데이터 삭제
	void removeBook();
	
}
