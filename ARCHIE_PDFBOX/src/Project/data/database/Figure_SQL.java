/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;

import Project.data.preparation.Configuration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author fang
 */
public class Figure_SQL extends SQL_operation {

    private Statement stmt_figure = null;
    private Connection conn = null;
    private ArrayList<String> attribute = new ArrayList<String>();
    private ArrayList<String> value = null;

    private String table_name = "";

    public Figure_SQL(Connection conn, String table_name) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        Insert_Figure(conn, table_name);
    }

    public void Insert_Figure(Connection conn, String table_name) throws SQLException {

        stmt_figure = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = stmt_figure.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        rs.last();
        int count = rs.getRow();
        rs.beforeFirst();
        if (count == 0) {
            int col = meta.getColumnCount();
            if (table_name.equalsIgnoreCase(Configuration.FIGURE_TBL)) {
                Insert_figure_value(meta);
                InsertFigureQuery(table_name, meta, value, stmt_figure);
            }

        } else {
            ;
        }
        stmt_figure.close();

    }

    public void InsertFigureQuery(String tableName, ResultSetMetaData meta, ArrayList<String> value, Statement stmt) throws SQLException {

        for (int i = 0; i < value.size(); i++) {
            String val_str = "";
            String insert_query = "INSERT INTO \"" + tableName + "\" (";
            String att_str = "";
            int col = meta.getColumnCount();
            for (int k = 1; k <= col; k++) {
                if (k == col) {
                    att_str = att_str + meta.getColumnName(k) + ") VALUES (";
                } else {
                    att_str = att_str + meta.getColumnName(k) + ",";
                }
            }

            val_str = val_str + "'" + value.get(i) + "')";

            String query_str = insert_query + att_str + val_str;
//            System.out.println(query_str);
            ExecuteInsertSQL(stmt_figure, query_str);

        }
    }

    public void Insert_figure_value(ResultSetMetaData meta) throws SQLException {

        value = new ArrayList<String>();
        for (int i = 0; i < 31; i++) {

            value.add(Configuration.FIGURE_IMAGE_PK + i);
        }
    }

}
