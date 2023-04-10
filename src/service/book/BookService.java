package service.book;

import java.util.List;

import domain.Book;
import domain.LibBook;

public interface BookService {
	
	List<Book> getBooks();
	
	List<LibBook> getLibBooks();
	
	// 재고 조회 : 보유 도서 조회
	void findBook();
	
	// 도서 검색 : 제목, 저자, 출판사
	List<Book> searchBook();
	
	// 소장도서 데이터 생성
	void regBook();
	
	// 소장도서 데이터 수정
	void modifyBook();
	
	// 소장도서 데이터 삭제
	void removeBook();
	
	// 도서 검색 값 리스트 양식 적용(일반 계정용)
	void searchList(List<Book> bookShelf);
	
}
