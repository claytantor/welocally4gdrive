<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<c:url value="/user/docs" var="userDocs"/>
<c:url value="/user/home" var="userHome"/>
<c:url value="/index/list" var="listIndexes"/>
<c:url value="/search/list" var="listSearches"/>
<c:url value="/index.html" var="indexPage"/>
<div class="span2">
  <div class="well sidebar-nav">
    <ul class="nav nav-list">
      <li class="nav-header">My Stuff</li>
      <li class="${homeSidebarState}"><a href="${userHome}">Home</a></li>
      <li class="${accountSidebarState}"><a href="${userDocs}">Account</a></li>
      <li class="${spreadsheetSidebarState}"><a href="${userDocs}">Spreadheets</a></li>
      <li class="${indexSidebarState}"><a href="${listIndexes}">Indexes</a></li>
      <li class="${searchesSidebarState}"><a href="${listSearches}">Searches</a></li>
      <li class="${settingsSidebarState}"><a href="#">Settings</a></li>
      <li class="${codeSidebarState}"><a href="#">Generate Code</a></li>
      <li class="nav-header">Help</li>
      <li class="${faqSidebarState}"><a href="#">FAQ</a></li>
      <li class="${docsSidebarState}"><a href="#">Docs</a></li>
      <li><a href="#">Support Site</a></li>
      <li class="nav-header">Welocally</li>
      <li class="${aboutSidebarState}"><a href="#">About</a></li>
      <li class="${contactSidebarState}"><a href="#">Contact</a></li>
    </ul>
  </div><!--/.well -->
</div><!--/span-->  