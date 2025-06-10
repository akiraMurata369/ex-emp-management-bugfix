"use strict";
$(() => {
  $("#get_address_btn").on("click", () => {
    $.ajax({
      url: "https://zipcoda.net/api",
      type: "GET",
      dataType: "json",
      data: {
        zipcode: $("#zipCode").val(), // ここを修正
      },
      async: true,
    })
      .done((data) => {
        console.log(data);
        console.dir(JSON.stringify(data));
        $("#address").val(data.items[0].address);
      })
      .fail((XMLHttpRequest, textStatus, errorThrown) => {
        alert("正しい結果を得られませんでした");
        console.log("XMLHttpRequest : " + XMLHttpRequest.status);
        console.log("textStatus", textStatus);
        console.log("errorThrown : " + errorThrown?.message);
      });
  });
});
