import QtQuick 2.15
import QtPositioning 5.15
import QtLocation 5.15
import QtGraphicalEffects 1.0

Item {
    anchors.fill: parent
    required property var lineId;
    required property var stopId;

    signal network(var lineId2, var stopId2)
    Timer {
        interval: 500
        running: true
        repeat: false
        onTriggered: () => {
            network(1, 1);
        }
    }

    property var stops;
    property var nextBus;
    property var brokenBuses;

    onStopsChanged: () => {
        var coords = [];
        for (const stop of stops._embedded.stops) {
            coords.push(stop.coordinate);

            if (stop.id === stopId) {
                location.coordinate = QtPositioning.coordinate(stop.coordinate.latitude, stop.coordinate.longitude)
            }
        }

        routePoly.path = coords;

        for (const coord of coords) {
            routeStops.append(coord);
        }
    }

    onNextBusChanged: () => {
        // TODO check for bad data
        var coord = nextBus.coordinate;
        nextBus.coordinate = QtPositioning.coordinate(coord.latitude, coord.longitude)
    }

    onBrokenBusesChanged: () => {
        // TODO check for bad data
        for (const brokenBus of brokenBuses) {
            var coord = brokenBus.coordinate;
            brokenBusesModel.append(QtPositioning.coordinate(coord.latitude, coord.longitude));
        }
    }

    Map {
        anchors.fill: parent
        plugin: mapPlugin
        center: QtPositioning.coordinate(47.41, 19.26)
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
            id: nextBus
            anchorPoint: Qt.point(busImage.width / 2, busImage.height / 2)
            sourceItem: Image {
                id: busImage
                smooth: true
                width:25
                height:25
                source: "image/bus.png"
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
}
