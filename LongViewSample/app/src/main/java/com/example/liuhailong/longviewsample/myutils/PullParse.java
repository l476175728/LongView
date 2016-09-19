package com.example.liuhailong.longviewsample.myutils;


import java.io.FileInputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

import com.example.liuhailong.longviewsample.bean.Cities;
import com.example.liuhailong.longviewsample.bean.Countries;
import com.example.liuhailong.longviewsample.bean.Provinces;

public class PullParse {
	
	private ArrayList<Provinces> province;
	private Provinces pro;
	private ArrayList<Cities> city;
	private Cities cit;
	private ArrayList<Countries> country;
	private Countries cou;

	
	public  ArrayList<Provinces> getCountey(XmlResourceParser xrp) throws Exception{
		
		
		//XmlPullParserFactory xpf=XmlPullParserFactory.newInstance();
		//XmlPullParser xpp=xpf.newPullParser();
		//FileInputStream fis=new FileInputStream("citys_weather.xml");
		int temp=xrp.getEventType();
		while(temp!=XmlPullParser.END_DOCUMENT){
			
			if(temp==XmlPullParser.START_DOCUMENT){
				province=new ArrayList<Provinces>();
				
			}
			else if(temp==XmlPullParser.START_TAG){
				
				String tag=xrp.getName();
				if("p".equals(tag)){
					pro=new Provinces();
					pro.setId(xrp.getAttributeValue(0));
				}
				if("pn".equals(tag)){
					pro.setName(xrp.nextText());
					city=new ArrayList<Cities>();
					
				}
				if("c".equals(tag)){
					cit=new Cities();
					cit.setId(xrp.getAttributeValue(0));
				}
				if("cn".equals(tag)){
					cit.setName(xrp.nextText());
					country=new ArrayList<Countries>();
					
				}
				if("d".equals(tag)){
					cou=new Countries();
					cou.setId(xrp.getAttributeValue(0));
					cou.setName(xrp.nextText());
					country.add(cou);
				}
			}
			else if(temp==XmlPullParser.END_TAG){
				if("c".equals(xrp.getName())){
					cit.setCities(country);
					city.add(cit);
				}
				if("p".equals(xrp.getName())){
					pro.setProvinces(city);
					province.add(pro);
					//System.out.println(province);
				}
			}
			temp=xrp.next();
		}

	return province;
	}
	
	
}
