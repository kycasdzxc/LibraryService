package domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 도서관에서 보유하고 있는 도서 데이터. '소장번호'로 관리
 */
@Getter @Setter
@NoArgsConstructor @ToString
public class LibBook implements Serializable { // 실제 도서관에 있는 책
	
	private static final long serialVersionUID = 2436242113453823347L;
	
	// 변수
	private int id;         // 소장번호 : LibBook 클래스의 기본키
	private boolean rent;   // 대여여부(false: 대여가능, true: 대여중)
	private int bookID;     // Book 클래스의 도서번호
		
	// 생성자
	public LibBook(int id, int bookID) { // 데이터 생성 시, rent의 기본값은 false(대여가능)
		this.id = id;
		this.bookID = bookID;
	}
	
}