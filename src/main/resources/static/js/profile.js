/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
  (8) 구독자 정보 모달 닫기
 */

// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) { // 이벤트 정보를 받아서
	if ($(obj).text() === "구독취소") {
		$.ajax({
			type: "delete",
			url: "/api/subscribe/" + toUserId, // 누구를 구독하는지에 대한 번호, 누가라는 정보는 로그인한 사람의 세션 정보를 가져오면 된다.
			dataType: "json"
		}).done(res => {
			$(obj).text("구독하기");
			$(obj).toggleClass("blue");
		}).fail(error => {
			console.log("구독취소실패", error);				
		});
	} else {
		$.ajax({
			type: "post",
			url: "/api/subscribe/" + toUserId,
			dataType: "json"
		}).done(res => {
			$(obj).text("구독취소");
			$(obj).toggleClass("blue");
		}).fail(error => {
			console.log("구독하기실패", error);				
		});
	}
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) { // 현재 페이지의 주인 id를 받아와야 한다.
	$(".modal-subscribe").css("display", "flex");
	
	$.ajax({
		url: `/api/user/${pageUserId}/subscribe`,
		dateType: "json"
	}).done(res => {
		console.log(res.data);
		
		res.data.forEach((u)=>{
			let item = getSubscribeModalItem(u);
			$("#subscribeModalList").append(item);
		});
	}).fail(error => {
		console.log("구독정보 불러오기 오류", error);
	});
}

function getSubscribeModalItem(u) {
	let item = `<div class="subscribe__item" id="subscribeModalItem-${u.id}">
	<div class="subscribe__img">
		<img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/person.jpeg'" />
	</div>
	<div class="subscribe__text">
		<h2>${u.username}</h2>
	</div>
	<div class="subscribe__btn">`; // 백틱을 한개만 넣고 끊고
	
	if(!u.equalUserState) { // 같은 유저가 아닐 때 버튼이 만들어져야함.
		if(u.subscribeState) { // 구독한 상태
			item += `<button class="cta blue" onclick="toggleSubscribe(${u.id}, this)">구독취소</button>`;	
		} else { // 구독안한 상태
		item += `<button class="cta" onclick="toggleSubscribe(${u.id}, this)">구독하기</button>`;
		// 버튼의 text가 구독취소가 될지 구독하기 될지는 경우에 따라 다르다.
		}
	} 
	// 구독을 하든 구독취소를 하든 u.id가 나중에 필요하다.		
	item += `
	</div>
</div>`;
	console.log(item)
	return item;
}

// (3) 유저 프로파일 사진 변경 (완)
function profileImageUpload(pageUserId, principalId) {
	
	//console.log("pageUserId", pageUserId);
	//console.log("principalId", principalId);
	
	if(pageUserId != principalId) {
		alert("프로필 사진을 수정할 수 없는 유저입니다.")
		return;
	}
	
	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}

		//서버에 이미지파일을 전송
		let profileImageForm = $("#userProfileImageForm")[0];	//form 태그 자체를 읽어온다.
		//console.log(profileImageForm);
		
		//formData 객체를 이용하면 form 태그와 필드에 포함된 값들을 일련의 key/value 쌍으로 담을 수 있다.
		let formData = new FormData(profileImageForm);	//form 태그 내의 값들만 담긴다.
		//console.log(formData);
		
		//form에 encType을 설정하면 여기에다 적지 않아도 된다.
		$.ajax({
			type: "put",
			url: `/api/user/${principalId}/profileImageUrl`,
			data: formData,
			contentType: false,	//필수 : x-www-form-urlencoded로 파싱되는 것을 방지
			processData: false,	//필수 : contentType을 false로 줬을 때 QueryString 장도 설정됨 해제.
			encType: "multipart/form-data",
			dataType: "json" 
		}).done(res => {
			// 사진 전송 성공시 이미지 변경
			let reader = new FileReader();
			reader.onload = (e) => {
				$("#userProfileImage").attr("src", e.target.result);
			}
			reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
		}).fail(error => {
			console.log("오류", error);
		});
				
	});
}


// (4) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}


// (5) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

// (6) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

// (7) 구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}
