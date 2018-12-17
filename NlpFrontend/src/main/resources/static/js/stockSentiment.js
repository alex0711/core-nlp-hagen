var getStockSentiment = function() {

	var selectedTickerSymbol = $("#stockSelect").val();

	var tickerSymbolText = document.getElementById('stockSelect').selectedOptions[0].text;

	var data = {
		tickerSymbol : tickerSymbolText,
	};

	$.ajax({
		url : "http://localhost:8082/api/v1/iex/sentiment/",
		method : "GET",
		contentType : "application/JSON",
		data : data,
		success : function(data) {
			document.getElementById("modalHead").innerHTML = "Result";
			element = document.getElementById("sentimentAlert");
			var modalText = "";
			switch (data.averageSentiment) {
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

			document.getElementById("modalSentText").innerHTML = "Average Sentiment is " + modalText + " on " + data.numNews + " News with "
			+ data.sumNewsLength + " Characters";
			document.getElementById("negNews").innerHTML = data.mostPositiveNews;
			document.getElementById("posNews").innerHTML = data.mostnegativeNews;

			$("#modal").modal();
		},
		error : function(data) {
			document.getElementById("modalHead").innerHTML = "HTTP Request failed";
			document.getElementById("modalHead").innerHTML = "Service unavailable";
			$("#modal").modal();
		}

	});

};

$(document).ready("#stockSelect").change(function() {

	$("#stockSelect").off("change").on("change", getStockSentiment);

});
