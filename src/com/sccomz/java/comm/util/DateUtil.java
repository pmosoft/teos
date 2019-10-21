package com.sccomz.java.comm.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateUtil {


	public static void main(String[] arg) {
		System.out.println("★★★★★★★★★★★★★★★[key] " + getDate("yyyyMMdd"));
	}

    /*
     * yyyyMMddHHmmss
     * */
	public static String getDate(String strFormat) {

		Calendar cal = Calendar.getInstance();
		Date currentTime = cal.getTime();

		//SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
		String ndate = formatter.format(currentTime);

		return ndate;
	}

	public static Date getCurrentDate() {
		try {
			TimeZone tz = new SimpleTimeZone( 9 * 60 * 60 * 1000, "KST" );
			TimeZone.setDefault(tz);
		} catch(Exception e) {}
		return new Date();
    }

	/**
     *
     * 현재 일자를 원하는 format 으로 변경하여 문자열로 리턴<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 5. 19.
     *
     * @param date_format 날짜 반환 유형
     *                                  yyyy - 년도
     *                                  MM - 월
     *                                  dd - 일
     *                                  HH - 시간
     *                                  mm - 분
     *                                  ss - 초
     * @return 문자열
     */
	public static String getTodayString(String date_format) {
		String dateStr = "";
		try {
			Date date=getCurrentDate();
			SimpleDateFormat sdf = new SimpleDateFormat(date_format, Locale.KOREAN);
			dateStr = sdf.format(date);
		} catch(Exception e) {}
		return dateStr;
	}

    /**
     *
     * 현재 일자를 YYYYMMDD 형식으로 리턴<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 11. 22.
     *
     * @param date_format 날짜 반환 유형
     *                                  yyyy - 년도
     *                                  MM - 월
     *                                  dd - 일
     *                                  HH - 시간
     *                                  mm - 분
     *                                  ss - 초
     * @return 문자열
     */
	public static String getTodayString2() {
		SimpleTimeZone kst = new SimpleTimeZone(0x1ee6280, "KST");
		Calendar cal = Calendar.getInstance(kst);
		String y = "" + cal.get(1);
		String m;
		if (cal.get(2) + 1 < 10)
			m = "0" + (cal.get(2) + 1);
		else
			m = "" + (cal.get(2) + 1);
		String d;
		if (cal.get(5) < 10)
			d = "0" + cal.get(5);
		else
			d = "" + cal.get(5);
		return y + m + d;
	}

    /**
     *
     * 입력한 일자를 원하는 format 으로 변경하여 문자열로 리턴<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 5. 19.
     *
     * @param date_format 날짜 반환 유형
     *                                  yyyy - 년도
     *                                  MM - 월
     *                                  dd - 일
     *                                  HH - 시간
     *                                  mm - 분
     *                                  ss - 초
     * @return 문자열
     */
	public static String getDateString(Date dt, String date_format) {
		String dateStr = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(date_format, Locale.KOREAN);
			dateStr = sdf.format(dt);
		} catch (Exception e) {}
        return dateStr;
	}

	public static String getDateString(Date dt) {
		String dateStr = "";
		try {
			dateStr = getDateString(dt, "yyyy-MM-dd");
		} catch (Exception e) {}
		return dateStr;
	}

	public static String getDateString(Object dt) {
		String dateStr = "";
		try {
			dateStr = getDateString((java.util.Date)dt);
		} catch (Exception e) {}
		return dateStr;
	}

	public static String getDateTimeString(Date dt) {
		String dateStr = "";
		try {
			dateStr = getDateString(dt, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {}
		return dateStr;
	}

	public static String getDateTimeString(Object dt) {
		String dateStr = "";
		try {
			dateStr = getDateTimeString((java.util.Date)dt);
		} catch (Exception e) {}
		return dateStr;
	}

   /**
    *
    *
    * JDBC용 특정 시점의 DATE를 리턴한다.<br>
    *
    * @author sung hyun jung
    * @version 1.0
    * @createdate 2001 6. 2.
    * @modifydate 2001 6. 2.
    *
    * @param dt 변환하고자 하는 날짜.
    * @return yyyy-MM-dd hh:mm:ss 형태의 날짜시간만 리턴
    */
	public static java.sql.Timestamp getSqlDateTime(java.util.Date dt) {
		java.util.Date d = null;
		if (dt == null) {
			d = new java.util.Date();
		} else {
			d = dt;
		}
		return new java.sql.Timestamp(d.getTime());
	}

    /**
     *
     *
     * JDBC용 특정 시점의 DATE를 리턴한다.!<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @createdate 2001 6. 2.
     * @modifydate 2001 6. 2.
     *
     * @param dt
     * @return yyyy-mm-dd 포멧의 날짜만 리턴.
     */
	public static java.sql.Date getSqlDate(java.util.Date dt) {
		java.util.Date d = null;
		if (dt == null) {
			d = new java.util.Date();
		} else {
			d = dt;
        }
		return new java.sql.Date(d.getTime());
	}

    /**
     *
     *
     * JDBC용 특정 시점의 시간을 리턴한다.!<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @createdate 2001 6. 2.
     * @modifydate 2001 6. 2.
     *
     * @param dt
     * @return hh:mm:ss 포멧의 시간만 리턴.
     */
	public static java.sql.Time getSqlTime(java.util.Date dt) {
		java.util.Date d = null;
		if (dt == null) {
			d = new java.util.Date();
		} else {
			d = dt;
		}
		return new java.sql.Time(d.getTime());
    }

    /**
     *
     *
     * 입력받은 String오브젝트를 특정한 포멧 형식의 Date 형으로 만들어 리턴 <br>
     * 예) stringToDate("2001-06-01", "yyyy-'-'MM'-'dd")<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 6. 7.
     *
     * @param d
     * @param format
     * @return
     */
	public static String stringToDateString(String d, String format) {
		java.util.Date ch = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			ch = sdf.parse(d);
		} catch (Exception dfdf) {}
		return getDateString(ch, format);
	}

    /**
     *
     *
     * 입력받은 String오브젝트를 특정한 포멧 형식의 Date 형으로 만들어 리턴 <br>
     * 예) stringToDate("2001-06-01", "yyyy-'-'MM'-'dd")<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 6. 7.
     *
     * @param d
     * @param format
     * @return
     */
	public static String stringToDateString(String d, String oldformat, String newformat) {
		java.util.Date ch = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(oldformat);
			ch = sdf.parse(d);
        } catch (Exception dfdf) {}
        return getDateString(ch, newformat);
	}

    /**
     *
     * 주어진 날짜 문자열을 java.util.Date 형으로 반환한다.<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 6. 10.
     *
     * @param d
     * @return
     */
	public static java.util.Date getDateUtil(String d) {
		java.util.Date ch = null;
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.KOREA);
			ch = df.parse(d);
        } catch (Exception dfdf) { }
        return ch;
    }


    /**
     *
     * 주어진 문자열을 java.util.Date 형으로 반환한다.<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 7. 2.
     *
     * @param d
     * @param format
     * @return
     */
	public static java.util.Date getDate (String d, String format) {
		java.util.Date ch = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREAN);
			ch = sdf.parse(d);
		} catch(Exception dfdf) { }
		return ch;
    }

    /**
     *
     * 오늘 날짜가 두 날짜 사이에 존재하는지 확인한다.<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 6. 10.
     *
     * @param first
     * @param second
     * @return
     */
	public static boolean betweenDate(String first, String second, String format) {
		boolean flag = false;
        java.util.Date start = null;
        java.util.Date end = null;
        java.util.Date current = null;

        try {
        	start = getDate(first, format);
            end = getDate(second, format);
            current = getDate(getTodayString(format), format);
        } catch (Exception pe) {
        	return false;
        }
        if ((start.before(current) && end.after(current)) || start.equals(current) || end.equals(current))
        	flag= true;
        return flag;
	}

    /**
     *
     * 주어진 날짜에 일자를 더한 날짜를 구한다.<br>
     *
     * @author sung hyun jung
     * @version 1.0
     * @modifydate 2001 11. 26.
     *
     * @param date
     * @param amount
     * @return
     */
	public static Date add (Date date, int amount) {
		Calendar c = Calendar.getInstance (Locale.KOREAN);
		c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c.getTime();
	}

     /**
      * 해당 주의 일요일부터 토요일까지의 날짜를 리턴
      *
      * @param year
      * @param month
      * @param date
      * @return
      */
	public static String[] getWeek(int year, int month, int date) {
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar g = new GregorianCalendar(year, month-1 , date);
		g.add(Calendar.HOUR,1);
		int dayOfWeek =  g.get(Calendar.DAY_OF_WEEK);
		String[] week = new String[7];
		// dayOfWeek : sunday = 1, Saturday = 7;
		week[0] = getFullDate(g, 1-dayOfWeek, format);
        week[1] = getFullDate(g, 1, format);
        week[2] = getFullDate(g, 1, format);
        week[3] = getFullDate(g, 1, format);
        week[4] = getFullDate(g, 1, format);
        week[5] = getFullDate(g, 1, format);
        week[6] = getFullDate(g, 1, format);

        return week;
	}


	private static String getFullDate(Calendar g, int i, Format format) {
		g.add(Calendar.DATE, i);
		return format.format(g.getTime());
    }

    /**
     * 날짜문자열을 날짜표시타입으로 변환한다. <BR>
     * (예) 19981210 --> 1998-12-10  delimeter(-)        <BR>
     *     19981210 --> 1998/12/10  delimeter(/)        <BR>
     *     19981210 --> 1998.12.10  delimeter(.)        <BR>
     * @param    nowDate String 날짜문자열 구분작 존재하지 않는 숫자로만 구성된 날짜 (yyyymmdd)
     *          delimeter String 년,월,일을 구분하기 위한 구분자. ('/','-','.' 등등)
     * @return   변경된 날짜 문자열.(구분자가 첨가된 날짜 형태) (yyyy-mm-dd)
     */
	public static String StringToDate(String nowDate, String delimeter) {
		String temp = null;
		String fac_no = null;

		if (nowDate.length() == 0 || nowDate == null)
			return "";
		if (nowDate.length() != 8)
			return "invalid length";

		temp = nowDate.substring(0, 4);
		fac_no = temp + delimeter ;
		temp = nowDate.substring(4, 6);
		fac_no = fac_no + temp + delimeter;
		temp = nowDate.substring(6, 8);
		fac_no = fac_no + temp;

		return fac_no;
	}

     /**
      * 두 날짜의 차이를 일수로 구하기
      *
      * @param nYear1
      * @param nMonth1
      * @param date
      * @param nYear2
      * @param nMonth2
      * @param nDate2
      * @return
      */
	public static  int getDifferenceOfDate ( int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2) {
		Calendar cal = Calendar.getInstance ( );
		int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;

		if (nYear1 > nYear2) {
			for (int i = nYear2; i < nYear1; i++) {
				cal.set(i,12,0);
				nDiffOfYear += cal.get(Calendar.DAY_OF_YEAR);
			}
			nTotalDate1 += nDiffOfYear;
		} else if (nYear1 < nYear2) {
			for (int i = nYear1; i < nYear2; i++) {
				cal.set(i,12,0);
				nDiffOfYear += cal.get(Calendar.DAY_OF_YEAR);
			}
			nTotalDate2 += nDiffOfYear;
		}
		cal.set(nYear1,nMonth1-1,nDate1);
		nDiffOfDay = cal.get(Calendar.DAY_OF_YEAR);
		nTotalDate1 += nDiffOfDay;

		cal.set(nYear2,nMonth2-1,nDate2);
		nDiffOfDay = cal.get(Calendar.DAY_OF_YEAR);
		nTotalDate2 += nDiffOfDay;

		return nTotalDate1-nTotalDate2;
	}

     /**
      * 날짜로 요일구하기
      *
      * @param year
      * @param month
      * @param date
      * @return
      */
	public static String getWeekNm(int year,int month,int day) {
		//제라의 공식
		//그레고리력(1582년 10월 15일) 이후에만 적용된다
		int a = year / 100;
		int b = year - a * 100;
		int c = month;
		int d = day;

		if (c < 3)
			c+=12;
		if (c > 12)
			b--;
		if (b < 0) {
			b += 100;
			a--;
		}
		//연월일만으로 요일을 계산하는 제라의 공식이 있다.
		//서기 a백 b년 c월 d일의 요일을 알아보자.(단, 1월과 2월은 전년의 13월과 14월로 생각한다.)
		//1. 먼저, w=[21a/4]+[5b/4]+[26(c+1)/10]+d-1 을 계산한다. (여기서 [x]는 가우스의 기호로써 x 이하의 정수 가운데 최대인 정수를 말한다.)
		//2. 다음에 w를 7로 나눈 나머지를 구하고, 그 나머지가 0, 1, 2, 3, 4, 5, 6에 따라서 요일을 일, 월, 화, 수, 목, 금, 토로 한다.
		int W = (21 * a / 4) + (5 * b / 4) + (26 * (c + 1) / 10) + d - 1;
		W %= 7;

		String[] week = {"일","월","화","수","목","금","토"};
		return week[W];
	}


	/**
	 * from ~ to 기간동안의 특정요일을 반환한다.
	 * @param from
	 * @param to
	 * @param week
	 * @return
	 */
	public static String[] getDayOfWeek(String from, String to, String[] week) {
		int term = getDaysBetween(from, to, "yyyyMMdd");
		int year, month, day;
		String strWeek = StringUtil.stringJoin(week, ",");
		ArrayList<String> list = new ArrayList<String>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
			Date date = formatter.parse(from);
			Timestamp time = null;
			for(int i=0; i<=term; i++) {
				time = addRelativeDate(date, i, Calendar.DAY_OF_MONTH);
				formatter.applyPattern("yyyy");
				year = Integer.parseInt(formatter.format(time));
				formatter.applyPattern("MM");
				month = Integer.parseInt(formatter.format(time));
				formatter.applyPattern("dd");
				day = Integer.parseInt(formatter.format(time));
				if(0 <= strWeek.indexOf( String.valueOf(getDayOFWeek(year, month, day)) )) {
					formatter.applyPattern("yyyyMMdd");
					list.add(formatter.format(time));
				}
			}
		} catch(Exception e) {}

		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 요일에 대한 int을 리턴한다.
	 * 0=일요일,1=월요일,2=화요일,3=수요일,4=목요일,5=금요일,6=토요일
	 *
	 * @param int year
	 * @param int month
	 * @param int day
	 *
	 * @return int
	 */
	public static int getDayOFWeek(int year,int month,int day)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1,day);
		int dayofWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return dayofWeek;
	}

	/**
	 * return days between two date strings with user defined format.
	 *
	 * @param String from date string
	 * @param String to date string
	 *
	 * @return int 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일자 리턴
	 *         -999: 형식이 잘못 되었거나 존재하지 않는 날짜 또는 기간의 역전
	 *         argument형식 : "01/15/2002","01/17/2002","MM/dd/yyyy"
	 *                        "20020115","20020117","yyyyMMdd"
	 */
	public static int getDaysBetween(String from, String to, String format)
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date d1 = null;
		java.util.Date d2 = null;
		try {
			d1 = formatter.parse(from);
			d2 = formatter.parse(to);
		} catch(java.text.ParseException e) {
			return -999;
		}
		if ( !formatter.format(d1).equals(from) ) return -999;
		if ( !formatter.format(d2).equals(to) ) return -999;

		long duration = d2.getTime() - d1.getTime();

		if ( duration < 0 ) return -999;
		return (int)( duration/(1000 * 60 * 60 * 24) );
	}

	/**
	 * date형의 해당년월일을 받아 감소하고자 하는 년 또는 월, 일을 n만큼 감소한다
	 *
	 * @param java.util.Date date
	 * @param int n
	 * @param int mode
	 *
	 * @return Timestamp
	 */
	public static Timestamp relativeDate(Date date, int n, int mode) {
		int year, mon, day = 0;

		Calendar beforeCal = Calendar.getInstance();
		Calendar afterCal = Calendar.getInstance();

		beforeCal.setTime( date );

		year = beforeCal.get( Calendar.YEAR );
		mon  = beforeCal.get( Calendar.MONTH );
		day  = beforeCal.get( Calendar.DAY_OF_MONTH );

		if ( mode == Calendar.YEAR ) {
			afterCal.set( year - n, mon, day, 0, 0, 0 );
		} else if ( mode == Calendar.MONTH ) {
			afterCal.set( year, mon - n, day, 0, 0, 0 );
		} else {
			afterCal.set( year, mon, day - n, 0, 0, 0 );
		}

		return new Timestamp (afterCal.getTime().getTime() );
	}

	/**
	 * date형의 해당년월일을 받아 증가하고자 하는 년 또는 월, 일을 n만큼 증가한다
	 *
	 * @param java.util.Date date
	 * @param int n
	 * @param int mode
	 *
	 * @return Timestamp
	 */
	public static Timestamp addRelativeDate(Date date, int n, int mode) {
		int year, mon, day = 0;

		Calendar beforeCal = Calendar.getInstance();
		Calendar afterCal = Calendar.getInstance();

		beforeCal.setTime( date );

		year = beforeCal.get( Calendar.YEAR );
		mon  = beforeCal.get( Calendar.MONTH );
		day  = beforeCal.get( Calendar.DAY_OF_MONTH );

		if ( mode == Calendar.YEAR ) {
			afterCal.set( year + n, mon, day, 0, 0, 0 );
		} else if ( mode == Calendar.MONTH ) {
			afterCal.set( year, mon + n, day, 0, 0, 0 );
		} else {
			afterCal.set( year, mon, day + n, 0, 0, 0 );
		}

		return new Timestamp (afterCal.getTime().getTime() );
	}




	/**
	 * 해당년월일의 마지막 날짜를 구한다.
	 *
	 * @param int year
	 * @param int month
	 *
	 * @return int
	 */
	public static int getLastDateOfMonth(int year,int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1, 1);
		int lastDateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDateOfMonth;
	}

}
