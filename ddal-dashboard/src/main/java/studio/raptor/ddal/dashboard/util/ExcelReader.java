package studio.raptor.ddal.dashboard.util;

import org.apache.poi.ss.usermodel.*;
import studio.raptor.ddal.dashboard.repository.Sequence;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** 
 * excel读取
 * HSSF用于2003版即以 .xls结尾
 * XSSF用于2007版即以 .xlsx结尾
 */  
public class ExcelReader {

    /**
     * 读取并解析 .xls/.xlsx 文件流
     * @param fileInputStream
     * @return
     * @throws Exception
     */
    public static List<Sequence> readSequencesData(FileInputStream fileInputStream)throws Exception{
        List<Sequence> sequenceList = new ArrayList();
        // 创建工作薄Workbook,读取2007版，以 .xlsx 结尾 or 读取2003版，以 .xls 结尾
        Workbook workBook = WorkbookFactory.create(fileInputStream);
        //Get the number of sheets in the xlsx file
        int numberOfSheets = workBook.getNumberOfSheets();
        // 循环 numberOfSheets
        for(int sheetNum = 0; sheetNum < numberOfSheets; sheetNum++){
            // 得到 工作薄 的第 N个表
            Sheet sheet = workBook.getSheetAt(sheetNum);
            Row row;
            Cell cell;
            String cellValue;
            Sequence sequence;
            for(int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++){
                // 循环行数
                row = sheet.getRow(i);
                sequence=new Sequence();
                int j=0;
                while(true){
                    cell = row.getCell(j++);
                    if(cell==null){
                        throw new Exception("表单"+(sheetNum+1)+"中第"+(i+1)+"行第"+(j)+"列值为空,输入非法。");
                    }
                    else{
                        cellValue=cell.toString().trim();
                        if(j==1){
                            if("centername".equals( cellValue.toLowerCase() )){
                                //表头直接循环下一行
                                break;
                            }
                            if(cell.toString().trim().length()<=0){
                                break;
                            }
                            sequence.setCenterName(cellValue  );
                        }
                        else if(j==2){
                            if(cell.toString().trim().length()<=0){
                                throw new Exception("表单"+(sheetNum+1)+"中第"+(i+1)+"行第"+(j)+"列值为空,输入非法。");
                            }
                            sequence.setName( cellValue );
                        }
                        else if(j==3){
                            try{
                                long newValue=(long)cell.getNumericCellValue();
                                sequence.setValue(newValue);
                            }catch (Exception e){
                                throw new Exception("表单"+(sheetNum+1)+"中第"+(i+1)+"行第"+(j)+"列序列值必须为整形数字,输入非法。");
                            }
                            sequence.setPosition( "表单"+(sheetNum+1)+"中第"+(i+1)+"行序列" );
                            sequenceList.add(sequence);
                            break;
                        }
                    }
                }
            }
        }
        return sequenceList;
    }

    /**
     * 读取并解析 .xls/.xlsx 文件流
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static List readExcelData(InputStream inputStream)throws Exception{
        List sequenceList = new ArrayList();
        // 创建工作薄Workbook,读取2007版，以 .xlsx 结尾 or 读取2003版，以 .xls 结尾
        Workbook workBook = WorkbookFactory.create(inputStream);
        //Get the number of sheets in the xlsx file  
        int numberOfSheets = workBook.getNumberOfSheets();
        // 循环 numberOfSheets  
        for(int sheetNum = 0; sheetNum < numberOfSheets; sheetNum++){
            // 得到 工作薄 的第 N个表  
            Sheet sheet = workBook.getSheetAt(sheetNum);  
            Row row;  
            String cell;  
            for(int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++){
                // 循环行数  
                row = sheet.getRow(i);
                for(int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++){
                    // 循环列数  
                    cell = row.getCell(j).toString();
                    sequenceList.add(cell);
                    System.out.print(cell+"\t");
                }
                System.out.println();
            }
            System.out.println("========");
        }  
        return sequenceList;
    }

    public static void main(String[] args)throws Exception {
        String fileName="E:\\Sequences重置模板1.xlsx";
//        List sequenceList = readExcelData(new FileInputStream(fileName));
//        System.out.println(sequenceList);
        try{
            List<Sequence> seqList=readSequencesData(new FileInputStream(fileName));
            System.out.println(seqList);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}