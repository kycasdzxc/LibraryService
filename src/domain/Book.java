package domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  도서관에 등록되어 있는 도서 데이터. '도서번호'로 관리
 */
@Getter @Setter @NoArgsConstructor
public class Book implements Serializable {
	
	private static final long serialVersionUID = -4314888347740767558L;
	
	// 변수
	private int id; 			// 도서번호 : Book 클래스의 기본키
	private String title; 		// 책제목
	private String author; 		// 저자
	private String publisher; 	// 출판사
	private String isbn; 		// ISBN
	private int amount = 0; 	// 재고 : 도서 대여·반납 또는 도서 데이터 추가·삭제 시 수량 변경
	
	// 생성자
	public Book(int id, String title, String author, String publisher, String isbn) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.isbn = isbn;
	}
}