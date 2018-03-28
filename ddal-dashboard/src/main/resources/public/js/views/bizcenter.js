$(function () {
  // set breadcrumb title
  $("li.breadcrumb-title").html("中心管理");

  function clearCreateInputs() {
    $("input[name*='ddal-center-']").val('');
  }

  $("button#bizcenter-btn-reset").click(function () {
    clearCreateInputs();
  });

  $("input[name*='ddal-center-']").keyup(function (event) {
    var $this = $(this);
    var $pDiv = $("#" + $this.data("pdiv"));
    if ($this.val() && $this.val().trim()) {
      if ($pDiv) {
        $pDiv.removeClass("has-danger").addClass("has-success");
        $this.removeClass('form-control-danger').addClass(
            "form-control form-control-success");
      }
    } else {
      if ($pDiv) {
        $pDiv.removeClass('has-success').addClass("has-danger");
        $this.removeClass('form-control-success').addClass(
            "form-control form-control-danger");
      }
    }
  });

  function resetInputWarning() {
    $("input[name*='ddal-center-']").each(function () {
      var $this = $(this);
      var $pDiv = $("#" + $this.data("pdiv"));
      $pDiv.removeClass("has-danger has-success");
      $this.removeClass("form-control-danger form-control-success")
    });
  }

  $("button#bizcenter-btn-submit").click(function () {
    if (!requiredCheck()) {
      return;
    }
    var $data = {
      "name": $("#ddal-center-name").val(),
      "code": $("#ddal-center-code").val(),
      "description": $("#ddal-center-desc").val()
    };
    $.ajax({
      type: 'POST',
      url: $.serviceUrl.appCenter.create,
      dataType: 'json',
      cache: false,
      async: true,
      data: JSON.stringify($data),
      contentType: "application/json;charset=UTF-8",
      success: function (data) {
        notie.alert({
          type: 1,
          text: "业务中心 [" + data.name + "] 创建成功！",
          stay: false,
          time: 3,
          position: "top"
        });
        clearCreateInputs();
        resetInputWarning();
        // load first page
        loadCenterList(1);
      },
      error: function (data) {
        if (data) {
          notie.alert({
            type: 3,
            text: data.responseJSON.message,
            stay: false,
            time: 3,
            position: "top"
          });
        }
      }
    });
  });

  function bindPagingBtn() {
    $("a.center-list-nav").click(function () {
      var $this = $(this);
      var navType = $this.data("paging-type");
      if (navType && navType === "pre") {
        timeoutLoading(500, function () {
          $("#center-list-table-container").html($.loadingAvg);
          $("#center-list-pagination").html('');
        }, loadCenterList, [$this.data("paging-number")]);
        // loadCenterList($this.data("paging-number"));
      } else if (navType && navType === "next") {
        timeoutLoading(500, function () {
          $("#center-list-table-container").html($.loadingAvg);
          $("#center-list-pagination").html('');
        }, loadCenterList, [parseInt($this.data("paging-number")) + 2]);
        // loadCenterList(parseInt($this.data("paging-number")) + 2);
      }
    });
  }

  function deleteAppCenter(id, success, fail) {
    $.ajax({
      type: 'DELETE',
      url: $.serviceUrl.appCenter.delete + id,
      // dataType: 'json',
      cache: false,
      async: true,
      // contentType: "application/json;charset=UTF-8",
      success: function (data) {
        success();
      },
      error: function (data) {
        fail();
      }
    });
  }

  function bindAppCenterListOp() {
    $("button.btn-del-center").click(function () {
      var $this = $(this);
      var $centerName = $this.data("center-name");
      var $centerId = $this.data("center-id");
      swal({
        title: "",
        text: "请确认是否删除业务中心！",
        type: "warning",
        showCancelButton: true,
        cancelButtonText: "取消",
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确认",
        showLoaderOnConfirm: true,
        preConfirm: function () {
          return new Promise(function (resolve, reject) {
            setTimeout(function () {
              deleteAppCenter($centerId, function() {
                resolve();
              }, function() {
                reject("删除业务中心失败！联系Raptor Team解决！");
              });
            }, 800)
          })
        }
      }).then(function () {
        notie.alert({
          type: 1,
          text: "业务中心 [" + $centerName + "] 已删除！",
          stay: false,
          time: 3,
          position: "top"
        });
        loadCenterList($curAppCenterPageNum);
      }).catch(swal.noop);
    });
  }

  var $centerTableTpl = $("#center-list-table-row").html();
  var $paginationNavTpl = $("#center-list-nav").html();
  var $curAppCenterPageNum = 1;

  function loadCenterList(page) {
    if (!page) {
      page = 1;
    }
    $curAppCenterPageNum = page;
    $.ajax({
      type: 'GET',
      url: $.serviceUrl.appCenter.paginationQuery + "?page=" + page,
      dataType: 'json',
      cache: false,
      async: true,
      contentType: "application/json;charset=UTF-8",
      success: function (data) {
        // 表格数据
        var centerList = [];
        if (data && data.content) {
          data.content.forEach(function (entry) {
            entry.registerTime = new Date(entry.registerTime).toLocaleString();
            centerList.push(entry);
          })
        }
        $("#center-list-table-container").html(
            juicer($centerTableTpl, {"centerList": centerList}));

        // 分页
        $("#center-list-pagination").html(juicer($paginationNavTpl, data));
        bindPagingBtn();
        bindAppCenterListOp();
      },
      error: function (data) {
      }
    });
  }

  // page init func call
  timeoutLoading(500, function () {
    $("#center-list-table-container").html($.loadingAvg);
    $("#center-list-pagination").html('');
  }, loadCenterList, [1]);

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

});