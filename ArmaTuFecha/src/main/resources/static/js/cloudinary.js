$(document).ready(function() {
	
	document.getElementById("upload_widget_opener").addEventListener("click",
			function() {
		console.log ('asdsdad'),
				cloudinary.openUploadWidget({
					cloud_name : 'drxc4iyfs',
					upload_preset : 'oj92lbou',
					sources: [ 'local', 'url', 'facebook', ]
				}, function(error, result) {
					debugger;
					var url = result[0].secure_url;
					console.log(error, result)
				});
			}, false);
	
});
