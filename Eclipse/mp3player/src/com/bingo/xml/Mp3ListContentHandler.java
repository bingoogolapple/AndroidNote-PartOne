package com.bingo.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bingo.bean.Mp3Info;

public class Mp3ListContentHandler extends DefaultHandler {
	private List<Mp3Info> list = null;
	private Mp3Info mp3Info = null;
	private String tagName = null;
	
	public Mp3ListContentHandler(List<Mp3Info> list) {
		super();
		this.list = list;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if(tagName.equals("id")) {
			mp3Info.setId(temp);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("resource")) {
			list.add(mp3Info);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if(localName.equals("resource")) {
			mp3Info = new Mp3Info();
		}
		if(qName.equals("mp3")) {
			for(int i = 0; i < attributes.getLength(); i++) {
				if(attributes.getLocalName(i).equals("name")) {
					mp3Info.setMp3Name(attributes.getValue(i));
				} else if(attributes.getLocalName(i).equals("size")) {
					mp3Info.setMp3Size(attributes.getValue(i));
				}
			}
		}
	}
	
}
