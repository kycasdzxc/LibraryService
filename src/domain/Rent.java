package domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  도서 대여·반납을 관리하는 데이터. '대여번호'로 관리
 */
@Getter @Setter
@NoArgsConstructor @ToString
public class Rent implements Serializable {
	
	private static final long serialVersionUID = 5129035402362307325L;

	// 변수
	private int rentNum;		// 대여번호 : Rent 클래스의 기본키
	
	private int userID;			// 도서를 대여한 계정의 ID
	private int libBookID;		// 대여된 도서의 소장번호

	// 기한 내에 반납처리가 되지 않은 책은 '연체'가 된다.
	private long dateRent = System.currentTimeMillis();	// 대여일
	private long dateReturn = 0;						// 반납일: 대여일 + 7일
	

	// 생성자
	public Rent(int rentNum, int userID, int libID) {
		this.rentNum = rentNum;
		this.userID = userID;
		this.libBookID = libID;
	}
	
}