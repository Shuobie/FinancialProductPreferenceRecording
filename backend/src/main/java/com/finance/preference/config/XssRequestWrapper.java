package com.finance.preference.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.util.HtmlUtils;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null)
            return null;
        String[] sanitized = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sanitized[i] = HtmlUtils.htmlEscape(values[i]);
        }
        return sanitized;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return value == null ? null : HtmlUtils.htmlEscape(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : HtmlUtils.htmlEscape(value);
    }
}
