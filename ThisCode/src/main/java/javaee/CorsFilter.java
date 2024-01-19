package javaee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // すべてのドメインからのリクエストを許可
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        // 他のCORSヘッダーも設定する場合はここで追加

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初期化処理
    }

    @Override
    public void destroy() {
        // 終了処理
    }
}

