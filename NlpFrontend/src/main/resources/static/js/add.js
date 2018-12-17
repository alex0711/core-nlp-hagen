$(document).ready(function() {

	$(".successarea").hide();
	
	$("#send").on("click", function() {
		var data = {
			title : $("#title").val(),
			comment : $("#comment").val(),
			commenter : $("#commenter").val()
		};

		$.ajax({
			url : "http://localhost:8080/guestbook/",
			method : "PUT",
			contentType : "application/JSON",
			data : JSON.stringify(data),
			success : function(data) {
				$(".successarea").show();
				$("form").hide();
			},
			error : function(data) {
				alert("Error");
			}

		});
	});

})