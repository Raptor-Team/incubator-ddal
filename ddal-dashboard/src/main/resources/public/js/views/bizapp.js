$(function () {
  // set breadcrumb title
  $("li.breadcrumb-title").html("应用管理");

  $("#ddal-app-center").selectpicker().ajaxSelectPicker({
    ajax: {
      contentType: "application/json;charset=UTF-8",
      url: $.serviceUrl.appCenter.list,
      type: 'POST',
      dataType: 'json',
      // Use "{{{q}}}" as a placeholder and Ajax Bootstrap Select will
      // automatically replace it with the value of the search query.
      data: {'name': '{{{q}}}'}
    },
    locale: {
      emptyTitle: '选择业务中心'
    },
    log: 3,
    preprocessData: function (data) {
      var i, l = data.length, array = [];
      if (l) {
        for (i = 0; i < l; i++) {
          array.push($.extend(true, data[i], {
            text: data[i].name,
            value: data[i].code,
            data: {
              subtext: data[i].code
            }
          }));
        }
      }
      // You must always return a valid array when processing data. The
      // data argument passed is a clone and cannot be modified directly.
      return array;
    }
  });

  $("#bizapp-btn-submit").click(function() {
    if(! requiredCheck()) {
      return;
    }
  });

  function requiredCheck() {
    var $passed = true;
    $("input.required").each(function () {
      if (!$(this).val()) {
        var $this = $(this);
        var $pDiv = $("#" + $this.data("pdiv"));
        if ($pDiv) {
          $pDiv.addClass("has-danger");
        }
        $this.addClass("form-control form-control-danger");
        $passed = false;
      }
    });
    return $passed;
  }
  // $('select').trigger('change');
});