<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:url value="/search/editform" var="searchEdit"/>
<c:url value="/search/grid" var="searchGrid"/>
<c:url value="/search/map" var="searchMap"/>
<c:set value="active" var="searchesSidebarState" scope="request"/>
<html>
  <jsp:include page="../head.jsp"/> 
<body>
<jsp:include page="../usernav.jsp"/> 

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">      
    
        <jsp:include page="../usersidebar.jsp"/>
    
	    <div class="span9">
		<h1>Search List</h1>
		<table id="publisher_list" width="100%" class="table">
				<thead>
					<tr>
						<th>name</th>
						<th>query</th>
						<th>location</th>
						<th>radius</th>
						<th>action</th>	
						<th>status</th>						
					</tr>
				</thead>
				<tbody>
				<c:forEach var="search" items="${searches}">
    			<tr>
	    			<td class="wl_row">${search.name}</td>
	    			<td class="wl_row">${search.query}</td>
	    			<td class="wl_row">${search.lat},${search.lng}</td>
	    			<td class="wl_row">${search.radius}</td>	    			
					<td>
						<div class="btn-group">
							  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							    Action
							    <span class="caret"></span>
							  </a>
							  <ul class="dropdown-menu">
							    <li><a tabindex="-1" href="${searchEdit}?id=${search.id}">Edit</a></li>
							    <li><a tabindex="-1" href="${indexPublish}?id=${index.indexId}">Publish</a></li>
							    <li><a tabindex="-1" href="${searchGrid}?id=${search.id}">Grid</a></li>
							    <li><a tabindex="-1" href="${searchMap}?id=${search.id}">Map</a></li>			   
							    <li class="divider"></li>
							    <li><a tabindex="-1" href="#">Delete</a></li>
							  </ul>
							</div>
					</td>
					<td class="wl_row">${search.publishStatus}</td>	    			
	    		</tr> 
    			</c:forEach>
				</tbody>
			</table>
			
			</div>	
		
		</div>

</div>

</body>
</html>
