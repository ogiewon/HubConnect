/*
 *	Copyright 2019 Steve White
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *	use this file except in compliance with the License. You may obtain a copy
 *	of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *	License for the specific language governing permissions and limitations
 *	under the License.
 *
 *
 */
metadata
{
	definition(name: "HubConnect Netatmo Outdoor Module", namespace: "shackrat", author: "Steve White", mnmn: "SmartThings", vid: "SmartThings-smartthings-Xiaomi_Temperature_Humidity_Sensor", ocfDeviceType: "oic.d.thermostat")
	{
		capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
		capability "Battery"
		capability "Refresh"

		attribute "min_temp", "number"
		attribute "max_temp", "number"
		attribute "temp_trend", "string"
		attribute "lastupdate", "string"
		attribute "version", "string"

		command "sync"
	}

	tiles (scale: 2)
	{
		multiAttributeTile(name:"main", type:"generic", width:6, height:4)
		{
			tileAttribute("temperature", key: "PRIMARY_CONTROL")
			{
				attributeState "temperature", label:'${currentValue}°', icon:"st.Weather.weather2", backgroundColors:[
					[value: 32, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 92, color: "#d04e00"],
					[value: 98, color: "#bc2323"]
				]
			}
			tileAttribute ("humidity", key: "SECONDARY_CONTROL")
			{
				attributeState "humidity", label:'Humidity: ${currentValue}%'
			}
		}
		valueTile("min_temp", "min_temp", width: 2, height: 1)
		{
 			state "min_temp", label: 'Min: ${currentValue}°'
 		}
		valueTile("max_temp", "max_temp", width: 2, height: 1)
		{
 			state "max_temp", label: 'Max: ${currentValue}°'
 		}
		valueTile("temp_trend", "temp_trend", width: 4, height: 1)
		{
 			state "temp_trend", label: 'Temp Trend: ${currentValue}'
 		}
		valueTile("battery", "device.battery", inactiveLabel: false, width: 2, height: 2)
		{
			state "battery_percent", label:'Battery: ${currentValue}%', unit:"", backgroundColors:[
				[value: 20, color: "#ff0000"],
				[value: 35, color: "#fd4e3a"],
				[value: 50, color: "#fda63a"],
				[value: 60, color: "#fdeb3a"],
				[value: 75, color: "#d4fd3a"],
				[value: 90, color: "#7cfd3a"],
				[value: 99, color: "#55fd3a"]
			]
		}
		valueTile("temperature", "device.temperature")
		{
 			state("temperature", label: '${currentValue}°', icon:"st.Weather.weather2", backgroundColors: [
 				[value: 31, color: "#153591"],
 				[value: 44, color: "#1e9cbb"],
 				[value: 59, color: "#90d2a7"],
 				[value: 74, color: "#44b621"],
 				[value: 84, color: "#f1d801"],
 				[value: 95, color: "#d04e00"],
 				[value: 96, color: "#bc2323"]
 				]
 				)
 		}
 		valueTile("lastupdate", "lastupdate", width: 4, height: 1, inactiveLabel: false)
		{
		  state "default", label:"Last updated: " + '${currentValue}'
		  }
 		valueTile("date_min_temp", "date_min_temp", width: 2, height: 1, inactiveLabel: false)
		{
		  state "default", label:'${currentValue}'
		}
		valueTile("date_max_temp", "date_max_temp", width: 2, height: 1, inactiveLabel: false)
		{
		  state "default", label:'${currentValue}'
		}
		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", action: "refresh.refresh", icon: "st.secondary.refresh"
		}
		standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
		}
		valueTile("version", "version", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: '${currentValue}'
		}

		main (["main"])
 		details(["main", "min_temp", "date_min_temp", "battery", "max_temp", "date_max_temp", "temp_trend", "lastupdate", "refresh", "sync", "version"])
	}
}


/*
	installed

	Doesn't do much other than call initialize().
*/
def installed()
{
	initialize()
}


/*
	updated

	Doesn't do much other than call initialize().
*/
def updated()
{
	initialize()
}


/*
	initialize

	Doesn't do much other than call refresh().
*/
def initialize()
{
	refresh()
}


/*
	parse

	In a virtual world this should never be called.
*/
def parse(String description)
{
	log.trace "Msg: Description is $description"
}


/*
	refresh

	Refreshes the device by requesting an update from the client hub.
*/
def refresh()
{
	// The server will update status
	parent.sendDeviceEvent(device.deviceNetworkId, "refresh")
}


/*
	sync

	Synchronizes the device details with the parent.
*/
def sync()
{
	// The server will respond with updated status and details
	parent.syncDevice(device.deviceNetworkId, "netatmowxoutdoor")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
def getDriverVersion() {[platform: "SmartThings", major: 1, minor: 6, build: 4]}