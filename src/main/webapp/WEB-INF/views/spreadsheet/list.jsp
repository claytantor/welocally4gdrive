<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/index/createForm" var="indexCreate"/>
<c:url value="/user/spreadsheet" var="viewSpreadsheet"/>
<c:set value="active" var="spreadsheetSidebarState"  scope="request"/>
<html>
  <jsp:include page="../head.jsp"/> 
<body>
<jsp:include page="../usernav.jsp"/> 

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;"> 
      	<jsp:include page="../usersidebar.jsp"/>      
 	    <div class="span9">
		<h1>Spreadsheet List</h1>
		<table id="publisher_list" width="100%" class="table">
				<thead>
					<tr>
						<th>id</th>
						<th>name</th>						
					</tr>
				</thead>
				<tbody>
				<c:forEach var="doc" items="${items}">
    			<tr>
	    			<td class="wl_row">${doc.id}</td>
	    			<td class="wl_row"><a href="${viewSpreadsheet}?key=${doc.id}">${doc.title}</a></td>	    			
	    		</tr> 
    			</c:forEach>
				</tbody>
			</table>
			
			</div>	
		
		</div>

</div>

</body>
</html>
