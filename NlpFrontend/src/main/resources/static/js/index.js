var getBotResponse = function(messageText) {

	var data = {
		message : messageText,
	};

	$.ajax({
		url : "http://localhost:8082/api/v1/bot/",
		method : "GET",
		contentType : "application/JSON",
		data : data,
		success : function(data) {
			displayBotResponse(data.response);
		},
		error : function(data) {
			alert("Querying Bot failed");
		}

	});

};

var sendMessageToBot = function() {
	if ($("#messageText").val() === "" || $("#messageText").val() === undefined) {
		return;
	}

	var messageText = $("#messageText").val();
	var messageHTML = "";
	var date = new Date().toDateString();

	messageHTML = '<div class="outgoing_msg">' + '<div class="sent_msg">'
			+ '<p>' + messageText + '</p>' + '<span class="time_date">' + date
			+ '</span>' + '</div>' + '</div>';

	$("#messages").append(messageHTML);

    getBotResponse(messageText);
	
	var msgs = document.getElementById("messages");
	
	var shouldScroll = msgs.scrollTop + msgs.clientHeight === msgs.scrollHeight;
	
	if (!shouldScroll) {
		msgs.scrollTop = msgs.scrollHeight;		
	}

};

var displayBotResponse = function(message) {
	var messageText = message;

	if (messageText === "" || messageText === undefined) {
		messageText = "No Response returned"
	}

	var messageHTML = "";
	var date = new Date().toDateString();

	messageHTML = '<div class="incoming_msg">'
			+ '<div class="incoming_msg_img">'
			+ '<img src="/img/linux.png" alt="bot"> </div>'
			+ '<div class="received_msg">' + '<div class="received_withd_msg">'
			+ '<p>' + messageText + '</p>' + '<span class="time_date">' + date
			+ '</span>' + '</div>' + '</div>' + '</div>';

	$("#messages").append(messageHTML);

};

var showTab = function(tabIndex) {
	$("#content").empty();
	switch (tabIndex) {
	case 1:
		$('#content').load('/view/sentiment.html');
		break;
	case 2:
		$('#content').load('/view/stockSentiment.html');
		break;
	case 3:
		$('#content').load('/view/bot.html');
		break;
	case 4:
		$('#content').load('/view/ner.html');
		break;
	case 5:
		$('#content').load('/view/for.html');
		break;
	default:
		break;
	}
};

var showSentimentTab = function() {
	showTab(1);
}

var showStockSentimentTab = function() {
	showTab(2);
}

var showBotTab = function() {
	showTab(3);
}

var showNerTab = function() {
	showTab(4);
}

var showForTab = function() {
	showTab(5);
}

$(document).ready(function() {

	$("#sentimentNav").on("click", showSentimentTab);
	$("#stockSentimentNav").on("click", showStockSentimentTab);
	$("#nerNav").on("click", showNerTab);
	$("#forNav").on("click", showForTab);
	$("#botNav").on("click", showBotTab);

	showTab();
});

$(document).ready("#sendMessageButton").click(function() {

	$("#sendMessageButton").off("click").on("click", sendMessageToBot);

});
