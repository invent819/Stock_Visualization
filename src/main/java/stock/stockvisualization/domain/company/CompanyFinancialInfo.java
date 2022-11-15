package stock.stockvisualization.domain.company;

import lombok.Data;

/*
*
* https://opendart.fss.or.kr/guide/detail.do?apiGrpCd=DS003&apiId=2019016
result
status	에러 및 정보 코드	(※메시지 설명 참조)
message	에러 및 정보 메시지	(※메시지 설명 참조)
list
rcept_no	접수번호	접수번호(14자리)

※ 공시뷰어 연결에 이용예시
- PC용 : https://dart.fss.or.kr/dsaf001/main.do?rcpNo=접수번호
bsns_year	사업 연도	2019
stock_code	종목 코드	상장회사의 종목코드(6자리)
reprt_code	보고서 코드	1분기보고서 : 11013 반기보고서 : 11012 3분기보고서 : 11014 사업보고서 : 11011
account_nm	계정명	ex) 자본총계
fs_div	개별/연결구분	CFS:연결재무제표, OFS:재무제표
fs_nm	개별/연결명	ex) 연결재무제표 또는 재무제표 출력
sj_div	재무제표구분	BS:재무상태표, IS:손익계산서
sj_nm	재무제표명	ex) 재무상태표 또는 손익계산서 출력
thstrm_nm	당기명	ex) 제 13 기 3분기말
thstrm_dt	당기일자	ex) 2018.09.30 현재
thstrm_amount	당기금액	9,999,999,999
thstrm_add_amount	당기누적금액	9,999,999,999
frmtrm_nm	전기명	ex) 제 12 기말
frmtrm_dt	전기일자	ex) 2017.01.01 ~ 2017.12.31
frmtrm_amount	전기금액	9,999,999,999
frmtrm_add_amount	전기누적금액	9,999,999,999
bfefrmtrm_nm	전전기명	ex) 제 11 기말(※ 사업보고서의 경우에만 출력)
bfefrmtrm_dt	전전기일자	ex) 2016.12.31 현재(※ 사업보고서의 경우에만 출력)
bfefrmtrm_amount	전전기금액	9,999,999,999(※ 사업보고서의 경우에만 출력)
ord	계정과목 정렬순서	계정과목 정렬순서
currency	통화 단위	통화 단위
*
* */
@Data
public class CompanyFinancialInfo {
    Long companyFinancialInfoId;
    String stock_name;
    String corp_code;
    String reprt_code; // 보고서 코드	1분기보고서 : 11013 반기보고서 : 11012 3분기보고서 : 11014 사업보고서 : 11011
    int bsns_year;
    String account_nm; // 계정명	ex) 자본총계
    String fs_div; // 개별/연결구분
    String sj_nm; // 재무제표명	ex) 재무상태표 또는 손익계산서 출력
    Long thstrm_amount; // 당기금액
    Long thstrm_add_amount; // 당기누적금액	9,999,999,999
    Long frmtrm_amount; // 전기금액
    Long frmtrm_add_amount; // 전기누적금액	9,999,999,999
    String induty_code; // 산업코드
    public CompanyFinancialInfo(){

    }
    public CompanyFinancialInfo(String stock_name, String corp_code){
        this.stock_name = stock_name;
        this.corp_code = corp_code;
    }
}
