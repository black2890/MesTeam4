document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        aspectRatio: 1.2,
        events: function (fetchInfo, successCallback, failureCallback) {
            axios.get('/api/workplans')
                .then(response => {
                    var events = response.data.map(workPlan => {
                        var color;
                        switch (workPlan.productId) {
                            case 1:
                                color = 'red';
                                break;
                            case 2:
                                color = 'green';
                                break;
                            case 3:
                                color = 'blue';
                                break;
                            case 4:
                                color = 'purple';
                                break;
                            default:
                                color = 'gray';
                        }
                        var startDate = new Date(workPlan.start);
                        var endDate = new Date(workPlan.end);
                        // endDate.setDate(endDate.getDate() + 1); // 종료일 설정

                        return {
                            title: workPlan.productName, // 제목을 제품명으로 설정
                            start: startDate,
                            end: endDate,
                            color: color,
                            display: 'block', // 바 형태로 표시
                            workPlanId: workPlan.workPlanId // workPlanId 추가
                        };
                    });
                    successCallback(events);
                })
                .catch(error => {
                    console.error('Error fetching work plans', error);
                    failureCallback(error);
                });
        },
        eventClick: function (info) {
            var workPlanId = info.event.extendedProps.workPlanId;
            fetchWorkOrders(workPlanId);
        },
    });
    calendar.render();
});