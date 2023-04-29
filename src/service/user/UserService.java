package service.user;

import domain.User;

public interface UserService {

	// 계정 데이터 생성
	void regUser();
	
	// 계정 데이터 목록 조회
	void listUser();
	
	// 계정 데이터 수정
	void modifyUser();
	
	// 계정 데이터 삭제
	void removeUser();
	
	// 관리자권한 부여, 회수
	void updateAdmin(User user);
	
	// 블랙리스트 등록, 해제
	void updateBlackList();
	
	// 로그인
	User login(int id, String pw);
	
}
