<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" %>
<aside class="sidebar-nav">
    <div class="left-top-line logo"><a href="/">컴온베이비</a></div>
    <%
        String currentPath = (String) request.getAttribute("javax.servlet.forward.request_uri");
        System.out.println(currentPath);
    %>
    <ul>
        <li><a href="/users/"><i class="fa fa-user" aria-hidden="true"></i> 회원관리</a></li>
        <li>
            <a href="/guide/featured-recipes"><i class="fa fa-file" aria-hidden="true"></i> 컴온가이드</a>
            <ul class="sub-menu">
                <li><a href="/guide/featured-recipes"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 추 천  레 시 피</a></li>
                <li><a href="/guide/fertilization"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 난 임  가 이 드</a></li>
            </ul>
        </li>
        <li>
            <a href="/notice/events"><i class="fa fa-users" aria-hidden="true"></i> 커뮤니티</a>
            <ul class="sub-menu">
                <li><a href="/notice/events"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 공지 / 이벤트</a></li>
                <li><a href="/my/story"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 자유 게시판</a></li>
                <li><a href="/my/recipes"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 나의 레시피</a></li>
                <li><a href="/my/husband"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 올챙이교실</a></li>
                <li><a href="/q-a/list"><i class="fa fa-angle-double-right" aria-hidden="true"></i> Q &amp; A</a></li>
            </ul>
        </li>
        <li><a href="/push/notification"><i class="fa fa-envelope" aria-hidden="true"></i> Push 리스트</a></li>
        <li>
            <a href="/report/generalMonthlyReport"><i class="fa fa-bar-chart" aria-hidden="true"></i> 리포트</a>
            <ul class="sub-menu">
                <li><a href="/report/generalMonthlyReport"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 월 간  리 포 트</a></li>
                <li><a href="/report/generalWeeklyReport"><i class="fa fa-angle-double-right" aria-hidden="true"></i> 주 간  리 포 트</a></li>
            </ul>
        </li>

        <%HttpSession session = request.getSession();
        if (session != null && "ADMIN".equals(session.getAttribute("UserRole"))) {%>
            <li><a href="/admin/manager-list"><i class="fa fa-user-secret" aria-hidden="true"></i> 관리자</a></li>
        <%}%>

        <li><a href="/setting/"><i class="fa fa-cogs" aria-hidden="true"></i> 설정</a></li>
    </ul>
    <div class="copyright">2017 &copy; 컴온베이비</div>
</aside>