package ticketrank;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.MovieDTO;
import persistence.MovieDAO;

public class DaumTicket {
		
		String base = "";
		int page = 1;
		String url = "";
		int count = 0; // 전체 댓글 수
		int total = 0; // 총 평점
		MovieDAO mDao = new MovieDAO();
		int score = 0;
		int regdate = 0;	
		
		public TicketDTO daumCrawler(String movie, String code) throws IOException{ // throws 예외처리를 던지는 것
			// 예외처리: 예외가 발생할수도 있는 상황에서 예외가 발생했을 때 어떻게 처리 할 것인지 개발자가 설정
			
			base="https://movie.daum.net/moviedb/grade?movieId="+code+"&type=netizen&page=";
														//movieId가 계속 바뀌어야하니까 code로 바꿔주기
			url= base+page;
			System.out.println("<<<<<<DAUM START>>>>>");
			while(true){
				// 1페이지으 ㅣ평점 10건 수집
				Document doc = Jsoup.connect(url).get();		
				Elements reply = doc.select("ul.list_review div.review_info");
								// 댓글전체를  싸고 있는 ul, 하나의 댓글을 싸고 있는 div.review_info
				// 마지막페이지면 수집 종료
				if(reply.isEmpty()) {
				break;
			}
			// 영화제목 수집 하는거 삭제
				
			String writer, content, basedate, subdate = "";
			
			for (Element one : reply) {
				count++;
				writer = one.select("em.link_profile").text();
				score = Integer.parseInt(one.select("em.emph_grade").text());
				content = one.select("p.desc_review").text();
				basedate = one.select("span.info_append").text();
				
				subdate= basedate.substring(0, 10);
				regdate = Integer.parseInt(subdate.replace(".","")); 			
				// 누적 평점 계산 : total = total + score;
				total += score; 		
				MovieDTO mDto = new MovieDTO(movie, content, writer, score, "daum", regdate);				
				//DB에 저장
				mDao.addMovie(mDto);
				System.out.println("■■"+count+"건" );
				System.out.println("영화 :"+ movie);
				System.out.println("평점 : "+ score);
				System.out.println("작성자 :"+ writer);
				System.out.println("내용 : "+ content);
				System.out.println("작성일자 : "+ regdate);
						
			} // 한 페이지가 끝나는 구간
			// 다음페이지로 이동하기 위해 page +1 증가
			page = page + 1;
			// 다음 페이지로 이동할 URL 작성
			url = base + page;			
		}
			System.out.println("<<<<<DAUM END>>>>>");
			TicketDTO tDto = new TicketDTO(count,total);
			return tDto;
		}	
	}

