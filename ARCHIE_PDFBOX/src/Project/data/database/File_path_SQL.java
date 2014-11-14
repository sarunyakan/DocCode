/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;

import Project.data.preparation.Configuration;
import java.nio.file.Path;
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
public class File_path_SQL extends SQL_operation {

    private Statement stmt_file_path = null;
    private Connection conn = null;
    private Path file_path;
    private String table_name = "";

    public File_path_SQL(Connection conn, String table_name, Path file_path) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.file_path = file_path;
        Insert_file_path(conn, table_name, file_path);
    }

    public void Insert_file_path(Connection conn, String table_name, Path file_path) throws SQLException {
        stmt_file_path = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_file_path.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";

        if (table_name.equalsIgnoreCase(Configuration.FILE_PATH_TBL)) {
            val_str = File_path_value_string(table_name, meta, file_path.toString(), stmt_file_path);
        }

        if (val_str.length() > 0) {
            InsertQuery(table_name, meta, stmt_file_path, att_str, val_str);
        }

    }

    public String File_path_value_string(String table_name, ResultSetMetaData meta, String filepath_ele, Statement stmt_file_path) throws SQLException {

        String val_str = "";

        String filepath = filepath_ele;
        boolean chk = CheckExistedValue(table_name, meta.getColumnName(2), filepath, stmt_file_path);
        if (!chk) {
            int seq = getNextval(Configuration.FILEPATH_SEQ, stmt_file_path);
            String filepath_pk = appendQuote(Configuration.FILEPATH_PK + seq);
            filepath = appendQuote(filepath);
            val_str = filepath_pk + "," + filepath;
        }

        return val_str;
    }
}
