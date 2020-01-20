package org.yez.easyweb.source.mybatis;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class PageInfo implements Cloneable, Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 3189305181328676803L;
    private String pageNumName;
    private String pageSizeName;
    private String totalName;
    private Page page;
    public String getPageNumName() {
        return pageNumName;
    }
    public void setPageNumName(String pageNumName) {
        this.pageNumName = pageNumName;
    }
    public String getPageSizeName() {
        return pageSizeName;
    }
    public void setPageSizeName(String pageSizeName) {
        this.pageSizeName = pageSizeName;
    }
    public String getTotalName() {
        return totalName;
    }
    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }
    public Page getPage(HttpServletRequest request){
        if (this.page != null || null == request){
            return this.page;
        }
        String pageNum = request.getParameter(this.pageNumName);
        String pageSize = request.getParameter(this.pageSizeName);
        this.page = new Page(
                pageNum == null ? 0 : Integer.valueOf(pageNum), 
                pageSize == null ? 0 : Integer.valueOf(pageSize));
        return this.page;
    }
}
