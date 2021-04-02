//package ru.gasevsky.jarsoft.filter;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
//
//public class RequestFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
//        HttpSession httpSession = req.getSession();
//        Set<Integer> watchedBanners = (Set<Integer>) httpSession.getAttribute("set");
//        if (watchedBanners == null)
//            httpSession.setAttribute("set", new HashSet<Integer>());
//        filterChain.doFilter(req, resp);
//    }
//}
