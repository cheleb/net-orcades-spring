<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Orcades Spring integration demo.</title>
<link rel="stylesheet" type="text/css" href="orcades.css" />
</head>
<body>
<%
	//Cypal studio is publishing the GWT module at the root of the
	// webapp application, while gwt mojo deploy module in it's folder...
	//
	boolean wtp = application.getResourceAsStream("/SampleModule.html") != null;
	String prefix = "";
	if (!wtp) {
		prefix = "springsample.SampleModule/";
	}
%>

<div class="title"><a href="http://www.orcades.net"><img
	src="img/banner_top.jpg" border="0" /></a></div>
<div class="title"><a href="<%=prefix%>SampleModule.html">Link
to this sample</a>:</div>


</body>
</html>