package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NaverMovie {

	public static void main(String[] args) throws IOException{

		
		// 네이버 1917영화 댓글 수집 
		// Oracle DB에 저장
		// DAO, DTO  그대로 DAUM에서 사용하던 거 사용하세요
		// 1번째 : 1페이지를 가져오는 것 
		// 
		
		// 데이터를 수집할 사이트 
		String base = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code=191431&type=after&i"
				+ "sActualPointWriteExecute=false&isMileageSubscriptionAlready=false&isMileageSubscriptionReject=false&page=";
		
		// 전체 댓글 페이지 
		int page = 1;
		String url = base + page;
		String title = "";
		int regdate, score = 0;
		int count = 0;
		//영화제목 수집
		
		String writer, content, basedate, subdate , a = "";
		
		
		while(true) {
		
			Document doc = Jsoup.connect(url).get();
			Elements reply = doc.select("div.score_result ul li");
			
			if(reply.isEmpty()) {
				break;
			}
	
			
//			Elements movieName = doc.select("h3.h_movie");
//			title = movieName.text();
//			
			
			for (Element one : reply) {
				
				count++;
				writer = one.select("div.score_reple dl dt em span").text();
				score = Integer.parseInt(one.select("div.star_score em").text());
				content = one.select("div.score_reple p span").text();
				basedate = one.select("div.score_reple dl dt em:last-child").text();
				subdate= basedate.substring(0, 10);
				regdate = Integer.parseInt(subdate.replace(".","")); 
				
				
				
				System.out.println("영화제목: 하이큐!! 땅vs하늘");
				System.out.println("▶▶ "+count+"건");
				System.out.println("평점"+ score);
				System.out.println("작성자"+writer);
				System.out.println("내용"+content);
				System.out.println("작성일자"+basedate);
				
			
			}
			page = page +1;
			url = base + page;		
		}
		// 수집 및 분석 결과 출력
		System.out.println("하이큐!! 땅vs하늘");
		System.out.println((page-1)+"페이지에서");
		System.out.println("수집한 평점은 총"+count+"건 입니다.");
		
		
		
	}

}
