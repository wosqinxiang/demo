/**
 * <p>Title: CreateCodeRequestReturnBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>
 * Title: CreateCodeRequestReturnBean
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author qinxiang
 * @date 2019年8月19日
 */
public class CreateCodeRequestReturnBean {

	private String imgStream;
	private Integer width;
	private String codeContent;

	public CreateCodeRequestReturnBean() {
		super();
	}

	public CreateCodeRequestReturnBean(String imgStream, Integer width) {
		super();
		this.imgStream = imgStream;
		this.width = width;
	}

	public String getImgStream() {
		return imgStream;
	}

	public void setImgStream(String imgStream) {
		this.imgStream = imgStream;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getCodeContent() {
		return codeContent;
	}

	public void setCodeContent(String codeContent) {
		this.codeContent = codeContent;
	}

}
