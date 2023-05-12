package hu.vecsesiot.backend.email

class FaultNotificationTemplate(
	name: String, // user's name
	line: String, // line's name
) : Template("FaultNotification", "Bus broke down in your way")
