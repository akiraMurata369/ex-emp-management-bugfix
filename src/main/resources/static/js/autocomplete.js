$(function () {
  $("#employeeName").on("input", function () {
    const query = $(this).val();
    if (query.length < 1) {
      //1文字未満（空文字）の場合は候補リストを非表示
      $("#autocomplete-list").empty();
      return;
    }

    $.ajax({
      url: "/employee/autocomplete",
      type: "GET",
      data: { term: query },
      success: function (data) {
        const list = $("#autocomplete-list");
        list.empty();
        if (data.length === 0) {
          list.append("<div>該当なし</div>");
          return;
        }
        data.forEach(function (name) {
          //候補リスト（文字列の配列）を1件ずつ div 要素にして表示
          const item = $("<div>").text(name);
          item.on("click", function () {
            $("#employeeName").val(name);
            list.empty();
          });
          list.append(item);
        });
      },
      error: function () {
        $("#autocomplete-list").empty();
      },
    });
  });

  // 入力欄外クリックでリスト非表示
  $(document).on("click", function (e) {
    if (!$(e.target).closest("#employeeName, #autocomplete-list").length) {
      $("#autocomplete-list").empty();
    }
  });
});
