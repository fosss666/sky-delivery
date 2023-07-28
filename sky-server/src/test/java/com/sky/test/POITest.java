package com.sky.test;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: fosss
 * Date: 2023/7/28
 * Time: 10:52
 * Description:使用Apache POI 操作excel
 */
public class POITest {

    public static void main(String[] args) throws IOException {
        //write();
        read();
    }

    /**
     * 写文件
     *
     * @throws IOException
     */
    public static void read() throws IOException {
        //创建输入流
        FileInputStream is = new FileInputStream("D://info.xlsx");

        //创建excel
        XSSFWorkbook excel = new XSSFWorkbook(is);
        //获取第一个sheet
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取最后一行有文字的行数
        int lastRowNum = sheet.getLastRowNum();
        //遍历每一行
        for (int i = 0; i <= lastRowNum; i++) {
            //取到每一行，获取单元格读出数据
            XSSFRow row = sheet.getRow(i);
            XSSFCell cell = row.getCell(0);
            String cellValue1 = cell.getStringCellValue();
            cell = row.getCell(1);
            String cellValue2 = cell.getStringCellValue();

            System.out.println(cellValue1 + " " + cellValue2);
        }

        is.close();
        excel.close();
    }

    /**
     * 读文件
     *
     * @throws IOException
     */
    public static void write() throws IOException {
        //在内存中创建excel
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建sheet
        XSSFSheet sheet1 = excel.createSheet("sheet1");
        //创建行
        XSSFRow row1 = sheet1.createRow(0);
        //创建单元格
        XSSFCell cell = row1.createCell(0);
        cell.setCellValue("姓名");
        cell = row1.createCell(1);
        cell.setCellValue("城市");

        //创建行
        XSSFRow row2 = sheet1.createRow(1);
        //创建单元格
        cell = row2.createCell(0);
        cell.setCellValue("张三");
        cell = row2.createCell(1);
        cell.setCellValue("北京");

        //创建行
        XSSFRow row3 = sheet1.createRow(2);
        //创建单元格
        cell = row3.createCell(0);
        cell.setCellValue("赵四");
        cell = row3.createCell(1);
        cell.setCellValue("南京");

        //写入磁盘
        FileOutputStream os = new FileOutputStream("D://info.xlsx");
        excel.write(os);

        //关闭资源
        os.close();
        excel.close();
    }
}


















