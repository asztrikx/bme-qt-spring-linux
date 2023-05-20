import QtQuick 2.15
import QtPositioning 5.15
import QtLocation 5.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent
    required property var lineId;
    required property var stopId;
    required property var currentCoo;

    signal network(var lineId2, var stopId2)

    onVisibleChanged: () => {
        if(!visible) {
            updater.running = false;
            return;
        }
        updater.running = true;
    }

    Timer {
        id: updater
        interval: 1500
        repeat: true
        running: false
        triggeredOnStart: true
        onTriggered: () => {
            // a bit overkill
            network(lineId, stopId)
        }
    }

    property var sections;
    property var stops;
    property var nextBus;
    property var brokenBuses;

    onSectionsChanged: () => {
        var coords = [];
        for (const section of sections._embedded.sections) {
            for (const sectionPoint of section.sectionPoints) {
                coords.push(sectionPoint);
            }
        }

        routePoly.path = coords;
    }

    onStopsChanged: () => {
        routeStops.clear();
        for (const stop of stops._embedded.stops) {
            if (stop.id === stopId) {
                location.coordinate = QtPositioning.coordinate(stop.coordinate.latitude, stop.coordinate.longitude)
            }
            routeStops.append(stop.coordinate);
        }
    }

    onNextBusChanged: () => {
        if (nextBus === "nodata") {
            nextBusItem.coordinate = QtPositioning.coordinate(-100, -100)
            return;
        }
        var coord = nextBus.coordinate;
        nextBusItem.coordinate = QtPositioning.coordinate(coord.latitude, coord.longitude)
    }

    onBrokenBusesChanged: () => {
        if (brokenBuses === "nodata") {
            brokenBusesModel.clear();
            return;
        }
        brokenBusesModel.clear();
        for (const brokenBus of brokenBuses) {
            var coord = brokenBus.coordinate;
            brokenBusesModel.append(QtPositioning.coordinate(coord.latitude, coord.longitude));
        }
    }

    Map {
        anchors.fill: parent
        plugin: mapPlugin
        center: QtPositioning.coordinate(currentCoo.latitude, currentCoo.longitude)
        zoomLevel: 14
        maximumZoomLevel: 15
        minimumZoomLevel: 13

        Plugin {
            id: mapPlugin
            name: "osm"
        }

        MapItemGroup {
            MapPolyline {
                id: routePoly
                line.width: 5
                line.color: "#009ee3"
            }

            MapItemView {
                model: ListModel {
                    id: routeStops
                }

                delegate: MapQuickItem {
                    coordinate: QtPositioning.coordinate(latitude, longitude)
                    anchorPoint: Qt.point(station.width / 2, station.height / 2)

                    sourceItem: Image {
                        id: station
                        smooth: true
                        width: 25
                        height: 25
                        source: "image/station.png"
                    }
                }
            }
        }

        MapQuickItem {
            id: nextBusItem
            anchorPoint: Qt.point(busImage.width / 2, busImage.height / 2)
            sourceItem: Image {
                id: busImage
                smooth: true
                width:25
                height:25
                source: "image/bus.png"
            }
            Behavior on coordinate {
                PropertyAnimation {
                    duration: 200
                    easing.type: Easing.Linear
                }
            }
        }
        MapQuickItem {
            id: location
            anchorPoint: Qt.point(locationImage.width / 2, locationImage.height / 2)
            sourceItem: Image {
                id: locationImage
                smooth: true
                width:25
                height:25
                source: "image/location.png"
            }
        }

        MapItemView {
            model: ListModel {
                id: brokenBusesModel
            }

            delegate: MapQuickItem {
                coordinate: QtPositioning.coordinate(latitude, longitude)
                anchorPoint: Qt.point(brokenImage.width / 2, brokenImage.height / 2)
                sourceItem: Image {
                    id: brokenImage
                    smooth: true
                    width:25
                    height:25
                    source: "image/broken.png"
                }
            }
        }
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
