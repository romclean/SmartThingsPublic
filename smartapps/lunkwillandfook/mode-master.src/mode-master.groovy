/**
 *  Mode-Master
 *
 *  Copyright 2015 Jeremy Huckeba
 *
 */

import groovy.time.TimeCategory

definition(
    name: "Mode Master",
    namespace: "LunkwillAndFook",
    author: "Jeremy Huckeba",
    description: "When the mode changes to the trigger mode, automatically set the mode to the target mode.",
    category: "My Apps",
    iconUrl: "http://cdn.device-icons.smartthings.com/Kids/kids5-icn.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Kids/kids5-icn@2x.png",
    iconX3Url: "http://cdn.device-icons.smartthings.com/Kids/kids5-icn@3x.png")

preferences {
	page(name: "page1", title: "Welcome", uninstall: true, install: true) {
		section() {
			paragraph "Welcome. This app will let you configure on or more trigger modes and a target mode so that the mode is automatically set to the target whenever it is changed to one of the triggers." 
    		label title: "Assign a name", required: false
			input(name: "triggerModes", type: "mode", title: "When mode is changed to", multiple: true, required: true)
			input(name: "modeToSet", type: "mode", title: "Automatically change it to this mode", multiple: false, required: true)
        }
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(location, "mode", modeChangeHandler)
}

def modeChangeHandler(evt) {
	def currentMode = evt.value
	log.debug "Updated with mode: ${currentMode}"
	if(triggerModes.contains(currentMode)) {
		use(TimeCategory) {
			def after5Seconds = new Date() + 5.seconds
			runOnce(after5Seconds, updateModeHandler)
		}
	}
}

def updateModeHandler() {
	log.debug "Target mode is ${modeToSet}"
	location.setMode(modeToSet)
	log.debug "Mode automatically changed to ${modeToSet}"
}