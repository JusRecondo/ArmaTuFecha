
$(document).ready(function() {
	
	$("#formulario-local").hide();
	$("#formulario-musico").hide();
	$("#continua").hide();
	
	
    $("#tipo-local").change(function () {
    	console.log("asdasd");
    	if($(this).is(':checked')){
    		$("#formulario-local").show(); 		
    		$("#formulario-musico").hide();
    		$("#continua").show();
    	}
    });
    
    $("#tipo-musico").change(function () {
    	console.log("qweqwe");
    	if($(this).is(':checked')){
    		$("#formulario-musico").show(); 		
    		$("#formulario-local").hide(); 
    		$("#continua").show();
    	}
    });

   $("form").submit( function(){
	   $(this).find(".mail").val($("#mail").val());
	   $(this).find(".contrasenia").val($("#contrasenia").val());
	   $(this).find(".contrasenia2").val($("#contrasenia2").val());
   
	   
   });


} );
