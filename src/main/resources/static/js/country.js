let operation = 0;//初始化
$(document).ready(function() {
	$("#detailContains").css("display", "none");
	// when click the create button, show the detailContains
	$("#selCreate").on('click', function() {
		// clear all input
		$(':input', '#frmDetail')
			.not(':button, :submit, :reset, :hidden')
			.val('');
		// show the detailContains
		$("#detailContains").css("display", "block");
		// hide the queryContainer
		$("#queryContainer").css("display", "none");
		operation = 1;//新建标识
	});

	// when click the update button, show the queryContainer
	$("#selUpdate, #selDelete").on('click', function() {
		// show the queryContainer
		$("#queryContainer").css("display", "block");
		// hide the detailContains
		$("#detailContains").css("display", "none");
		// set the form action for update
		$("#frmDetail").attr("action", "/UpdateCountry");
		if ($(this).attr("id") == "selUpdate") {
			operation = 2;//选择更新时标识为2
		} else {
			operation = 3;//选择删除时标识为3
		}
	});

	// when click the return button, hide the detailContains
	$("#returnBtn").on('click', function() {
		// show the queryContainer
		// $("#queryContainer").css("display", "block");
		// hide the detailContains
		$("#detailContains").css("display", "none");
	});

	$("#queryBtn").on('click', function() {
		// use ajax to post data to controller
		// recived the data from controller with json
		// show the data in the detailContains
		$.ajax({
			type: "POST",
			url: "/country/getCountry",        //  <- controller function name
			data: $("#frmSearch").serialize(),
			dataType: 'json',
			success: function(data) {
				$("#detailContains").css("display", "block");
				// show the data in the detailContains
				//更新时固定国家代码
				if (operation == 2){
				$("#cd").val(data.mstcountrycd).prop('readonly', true);
				$("#name").val(data.mstcountrynanme);
				}
				//删除时固定国家代码和国名
				if (operation == 3){
				$("#cd").val(data.mstcountrycd).prop('readonly', true);
				$("#name").val(data.mstcountrynanme).prop('readonly', true);
				}
				

			},
			error: function(e) {
				alert("error");
			}
		});
	});
	//updateBtn绑定事件，根据operation的值判断提交表单的URL
	$("#updateBtn").on('click', function() {
		var url = " ";
		if (operation == 3) {
			url = "/country/delete";
		} else if (operation == 2) {
			url = "/country/update";
		} else if (operation == 1) {
			url = "/country/create";
		} else {
			alert("operation error");
			return;
		}
		$.ajax({
			type: "POST",
			url: url,
			data: $("#frmDetail").serialize(),
			dataType: 'json',
			success: function(data) {
				if (data.status == 0) {
					alert(data.message);
					return;
				} else {
					alert("Data insert failed");
					return;
				}
			}, complete: function() {
				// 更新后，清空查询输入框，解除details框固定
				$("#frmDetail :input").val('');
				$("#cd").prop('readonly', false);
				$("#name").prop('readonly', false);
				
			}
		})
	});

});