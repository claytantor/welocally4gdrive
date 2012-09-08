<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="active" var="searchesSidebarState" scope="request"/>
<c:url value="/images/ajax-loader.gif" var="ajaxLoader" scope="request"/>
<html>
  <jsp:include page="../head.jsp"/>
<script type="text/javascript">
$(document).ready(function() {
	//indexId=c26b3722e7e1&q=shopping&loc=40.721417_-73.988274&radiusKm=10
	$('#geoearchGrid').searchGrid({ 
		query: {
			indexId: '${search.index.indexId}',
			q: '${search.query}',
			loc: '${search.lat}_${search.lng}',
			radiusKm: ${search.radius}
		}
	});
});
</script>   
<body>
<jsp:include page="../usernav.jsp"/> 
<div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">        
        <jsp:include page="../usersidebar.jsp"/>    
	    <div class="span9">
		<h1>Search Grid</h1>
		<div><strong>${search.name}</strong> <span class="label label-info">${search.query}</span></div>
		<div id="geoearchGrid"><img src="${ajaxLoader}"><br/>&nbsp;Please wait, searching...</div>			
		</div>			
	</div>
</div>
</body>
</html>
