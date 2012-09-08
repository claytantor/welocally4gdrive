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
<c:url value="/index/publish" var="indexPublish"/>
<c:url value="/index/editform" var="indexEdit"/>
<c:url value="/index/delete" var="indexDelete"/>
<c:url value="/search/createform" var="createSearch"/>
<c:url value="/user/docs" var="userDocs"/>
<c:url value="/user/home" var="userHome"/>
<c:set value="active" var="indexSidebarState" scope="request"/>
<html>
  <jsp:include page="../head.jsp"/> 
<body>
<jsp:include page="../usernav.jsp"/> 

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">  
      	<jsp:include page="../usersidebar.jsp"/>    
        
	    <div class="span9">
		<h1>Index List</h1>
		<table id="publisher_list" width="100%" class="table">
				<thead>
					<tr>
						<th>id</th>
						<th>name</th>
						<th>primaryKey</th>
						<th>searchFields</th>
						<th>lat</th>
						<th>lng</th>						
						<th>action</th>
						<th>status</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="index" items="${indexes}">
		    		<tr>
		    			<td class="wl_row">${index.indexId}</td>
		    			<td class="wl_row">${index.name}</td>
		    			<td class="wl_row"><div class="label label-info" style="margin:2px;">${index.primaryKey}</div></td>
		    			<td class="wl_row">
		    			<c:forEach var="field" items="${fn:split(index.searchFields, ',')}">
                        	<div class="label label-info" style="margin:2px;"><c:out value="${field}" /></div> 
                    	</c:forEach>
                    	</td>
		    			<td class="wl_row"><div class="label label-info" style="margin:2px;">${index.lat}</div></td>
		    			<td class="wl_row"><div class="label label-info" style="margin:2px;">${index.lng}</div></td>		    			
						<td>
							<div class="btn-group">
								  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								    Action
								    <span class="caret"></span>
								  </a>
								  <ul class="dropdown-menu">
								    <li><a tabindex="-1" href="${indexEdit}?id=${index.indexId}">Edit</a></li>
								    <li><a tabindex="-1" href="${indexPublish}?id=${index.indexId}">Publish</a></li>
								    <li><a tabindex="-1" href="${createSearch}?id=${index.indexId}">Create Search</a></li>
								    <li class="divider"></li>
								    <li><a tabindex="-1" href="${indexDelete}?id=${index.indexId}">Delete</a></li>
								  </ul>
								</div>
						</td>
						<td class="wl_row">${index.publishStatus}</td>
		    		</tr>
				</c:forEach>
				</tbody>
			</table>
			
			</div>	
		
		</div>

</div>

</body>
</html>
