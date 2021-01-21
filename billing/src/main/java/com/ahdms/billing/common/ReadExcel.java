/**
 * Created on 2019年11月27日 by liuyipin
 */
package com.ahdms.billing.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.ahdms.billing.model.BoxInfo;




/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class ReadExcel {

	private int totalRows = 0;

	private int totalCells = 0;

	private String errorMsg;

	public ReadExcel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalCells() {
		return totalCells;
	}

	public void setTotalCells(int totalCells) {
		this.totalCells = totalCells;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * 读EXCEL文件，获取信息集合
	 * 
	 * @param fielName
	 * @return
	 */
	public List<BoxInfo> getExcelInfo(MultipartFile mFile) {
		String fileName = mFile.getOriginalFilename();// 获取文件名
		//        List<Map<String, Object>> userList = new LinkedList<Map<String, Object>>();
		try {
			if (!validateExcel(fileName)) {// 验证文件名是否合格
				return null;
			}
			boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
			if (isExcel2007(fileName)) {
				isExcel2003 = false;
			}
			return createExcel(mFile.getInputStream(), isExcel2003);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据excel里面的内容读取客户信息
	 * 
	 * @param is      输入流
	 * @param isExcel2003   excel是2003还是2007版本
	 * @return
	 * @throws IOException
	 */
	public List<BoxInfo> createExcel(InputStream is, boolean isExcel2003) {
		try {
			Workbook wb = null;
			if (isExcel2003) {// 当excel是2003时,创建excel2003
				wb = new HSSFWorkbook(is);
			} else {// 当excel是2007时,创建excel2007
				wb = new XSSFWorkbook(is);
			}
			return readExcelValue(wb);// 读取Excel里面客户的信息
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取Excel里面客户的信息
	 * 
	 * @param wb
	 * @return
	 */
	private List<BoxInfo> readExcelValue(Workbook wb) {
		// 得到第一个shell
		Sheet sheet =  wb.getSheetAt(0);
		// 得到Excel的行数
		this.totalRows = sheet.getPhysicalNumberOfRows();
		// 得到Excel的列数(前提是有行数)
		if (totalRows > 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		List<BoxInfo> boxList = new ArrayList<BoxInfo>();
		// 循环Excel行数
		for (int r = 2; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			} 
			BoxInfo box = new BoxInfo();
			// 循环Excel的列 
			for (int c = 0; c < this.totalCells; c++) {
				Cell cell = row.getCell(c);
				if (null != cell) {
					if (c == 1) {
						// 如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							String boxId = String.valueOf(cell.getNumericCellValue());
							box.setBoxId(boxId.substring(0, boxId.length() - 2 > 0 ? boxId.length() - 2 : 1));// 设备id
						} else {
							if(StringUtils.isNotBlank(cell.getStringCellValue())){
								box.setBoxId(cell.getStringCellValue());// 设备id
							}
						}
					} else if (c == 2) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							String boxNum = String.valueOf(cell.getNumericCellValue());
							box.setBoxNum(boxNum.substring(0, boxNum.length() - 2 > 0 ? boxNum.length() - 2 : 1));// 设备芯片id
						} else {
							if(StringUtils.isNotBlank(cell.getStringCellValue())){
								box.setBoxNum(cell.getStringCellValue());// 设备芯片id
							}
						}
					} else if (c == 3) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							String boxLocation = String.valueOf(cell.getNumericCellValue());
							box.setBoxLocation(boxLocation.substring(0, boxLocation.length() - 2 > 0 ? boxLocation.length() - 2 : 1));// 设备所在地
						} else {
							if(StringUtils.isNotBlank(cell.getStringCellValue())){
								box.setBoxLocation(cell.getStringCellValue());// 设备所在地
							}
						}
					} else if (c == 4) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							String boxType = String.valueOf(cell.getNumericCellValue());
							box.setBoxType(boxType.substring(0, boxType.length() - 2 > 0 ? boxType.length() - 2 : 1));// 设备类型
						} else {
							if(StringUtils.isNotBlank(cell.getStringCellValue())){
								box.setBoxType(cell.getStringCellValue());// 设备类型
							}
						}
					} else if (c == 5) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							String serverDepartment = String.valueOf(cell.getNumericCellValue());
							box.setServerDepartment(serverDepartment.substring(0, serverDepartment.length() - 2 > 0 ? serverDepartment.length() - 2 : 1));// 设备所属部门
						} else {
							if(StringUtils.isNotBlank(cell.getStringCellValue())){
								box.setServerDepartment(cell.getStringCellValue());// 设备所属部门
							}
						}
					}
				} 
			}

			// 添加到list
			boxList.add(box);
		}
		return boxList;
	}

	/**
	 * 验证EXCEL文件
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean validateExcel(String filePath) {
		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			errorMsg = "文件名不是excel格式";
			return false;
		}
		return true;
	}

	// @描述：是否是2003的excel，返回true是2003
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}


	// @描述：是否是2007的excel，返回true是2007
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

}



