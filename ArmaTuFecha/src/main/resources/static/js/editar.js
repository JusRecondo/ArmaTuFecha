$(document).ready(function() {
	
	$("#upload_widget_opener1").hide();
	$("#upload_widget_opener2").hide();
	$("#upload_widget_opener3").hide();

	$("#eliminar1").click(function() {
		
		$("#eliminar1").hide();
		$("#foto-preview1").remove();	
		$("#upload_widget_opener1").show();
	});
	
	$("#eliminar2").click(function() {
	
		$("#eliminar2").hide();
		$("#foto-preview2").remove();	
		$("#upload_widget_opener2").show();
	});
	
	$("#eliminar3").click(function() {
		
		$("#eliminar3").hide();
		$("#foto-preview3").remove();	
		$("#upload_widget_opener3").show();
	});

});