package com.yuhang.vediostream.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {

	/**
	 * 字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNull(String s) {
		if (s == null || s.equals("") || s.equals("null") || s.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 去除末尾的逗号
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteEndDouhao(String str) {
		if (isNull(str)) {
			return str;
		}
		if (str.endsWith(",")) {
			return str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * 把byte字节数组解析成hex字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	/**
     * 生产单点批次号
     * n ： 需要的长度
     * @return
     */
    public static String getDanDianBatno( int n )
    {
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return "DANDIAN"+new SimpleDateFormat("yyyyMMdd").format(new Date())+val.toUpperCase();
    }
    
    /**
     * 生产单点批次号
     * n ： 需要的长度
     * @return
     */
    public static String getDuoDianBatno( int n )
    {
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return "DUODIAN"+new SimpleDateFormat("yyyyMMdd").format(new Date())+val.toUpperCase();
    }

    /**
     *   在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压
     * @param h
     * @param w
     * @return
     */
    public static int getPercent(float h,float w)
	{
		int p=0;
		float p2=0.0f;
		if(h>w)
		{
			p2=297/h*100;
		}
		else
		{
			p2=210/w*100;
		}
		p=Math.round(p2);
		return p;
	}
    
    /**
     * 第二种解决方案，统一按照宽度压缩
	 * 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
     * @param h
     * @param w
     * @return
     */
    public static int getPercent2(float h,float w)
	{
		int p=0;
		float p2=0.0f;
		p2=530/w*100;
		p=Math.round(p2);
		return p;
	}

    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
