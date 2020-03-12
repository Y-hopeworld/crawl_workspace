/*
 * Program: Movie Crawler Ver1.2
 * Developer: 1Verse
 * Date: 2020.02.07
 * Summary: 영화 실시간 예매 1~10위(네이버,다음)까지를 대상으로
 * 			네이버와 다음에서 각각 댓글(평점)을 수집하고
 * 			댓글수와 평점 평균 통계량을 출력하는 프로그램
 * Tools : Java,Jsoup,Jdbc,Mybatis,Oracle,SqlDeveloper,SQL
 * 
 */

package ticketrank;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TicketMain {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		int selCode = 0; // 사용자가 선택한 영화(순위)
		int score = 0;
		
		//1~10위까지 영화 제목과 고유코드를 저장하는 배열
		// [i] : 영화 1~10위까지 뜻함
		// 배열 인덱스는 0~9로 , 순위 인덱스는 1부터 10까지, 사용자가 입력하는 값도 1~10까지
		
		// [i][0]=영화제목 [i][1]=네이버코드 [i][2]= 다음코드 
		String[][] movieArr = new String[10][3];
		
		// 현재시간 계산
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		
		
		Date date = new Date(); //현재시간 구하기
		String time = dateFormat.format(date); // 현재 시간 디자인: 위에 dateFomat 디자인으로 된 것으로 출력하게 만들어줌
		
		// 프로그램 시작부 출력
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■■■■ MovieCrawler Ver1.2");
		System.out.println("■■■■■ Developer : 1Verse");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■■■■ Movie Ticketing Ranking(CurrentTime:"+time+")");
		
		
		// 네이버, 다음 예매 1~10위 영화고유코드를 수집하는 URL
		String naverRank = "https://movie.naver.com/movie/running/current.nhn#"; // 네이버 예매 1~10위
		String daumRank = "http://ticket2.movie.daum.net/Movie/MovieRankList.aspx";  // 다음 예매 1~10위
		
		// 네이버 영화 실시간 예매 순위 1~10위까지 수집(전처리)
		Document naverDoc = Jsoup.connect(naverRank).get(); // 네이버 순위 페이지 전체를 긁어옴
		Elements naverList = naverDoc.select("dt.tit > a"); // 순위 99위까지 나옴 
		
		
//		int count = 0; // 순위 개수
//		for (Element element : naverList) { 10위까지 순위만 가져올거여서 for each 안씀
//			count++;
//			System.out.println(element);
//			System.out.println(count);
//		}
		
		//네이버 영화 예매순위 1~10위까지 추출(전처리)
		for (int i = 0; i < 10; i++) {
			// 네이버 영화 1~10위까지 데이터를 제목만 뽑아오기 → 
			// 제목만 뽑아서 배열movieArr에 담기
			movieArr[i][0] = naverList.get(i).text();
			
			// 영화의 각 고유 코드를 뽑아내기 : attr 뽑아낼거야 href에 있는 코드값만
			// href = /movie/bi/mi/basic.nhn?code=181925 ;
			String href = naverList.get(i).attr("href");
			
			
			//substring(start, end); substring(0,10) 0~9까지 슬라이스
			//substring(5); 시작값만 넣음, 그때는 시작값부터 끝까지를 지정하는것
			//indexOf() 내가 원하는 텍스트의 인덱스 번호를 리턴
			// → 처음부터 찾음, 텍스트에 =이 3개 포함 되어 있으면 첫번째 =의 인덱스 번호를 리턴함
			// String str = "동선=뚱선=똥선";
			// str.indexOf("=");  >> 2
			//lastIndexOf() 끝에서부터 검색
			// str.lastIndexOf("=");  >> 5			
			movieArr[i][1] = href.substring(href.lastIndexOf("=")+1);
											// lastIndexOf("=")+1 → = 뒤 코드값만 불러오기			
		}
		
		// 다음 영화 실시간 예매 순위 1~위까지 수집
		Document daumDoc = Jsoup.connect(daumRank).get();
		Elements daumList = daumDoc.select("strong.tit_join > a");
		
		
//		int count = 0;
//		for (Element element : daumList) {
//			count++;
//			System.out.println(element);
//		}
//		System.out.println(count);
		
		// 다음 영화 제목 고유코드 : 다음은 고유코드로 들어가지 못해서, 한번 클릭하고 나온 페이지에서 id값을 가져와야함
		for (int j = 0; j < 10; j++) {
			String url = daumList.get(j).attr("href");
			
			// 순위별 영화상세정보를 가져옴
			Document detailMovie = Jsoup.connect(url).get();
			// 영화 상세정보 내의 예매하기에서 id값을 가져오세요
			// href : 포털, url을 통해 어딘가로 이동시켜주는 것
			String href = detailMovie.select("div.wrap_pbtn > a").attr("href");
			movieArr[j][2] = href.substring(href.lastIndexOf("=")+1); // 다음영화코드				
			
		}
		
		
		
		// 개발 : 배열 출력(영화제목, 네이버코드,다음코드) 출력
//		for (int i = 0; i < movieArr.length; i++) {
//			
//			for (int j = 0; j < 3; j++) {
//				System.out.println(movieArr[i][j] +"\t");				
//			}
//			System.out.println();
//		}
				
		// 프로그램 영화랭킹(1~10)출력
		for(int i = 0; i < movieArr.length; i++) {
			System.out.println((i+1)+"위 \t"+movieArr[i][0]);
		}		
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ Please enter a movie rank");
		//사용자 입력값 유효성 체크
	while(true) {
		
	
		System.out.print("순위>>");
		selCode = sc.nextInt();
		
		if(selCode >= 1 && selCode <= 10 ) {
			break;
		}else {
			System.out.println("You mistyped it. Please");
			continue;
		}		
		// 실제 사용자가 입력한 영화 수집 시작 
		
		//네이버 수집		
		// 영화제목 네이버코드 다음 코드 >> 배열 0~9
		
			
	}	
	NaverTicket nTicket = new NaverTicket();
	TicketDTO nDto = nTicket.naverCrawler(movieArr[selCode-1][0],movieArr[selCode-1][1]);
	
	//-----------------------------------------------------------------------------------------------------
	// 다음 영화 예매 순위 수집
	DaumTicket dTicket = new DaumTicket();
	TicketDTO dDto = dTicket.daumCrawler(movieArr[selCode-1][0],movieArr[selCode-1][2]);
	
	//nDto → 네이버 댓글수, 평점의 합	
	//dDto → 다음댓글수,평점의 합
	
	
	// 프로그램 수집결과 요약 통계
	int nCnt = nDto.getCount(); // 네이버 댓글수
	int dCnt =  dDto.getCount(); // 다음 댓글수
	int totalCnt = nCnt + dCnt; // 네이버  + 다음 댓글 수
	
	int nSum = nDto.getTotal(); // 네이버 평점 합
	int dSum = dDto.getTotal(); // 다음 평점합
	int totalSum = nSum + dSum; // 네이버+다음 평점합
	
	DecimalFormat df = new DecimalFormat("0.0");
	double nAvg = (double)nSum/nCnt; // 네이버 평점 평균
	double dAvg = (double)nSum/nCnt; // 다음 평점 평균
	double totalAvg = (double)totalSum/totalCnt; // 네이버+다음 평점 평균
 	
	//프로그램 수집 결과 분석
	String report = ""; // 분석결과 저장
	if(totalAvg >= 8 && totalAvg <= 10) {
		report = "꼭 봐야하는 :-)";			
	}else if(totalAvg >=6 && totalAvg < 8 ) {
		report = "재미있는 :)";
	}else if(totalAvg >=4 && totalAvg < 6 ) {
		report = "팝콘 두 개 사서 봐야하는";
	}else if(totalAvg >=2 && totalAvg < 4 ) {
		report = "팝콘이 먹는 재미가 큰";
	}else {
		report = "도무지 1도 재미없는 ";
	}
	
	// 프로그램 수집결과 출력	
	System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
	System.out.println("■■ Collection completed:)");
	System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
	System.out.println("■■ Summary");
	System.out.println("■■ NAVER Count:" + nCnt);
	System.out.println("■■ NAVER Sum:" + nSum);
	System.out.println("■■ NAVER Avg:" + df.format(nAvg)); 
											// String 타입으로 변환
	System.out.println("■■ =========================================");
	System.out.println("■■ DAUM Count:" + dCnt);
	System.out.println("■■ DAUM Sum:" + dSum);
	System.out.println("■■ DAUM Avg:" + df.format(dAvg));
	System.out.println("■■ =========================================");
	System.out.println("■■ Total Reply Count : " + totalCnt);
	System.out.println("■■ Total Sum:" + totalSum);
	System.out.println("■■ Total Avg:" + df.format(totalAvg));
	System.out.println("■■ =========================================");
	System.out.println("■■ 2. Report");
	System.out.println("■■ "+movieArr[selCode-1][0]+"'수집 및 분석결과'");
	System.out.println("■■ 전체 댓글 수 "+ totalCnt+", 평균평점 "+ df.format(totalAvg)+"점,");
	System.out.println("■■ "+report +"'영화입니다.'");
	
	
	}
	
	
}