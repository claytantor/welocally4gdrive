<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:url value="/user/docs" var="userDocs"/>
<c:url value="/user/home" var="userHome"/>
<c:url value="/index/list" var="listIndexes"/>
<c:url value="/index.html" var="indexPage"/>
<html>
  <jsp:include page="head.jsp"/> 
  <body>
  	 <jsp:include page="usernav.jsp"/>

    <div class="container-fluid">
      <div class="row-fluid" style="margin-top: 45px;">      
        <div class="span12">
          <div class="hero-unit">  
          	<h1>Welocally For Google Drive</h1>
          	<div style="margin: 10px; padding: 10px;">        	          	
	            <p><img src="<c:url value='/images/welocally-drive-320.png' />" style="float:left">Welocally for Google Drive turns your spreadsheet data 
	            into search filtered maps and grids in minutes. Creating simple widgets that you can embed on your website is as easy as drag and drop. 
	            Make your cloud data a responsive HTML5 location aware mobile application you can share as a simple sharable URL with one click publishing.
	            </p>  
	            <div style="clear: both;"></div>
            </div>         
          </div>
          <div class="row-fluid">
            <div class="span3 offset1">
              <h2>Heading</h2>
              <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
              <p><a class="btn" href="#">View details &raquo;</a></p>
            </div><!--/span-->
            <div class="span3">
              <h2>Heading</h2>
              <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
              <p><a class="btn" href="#">View details &raquo;</a></p>
            </div><!--/span-->
            <div class="span3">
              <h2>Heading</h2>
              <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
              <p><a class="btn" href="#">View details &raquo;</a></p>
            </div><!--/span-->
          </div><!--/row-->
          
        </div><!--/span-->
      </div><!--/row-->

      <hr>

      <footer>
        <p>&copy; Company 2012</p>
      </footer>

    </div><!--/.fluid-container-->
  	  
  </body>
</html>