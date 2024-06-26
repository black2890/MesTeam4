function initDatepicker() {
    $("#datepicker").datepicker({
        maxDate: '0',
        showMonthAfterYear: true,
        showOn: "button",
        buttonImage: "../svg/calendar.svg",
        buttonImageOnly: true,
        dateFormat: 'yy-mm-dd',
        nextText: '다음 달',
        prevText: '이전 달',
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    });
}