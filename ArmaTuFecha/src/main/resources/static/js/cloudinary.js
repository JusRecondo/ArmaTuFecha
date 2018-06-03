$(document).ready(function() {

	$("#upload_widget_opener1").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			
			folder : 'perfiles_locales',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)			 
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;

			$("form").find('#foto1').val(url);
			$("#preview1").append( '<img src=\"' + thumbnail + '\" />' );
			

		})
	})
	$("#upload_widget_opener2").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			max_files : 5,
			folder : 'perfiles_locales',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;
			
			$("form").find('#foto2').val(url);
			$("#preview2").append( '<img src=\"' + thumbnail + '\" />' );

		})
	})
	$("#upload_widget_opener3").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			max_files : 5,
			folder : 'perfiles_locales',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;
			$("form").find('#foto3').val(url);
			$("#preview3").append( '<img src=\"' + thumbnail + '\" />' );

		})
	})

	$("#upload_widget_opener4").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			max_files : 5,
			folder : 'perfiles_musicos',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;
			$("form").find('#foto4').val(url);
			$("#preview4").append( '<img src=\"' + thumbnail + '\" />' );

		})
	})

	$("#upload_widget_opener5").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			folder : 'perfiles_musicos',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;
			$("form").find('#foto5').val(url);
			$("#preview5").append( '<img src=\"' + thumbnail + '\" />' );

		})
	})
	$("#upload_widget_opener6").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'oj92lbou',
			cloud_name : 'drxc4iyfs',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			folder : 'perfiles_musicos',
			sources : [ 'local', 'url', 'facebook' ],
		}, function(error, result) {
			console.log(error, result)
			var url = result[0].url;
			var thumbnail = result[0].thumbnail_url;
			$("form").find('#foto6').val(url);
			$("#preview6").append( '<img src=\"' + thumbnail + '\" />' );

		})
	})
	

})

