// 메소드로 Ajax 요청을 수행하는 함수
function fetchChartData(process, date) {

    $.ajax({
        url: '/api/createChart',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ processText: process, searchDate: date }),
        dataType: 'json',
        success: function(response) {
            const labels = [["가동 시간", "비가동 시간"],
                ["매실 젤리스틱", "석류 젤리스틱", "양배추 즙", "흑마늘 즙"],
                ["매실 젤리스틱", "석류 젤리스틱", "양배추 즙", "흑마늘 즙"]];
            const data = response.data;
            const titles = ["가동률", "생산량", "불량률"];

            for (let i = 0; i < titles.length; i++) {
                if (charts[i]) {
                    updateChart(charts[i], labels[i], data[i], titles[i]);
                } else {
                    if(i===0){
                        charts[i] = createChart(`chart-${i + 1}`, "doughnut", labels[i], data[i], titles[i]);
                    } else {
                        charts[i] = createChart(`chart-${i + 1}`, "bar", labels[i], data[i], titles[i]);
                    }
                }
            }
        },
        error: function(xhr, status, error) {
            console.log(data);
            console.error('fetch에러 발생', error);
        }
    });
}

// 차트 생성 메서드
function createChart(containerId, type, title, labels, data) {
    const ctx = document.getElementById(containerId).getContext('2d');  //ctx = 그래픽 컨텍스트
    const chartData = setHalfDoughnutChartData(labels, data);
    const chartOptions = setHalfDoughnutChartOptions(title);

    new Chart(ctx, {                                    // cavans Id에 차트 할당 및 생성
        type: type,                                      // 차트 타입 지정
        data: chartData,                                // 차트 데이터 지정
        options: chartOptions                           // 차트 설정 지정
    });
}

// 차트 업데이트 메서드
function updateChart(chart, labels, data, title) {
    chart.data.labels = labels;
    chart.data.datasets[0].data = data;
    chart.options.title.text = title;
    chart.update();
}

// 차트 데이터 설정 메서드
function setHalfDoughnutChartData(labels, data) {
    return {
        labels: labels,                                 // 데이터 범례 이름 => String / List<String> 1. 가동률 [ 설비 가동 시간 / 설비 비가동 시간 ] 2. [ 흑마늘즙 / 양배추즙 / 매실 젤리스틱 / 석류 젤리스틱 ] 3. [ 수율, 불량률 ]
        datasets: [{
            label: "24시간 대비 생산 시간 비중",
            backgroundColor: ["#87CEEB", "#6495ED"],    // View 차트 데이터 색상 지정
            data: data                                  // 데이터 값 => int / List<int> 1. 가동률 [ 설비 공정 시간 / 24 - 설비 공정 시간 ], 2. [ 흑마늘즙/ 양배추즙/ 매실 젤리스틱/ 석류 젤리스틱 생산량 ] 3. [ 작업계획량-재고 입고내역/작업계획량]
            // 1. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 공정 소요 시간 합연산
            // 2. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 제품별 생산량 공정에 따라 계산
            // 3. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 제품별 불량률 공정에 따라 계산
        }]
    };
}

// 차트 옵션 설정 메서드
function setHalfDoughnutChartOptions(title) {
    return {
        rotation: 1 * Math.PI,                          // 차트 시작 지점 설정
        circumference: 1 * Math.PI,                     // 차트 들레 설정
        title: {                                        // 차트 제목 설정
            display: true,                              // 차트 제목 표시 유무
            text: title                                 // 차트 제목 텍스트 설정
        }
    };
}
/*****************************************************************************************************************/

function fetchMainChartData() {

    $.ajax({
        url: '/api/createMainChart',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ searchDate: date }),
        dataType: 'json',
        success: function(response) {
            const labels = [["양배추 즙 생산량", "흑마늘 즙 생산량", "즙 CAPA 잔량"],
                ["매실 젤리스틱 생산량", "석류 젤리스틱 생산량", "젤리스틱 CAPA 잔량"]];
            const data = [ [300, 500, 5200], [300, 350, 3350]];    //response.data;
            const titles = ["즙", "젤리스틱"];

            for (let i = 0; i < titles.length; i++) {
                charts[i] = createMainChart(`mainChart-${i + 1}`, "doughnut", titles[i], labels[i], data[i]);
            }

        },
        error: function(xhr, status, error) {
            console.error('fetch error occurred', error);
            console.error('status:', status);
            console.error('xhr:', xhr);
        }
    });
}

// 차트 생성 메서드
function createMainChart(containerId, type, title, label, data) {
    const ctx = document.getElementById(containerId).getContext('2d');  //ctx = 그래픽 컨텍스트
    const chartData = setMainChartData(label, data);
    const chartOptions = setMainChartOptions(title);

    new Chart(ctx, {                                    // cavans Id에 차트 할당 및 생성
        type: type,                                      // 차트 타입 지정
        data: chartData,                                // 차트 데이터 지정
        options: chartOptions                           // 차트 설정 지정
    });
}

// 차트 데이터 설정 메서드
function setMainChartData(label, data) {
    return {
        labels: label,                                 // 데이터 범례 이름 => String / List<String> 1. 가동률 [ 설비 가동 시간 / 설비 비가동 시간 ] 2. [ 흑마늘즙 / 양배추즙 / 매실 젤리스틱 / 석류 젤리스틱 ] 3. [ 수율, 불량률 ]
        datasets: [{
            label: "즙 월별 CAPA 대비 생산량",
            backgroundColor: ["#87CEEB", "#6495ED"],    // View 차트 데이터 색상 지정
            data: data                                  // 데이터 값 => int / List<int> 1. 가동률 [ 설비 공정 시간 / 24 - 설비 공정 시간 ], 2. [ 흑마늘즙/ 양배추즙/ 매실 젤리스틱/ 석류 젤리스틱 생산량 ] 3. [ 작업계획량-재고 입고내역/작업계획량]
            // 1. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 공정 소요 시간 합연산
            // 2. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 제품별 생산량 공정에 따라 계산
            // 3. 작업 지시에서 공정 종료일이 searchDate와 같은 경우 + 공정명에 해당하는 제품별 불량률 공정에 따라 계산
        }]
    };
}

// 차트 옵션 설정 메서드
function setMainChartOptions(title) {
    return {
        maintainAspectRatio: false,
        title: {                                        // 차트 제목 설정
            display: true,                              // 차트 제목 표시 유무
            text: title                                 // 차트 제목 텍스트 설정
        }
    };
}