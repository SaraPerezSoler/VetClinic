## happy path
* greet
  - utter_greet

## ask hours
* hour
  - utter_hours

## make an appointment
* make_appointment
  - appointment_form
  - form{"name": "appointment_form"}
  - form{"name": null}

## make an appointment greet interrupt
* make_appointment
  - appointment_form
  - form{"name": "appointment_form"}
* greet
  - utter_greet
  - action_deactivate_form
  - form{"name": null}

## make an appointment hour interrupt and stop
* make_appointment
  - appointment_form
  - form{"name": "appointment_form"}
* hour
  - utter_hours
  - utter_ask_continue
* deny
  - action_deactivate_form
  - form{"name": null}

## make an appointment hour interrupt and stop
* make_appointment
  - appointment_form
  - form{"name": "appointment_form"}
* hour
  - utter_hours
  - utter_ask_continue
* affirm
  - appointment_form
  - form{"name": null}