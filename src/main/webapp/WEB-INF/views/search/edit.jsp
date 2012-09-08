<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="/search/save" var="saveSearchAction"/>
<c:set value="active" var="searchesSidebarState" scope="request"/>
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
		    <h2>Search for Index ${searchForm.index.indexId}</h2>
		    <c:if test="${not empty errors}">
		    <div class="alert alert-error">
		    	<strong>Please correct errors found:</strong><br/>
		    	<ul>
		    	<c:forEach var="error" items="${errors}">
		    		<li>${error.defaultMessage}</li>
		    	</c:forEach>
		    	</ul>
		    </div>
		    </c:if>
		    <div>
				<form:form modelAttribute="searchForm" action="${saveSearchAction}" method="post" cssClass="form-horizontal">	
					<fieldset>		
						<legend>Search Info</legend>
						<form:hidden path="id" />
						<form:hidden path="version" />
						<form:hidden id="index-id" path="index.id" />
						<form:hidden id="publishStatus" path="publishStatus" />
						<div class="control-group">
							<form:label	for="name" path="name" cssErrorClass="error" cssClass="control-label">Name</form:label>
						    <div class="controls">
						    	<input type="text" id="name" value="${searchForm.name}" placeholder="Name" name="name"/> 
						    </div>
					  	</div>
					  	
					  	<div class="control-group">
							<form:label	for="query" path="query" cssErrorClass="error" cssClass="control-label">Query</form:label>
						    <div class="controls">
						    	<input type="text" id="query" name="query" value="${searchForm.query}" placeholder="Query"/> 
						    </div>
					  	</div>
					  	
					  	<div class="control-group">
							<label class="control-label">Location</label>
						    <div class="controls form-inline">						    	
						    	<input class="input-small" type="text" id="lat" name="lat" value="${searchForm.lat}" placeholder="Latitude"/>
						    	<input class="input-small" type="text" id="lng" name="lng" value="${searchForm.lng}" placeholder="Longitude"/> 
						    </div>
					  	</div>
					  	
					  	<div class="control-group">
							<form:label	for="radius" path="radius" cssErrorClass="error" cssClass="control-label">Radius (Km)</form:label>
						    <div class="controls form-inline">			    
						    	<form:select path="radius">
								<form:option value="0" label="Select" />
								<form:options items="${radiusChoices}" />
								</form:select>
						    </div>
					  	</div>			
					  	
					  	<div class="control-group">
						    <div class="controls">
						      <button type="submit" class="btn">Save</button>
						    </div>
						</div>
						  		
  																	
					</fieldset>

				</form:form>	    		    
		    </div>
		</div>
	</div>
</div>	

</body>
</html>
