package com.ahdms.ap.model;

public class AuthCount {
    private String id;

    private String serverAccount;

    private String nowDay;

    private Integer count;

    public String getId() {
        return id;
    }

    public AuthCount(String id, String serverAccount, String nowDay, Integer count) {
		super();
		this.id = id;
		this.serverAccount = serverAccount;
		this.nowDay = nowDay;
		this.count = count;
	}

	public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServerAccount() {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount) {
        this.serverAccount = serverAccount == null ? null : serverAccount.trim();
    }

    public String getNowDay() {
        return nowDay;
    }

    public void setNowDay(String nowDay) {
        this.nowDay = nowDay == null ? null : nowDay.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

	public AuthCount() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}