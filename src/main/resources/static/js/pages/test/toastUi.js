document.addEventListener('DOMContentLoaded', () => {

    const container = document.getElementById('tui-date-picker-container');
    const target = document.getElementById('tui-date-picker-target');

    const datePicker = new tui.DatePicker(container, {
        input: {
            element: target,
            format: 'yyyy-MM-dd', // 날짜 포맷
        },
        date: new Date(), // 기본 날짜
        language: 'ko', // 한글 설정
        showJumpButtons: false,
    });

    // 선택된 날짜 가져오기
    datePicker.on('change', () => {
        console.log(`Selected date: ${datePicker.getDate()}`);
    });

    const rangePicker = tui.DatePicker.createRangePicker({
        startpicker: {
            input: '#startpicker-input',
            container: '#startpicker-container',
            date: new Date(),
        },
        endpicker: {
            input: '#endpicker-input',
            container: '#endpicker-container',
            date: new Date(),
        },
        type: 'date', // 선택 유형 (date, month, year)
        format: 'yyyy-MM-dd',
        timePicker: false,
        language: 'ko',
        selectableRanges: [
            [new Date(2023, 0, 1), new Date(2025, 11, 31)]
        ]
    });

    rangePicker.on('change:end', () => {
        alert(`${rangePicker.getStartDate()} - ${rangePicker.getEndDate()}`);
    });

    const timepicker = new tui.TimePicker('#timepicker-container', {
        initialHour: 15,
        initialMinute: 13,
        inputType: 'spinbox', // 'selectBox' or 'spinbox'
        showMeridiem: false,
    });

    const dateTimePicker = new tui.DatePicker('#timepicker-container', {
        date: new Date(),
        language: 'ko',
        input: {
            element: '#datetime-picker-input',
            format: 'yyyy-MM-dd HH:mm A'
        },
        timePicker: timepicker,
    });
});