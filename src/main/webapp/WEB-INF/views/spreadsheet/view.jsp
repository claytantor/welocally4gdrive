<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:url value="/index/createform" var="indexCreate"/>
<c:url value="/user/worksheet" var="userWorksheet"/>
<c:url value="/user/worksheetdata.json" var="userWorksheetData"/>
<c:set value="active" var="spreadsheetSidebarState" scope="request"/>
<html>
  <jsp:include page="../head.jsp"/> 
<body>
<jsp:include page="../usernav.jsp"/> 

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">      
    
        <jsp:include page="../usersidebar.jsp"/>
    
	    <div class="span9">
			<ul class="breadcrumb">
			  <li><a href="#">Home</a> <span class="divider">/</span></li>
			  <li><a href="#">Library</a> <span class="divider">/</span></li>
			  <li class="active">Data</li>
			</ul>	    
		    <h2>${spreadsheet.title.name}</h2>
	
		 	<table width="100%" class="table">
		 	<c:forEach var="entry" items="${spreadsheet.entry}">
		    		
		    		<c:forEach var="link" items="${entry.link}">

		    			<c:if test='${link.rel eq "http://schemas.google.com/spreadsheets/2006#cellsfeed" }'>
			    		<tr>
			    			<td class="wl_row">${entry.title.name}</td>
			    			<td>
								<div class="btn-group">
									  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									    Action
									    <span class="caret"></span>
									  </a>
									  <ul class="dropdown-menu">
									    <li><a tabindex="-1" href="${userWorksheet}?url=${link.href}&key=${key}"/>View Data</a></li>
									    <li><a tabindex="-1" href="${indexCreate}?url=${link.href}&key=${key}">Define Index</a></li>
									    <li class="divider"></li>
									    <li><a tabindex="-1" href="${userWorksheetData}?id=${index.indexId}">JSON</a></li>
									  </ul>
									</div>
							</td>
			    		
			    		</tr> 
			    		</c:if>  
			    				
		    		</c:forEach>
				</c:forEach>
			</table>
		</div>
	</div>

</div>

</body>
</html>
