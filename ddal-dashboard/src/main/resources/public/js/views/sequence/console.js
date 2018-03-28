$(function () {
  /**
   * 连接ZK客户端
   */
  $("#create-connection").click(function () {
    var newAddress = $("#new-zk-address").val();
    if(!newAddress || "" == newAddress){
      alert("ZK地址不能为空");
      return;
    }
    $.ajax({
      type: 'GET',
      url: $.serviceUrl.gid.zookeeper.modify,
      cache: false,
      async: true,
      contentType: "application/json;charset=UTF-8",
      data: 'zkAddress='+newAddress,
      success: function (data) {
        if (data) {
          alert(data);
          loadZkInfo();
          loadCenters();
          $("#table-body").empty();
        }
      },
      error: function (data) {
        alert(data);
      }
    });
  })

  function loadZkInfo() {
    $("#info-server-string").text("");
    $("#info-time-start").text("");
    $("#info-time-duration").text("");
    $("#info-state").text("");
    $.ajax({
      type: 'GET',
      url: $.serviceUrl.gid.zookeeper.info,
      dataType: 'json',
      cache: false,
      async: true,
      contentType: "application/json;charset=UTF-8",
      success: function (data) {
        if (data) {
          $("#info-server-string").text(data.zkConnectString);
          $("#info-time-start").text(data.startTime);
          $("#info-time-duration").text(data.duration);
          $("#info-state").text(data.state);
        }
      },
      error: function (data) {
      }
    });
  }
  loadZkInfo();

  /**
   * 加载业务中心
   */
  function loadCenters(){
    $("#center-select").empty();
    $("#center-select").append("<option value='0'>请选择业务中心</option>");
    $.ajax({
      type: 'GET',
      url: $.serviceUrl.gid.centers,
      dataType: 'json',
      cache: false,
      async: true,
      contentType: "application/json;charset=UTF-8",
      success: function (data) {
        if (data && data.length > 0) {
          var index = 0;
          data.forEach(function (entry) {
            $("#center-select").append("<option value='"+(++index)+"'>"+entry+"</option>");
          })
        }
      },
      error: function (data) {
      }
    });
  }
  loadCenters();

  /**
   * 注册业中心选中事件
   */
  $("#center-select").change(function () {
    var selected = $("#center-select option:selected");
    var value = $(selected).val();
    var center = $(selected).text();
    if(value>0){
      loadSequenceTable(center);
    }
  });

  /**
   * 加载序列列表
   * @param center
   */
  function loadSequenceTable(center){
    $.ajax({
      type: 'GET',
      url: "/gid/"+center+"/list",
      dataType: 'json',
      cache: false,
      async: true,
      contentType: "application/json;charset=UTF-8",
      success: function (data) {
        if (data && data.length > 0) {
          var index = 1;
          var parent = $("#table-body");
          var template = $("tr[name=table-row-template]");

          $(parent).empty();
          data.forEach(function (entry) {
            buildRow(template, parent, index++, entry);
          })
        }
      },
      error: function (data) {
      }
    });
  }

  /**
   * 创建序列行
   * @param template
   * @param parent
   * @param index
   * @param sequence
   */
  function buildRow(template, parent, index, sequence) {
    var row = $(template).clone();
    $(row).removeAttr("name");
    $(row).find("td[name=table-row-id]").text(index);
    $(row).find("td[name=table-row-seq-name]").text(sequence.name);
    $(row).find("td[name=table-row-seq-now]").text(sequence.id);
    $(row).find("button[name=button-seq-reset]").click(function () {
      var msg = "确认重置？\n\n请确认！";
      if (confirm(msg)==false){
        return;
      };

      var center = $("#center-select option:selected").text();
      var name = $(row).find("td[name=table-row-seq-name]").text();
      var newValue = $(row).find("input[name=input-seq-new]").val();
      if(!newValue || "" == newValue){
        alert("重置值不能为空！");
        return;
      }
      $.ajax({
        type: 'POST',
        url: $.serviceUrl.gid.reset+center+"&"+name+"&"+newValue,
        dataType: 'json',
        cache: false,
        async: true,
        contentType: "application/json;charset=UTF-8",
        complete: function () {
          "正在重置序列...";
        },
        success: function (data) {
          if (data && data == true) {
            loadSequenceTable(center);
          }
        },
        error: function (data) {
          alert(data);
        }
      })
    });
    $(parent).append(row.show());
  }

   $("#downloadTemplate").click(function () {
        var url = "/gid/download";
        //var fileName = "sequenceTemplate.xls";
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        //form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", fileName));
        form.appendTo('body').submit().remove();
   })

    $("#uploadTemplate").click(function () {

        //如果文件为空
        if ($("#fileName").val()==null || $("#fileName").val()=='') {
            alert('请上传excel文件!');
            return;
        }

        //如果文件不是xls,提示输入正确的excel文件
        var str=$("#fileName").val();
        var ext1=str.lastIndexOf(".xls");
        var ext2=str.lastIndexOf(".xlsx");
        var len=str.length;
        if(str.substring(ext1,len)!='.xls' && str.substring(ext2,len)!=".xlsx"){
            alert('请上传正确的excel,后缀名为xls或.xlsx!');
            return;
        }

        $("#uploadForm").ajaxSubmit({
                type: "post",
                url: "/gid/resetAll",
                clearForm: true,
                success: function (data) {
                  if(data!=null) {
                      data=JSON.parse(data);
                      alert(data.respMsg);
                      data=null;
                  }
                },
                error: function (data) {
                  if(data!=null) {
                      data=JSON.parse(data);
                      alert(data.respMsg);
                      data=null;
                  }
                }
        });
    })
});