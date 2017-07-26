<%@ page import="com.ComeOnBaby.model.RecipeGuide" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no, minimal-ui"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="format-detection" content="address=no"/>

    <meta name="description" content=""/>
    <meta name="keywords" content=""/>

    <title>추 천 레 시 피 :: 컴온베이비</title>

    <%@ include file="headerStyles.jsp" %>

</head>
<body>
<!-- Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <div id="sidebar-wrapper">

        <%@ include file="leftCategoriesMenu.jsp" %>

    </div>
    <!-- #End Sidebar -->

    <!-- Page Content -->
    <div id="page-content-wrapper">

        <%@ include file="topLine.jsp" %>

        <%RecipeGuide recipeGuide = (RecipeGuide) request.getAttribute("recipeGuide");%>
        <%boolean isNew = (boolean) request.getAttribute("isNew");%>
        <%String formAction = "/guide/save-new-recipe";%>
        <!-- Content section -->
        <section class="container-fluid content">

            <%
                String headTitle = "새로운";
                if (!isNew) headTitle = recipeGuide.getTitle();
            %>
            <h3><i class="fa fa-list-ul"></i><%out.print(headTitle);%></h3>

            <!-- Edit form -->
            <form action="<%out.print(formAction);%>" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-6">

                        <input type="hidden" name="id" value="<%if (!isNew)out.print(recipeGuide.getId());%>">

                        <%
                            String title = "";
                            if (!isNew) title = recipeGuide.getTitle();
                        %>
                        <div class="form-group">
                            <label for="title">표제</label>
                            <input type="text" class="form-control" name="title" id="title"
                                   value="<%out.print(title);%>" placeholder="가이드 제목" required>
                        </div>

                        <%
                            String urlNaver = "";
                            if (!isNew) urlNaver = recipeGuide.getUrlNaver();
                        %>
                        <div class="form-group">
                            <label for="url">URL <%if (!isNew) {%><a href="javascript:void(0);"
                                                                     class="pl5 recipesOpenLink"><i
                                    class="fa fa-link"></i> 링크 열기</a><%}%></label>
                            <input type="text" class="form-control" name="url" id="url" value="<%out.print(urlNaver);%>"
                                   placeholder="URL">
                        </div>

                        <% if (!isNew) {
                            String urlPic = recipeGuide.getImageThumbnail(); %>
                        <div class="form-group form-img-thumbnail">
                            <a data-fancybox="gallery" href="<%out.print(urlPic);%>"><img src="<%out.print(urlPic);%>"
                                                                                          alt="<%out.print(title);%>"
                                                                                          class="img-thumbnail"></a>
                        </div>
                        <%}%>


                        <div class="form-group">
                            <label class="control-label">영상</label>
                            <input id="fileInput" name="filePicture[]" type="file" class="file"
                                   <%if (isNew){%>required <%}%>>
                        </div>
                    </div>
                </div>

                <div class="mt20 delimiter">
                    <a href="/guide/featured-recipes" class="btn btn-default">뒤로</a>
                    <button class="btn btn-primary" type="submit">구하다</button>
                </div>
            </form>
            <!-- #End Edit form -->

        </section>
        <!-- Content section -->

    </div>
    <!-- #End Page-content -->

</div>
<!-- #End Wrapper -->

<%@ include file="footerJavaScript.jsp" %>

</body>
</html>