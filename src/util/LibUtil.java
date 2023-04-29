package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import exception.RangeException;

public class LibUtil {
	private static Scanner scanner = new Scanner(System.in);
	
	public static String nextLine(String input) {
		System.out.print(input);
		return scanner.nextLine();		
	}
	
	public static String nextLine(String input, boolean korean, boolean num) {
		System.out.print(input);
		String str = scanner.nextLine();
		if (korean) {
			for (int i = 0 ; i < str.length() ; i++) {
				if (!(str.charAt(i) < 'ㄱ' || str.charAt(i) > 'ㅣ')) {
					throw new RuntimeException("다시 한 번 확인해주세요.");
				}
			}
		}
		if (num) {
			for (int i = 0 ; i < str.length() ; i++) {
				if (str.charAt(i) < '0' || str.charAt(i) > '9') {
					throw new RuntimeException("숫자만 입력해주세요.");
				}
			}
		}
		return str;
	}
	
	public static int nextInt(String input) {
		return Integer.parseInt(nextLine(input));
	}
	
	public static int nextInt(String input, int start, int end) {
		int result = Integer.parseInt(nextLine(input));
		if (start > result || end < result)
			throw new RangeException(start, end);
		return result;
	}
	
	public static int getKorCnt(String kor) {
		int cnt = 0;
		for (int i = 0 ; i < kor.length() ; i++) {
			if (kor.charAt(i) >= '가' && kor.charAt(i) <= '힣') {
				cnt++;
			}
		} return cnt;
	}
	
	public static String convert(String word, int size) {
		String formatter = String.format("%%%ds", size - getKorCnt(word));
		return String.format(formatter, word);
	}
	
	public static String convertLeft(String word, int size) {
		String formatter = String.format("%%-%ds", size - getKorCnt(word));
		return String.format(formatter, word);
	}
	
	public static String dateFormat(long time) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(time);
	}
	
	/**
	 * 계정목록 Index 양식 메서드
	 */
	public static void userIndex() {
		System.out.printf("──────────────────────────────────────────────────────────────────%n");
		System.out.printf("│       이름       │   생년월일   │    전화번호   │     비고     │%n");
		System.out.printf("==================================================================%n");
	}
	
	/**
	 * 도서 재고 조회 Index 양식 메서드
	 */
	public static void bookIndex() {
		System.out.printf("────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		System.out.printf("│  도서번호  │          책제목          │         저자         │        출판사        │      ISBN     │  재고  │%n");
		System.out.printf("================================================================================================================%n");
	}
	
	/**
	 * 소장도서 대여여부 조회 Index 양식 메서드
	 */
	public static void libBookIndex() {
		System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		System.out.printf("│  도서번호  │  소장번호  │          책제목          │         저자         │        출판사        │ 대여여부 │%n");
		System.out.printf("===============================================================================================================%n");
	}
	
	/**
	 * 도서대여이력 Index 양식 메서드
	 */
	public static void rentHistIndex() {
		System.out.printf("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		System.out.printf("│  대여번호  │       이름       │          책제목          │         저자         │        출판사        │   대여일   │   반납일   │ 대여여부 │%n");
		System.out.printf("===============================================================================================================================================%n");
	}

	
	/**
	 * 대여할 도서목록 Index 양식 메서드
	 */
	public static void rentIndex() {
		System.out.printf("────────────────────────────────────────────────────────────────────────────────────────%n");
		System.out.printf("│    │          책제목          │         저자         │        출판사        │  재고  │%n");
		System.out.printf("========================================================================================%n");
	}

	/**
	 * 반납할 도서목록 Index 양식 메서드
	 */
	public static void returnIndex() {
		System.out.printf("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────%n");
		System.out.printf("│    │ 대여번호 │          책제목          │         저자         │        출판사        │   대여일   │  연체여부  │%n");
		System.out.printf("====================================================================================================================%n");
	}
}