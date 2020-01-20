package org.yez.easyweb.source.RDBMS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.Template;
import org.yez.easyweb.source.mybatis.Page;

public class RDBMSTemplete implements Template {

    private Connection connection;

    public RDBMSTemplete(Connection connection) {
        this.connection = connection;
    }

    @Override
    public JSON select(ResultInfo info, Map<String, Object> params, Page page) {
        int totalCount = 0;
        try {
            String sql = info.getSql();
            for (String key : params.keySet()) {
                sql = sql.replace("${" + key + "}", String.valueOf(params.get(key)));
            }
            //查询总数
            if (isPage(info, page)){
                try(Statement stmt = this.connection.createStatement();
                        ResultSet rs = stmt.executeQuery("select count(1) from (" + sql + ") a");
                        ){
                    if (rs.next()){
                        totalCount = rs.getInt(1);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                sql = "select a.* from (" + sql + ") a limit " + page.getStartRow() + ", " + page.getPageSize();
            }
            
            try (Statement stmt = this.connection.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)){
                // 创建一个JSONArray对象
                JSON json = null;
                // 获得ResultSetMeataData对象
                ResultSetMetaData rsmd = rs.getMetaData();
                if (ApiInfo.RETURN_TYPE_LIST.equals(info.getResultType())){
                    JSONArray dataJson = new JSONArray();
                    while (rs.next()) {
                        // 定义json对象
                        JSONObject obj = new JSONObject();
                        // 判断数据类型&获取value
                        getType(rs, rsmd, obj);
                        // 将对象添加到JSONArray中
                        dataJson.add(obj);
                    }
                    if (isPage(info, page)){
                        json = new JSONObject();
                        ((JSONObject)json).put(info.getPageInfo().getTotalName(), totalCount);
                        ((JSONObject)json).put("isPaging", true);
                        ((JSONObject)json).put(info.getDataName(), dataJson);
                    } else {
                        json = dataJson;
                    }
                } else {
                    // 定义json对象
                    json = new JSONObject();
                    if (rs.next()) {
                        // 判断数据类型&获取value
                        getType(rs, rsmd, (JSONObject)json);
                    }
                }
                return json;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isPage(ResultInfo info, Page page) {
        return info.isPaging() && page.getPageSize() > 0;
    }

    private void getType(ResultSet rs, ResultSetMetaData rsmd, JSONObject obj) throws SQLException {
        int total_rows = rsmd.getColumnCount();
        for (int i = 0; i < total_rows; i++) {
            String columnName = rsmd.getColumnLabel(i + 1);
            switch (rsmd.getColumnType(i + 1)) {
            case java.sql.Types.ARRAY:
                obj.put(columnName, rs.getArray(columnName));
                break;
            case java.sql.Types.BIGINT:
                obj.put(columnName, rs.getInt(columnName));
                break;
            case java.sql.Types.BOOLEAN:
                obj.put(columnName, rs.getBoolean(columnName));
                break;
            case java.sql.Types.BLOB:
                obj.put(columnName, rs.getBlob(columnName));
                break;
            case java.sql.Types.DOUBLE:
                obj.put(columnName, rs.getDouble(columnName));
                break;
            case java.sql.Types.FLOAT:
                obj.put(columnName, rs.getFloat(columnName));
                break;
            case java.sql.Types.INTEGER:
                obj.put(columnName, rs.getInt(columnName));
                break;
            case java.sql.Types.NVARCHAR:
                obj.put(columnName, rs.getNString(columnName));
                break;
            case java.sql.Types.VARCHAR:
                obj.put(columnName, rs.getString(columnName));
                break;
            case java.sql.Types.TINYINT:
                obj.put(columnName, rs.getInt(columnName));
                break;
            case java.sql.Types.SMALLINT:
                obj.put(columnName, rs.getInt(columnName));
                break;
            case java.sql.Types.DATE:
                obj.put(columnName, rs.getDate(columnName));
                break;
            case java.sql.Types.TIMESTAMP:
                obj.put(columnName, rs.getTimestamp(columnName));
                break;
            default:
                obj.put(columnName, rs.getObject(columnName));
                break;
            }
        }
    }

}
