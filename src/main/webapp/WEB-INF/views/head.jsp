<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>welocally</title>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>		
		<script src="https://maps.google.com/maps/api/js?key=AIzaSyACXX0_pKBA6L0Z2ajyIvh5Bi8h9crGVlg&sensor=true&language=en"
	  		type="text/javascript"></script>
	
	  	<!-- welocally-developer scripts  -->	

		
<!--		third party -->		
		<script src="<c:url value='/js/bootstrap.min.js' />" type="text/javascript"></script> 	
		<script src="<c:url value='/js/mustache.js' />" type="text/javascript"></script>

<!--		welocally -->		
		<script src="<c:url value='/js/WELOCALLY_Util.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/WELOCALLY_IndexFormManager.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/geodb/WELOCALLY_GeodbRecord.js' />" type="text/javascript"></script> 		
		<script src="<c:url value='/js/geodb/WELOCALLY_GeodbRecordSearch.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/geodb/WELOCALLY_InfoBox.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/geodb/WELOCALLY_GeodbRecordsGrid.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/geodb/WELOCALLY_GeodbRecordsMap.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/geodb/jquery.wlGeodb.js' />" type="text/javascript"></script> 
		
		
<!--		styles -->
		
		<link href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/themes/smoothness/jquery-ui.css' rel='stylesheet' type='text/css'>
		<link href='http://fonts.googleapis.com/css?family=Ubuntu+Mono:400,700|Carme|Fjord+One' rel='stylesheet' type='text/css'>
		<link href="<c:url value='/css/bootstrap.min.css' />" rel="stylesheet">
		<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css">

<!--		geodb maps -->		
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places_multi.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places_place.css' />" type="text/css">
		 
		
		<link rel="shortcut icon" href="<c:url value='/images/we_16.png' />"/> 
		
		
	
	</head>

<c:set value="/images" var="imageUrl" scope="request"/>