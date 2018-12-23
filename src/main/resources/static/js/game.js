$(function () {

  var $allButtons = $('.btn');
  var url         = 'ws://'
      + (window.location.hostname ? window.location.hostname : 'localhost')
      + (window.location.port ? ':' + window.location.port : '')
      + '/game';
  var $message    = $('#message');
  var $pits       = [
    $('#pit0'), $('#pit1'), $('#pit2'), $('#pit3'), $('#pit4'), $('#pit5'), $('#pit6'),
    $('#pit7'), $('#pit8'), $('#pit9'), $('#pit10'), $('#pit11'), $('#pit12'), $('#pit13')
  ];
  var $buttons    = [$('#btn0'), $('#btn1'), $('#btn2'), $('#btn3'), $('#btn4'), $('#btn5')];
  var amISecond   = null;
  var ws          = new WebSocket(url);

  // functions ---------------------------------------------------------------------------------------------------------

  function enableAll() {
    $allButtons.prop('disabled', false);
  }

  function disableAll() {
    $allButtons.prop('disabled', true);
  }

  function updateBoard(board, isMyTurn) {

    for (var i = 0; i < board.pits.length; i++) {
      var value = board.pits[(i + (amISecond ? 7 : 0)) % 14];
      $pits[i].html(value);

      if (isMyTurn && i < 6 && value === 0) {
        $buttons[i].prop('disabled', true);
      }
    }
  }

  // main section ------------------------------------------------------------------------------------------------------

  ws.onopen = function () {

    console.log('Connection successfully opened');

    $message.html('Waiting for other player.');
  };

  ws.onmessage = function (msg) {

    var data = JSON.parse(msg.data);

    console.log('Message received', data);

    var board    = data.board;
    var isMyTurn = data.connectedUser === board.turn;

    if (board.state === 'OVER') {
      disableAll();

      if (board.winner === -1) {
        $message.html('Draw, try again! (Score: ' + board.pits[6] + ')');
      } else {
        var score = data.connectedUser === 0 ? board.pits[6] : board.pits[13];

        if (data.connectedUser === board.winner) {
          $message.html('You win! (Score: ' + score + ')');
        } else {
          $message.html('You lost! (Score: ' + score + ')');
        }
      }
    } else {
      isMyTurn ? enableAll() : disableAll();

      if (board.state === 'STARTED') {
        amISecond = data.connectedUser === 1;
        amISecond ? $message.html('Welcome player 2 !') : $message.html('Welcome player 1 !');
      }
    }

    updateBoard(board, isMyTurn);
  };

  ws.onclose = function () {

    console.log('Connection was closed.');

    disableAll();
    $message.html('Waiting for players!!!');
  };

  ws.error = function (err) {

    console.error(err);

    disableAll();
    $message.html('Waiting for players!!!');
  };

  window.select = function (pitId) {

    disableAll();
    ws.send(pitId);
  }
});
