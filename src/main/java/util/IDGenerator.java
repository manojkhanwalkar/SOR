package util;

import java.util.Calendar;

public class IDGenerator {

    public enum Type { Order, Execution , MarketData}


    public String getId(Type type)
    {
        StringBuilder builder = new StringBuilder();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String strYear = String.valueOf(year).substring(2);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        builder.append(strYear );
        String strDay = String.valueOf(day);
        for (int i=0;i<3-strDay.length();i++)
        {
            builder.append('0');
        }
        builder.append(strDay);

        builder.append(getNext());
        char c=' ';
        switch(type)
        {
            case Order :
                c= 'O';
                break;
            case Execution :
                c='E';
                break;

            case MarketData:
                c='M';

        }
        builder.append(c);
        return builder.toString();

    }


    char[] arr = {'a','a','a','a'};

    char max = 'z';

   private synchronized String getNext() {

       String s = new String(arr);
       for (int i=arr.length-1;i>=0;i--)
       {
           if (arr[i]!=max)
           {
               arr[i]=(char)(arr[i]+1);
               break;
           }
           else
           {
               arr[i]='a';
           }
       }

       return s;


    }


    public static void main(String[] args) throws Exception {

        IDGenerator generator = new IDGenerator();

        for (int i=0;i<1000;i++) {
            System.out.println(generator.getId(Type.Order));
            Thread.sleep(200);
        }

    }
}
