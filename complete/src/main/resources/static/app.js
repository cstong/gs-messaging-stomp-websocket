var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    $("#userId").val(new Date().getTime());
    console.log("userId : " + $("#userId").val());
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
        stompClient.connect({userId : $("#userId").val(), token : "aaaaaaaa"}, function (frame) {
            setConnected(true);
        console.log('Connected: ' + frame);
        // stompClient.subscribe('/app/init', function (greeting) {
        //     showGreeting(JSON.parse(greeting.body).content);
        // });
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });

        stompClient.subscribe('/user/' + $("#userId").val() +'/message', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendMsg() {

    stompClient.send("/app/message/" + $("#userId").val(), {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#sendMsg" ).click(function() { sendMsg(); });
});

