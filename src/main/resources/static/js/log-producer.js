function getUsedTimePretty(nanos) {
    var ms = nanos * 1.0 / 1000000;
    if (ms < 1000) {
        return ms + "ms";
    }

    var s = ms / 1000;
    return s + "s";
}

function getSpeedPretty(progress) {
    var speed = progress.count * 1000000000.0 / progress.usedNanos;
    return speed.toFixed(2) + "";
}

function checkProgress(progressId) {
  $.get("/progress/" + progressId)
      .done(function (progress) {
          $("#progressIdSpan").html(progress.id);
          $("#progressBar")
              .css("width", progress.count * 100 / progress.totalExpected + "%")
              .html(progress.count + "/" + progress.totalExpected);
          $("#beginAt").html(progress.beginAt);
          $("#endAt").html(progress.endAt);
          $("#usedTime").html(getUsedTimePretty(progress.usedNanos));
          $("#speed").html(getSpeedPretty(progress));

          if (progress.running) {
              setTimeout(function () {
                  checkProgress(progressId);
              }, 1000);
          }
      });
};

$("#btnStart").click(function() {
    $.post(
        "/log-producer",
        {
            logPerSecond: $("#inputExpectSpeed").val(),
            total: $("#inputTotal").val(),
            length: $("#inputLength").val()
        },
        function (progressId) {
            checkProgress(progressId);
        });
});
