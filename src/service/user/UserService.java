package service.user;

import java.util.List;

import domain.User;

public interface UserService {

	List<User> getUsers();
	
	// 계정 데이터 목록 조회
	void userList();
	
	// 계정 데이터 생성
	void regUser();
	
	// 계정 데이터 수정
	void modifyUser();
	
	// 계정 데이터 삭제
	void removeUser();
	
	// 관리자권한 부여, 회수
	void admin(int userID);
	
	// 블랙리스트 등록, 해제
	void blackList(int userID);
	
	// 로그인 : 일반 계정, 관리자 계정 분리
	int logIn(int userID, String userPW);
	
	// 로그아웃
	void logOut();
	
}
