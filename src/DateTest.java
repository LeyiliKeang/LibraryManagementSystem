
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) throws ParseException {
        //String转换为Date
        String today = "2018-8-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(today);
        System.out.println(today);
        System.out.println(date);


//        boolean after(Object when) 判断此 Calendar 表示的时间是否在指定 Object 表示的时间之后，返回判断结果。
//        boolean before(Object when) 判断此 Calendar 表示的时间是否在指定 Object 表示的时间之前，返回判断结果。

        //设置日期为三月后的当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3);
        Date date1 = calendar.getTime();
        System.out.println(sdf.format(date1));


        //计算两个日期之间的天数
        System.out.println("----------------");
        String day1 = "2018-6-09";
        String day2 = "2018-4-30";
        Date d1 = sdf.parse(day1);
        Date d2 = sdf.parse(day2);
        System.out.println(d1);
        System.out.println(d2);
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        long time1 = c.getTimeInMillis();
        c.setTime(d2);
//        long time2 = c.getTimeInMillis();
        long time2 = System.currentTimeMillis();
        long betweenDays = (time1-time2)/(1000*60*60*24);
        System.out.println(betweenDays);
        System.out.println(String.valueOf(betweenDays));

        System.out.println(betweenDays*0.02f);
    }
}
