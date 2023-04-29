package app;

import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.text.ParseException;

import domain.User;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.rent.RentService;
import service.rent.RentServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

public class LibEx {
	
	public static void main(String[] args) throws ParseException {
		
		UserService userService = new UserServiceImpl();
		BookService bookService = new BookServiceImpl();
		RentService rentService = new RentServiceImpl();
		
		while ( true ) {
			libraryIndex();
			
			int userID = nextInt("ID를 입력해주세요. > ");
			String password =  nextLine("password를 입력해주세요. > ");
			
			User user = userService.login(userID, password);
			
			if( user != null ) {
				// 관리자 계정
				if ( user.isAdmin() ) {
					
					while ( true ) {
						try {
							int index = nextInt("1.도서관리 2.계정관리 3.대여이력조회 4.로그아웃 > ", 1, 4);
							
							switch (index) {
							// 도서관리
							case 1: System.out.printf("%n도서관리입니다.%n");
								
								index = nextInt("1.재고조회 2.도서대여 3.도서반납 4.도서검색 5.신규도서등록 6.도서정보수정 7.도서정보삭제 8.처음으로 > ", 0, 8);
								
								switch (index) {
								// 도서 목록 조회
								case 1: bookService.findBook(); break;
								
								// 도서 대여
								case 2: rentService.rentBook(); break;
			
								// 도서 반납
								case 3: rentService.returnBook(); break;
			
								// 사서용 도서 검색
								case 4: bookService.searchBook(); break;
			
								// 도서 정보 등록
								case 5: bookService.regBook(); break;
			
								// 도서 정보 수정
								case 6: bookService.modifyBook(); break;
			
								// 도서 정보 삭제
								case 7: bookService.removeBook(); break;
			
								case 8: break;
								
								} continue;
								
							// 계정관리	
							case 2: System.out.printf("%n계정관리입니다.%n");
	
								index = nextInt("1.계정목록 2.계정등록 3.계정정보수정 4.계정삭제 5.블랙리스트관리 6.관리자등록 7.처음으로 > ", 1, 7);
								
								switch (index) {
								// 계정 목록
								case 1: userService.listUser(); break;
			
								// 계정 정보 등록
								case 2: userService.regUser(); break;
			
								// 계정 정보 수정
								case 3: userService.modifyUser(); break;
			
								// 계정 정보 삭제
								case 4: userService.removeUser(); break;
									
								// 블랙리스트 등록, 해제
								case 5: userService.updateBlackList(); break;
	
								// 관리자 등록, 해제
								case 6: userService.updateAdmin(user); break;
									
								case 7: break;
								
								} continue;
			
							// 대여 이력 조회
							case 3: rentService.listRent(user); continue;
			
							// 로그아웃
							case 4: System.out.println("로그아웃이 성공적으로 이루어졌습니다."); break;
							
							} break;
						
						} catch (NumberFormatException e) {
							System.out.println("잘못 입력하셨습니다.");
						} catch (RuntimeException e) {
							System.out.println(e.getMessage());
						} 
					}
				}
				// 일반 계정
				else {
					while ( true ) {
						try {
							int index = nextInt("1.도서검색 2.도서대여 3.도서반납 4.도서대여이력 5.로그아웃 > ", 0, 5);
							
							switch (index) {
							// 도서 검색
							case 1: bookService.searchBook(); continue;
								
							// 도서 대여
							case 2: rentService.rentBook(); continue;
								
							// 도서 반납
							case 3: rentService.returnBook(); continue;
								
							// 대여 이력 조회
							case 4: rentService.listRent(user); continue;
								
							case 5: System.out.println("로그아웃이 성공적으로 이루어졌습니다."); break;
							
							} break;
							
						} catch (NumberFormatException e) {
							System.out.println("잘못 입력하셨습니다.");
						} catch (RuntimeException e) {
							System.out.println(e.getMessage());
						} 
					}
				}
			}
		}
	}
	
	private static void libraryIndex() {
		System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		System.out.println("        :::::::::::        :::         ::::::::    :::     :::   ::::::::::        :::::::::      ::::::::      ::::::::      :::    ::: ");
		System.out.println("           :+:            :+:        :+:    :+:   :+:     :+:   :+:               :+:    :+:    :+:    :+:    :+:    :+:     :+:   :+:   ");
		System.out.println("          +:+            +:+        +:+    +:+   +:+     +:+   +:+               +:+    +:+    +:+    +:+    +:+    +:+     +:+  +:+     ");
		System.out.println("         +#+            +#+        +#+    +:+   +#+     +:+   +#++:++#          +#++:++#+     +#+    +:+    +#+    +:+     +#++:++       ");
		System.out.println("        +#+            +#+        +#+    +#+    +#+   +#+    +#+               +#+    +#+    +#+    +#+    +#+    +#+     +#+  +#+       ");
		System.out.println("       #+#            #+#        #+#    #+#     #+#+#+#     #+#               #+#    #+#    #+#    #+#    #+#    #+#     #+#   #+#       ");
		System.out.println("  ###########        ##########  ########        ###       ##########        #########      ########      ########      ###    ###       ");
		System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}
}
