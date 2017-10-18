package com.stouduo.dcw.util;

import com.alibaba.fastjson.JSON;
import com.stouduo.dcw.domain.FormValue;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

    /**
     * @param list      数据源
     *                  如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
     *                  如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
     *                  fieldMap.put("college.collegeName","学院名称")
     * @param sheetName 工作表的名称
     * @param sheetSize 每个工作表中记录的最大个数
     * @param out       导出流
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
     */
    public static void listToExcel(
            List<FormValue> list,
            String sheetName,
            List<String> fieldNames,
            int sheetSize,
            OutputStream out
    ) throws ExcelException {


        if (list.size() == 0 || list == null) {
            throw new ExcelException("数据源中没有任何数据");
        }

        if (sheetSize > 65535 || sheetSize < 1) {
            sheetSize = 65535;
        }

        //创建工作簿并发送到OutputStream指定的地方
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(out);
            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            //1.计算一共有多少个工作表
            double sheetNum = Math.ceil(list.size() / new Integer(sheetSize).doubleValue());

            //2.创建相应的工作表，并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                //如果只有一个工作表的情况
                if (1 == sheetNum) {
                    WritableSheet sheet = wwb.createSheet(sheetName, i);
                    fillSheet(sheet, list, fieldNames, 0, list.size() - 1);

                    //有多个工作表的情况
                } else {
                    WritableSheet sheet = wwb.createSheet(sheetName + (i + 1), i);

                    //获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1 : (i + 1) * sheetSize - 1;
                    //填充工作表
                    fillSheet(sheet, list, fieldNames, firstIndex, lastIndex);
                }
            }

            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
            //如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                //否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }

    }

    /**
     * @param list 数据源
     * @param out  导出流
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，工作表大小为2003支持的最大值）
     */
    public static void listToExcel(
            List<FormValue> list,
            String sheetName,
            List<String> fieldNames,
            OutputStream out
    ) throws ExcelException {

        listToExcel(list, sheetName, fieldNames, 65535, out);

    }


    /**
     * @param list      数据源
     * @param sheetSize 每个工作表中记录的最大个数
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
     */
    public static void listToExcel(
            List<FormValue> list,
            String formName,
            List<String> fieldNames,
            String sheetName,
            int sheetSize

    ) throws ExcelException {
        HttpServletResponse response = ControllerUtil.getCurrentResponse();
        //设置默认文件名为当前时间：年月日时分秒
        String fileName = formName + "_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();

        //设置response头信息
        response.reset();
        response.setContentType("application/octet-stream");        //改成输出excel文件
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        response.setHeader("content-type", "application/octet-stream");
        //创建工作簿并发送到浏览器
        try {

            OutputStream out = response.getOutputStream();
            listToExcel(list, sheetName, fieldNames, sheetSize, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

            //如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                //否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }
    }


    /**
     * @param list 数据源
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值）
     */
    public static void listToExcel(
            List<FormValue> list,
            String sheetName,
            List<String> fieldNames,
            String formName
    ) throws ExcelException {

        listToExcel(list, formName, fieldNames, sheetName, 65535);
    }

    /**
     * @param excelPath    ：承载着Excel的路劲
     * @param sheetName    ：要导入的工作表序号
     * @param fieldNames   ：Excel中的中文列头和类的英文属性的对应关系Map
     * @param uniqueFields ：指定业务主键组合（即复合主键），这些列的组合不能重复
     * @return ：List
     * @throws ExcelException
     * @MethodName : excelToList
     * @Description : 将Excel转化为List
     */
    public static List<FormValue> excelToList(
            String excelPath,
            String sheetName,
            List<String> fieldNames,
            String[] uniqueFields,
            String formId
    ) throws ExcelException, FileNotFoundException {
        return excelToList(new FileInputStream(ResourceUtils.getFile("classpath:" + excelPath)), sheetName, fieldNames, uniqueFields, formId);
    }

    public static List<FormValue> excelToList(InputStream in, String sheetName, List<String> fieldNames, String[] uniqueFields, String formId) throws ExcelException {

        //定义要返回的list
        List<FormValue> resultList = new ArrayList<>();

        try {

            //根据Excel数据源创建WorkBook
            Workbook wb = Workbook.getWorkbook(in);
            //获取工作表
            Sheet sheet = wb.getSheet(sheetName);

            //获取工作表的有效行数
            int realRows = 0;
            for (int i = 0; i < sheet.getRows(); i++) {

                int nullCols = 0;
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell currentCell = sheet.getCell(j, i);
                    if (currentCell == null || "".equals(currentCell.getContents().toString())) {
                        nullCols++;
                    }
                }

                if (nullCols == sheet.getColumns()) {
                    break;
                } else {
                    realRows++;
                }
            }


            //如果Excel中没有数据则提示错误
            if (realRows <= 1) {
                throw new ExcelException("Excel文件中没有任何数据");
            }


            Cell[] firstRow = sheet.getRow(0);

            String[] excelFieldNames = new String[firstRow.length];

            //获取Excel中的列名
            for (int i = 0; i < firstRow.length; i++) {
                excelFieldNames[i] = firstRow[i].getContents().toString().trim();
            }

            //判断需要的字段在Excel中是否都存在
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (String cnName : fieldNames) {
                if (!excelFieldList.contains(cnName)) {
                    isExist = false;
                    break;
                }
            }

            //如果有列名不存在，则抛出异常，提示错误
            if (!isExist) {
                throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
            }


            //将列名和列号放入Map中,这样通过列名就可以拿到列号
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < excelFieldNames.length; i++) {
                colMap.put(excelFieldNames[i], firstRow[i].getColumn());
            }

            if (uniqueFields != null) {
                //判断是否有重复行
                //1.获取uniqueFields指定的列
                Cell[][] uniqueCells = new Cell[uniqueFields.length][];
                for (int i = 0; i < uniqueFields.length; i++) {
                    int col = colMap.get(uniqueFields[i]);
                    uniqueCells[i] = sheet.getColumn(col);
                }

                //2.从指定列中寻找重复行
                for (int i = 1; i < realRows; i++) {
                    int nullCols = 0;
                    for (int j = 0; j < uniqueFields.length; j++) {
                        String currentContent = uniqueCells[j][i].getContents();
                        Cell sameCell = sheet.findCell(currentContent,
                                uniqueCells[j][i].getColumn(),
                                uniqueCells[j][i].getRow() + 1,
                                uniqueCells[j][i].getColumn(),
                                uniqueCells[j][realRows - 1].getRow(),
                                true);
                        if (sameCell != null) {
                            nullCols++;
                        }
                    }

                    if (nullCols == uniqueFields.length) {
                        throw new ExcelException("Excel中有重复行，请检查");
                    }
                }
            }
            FormValue formValue;
            Map<String, String> values = new HashMap<>();
            String fieldValue;
            String[] clientMsg = ControllerUtil.getUserAgent();
            //将sheet转换为list
            String ip = ControllerUtil.getIpAddress();
            String username = SecurityUtil.getUsername();
            Date now = new Date();
            for (int i = 1; i < realRows; i++) {
                //新建要转换的对象
                formValue = new FormValue();
                //给对象中的字段赋值
                for (String fieldName : fieldNames) {
                    //根据中文字段名获取列号
                    //获取当前单元格中的内容
                    fieldValue = sheet.getCell(colMap.get(fieldName), i).getContents().toString().trim();
                    values.put(fieldName, fieldValue);
                    //给对象赋值
                    // setFieldValueByName(enNormalName, content, entity);
                }
                formValue.setOs(clientMsg[1]);
                formValue.setSubmitIP(ip);
                formValue.setBrowser(clientMsg[0]);
                formValue.setAuthor(username);
                formValue.setLastModifyPerson(username);
                formValue.setCreateTime(now);
                formValue.setForm(formId);
                formValue.setValue(JSON.toJSON(values).toString());
                values.clear();
                resultList.add(formValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                //否则将其它异常包装成ExcelException再抛出
            } else {
                e.printStackTrace();
                throw new ExcelException("导入Excel失败");
            }
        }
        return resultList;
    }



    /*<-------------------------辅助的私有方法----------------------------------------------->*/

    /**
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     * @MethodName : getFieldValueByName
     * @Description : 根据字段名获取字段值
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {

        Object value = null;
        Field field = getFieldByName(fieldName, o.getClass());

        if (field != null) {
            field.setAccessible(true);
            value = field.get(o);
        } else {
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }

        return value;
    }

    /**
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        //拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        //如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        //否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        //如果本类和父类都没有，则返回空
        return null;
    }


    /**
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws Exception
     * @MethodName : getFieldValueByNameSequence
     * @Description :
     * 根据带路径或不带路径的属性名获取属性值
     * 即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {

        Object value = null;

        //将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            //根据属性名获取属性对象
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;

    }


    /**
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     */
    private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {

        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            //获取字段类型
            Class<?> fieldType = field.getType();

            //根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType)
                    || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType)
                    || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType)
                    || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null) && (fieldValue.toString().length() > 0)) {
                    field.set(o, Character
                            .valueOf(fieldValue.toString().charAt(0)));
                }
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }
    }


    /**
     * @param ws
     * @MethodName : setColumnAutoSize
     * @Description : 设置工作表自动列宽和首行加粗
     */
    private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
        //获取本列的最宽单元格的宽度
        for (int i = 0; i < ws.getColumns(); i++) {
            int colWith = 0;
            for (int j = 0; j < ws.getRows(); j++) {
                String content = ws.getCell(i, j).getContents().toString();
                int cellWith = content.length();
                if (colWith < cellWith) {
                    colWith = cellWith;
                }
            }
            //设置单元格的宽度为最宽宽度+额外宽度
            ws.setColumnView(i, colWith + extraWith);
        }

    }

    /**
     * @param sheet      工作表
     * @param list       数据源
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     * @MethodName : fillSheet
     * @Description : 向工作表中填充数据
     */
    private static void fillSheet(
            WritableSheet sheet,
            List<FormValue> list,
            List<String> fieldNames,
            int firstIndex,
            int lastIndex
    ) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < fieldNames.size(); i++) {
            sheet.addCell(new Label(i, 0, fieldNames.get(i)));
        }
        //填充内容
        int rowNo = 1;
        int c = 0;
        for (int index = firstIndex; index <= lastIndex; index++) {
            //获取单个对象
            FormValue item = list.get(index);
            c = 0;
            for (Map.Entry<String, String> entry : ((Map<String, String>) JSON.parse(item.getValue())).entrySet()) {
                sheet.addCell(new Label(c++, rowNo, entry.getValue()));
            }
            sheet.addCell(new Label(c++, rowNo, item.getAuthor()));
            sheet.addCell(new Label(c++, rowNo, item.getLastModifyPerson()));
            sheet.addCell(new Label(c++, rowNo, item.getCreateTime() == null ? "" : sdf.format(item.getCreateTime())));
            sheet.addCell(new Label(c++, rowNo, item.getLastModifyTime() == null ? "" : sdf.format(item.getLastModifyTime())));
            sheet.addCell(new Label(c++, rowNo, item.getBrowser()));
            sheet.addCell(new Label(c++, rowNo, item.getOs()));
            sheet.addCell(new Label(c, rowNo++, item.getSubmitIP()));
        }

        //设置自动列宽
        setColumnAutoSize(sheet, 5);
    }

}