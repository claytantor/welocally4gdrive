<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<c:url value="/user/docs" var="userDocs"/>
<c:url value="/user/home" var="userHome"/>
<c:url value="/index/list" var="listIndexes"/>
<c:url value="/index.html" var="indexPage"/>
  	 <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="${indexPage}"><img src="<c:url value='/images/welocally-drive-32.png' />"> Welocally for Google Drive</a>
          <div class="nav-collapse collapse">
          	<sec:authorize ifAnyGranted="ROLE_USER">
            <p class="navbar-text pull-right">
              <a href="${userHome}" class="navbar-link">Logged in</a>
            </p>
            </sec:authorize>
            <ul class="nav">
              <li class="active"><a href="${userHome}">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>