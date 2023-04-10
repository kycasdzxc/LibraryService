package service.user;

public interface UserService {

	// 계정 데이터 목록 조회
	void listUser();
	
	// 계정 데이터 생성
	void regUser();
	
	// 계정 데이터 수정
	void modifyUser();
	
	// 계정 데이터 삭제
	void removeUser();
	
	// 관리자권한 부여, 회수
	void updateAdmin(int id);
	
	// 블랙리스트 등록, 해제
	void updateBlackList(int id);
	
	// 로그인 : 일반 계정, 관리자 계정 분리
	int login(int id, String pw);
	
}
