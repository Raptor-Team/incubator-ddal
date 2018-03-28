/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.core.executor.resultset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库返回数据抽象
 *
 * @author Charley
 * @since 1.0
 */
public class ResultData {


    // -------- 查询操作的返回 ---------------
    // 列数量
    private int columnCount;
    // 列定义
    private List<ColumnDefinition> head;
    // MetaData
    private ResultSetMetaData metaData;
    // 行数据
    private List<RowData> rows;

    // -------- 更新操作的返回 ---------------
    private int affectedRows; // 影响行数

    public ResultData() {
    }

    public ResultData withQueryMode() {
        this.columnCount = 0;
        this.head = new ArrayList<>();
        this.rows = new ArrayList<>();
        return this;
    }

    public ResultData withUpdateMode() {
        this.affectedRows = 0;
        return this;
    }

    /**
     * IUD结果集转换
     *
     * @param affectedRows 影响行数
     */
    public ResultData(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    /**
     * 对Sequence进行包装
     *
     * @param nextId
     */
    public ResultData(String sequenceName, long nextId){
        this.withQueryMode();
        this.columnCount = 1;
        createSequenceWrapper(this.head, this.rows, sequenceName, nextId);
    }

    /**
     * JDBC连接ResultSet转ResultData
     *
     * @param rs JDBC结果集RseultSet
     */
    public ResultData(ResultSet rs) throws SQLException {
        this.withQueryMode();
        ResultSetMetaData metaData = new ResultMetaData(rs.getMetaData());
        int columnCount = metaData.getColumnCount();
        if (columnCount == 0) {
            return;
        }

        this.columnCount = columnCount;
        this.metaData = metaData;
        //创建Head
        createHead(this.head, columnCount, metaData);
        //创建行数据
        createRows(this.rows, columnCount, rs);
    }

    /**
     * 判断是否有数据
     * @return true:没有数据 false：有数据
     */
    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public List<ColumnDefinition> getHead() {
        return head;
    }

    public ResultSetMetaData getMetaData() {
        return metaData;
    }

    public long getRowCount() {
        if(null == rows) {
            return 0;
        }
        return rows.size();
    }

    public List<RowData> getRows() {
        return rows;
    }

    public void setRows(List<RowData> rows) {
        this.rows = rows;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void addAffectedRows(int affectedRows) {
        this.affectedRows += affectedRows;
    }

    public void addRows(List<RowData> rowData) {
        this.rows.addAll(rowData);
    }

    public void clearAndAddRows(List<? extends RowData> rowDatas) {
        this.rows.clear();
        this.rows.addAll(rowDatas);
    }

    /**
     * 创建返回数据列定义
     *
     * @param columnCount 字段个数
     * @param metaData    ResultSet中MetaData
     * @return 列定义列表
     * @throws SQLException
     */
    private static void createHead(List<ColumnDefinition> head, int columnCount, ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            String schema = metaData.getCatalogName(i);
            String table = metaData.getTableName(i);
            String originalTable = table;
            String name = metaData.getColumnLabel(i).toLowerCase();
            String originalName = metaData.getColumnName(i);
            int type = metaData.getColumnType(i);
            int columnLength = metaData.getColumnDisplaySize(i);

            ColumnDefinition columnDefinition = new ColumnDefinition();
            columnDefinition.setSchema(schema);
            columnDefinition.setTable(table);
            columnDefinition.setOriginalTable(originalTable);
            columnDefinition.setName(name);
            columnDefinition.setOriginalName(originalName);
            columnDefinition.setColumnLength(columnLength);
            columnDefinition.setType(type);
            columnDefinition.setIndex(i);

            head.add(columnDefinition);
        }
    }

    /**
     * 创建返回行数据
     *
     * @param columnCount 字段个数
     * @param rs          JDBC返回结果集ResultSet
     * @return 行数据列表
     * @throws SQLException
     */
    private static void createRows(List<RowData> rows, int columnCount, ResultSet rs) throws SQLException {
        while (rs.next()) {
            RowData rowData = new RowData(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.setCell(i, rs.getObject(i));
            }
            rows.add(rowData);
        }
    }

    /**
     * 创建Sequence包装
     *
     * @param head
     * @param rows
     * @param sequenceName
     * @param nextId
     */
    private static void createSequenceWrapper(List<ColumnDefinition> head, List<RowData> rows, String sequenceName, long nextId){
        ColumnDefinition columnDefinition = new ColumnDefinition();
        //TODO 考虑换成虚拟DB名称
        columnDefinition.setSchema("SEQUENCE");
        columnDefinition.setTable(sequenceName);
        columnDefinition.setOriginalTable(sequenceName);
        columnDefinition.setName("NEXTVAL");
        columnDefinition.setOriginalName("NEXTVAL");
        columnDefinition.setColumnLength(7);
        columnDefinition.setType(Types.BIGINT);
        columnDefinition.setIndex(1);
        head.add(columnDefinition);

        RowData rowData = new RowData(1);
        rowData.setCell(1, nextId);
        rows.add(rowData);
    }

}
