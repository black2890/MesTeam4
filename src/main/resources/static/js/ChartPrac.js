let initDate = new Date();
let charts = [];

// document의 최초 로드의 경우 1.공정 선택란 초기값과 2. 현재 일자 기준으로 Data 불러와 chart 비동기 구현
$(document).ready(function() {

    const labels = [["양배추 즙 생산량", "흑마늘 즙 생산량", "즙 CAPA 잔량"],
        ["매실 젤리스틱 생산량", "석류 젤리스틱 생산량", "젤리스틱 CAPA 잔량"]];
    const data = [ [300, 500, 5200], [300, 350, 3350]];    //response.data;
    const titles = ["즙", "젤리스틱"];

    for (let i = 0; i < titles.length; i++) {
        charts[i] = createMainChart(`mainChart-${i + 1}`, "doughnut", titles[i], labels[i], data[i]);
    }

});