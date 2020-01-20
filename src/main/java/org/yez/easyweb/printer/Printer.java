package org.yez.easyweb.printer;

import org.yez.easyweb.module.BackModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Printer {

    public void print(HttpServletRequest request, HttpServletResponse response, BackModule modul);
}
