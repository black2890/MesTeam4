// 드롭다운 메뉴에서 검색 조건 변경 시 버튼 텍스트 업데이트 함수
function changeButtonText(text) {
    $("#dropdownMenuButton").text(text);

    // 검색 조건에 따라 입력 필드 및 검색 버튼 표시 여부 변경
    if (text === '제품명' || text === '고객명(업체)') {
        $("#searchInputGroup").show();
    } else {
        $("#searchInputGroup").hide();
    }
}

// 검색 버튼 클릭 시 동작할 함수
$("#button-addon2").click(function() {
    table.ajax.reload(); // DataTables를 다시 로드하여 새로운 검색 조건을 적용
});