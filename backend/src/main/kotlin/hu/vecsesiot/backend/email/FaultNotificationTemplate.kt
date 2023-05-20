package hu.vecsesiot.backend.email

class FaultNotificationTemplate(
	val name: String, // user's name
	val line: String, // line's name
) : Template("FaultNotification", "Bus broke down in your way")
