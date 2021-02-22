package ch16JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BoardUpdateDAO {

	static Scanner scanner = new Scanner(System.in); //입력 받는 객체 생성 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//필요한 객체 선언 업데이트를 할 때는 2가지 객체 + 글 보기 기능 추가 1 객체 추가 
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//접속정보
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "java00";
		String pw = "java00";
		
		//수정을 위한 데이터 가져오기 객체 선언 : BoardVO
		BoardVO vo = null;
		//수정 결과를 저장하는 변수 (insert-int)
		int result = 0;
		
		try {
			//수정 할 글 번호 받기 -no
			System.out.print("수정 할 글 번호를 입력 -> ");
			String data = scanner.nextLine();
			long no = Long.parseLong(data);
			System.out.println("수정 할 글 번호 출력 :" + no);
			
			//수정 할 글 번호에 해당되는 데이터 가져오기 -BoardVO : 1~7  ->view : BoardViewDAO try{}전체  / 조회수 1증가 부분은 삭제함 
			Class.forName(driver);
			System.out.println("데이터 가져오기 드라이버 확인");
			//2.연결
			con = DriverManager.getConnection(url, id, pw);
			 System.out.println("데이터 가져오기 - 연결완료"); 
			//3.쿼리작성
			String sql = "SELECT no,title,content,writer,writeDate,hit "
					+ " FROM board "
					+ " WHERE no=?"; // ? : 값을 대체해야 하는 부분의 대체 문자  
			System.out.println("데이터 가져오기-쿼리작성");
			//4.실행객체 & 데이터 셋팅 
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, no);
			System.out.println("데이터 가져오기 - 실행 객체 생성 & 데이터 셋팅 ");
			//5.실행 select -> executeQuery() / 그 외 -> executeUpdate() 결과 int 
			rs=pstmt.executeQuery();
			System.out.println("데이터 가져오기 - 실행 완료 ");
			//6.출력 --데이터가 한 개 나오거나 또는 안나옴 
			if(rs !=null && rs.next()) { // 데이터를 가져온 경우
				vo = new BoardVO(); //저장할 객체 생성
				vo.setNo(rs.getLong("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setWriter(rs.getString("writer"));
				vo.setWriteDate(rs.getString("writeDate"));
				vo.setHit(rs.getLong("hit"));
				
			}
			System.out.println("+----------------------------------+");
			System.out.println("번호 : " + vo.getNo());
			System.out.println("제목 : " + vo.getTitle());
			System.out.println("내용: " + vo.getContent());
			System.out.println("작성자 : " + vo.getWriter());
			System.out.println("작성일: " + vo.getWriteDate());
			System.out.println("조회수 : " + vo.getHit());
			System.out.println("+----------------------------------+");
			System.out.println("데이터 가져오기 부분 출력 완료 ");
			
			//7.닫기 
			if(con!=null)con.close();
			if(pstmt!=null)pstmt.close();
			if(rs!=null)rs.close();
			//****수정할 데이터 받아오기 끝****
			
			//가져 온 데이터를 수정하기 -vo.set -> BoardWriteDAO -> 데이터 확인 수정 무한루프 while(true)
			while(true) {
				//데이터 확인
				System.out.println(vo);
				//수정할 항목 선택
				//메뉴 출력
				System.out.println("1.제목 2.내용 3.작성자 0.수정완료");
				System.out.println("수정할 항목을 입력-> ");
				//메뉴선택
				String menu = scanner.nextLine();
				if(menu.equals("1")) {
					System.out.print("제목 -> ");
					vo.setTitle(scanner.nextLine());
				}else if (menu.equals("2")) {
					System.out.print("내용-> ");
					vo.setContent(scanner.nextLine());
				}else if (menu.equals("3")) {
					System.out.print("작성자-> ");
					vo.setWriter(scanner.nextLine());
				}else if (menu.equals("0")) {
					break;
				}else {
					System.out.println("잘못된 항목 선택.0~3번 입력-> " );
				}
		} // end of while (true) 데이터 수정 입력 루프 
			System.out.println("데이터 수정 입력 완료(DB수정 전) -vo :" +vo);
			
			//수정 한 데이터 DB에 적용하기 -insert 1~7 : insert,update,delete->비슷한 류의 프로그램 ->복붙 후 약간의 수정 : BoardwriteDAO
			//1.드라이버 확인 -이미 했음 
			//Class.forName(driver); // 메인메모리에 로딩됨 
			System.out.println("드라이버 확인 완료 ");
			//2.연결
			con = DriverManager.getConnection(url, id, pw);
			System.out.println("DB수정-연결객체 생성완료");
			//3.쿼리 작성
			sql = "UPDATE board SET title= ?,content=?,writer=? WHERE no=? ";
			System.out.println("DB수정 - sql :" + sql);
			//4.실행 객체 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getWriter());
			pstmt.setLong(4, vo.getNo());
			System.out.println("DB수정 -실행 객체와 데이터 셋팅 완료 ");
		
			//5.실행 
			result = pstmt.executeUpdate();
			System.out.println("DB수정- 실행완료 ");
			//6.출력
			if(result > 0) System.out.println("게시판의 글"+vo.getNo() +"번이 수정되었습니다.");
			System.out.println("DB수정- 수정완료");
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try { //7.닫기 
				if(con != null) con.close();
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	} //end of main 

}
