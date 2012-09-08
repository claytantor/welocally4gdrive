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
<c:url value="/index/editform" var="indexEdit"/>
<c:url value="/index/save" var="indexSaveAction"/>
<c:set value="active" var="indexSidebarState" scope="request"/>
<html>
<jsp:include page="../head.jsp"/> 

<script type="text/javascript">
$(document).ready(function() {

	<c:if test="${not empty indexForm.indexId}">	
	var sfields = '${indexForm.searchFields}';
	var ssplit = sfields.split(',');	
	var mgr = new WELOCALLY_IndexFormManager();
	mgr.initCfg({
		saveEndpoint: '${indexSaveAction}',
		indexId:'${indexForm.indexId}',
		spreadsheetKey:'${spreadsheetKey}',
		worksheetFeed:'${worksheetFeed}',
		searchFields:ssplit,
		primaryKey:'${indexForm.primaryKey}',
		lat:'${indexForm.lat}',
		lng:'${indexForm.lng}'		
	});	
	</c:if>
	
	<c:if test="${empty indexForm.indexId}">
	var mgr = new WELOCALLY_IndexFormManager();
	mgr.initCfg({
		saveEndpoint: '${indexSaveAction}',
		indexId:'',
		spreadsheetKey:'${spreadsheetKey}',
		worksheetFeed:'${worksheetFeed}',
		searchFields:[],
		primaryKey:'',
		lat:'',
		lng:''		
	});		
	</c:if>

	var fieldsearchable = $('#fieldsearchable ol');
	mgr.makeDroplist(fieldsearchable,$(fieldsearchable).parent().attr('id'));

	var fieldpk = $('#fieldpk ol');
	mgr.makeDroplist(fieldpk,$(fieldpk).parent().attr('id'));
	
	var fieldlat = $('#fieldlat ol');
	mgr.makeDroplist(fieldlat,$(fieldlat).parent().attr('id'));
	
	var fieldlng = $('#fieldlng ol');
	mgr.makeDroplist(fieldlng,$(fieldlng).parent().attr('id'));
	
		
	$( "#fields" ).find('.dragfield').draggable({
		appendTo: "body",
		helper: "clone"
	});

	
	$( "ol.fdrop" ).droppable({
		activeClass: "ui-state-default",
		hoverClass: "ui-state-hover",
		accept: ":not(.ui-sortable-helper)",
		drop: function(event,ui){
			mgr.handleDrop(this,ui);
		}
	}).sortable({
		items: "li:not(.phold)",
		sort: function() {
			// gets added unintentionally by droppable interacting with sortable
			// using connectWithSortable fixes this, but doesn't allow you to customize active/hoverClass options
			$( this ).removeClass( "ui-state-default" );
			
		}
	});

	
	$( "#btnReset" ).click(function( event, ui ) {
		var cfg = {
				searchFields:[],
				primaryKey:'',
				lat:'',
				lng:''		
		};

		mgr.initCfg(cfg);
		
		var fieldsearchable = $('#fieldsearchable ol');
		mgr.makeDroplist(fieldsearchable,$(fieldsearchable).parent().attr('id'));

		var fieldpk = $('#fieldpk ol');
		mgr.makeDroplist(fieldpk,$(fieldpk).parent().attr('id'));
		
		var fieldlat = $('#fieldlat ol');
		mgr.makeDroplist(fieldlat,$(fieldlat).parent().attr('id'));
		
		var fieldlng = $('#fieldlng ol');
		mgr.makeDroplist(fieldlng,$(fieldlng).parent().attr('id'));

		return false;
	});

	$( "#btnSave" ).click(function( event, ui ) {
		$('#messageArea').hide();
		var model = mgr.makeSelectionModel( $('#selectionArea') );
		var messages = mgr.saveIndex( model);
		
		if(messages && messages.errors.length>0){
			var errorHtml = WELOCALLY_Util.ui.getErrorString(messages.errors);
			$('#messageArea').html('<strong>Some things need to be fixed before you can save.</strong>&nbsp;'+errorHtml);
			//class="alert alert-error"
			$('#messageArea').addClass( "alert alert-error" );
			$('#messageArea').alert();
			$('#messageArea').show('slow');			
		} else {
			
		}
			
		return false;
	});

	
});
</script>
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
			<c:if test="${empty indexForm.indexId}"><h2>Create New Index From Spreadsheet</h2></c:if>	    
		    <c:if test="${not empty indexForm.indexId}"><h2>Edit Index ${indexForm.indexId}</h2></c:if>	    
		    
		    <c:if test="${indexForm.indexExists}">
		    <div class="alert alert-block">
		    		<button type="button" class="close" data-dismiss="alert">×</button>
	   				<strong>An index was already found for this spreadsheet.</strong> There
	   				is only one index per worksheet so you want to include all searchable fields in your definition.  
	   				You can make changes to the index and republish it if you like.
	   				Drag available field items to the type of field it is, primary key, searchable, or location.
			</div>
			</c:if>
			<c:if test="${!indexForm.indexExists}">
		    <div class="alert alert-info">
		    		<button type="button" class="close" data-dismiss="alert">×</button>
	   				<strong>Define your index for the worksheet.</strong> Drag available field items to the type of field 
	   				it is, primary key, searchable, or location. An index is a repository for all the 
	   				rows in the spreadsheet. You can create many different searches from one worksheet index. There
	   				is only one index per worksheet so you want to include all searchable fields in your definition. 
			</div>
			<div style="display:none;" id="messageArea"></div>
			</c:if>
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

		    <%-- new form --%>
			<div>
		    	<div class="span3"><strong>Available Fields</strong></div>
	     		<div class="span3"><strong>Index Fields</strong></div>
	     		<div class="span3">&nbsp;</div>
			</div>
			<div>
		    	<div class="span3" id="fields">
		    			<c:forEach var="field" items="${fields}">
		    				<div class="dragfield">${field.inputValue}</div>
		    			</c:forEach>	    		
		    	</div>
		    	<div class="span4" style="display:inline" id="selectionArea">
		    		<div id="fieldpk" class="fieldbucket">
			    		<strong>Primary Key</strong>
			    		<ol class="fdrop"></ol>
					</div>
					<div id="fieldlat" class="fieldbucket">
						<strong>Latitude</strong>
			    		<ol class="fdrop"></ol>
					</div>
					<div id="fieldlng" class="fieldbucket">
						<strong>Longitude</strong>
			    		<ol class="fdrop"></ol>
					</div>											
					<div id="fieldsearchable" class="fieldbucket">
						<strong>Search Fields</strong>
			    		<ol class="fdrop"></ol>
					</div>

		    	</div>
		    	<div class="span3">
		    		<input type="text" name="name" id="name" value="${indexForm.name}">
					<a href="#" class="btn" id="btnReset">Reset</a>
					<a href="#" class="btn" id="btnSave">Save</a>
		    	</div>
	    	</div>		    
		    

		</div>
		
	</div>

</div>

</body>
</html>
