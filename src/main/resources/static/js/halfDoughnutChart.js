// 메소드로 Ajax 요청을 수행하는 함수
function fetchChartData(process, date) {
    $.ajax({
        url: '/api/createHalfDoughnutChart',
        method: 'POST',
        data: JSON.stringify({ process: process, date: date }),
        dataType: 'json',
        success: function(response) {
            const labels = response.labels;
            const data = response.data;
            const titles = ["가동률", "생산량", "불량률"];

            for (let i = 0; i < labels.length; i++) {
                if (charts[i]) {
                    updateChart(charts[i], labels[i], data[i], titles[i]);
                } else {
                    charts[i] = createHalfDoughnutChart(`halfDoughnut-chart-${i + 1}`, labels[i], data[i], titles[i]);
                }
            }
        },
        error: function(xhr, status, error) {
            console.error('Error while fetching data:', error);
        }
    });
}

// 차트 생성 메서드
function createHalfDoughnutChart(containerId, title, labels, data) {
    const ctx = document.getElementById(containerId).getContext('2d');  //ctx = 그래픽 컨텍스트
    const chartData = setHalfDoughnutChartData(labels, data);
    const chartOptions = setHalfDoughnutChartOptions(title);

    new Chart(ctx, {                                    // cavans Id에 차트 할당 및 생성
        type: 'doughnut',                               // 차트 타입 지정
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
        labels: labels,                                 // 데이터 범례 이름 => String / List<String>
        datasets: [{
            label: "24시간 대비 생산 시간 비중",
            backgroundColor: ["#87CEEB", "#6495ED"],    // View 차트 데이터 색상 지정
            data: data                                  // 데이터 값 => int / List<int>
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