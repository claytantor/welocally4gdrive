<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:url value="/index/data.json" var="indexData"/>
<c:url value="/index/createform" var="indexCreate"/>
<c:url value="/user/docs" var="userDocs"/>
<c:url value="/user/home" var="userHome"/>
<c:url value="/user/spreadsheet" var="userSpreadsheet"/>
<html>
  <jsp:include page="../head.jsp"/> 
<body>
<jsp:include page="../usernav.jsp"/> 

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">         
        <jsp:include page="../usersidebar.jsp"/>
    
	    <div class="span9">
	    	<div>
			    <ul class="breadcrumb">
				  <li><a href="#">Home</a> <span class="divider">/</span></li>
				  <li><a href="#">Library</a> <span class="divider">/</span></li>
				  <li class="active">Data</li>
				</ul>				
	   		</div>	   		
	   		<div>
	   			<h1>${worksheet.name}</h1>
				<table width="100%" class="geogrid table">
					<tbody>
				    <c:forEach var="row" items="${worksheet.rows}">
				    	<tr>
				    		<c:forEach var="column" items="${row.columns}">
				    			<td>${column.cell.value}</td>   	
							</c:forEach>
				    	</tr>
				    </c:forEach>
	    			</tbody>
				</table>
			</div>	
		
		</div>	
   </div>

</div>

</body>
</html>
