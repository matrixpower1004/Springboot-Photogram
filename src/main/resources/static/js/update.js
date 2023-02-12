// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼태그 액션을 막기!!
	
	//form data는 사진과 일반 문자들을 섞어서 보낼 때 사용하고,
	//일반적인 문자 form data를 key/value 형태로 전송하려면 serialize를 사용한다. 
	let data = $("#profileUpdate").serialize();	//key=value 형식으로 받아올 때 사용

	// console.log(data);

	$.ajax({
		type: "put",
		url: `/api/user/${userId}`, // 백틱. 쌍따옴표 아님
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json"
	}).done(res => { // HttpStatus 상태코드 200번대
		console.log("성공", res);
		location.href = `/user/${userId}`;
	}).fail(error => { // HttpStatus 상태코드 200번대가 아닐 때
		// console.log(error);
		if(error.data == null) {
			alert(error.responseJSON.message);
		} else {
		alert(JSON.stringify(error.responseJSON.data));
		}
	});

}
