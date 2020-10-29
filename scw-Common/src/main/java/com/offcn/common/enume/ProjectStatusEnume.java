package com.offcn.common.enume;

/**
 * 项目状态
 */
public enum ProjectStatusEnume {

	DRAFT( 0,"草稿"),
	SUBMIT_AUTH( 1,"提交审核申请"),
	AUTHING( 2,"后台正在审核"),
	AUTHED( 3,"后台审核通过"),
	AUTHFAIL( 4,"审核失败"),
	STARTING( 5,"开始众筹"),//新增众筹项目一些状态
	SUCCESS( 6,"众筹成功"),
	FINISHED( 7,"众筹完成"),
	FAIL( 8,"众筹失败");
	
	private Integer code;
	private String status;

	ProjectStatusEnume(Integer code, String status) {
		this.code = code;
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
