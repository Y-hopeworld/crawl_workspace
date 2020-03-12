package ticketrank;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import domain.MovieDTO;
import persistence.MovieDAO;

public class NaverTicket {

	String base = "";
	int page = 1; // 수집한 페이지수
	String url = "";
	int count = 0; 	// 전체 댓글 수
	int total = 0; // 평점 모두 더하는 변수
	int score = 0;
	String compare = ""; // 마지막 페이지 종료를 위한 변수
	MovieDAO mDao = new MovieDAO();
	
	//네이버 영화 댓글(평점)수집하는 메서드
	public TicketDTO naverCrawler(String movie, String code) throws IOException{
		base = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code="+code+"&type=after&i"
				+ "sActualPointWriteExecute=false&isMileageSubscriptionAlready=false&isMileageSubscriptionReject=false&page=";
		url = base + page;
		System.out.println("■■NAVER START■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
		label:while(true) {
			
			Document doc = Jsoup.connect(url).get();
			Elements reply = doc.select("div.score_result ul li");
			
			if(reply.isEmpty()) {
				break;
			}	
			String writer, content, basedate, subdate = "";
			int regdate = 0;
			
			for (int i = 0; i < reply.size(); i++) {
				
				count++;
				score = Integer.parseInt(reply.get(i).select("div.star_score em").text());
								// reply:에서 get: 하나씩 꺼내와 
				content = reply.get(i).select("div.score_reple p span").text();
				writer = reply.get(i).select("div.score_reple dl dt em span").text();
				basedate = reply.get(i).select("div.score_reple dl dt em:last-child").text();
				// em이 3개인 경우:nth-child(위치 숫자값); odd-child,even-child
				
				subdate= basedate.substring(0, 10);
				regdate = Integer.parseInt(subdate.replace(".","")); 				
				//페이지 종료
				if(i==0) {
					if(compare.equals(writer)) {
						//compare와 writer의 내용이 같은지 확인 한 후 같으면 break;
						// break를하게 되면 위에 있는 for문만 빠져나가게 됌, 우리는 while을 빠져나가야함
						// 그래서 label을 정의해서 label로 정의 된 반복문을 빠져나감
						break label;
					}else {
						// compare와 writer 내용이 다르면 writer 값을 compare에 넣으세요.
						compare = writer;
					}
				}				
				MovieDTO mDto = new MovieDTO(movie, content, writer, score, "Naver", regdate);
				mDao.addMovie(mDto);
				//출력
				System.out.println("▶▶ "+count+"건");
				System.out.println("영화 :"+movie);
				System.out.println("평점"+ score);
				System.out.println("작성자"+writer);
				System.out.println("내용"+content);
				System.out.println("작성일자"+regdate);
				total = total + score ;
			}
			page = page +1;
			url = base + page;		
		}
		System.out.println("■■■■■■■■■■■■■■■NAVER END■■■■■■■■■■■■■■■");
		// TicketMain으로 댓글수 , 평점의 합을 return
		TicketDTO tDto = new TicketDTO(count,total);
		return tDto;
		
		
	}
	
}
