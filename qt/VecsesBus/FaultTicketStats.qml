import QtQuick 2.0
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent
    property int created: 0
    property int inProgress: 0
    property int resolved: 0

    function calculate() {
        return created + inProgress + resolved;
    }

    function invalidate() {
        diagram.requestPaint()
    }

    property color createdColor: "red"
    property color inProgressColor: "yellow"
    property color resolvedColor: "green"

    Canvas {
        id: diagram
        visible: (calculate() !== 0)
        anchors.fill: parent

        function angleForValue(value) {
            if (calculate() === 0) return 360
            return value / calculate() * 360
        }

        onPaint: {
            var context = getContext("2d")
            var centerX = width / 2
            var centerY = height / 2
            var radius = Math.min(centerX, centerY) * 0.9
            var startAngle = 0

            var angle = angleForValue(created)
            context.fillStyle = createdColor
            context.beginPath()
            context.moveTo(centerX, centerY)
            context.arc(centerX, centerY, radius, startAngle * Math.PI / 180, (startAngle + angle) * Math.PI / 180, false)
            context.lineTo(centerX, centerY)
            context.fill()
            context.stroke()
            startAngle += angle

            angle = angleForValue(inProgress)
            context.fillStyle = inProgressColor
            context.beginPath()
            context.moveTo(centerX, centerY)
            context.arc(centerX, centerY, radius, startAngle * Math.PI / 180, (startAngle + angle) * Math.PI / 180, false)
            context.lineTo(centerX, centerY)
            context.fill()
            context.stroke()
            startAngle += angle

            angle = angleForValue(resolved)
            context.fillStyle = resolvedColor
            context.beginPath()
            context.moveTo(centerX, centerY)
            context.arc(centerX, centerY, radius, startAngle * Math.PI / 180, (startAngle + angle) * Math.PI / 180, false)
            context.lineTo(centerX, centerY)
            context.fill()
            context.stroke()
            startAngle += angle
        }
    }

    ListView {
        visible: (calculate() !== 0)
        id: ticketList
        anchors.fill: parent
        anchors.leftMargin: 20
        anchors.topMargin: 20
        model: [{ title : "Created", value: created, color : createdColor },
                { title : "In Progress", value: inProgress, color : inProgressColor },
                { title : "Resolved", value: resolved, color : resolvedColor }]
        delegate: Rectangle {
            height: 30

            Row {
                height: parent.height
                width: parent.width
                spacing: 10

                Rectangle {
                    width: 20
                    height: 20
                    color: modelData.color
                    border.width: 1
                    border.color: "black"
                }

                Text {
                    id: legend
                    text: modelData.title + " (" + (modelData.value / calculate() * 100).toFixed(2) + "%)"
                }
            }
        }
    }

    Text {
        visible: (calculate() === 0)
        anchors.centerIn: parent
        text: "No failure ticket"
        font.pixelSize: 20
    }

    Button{
        anchors.bottomMargin: 50
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.bottom: parent.bottom
        text: "Back"
        font.pixelSize: 16
        onClicked: onBack()
    }
}
