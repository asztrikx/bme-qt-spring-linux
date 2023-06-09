QT       += core gui quick quickcontrols2 qml

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++17

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    driverprofilemanager.cpp \
    mapmanager.cpp \
    faultticketmanager.cpp \
    linemanager.cpp \
    loginmanager.cpp \
    main.cpp \
    networkmanager.cpp \
    timetablemanager.cpp

HEADERS += \
    driverprofilemanager.h \
    mapmanager.h \
    faultticketmanager.h \
    linemanager.h \
    loginmanager.h \
    networkmanager.h \
    timetablemanager.h

FORMS +=

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

RESOURCES += \
    resources.qrc

DISTFILES +=
