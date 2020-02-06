package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.MovieDTO;
import persistence.MovieDAO;

public class NaverMovie2 {

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
		int count = 0; 	// 전체 댓글 수
	
		double scoreAvg = 0.0; // 평균 평점
		String compare = ""; // 마지막 페이지 종료를 위한 변수
		MovieDAO mDao = new MovieDAO();
		
		label:while(true) {
		
			Document doc = Jsoup.connect(url).get();
			Elements reply = doc.select("div.score_result ul li");
			
			if(reply.isEmpty()) {
				break;
			}
	
			String writer, content, basedate, subdate = "";

			for (int i = 0; i < reply.size(); i++) {
				
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
			
				
				
	
				//출력
				System.out.println("영화제목: 하이큐!! 땅vs하늘");
				System.out.println("▶▶ "+count+"건");
				System.out.println("평점"+ score);
				System.out.println("작성자"+writer);
				System.out.println("내용"+content);
				System.out.println("작성일자"+regdate);
				
			
			
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
