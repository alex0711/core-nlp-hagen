$(document).ready( function() {

	var refresh = function() {
		$("#sentiment0").hide();
		$("#sentiment1").hide();
		$("#sentiment2").hide();
		$("#sentiment3").hide();

		if ($("#sentimentText").val() === ""
				|| $("#sentimentText").val() === undefined) {
			return;
		}

		var data = {
			text : $("#sentimentText").val(),
		};

		$.ajax({
					url : "http://localhost:8082/api/v1/sentiment/",
					method : "POST",
					contentType : "application/JSON",
					data : JSON.stringify(data),
					success : function(data) {
						document.getElementById("modalHead").innerHTML = "Result";
						element = document
								.getElementById("sentimentAlert");
						var modalText = "";
						switch (data.sentiment) {
						case 0:
							element.className = "alert alert-danger";
							modalText = "Very Negative";
							break;
						case 1:
							element.className = "alert alert-warning";
							modalText = "Negative";
							break;
						case 2:
							element.className = "alert alert-secondary";
							modalText = "Neutral";
							break;
						case 3:
							element.className = "alert alert-success";
							modalText = "Positive";
							break;
						case 4:
							element.className = "alert alert-success";
							modalText = "Very Positive";
							break;
						default:
							element.className = "alert";
							break;
						}

						document.getElementById("modalText").innerHTML = modalText;

						$("#modal").modal();
					},
					error : function(data) {
						document.getElementById("modalHead").innerHTML = "HTTP Request failed";
						document.getElementById("modalHead").innerHTML = "Service unavailable";
						$("#modal").modal();
					}

				});

	};

	refresh();

	$("#sentiment").on("click", refresh);
});