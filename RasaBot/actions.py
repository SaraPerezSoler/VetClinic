# This files contains your custom actions which can be used to run
# custom Python code.
#
# See this guide on how to implement these action:
# https://rasa.com/docs/rasa/core/actions/#custom-actions/


# This is a simple example for a custom action which utters "Hello World!"

# from typing import Any, Text, Dict, List
#
# from rasa_sdk import Action, Tracker
# from rasa_sdk.executor import CollectingDispatcher
#
#
# class ActionHelloWorld(Action):
#
#     def name(self) -> Text:
#         return "action_hello_world"
#
#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
#
#         dispatcher.utter_message("Hello World!")
#
#         return []
from typing import Dict, Text, Any, List, Union, Optional

from rasa_sdk import ActionExecutionRejection
from rasa_sdk import Tracker
from rasa_sdk.events import SlotSet
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.forms import FormAction, REQUESTED_SLOT
from duckling import Duckling, Dim, Language
import time
import requests

class AppointmentForm(FormAction):
    """Example of a custom form action"""
    def __init__(self):
        self.d = Duckling()
        self.d.load()

    def name(self):
        # type: () -> Text
        """Unique identifier of the form"""

        return "appointment_form"

    @staticmethod
    def required_slots(tracker: Tracker) -> List[Text]:
        """A list of required slots that the form has to fill"""

        return ["date", "time", "pet", "petName"]

    def slot_mappings(self):

        return {"date": [self.from_entity(entity="date"),
                         self.from_text()],
            "time": [self.from_entity(entity="time"),
                         self.from_text()],
            "pet": [self.from_entity(entity="pet"),
                         self.from_text()],
            "petName": [self.from_entity(entity="petName"),
                         self.from_text()],
            }

    @staticmethod
    def pet_db():
        # type: () -> List[Text]
        """Database of supported cuisines"""
        return ["dog",
                "cat",
                "bird",
                "tortoise",
                "rabbit",
                "guinea pig",
                "mouse",
                "hamster", 
                "turtles"]

    def parseRet(self, value:Text, dim:Text, grain:Text) -> Text:
        parses = self.d.parse(value)
        for parse in parses:
            if parse ['dim'] == dim:
                if parse['value'].get('grain') == grain:
                    return parse ['value']['value']
        return None
        

    def validate_date(self,
                         value: Text,
                         dispatcher: CollectingDispatcher,
                         tracker: Tracker,
                         domain: Dict[Text, Any]) -> Dict[Text, Any]:
        """Validate date value."""
       
        date = self.parseRet(value, 'time', 'day')
        if (date != None):
            return {"date": date}
        else:
            dispatcher.utter_template('utter_wrong_date', tracker)
            # validation failed, set this slot to None, meaning the
            # user will be asked for the slot again
            return {"date": None}

    def validate_pet(self, value: Text,
                         dispatcher: CollectingDispatcher,
                         tracker: Tracker,
                         domain: Dict[Text, Any]) -> Dict[Text, Any]:
        """Validate pet value."""

        if value.lower() in self.pet_db():
            # validation succeeded
            return {"pet":value}
        else:
            dispatcher.utter_template('utter_wrong_pet', tracker)
            # validation failed, set this slot to None, meaning the
            # user will be asked for the slot again
            return {"pet": None}

    def validate_time(self,
                            value: Text,
                            dispatcher: CollectingDispatcher,
                            tracker: Tracker,
                            domain: Dict[Text, Any]) -> Dict[Text, Any]:
        """Validate time value."""

        time = self.parseRet(value, 'time', 'minute')
        if (time != None):
            return {"time":time}
        else:
            dispatcher.utter_template('utter_wrong_time', tracker)
            # validation failed, set slot to None
            return {"time": None}

    def validate_petName(self,
                            value: Text,
                            dispatcher: CollectingDispatcher,
                            tracker: Tracker,
                            domain: Dict[Text, Any]) -> Dict[Text, Any]:
        """Validate petName value."""
        print (value)
        return {"petName": value}

    def submit(self,
               dispatcher: CollectingDispatcher,
               tracker: Tracker,
               domain: Dict[Text, Any]) -> List[Dict]:
        """Define what the form has to do
            after all required slots are filled"""

        # utter submit template
        print("petName: "+tracker.get_slot("petName"))
        print("pet: "+tracker.get_slot("pet"))
        print("time: "+tracker.get_slot("time"))
        print("date: "+tracker.get_slot("date"))        
        
        url = 'https://dimo1.ii.uam.es:8443/VeterinarioApi/Rasa'
        myobj = {'petName': tracker.get_slot("petName"),
                 'pet': tracker.get_slot("pet"),
                 'time': tracker.get_slot("time"),
                 'date': tracker.get_slot("date"),}

        x = requests.post(url, json = myobj)

        print(x.text)
        dispatcher.utter_message(x.text)
        #dispatcher.utter_template('utter_submit', tracker)
        return [SlotSet("petName", None), SlotSet("pet", None), SlotSet("time", None), SlotSet("date", None)]
