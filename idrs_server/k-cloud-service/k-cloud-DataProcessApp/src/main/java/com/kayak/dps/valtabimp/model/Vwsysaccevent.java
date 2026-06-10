package com.kayak.dps.valtabimp.model;

public class Vwsysaccevent {

	private int id;
	private String event_name;
	private int event_level;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public int getEvent_level() {
		return event_level;
	}
	public void setEvent_level(int event_level) {
		this.event_level = event_level;
	}
	public Vwsysaccevent(int id, String event_name, int event_level) {
		super();
		this.id = id;
		this.event_name = event_name;
		this.event_level = event_level;
	}
	
	public Vwsysaccevent(){}
}
