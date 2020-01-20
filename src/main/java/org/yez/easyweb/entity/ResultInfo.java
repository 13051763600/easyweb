package org.yez.easyweb.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import org.yez.easyweb.source.DataSource;
import org.yez.easyweb.source.DataSourceFactory;
import org.yez.easyweb.source.Template;
import org.yez.easyweb.source.mybatis.Page;
import org.yez.easyweb.source.mybatis.PageInfo;

public class ResultInfo extends BaseEntity implements Cloneable, Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 2004733745475516065L;
    private String paging;
    private String sql;
    private String dataSourceName;
    private PageInfo pageInfo;
    private String resultType;
    private JSONObject result;
    private String dataName;
    
    public String getPaging() {
        return paging;
    }
    public void setPaging(String paging) {
        this.paging = paging;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public String getDataSourceName() {
        return dataSourceName;
    }
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    public JSONObject getResult() {
        return result;
    }
    public void setResult(JSONObject result) {
        this.result = result;
    }
    public String getDataName() {
        return dataName;
    }
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    public String getResultType() {
        return resultType;
    }
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
    public Template getTemplate() {
        DataSource dataSource = DataSourceFactory.getInstant().getDataSource(this.dataSourceName);
        return dataSource.getTemplate();
    }
    public boolean isPaging(){
        if (this.pageInfo == null){
            return false;
        }
        if (!Boolean.valueOf(this.paging)){
            return false;
        }
        Page page = this.getPageInfo().getPage(null);
        if (null != page){
            return page.getPageSize() > 0;
        }
        return true;
    }
}
