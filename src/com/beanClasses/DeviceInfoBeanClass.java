package com.beanClasses;

public class DeviceInfoBeanClass 
{
	//  {"desc":"***DEVELOPMENT*** TransitChatter.com mobile client access API","ver":"0.2.0","time":1341636725,"motd":"Todays message","all_ann_types":["deal","event","info","wiki","alert","crime"]
	//,"min_app_ver_ios":"0.4.0","pref_app_ver_ios":"0.4.0","min_app_ver_andr":"0.4.0","pref_app_ver_andr":"0.4.0 "}
	private String desc;
	private String version;
	private String time;
	private String motd;
	private String all_ann_types[];
	private String min_app_ver_ios;
	private String pref_app_ver_ios;
	private String min_app_ver_andr;
	private String pref_app_ver_andr;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMotd() {
		return motd;
	}
	public void setMotd(String motd) {
		this.motd = motd;
	}
	public String[] getAll_ann_types() {
		return all_ann_types;
	}
	public void setAll_ann_types(String[] all_ann_types) {
		this.all_ann_types = all_ann_types;
	}
	public String getMin_app_ver_ios() {
		return min_app_ver_ios;
	}
	public void setMin_app_ver_ios(String min_app_ver_ios) {
		this.min_app_ver_ios = min_app_ver_ios;
	}
	public String getPref_app_ver_ios() {
		return pref_app_ver_ios;
	}
	public void setPref_app_ver_ios(String pref_app_ver_ios) {
		this.pref_app_ver_ios = pref_app_ver_ios;
	}
	public String getMin_app_ver_andr() {
		return min_app_ver_andr;
	}
	public void setMin_app_ver_andr(String min_app_ver_andr) {
		this.min_app_ver_andr = min_app_ver_andr;
	}
	public String getPref_app_ver_andr() {
		return pref_app_ver_andr;
	}
	public void setPref_app_ver_andr(String pref_app_ver_andr) {
		this.pref_app_ver_andr = pref_app_ver_andr;
	}
	
}
