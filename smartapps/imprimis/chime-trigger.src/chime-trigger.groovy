/**
 *  Chime
 *
 *  Copyright 2016 Jiri Culik
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Chime Trigger",
    namespace: "imprimis",
    author: "Jiri Culik",
    description: "Triggers a chime connected to a switch device, in response to an open/close event, to motion, or a switch state.",
    category: "Safety & Security",
    iconUrl: "http://www.imprimislc.com/st-icons/bell-80x80.png",
    iconX2Url: "http://www.imprimislc.com/st-icons/bell-175x175.png",
    iconX3Url: "http://www.imprimislc.com/st-icons/bell-300x300.png")


preferences {
	section("When any of the following devices trigger..."){
		input "contacts", "capability.contactSensor", title: "Contact Sensor?", required: false, multiple: true
		input "motions", "capability.motionSensor", title: "Motion Sensor?", required: false, multiple: true
		input "switches", "capability.switch", title: "Switch?", required: false, multiple: true
	}
	section("Then sound chime connected as..."){
		input "chime", "capability.switch", title: "Chime"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	subscribe()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	subscribe()
}

def subscribe() {
	if (contacts) {
		subscribe(contacts, "contact.open", triggerHandler)
	}
	if (motions) {
		subscribe(motions, "motion.active", triggerHandler)
	}
	if (switches) {
		subscribe(switches, "switch.on", triggerHandler)
	}
}

/*
 * 
 */
def triggerHandler(evt) {
	log.debug "trigger event: $evt.device $evt.value"

	// turn off the switch just in case if was left on
	chime.off()
    
	// turn wth switch on (which causes a positive edge triggered chime)
    chime.on()
    
	// turn the switch off again
    chime.off()
}
