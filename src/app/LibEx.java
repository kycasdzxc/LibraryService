package app;

import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.text.ParseException;

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
		
		for (boolean bIndex = true; bIndex;) {
			libraryIndex();
			
			try {
				int loginID = nextInt("ID를 입력해주세요. > ");
				int login = userService.login(loginID, nextLine("password를 입력해주세요. > "));
				
				switch (login) {
				case 1:
					for (boolean bAdmin = true; bAdmin;) {
						try {
							switch (nextInt("1.도서관리 2.계정관리 3.대여이력조회 4.로그아웃 > ", 1, 4)) {
							case 1:
			
								System.out.printf("%n도서관리입니다.%n");
			
								switch (nextInt("1.재고조회 2.도서대여 3.도서반납 4.도서검색 5.신규도서등록 6.도서정보수정 7.도서정보삭제 8.처음으로 > ", 0, 8)) {
								
								case 1: // 도서 목록 조회
									bookService.findBook();
									break;
								
								case 2: // 도서 대여
									rentService.rentBook(nextInt("ID를 입력해주세요. > "));
									break;
			
								case 3: // 도서 반납
									rentService.returnBook(nextInt("ID를 입력해주세요. > "));
									break;
			
								case 4: // 사서용 도서 검색
									bookService.searchBook();
									break;
			
								case 5: // 도서 정보 등록
									bookService.regBook();
									break;
			
								case 6: // 도서 정보 수정
									bookService.modifyBook();
									break;
			
								case 7: // 도서 정보 삭제
									bookService.removeBook();
									break;
			
								case 8:
									break;
									
								}
								continue;
								
							case 2:
								System.out.printf("%n계정관리입니다.%n");
	
								switch (nextInt("1.계정목록 2.계정등록 3.계정정보수정 4.계정삭제 5.블랙리스트관리 6.관리자등록 7.처음으로 > ", 1, 7)) {
								case 1: // 계정 목록
									userService.listUser();
									break;
			
								case 2: // 계정 정보 등록
									userService.regUser();
									break;
			
								case 3: // 계정 정보 수정
									userService.modifyUser();
									break;
			
								case 4: // 계정 정보 삭제
									userService.removeUser();
									break;
			
								case 5:
									userService.updateBlackList(nextInt("ID를 입력해주세요. > "));
									break;
	
								case 6:
									int adminID = nextInt("ID를 입력해주세요. > ");
									
									if (loginID == adminID) {
										System.out.println("본인 계정의 권한 회수는 불가합니다.");
										break;
									}
									userService.updateAdmin(adminID);
									break;
									
									
								case 7:
									break;
								}
								continue;
			
							case 3: // 대여 이력 조회
								rentService.listRent(loginID);
								continue;
			
							case 4:
								System.out.println("로그아웃이 성공적으로 이루어졌습니다.");
								break;
							}
							break;
						
						} catch (NumberFormatException e) {
							System.out.println("잘못 입력하셨습니다.");
						} catch (RuntimeException e) {
							System.out.println(e.getMessage());
						} 
					}
					break;
					
				case 2:
					
					for (boolean bUser = true; bUser;) {
						try {
							switch (nextInt("1.도서검색 2.도서대여 3.도서반납 4.도서대여이력 5.로그아웃 > ", 0, 5)) {
							case 1: // 도서 검색
								bookService.searchBook();
								continue;
								
							case 2: // 도서 대여
								rentService.rentBook(loginID);
								continue;
								
							case 3: // 도서 반납
								rentService.returnBook(loginID);
								continue;
								
							case 4: // 대여 이력 조회
								rentService.listRent(loginID);
								continue;
								
							case 5:
								System.out.println("로그아웃이 성공적으로 이루어졌습니다.");
								break;
							}
						} catch (NumberFormatException e) {
							System.out.println("잘못 입력하셨습니다.");
						} catch (RuntimeException e) {
							System.out.println(e.getMessage());
						} 
						break;
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("잘못 입력하셨습니다.");
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
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
